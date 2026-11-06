package com.mertyigit0.remoteshutdownforwindows.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mertyigit0.remoteshutdownforwindows.data.ComputerModel
import com.mertyigit0.remoteshutdownforwindows.data.DataStoreManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.POST
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.HttpURLConnection
import java.net.InetAddress
import java.net.URL


class DeviceViewModelFactory(
    private val dataStoreManager: DataStoreManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DeviceViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DeviceViewModel(dataStoreManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class DeviceViewModel(private val dataStoreManager: DataStoreManager) : ViewModel() {

    /** Kaydedilmiş cihaz. Tüm komutlar bu değerleri kullanır (formdaki kaydedilmemiş değerleri değil). */
    val savedDevice: StateFlow<ComputerModel> = dataStoreManager.device
        .stateIn(viewModelScope, SharingStarted.Eagerly, ComputerModel())

    // Add Device formunun alanları
    val deviceName = MutableStateFlow("")
    val macAddress = MutableStateFlow("")
    val ipAddress = MutableStateFlow("")
    val networkIpAddress = MutableStateFlow("")
    val wakeOnLanPort = MutableStateFlow("")

    val awakeStatus = MutableStateFlow("Checking...")
    val pingTimeMs = MutableStateFlow("--")

    /** UI'da Toast olarak gösterilecek tek seferlik mesajlar. */
    private val _messages = Channel<String>(Channel.BUFFERED)
    val messages = _messages.receiveAsFlow()

    private var pingJob: Job? = null

    init {
        viewModelScope.launch { loadForm(dataStoreManager.device.first()) }
    }

    /** Formu kaydedilmiş değerlerle doldurur (kaydedilmeden çıkılan düzenlemeleri atar). */
    fun resetForm() = loadForm(savedDevice.value)

    private fun loadForm(device: ComputerModel) {
        deviceName.value = device.deviceName
        macAddress.value = device.macAddress
        ipAddress.value = device.ipAddress
        networkIpAddress.value = device.networkIpAddress
        wakeOnLanPort.value = device.wakeOnLanPort
    }

    fun saveDevice() {
        val computerModel = ComputerModel(
            deviceName.value.trim(),
            macAddress.value.trim(),
            ipAddress.value.trim(),
            networkIpAddress.value.trim(),
            wakeOnLanPort.value.trim()
        )
        viewModelScope.launch {
            dataStoreManager.saveDevice(computerModel)
        }
    }

    // Retrofit interface – README'deki sunucu POST bekliyor
    interface ShutdownApi {
        @POST("shutdown")
        fun shutdownComputer(): Call<ResponseBody>

        @POST("restart")
        fun restartComputer(): Call<ResponseBody>

        @POST("sleep")
        fun sleepComputer(): Call<ResponseBody>

        @POST("lock")
        fun lockComputer(): Call<ResponseBody>
    }

    private fun getApiService(ip: String): ShutdownApi =
        Retrofit.Builder()
            .baseUrl("http://$ip:5000/")
            .build()
            .create(ShutdownApi::class.java)

    private fun sendCommand(label: String, call: (ShutdownApi) -> Call<ResponseBody>) {
        val ip = savedDevice.value.ipAddress
        if (ip.isBlank()) {
            _messages.trySend("Please add a device first.")
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            val message = try {
                val response = call(getApiService(ip)).execute()
                response.body()?.close()
                response.errorBody()?.close()
                if (response.isSuccessful) "$label command sent"
                else "$label failed (HTTP ${response.code()})"
            } catch (e: Exception) {
                e.printStackTrace()
                "$label failed: PC unreachable"
            }
            _messages.send(message)
        }
    }

    fun shutdownPC() = sendCommand("Shutdown") { it.shutdownComputer() }
    fun rebootPC() = sendCommand("Reboot") { it.restartComputer() }
    fun putPCToSleep() = sendCommand("Sleep") { it.sleepComputer() }
    fun lockPC() = sendCommand("Lock") { it.lockComputer() }

    fun turnOnPC() {
        val device = savedDevice.value
        val mac = device.macAddress.replace(":", "").replace("-", "")

        if (!mac.matches(Regex("[0-9A-Fa-f]{12}"))) {
            _messages.trySend("Invalid MAC address. Please check device settings.")
            return
        }
        if (device.networkIpAddress.isBlank()) {
            _messages.trySend("Network IP address is missing. Please check device settings.")
            return
        }

        val macBytes = mac.chunked(2).map { it.toInt(16).toByte() }.toByteArray()
        val bytes = ByteArray(6 + 16 * macBytes.size)
        for (i in 0 until 6) bytes[i] = 0xFF.toByte()
        for (i in 6 until bytes.size step macBytes.size) {
            System.arraycopy(macBytes, 0, bytes, i, macBytes.size)
        }

        viewModelScope.launch(Dispatchers.IO) {
            val message = try {
                val address = InetAddress.getByName(device.networkIpAddress)
                val port = device.wakeOnLanPort.toIntOrNull()?.takeIf { it in 1..65535 } ?: 9
                DatagramSocket().use { socket ->
                    // Broadcast adresine (örn. 192.168.1.255) gönderim için gerekli; yoksa EACCES
                    socket.broadcast = true
                    socket.send(DatagramPacket(bytes, bytes.size, address, port))
                }
                "Wake-on-LAN packet sent"
            } catch (e: Exception) {
                e.printStackTrace()
                "Failed to send Wake-on-LAN packet"
            }
            _messages.send(message)
        }
    }

    /**
     * Sunucuya HTTP isteği atarak cihazın açık olup olmadığını kontrol eder.
     * ICMP (InetAddress.isReachable) Android'de root olmadan çalışmadığı için kullanılmıyor.
     */
    fun pingDevice() {
        val ip = savedDevice.value.ipAddress
        if (ip.isBlank()) {
            awakeStatus.value = "No device"
            pingTimeMs.value = "--"
            return
        }

        // Önceki ping hâlâ sürüyorsa iptal et; eski sonuç yenisinin üzerine yazmasın
        pingJob?.cancel()
        pingJob = viewModelScope.launch(Dispatchers.IO) {
            var connection: HttpURLConnection? = null
            val result = try {
                val startTime = System.currentTimeMillis()
                connection = URL("http://$ip:5000/").openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.connectTimeout = 3000
                connection.readTimeout = 3000
                connection.instanceFollowRedirects = false
                // Herhangi bir HTTP yanıtı (404 dahil) sunucunun açık olduğunu gösterir
                connection.responseCode
                "Awake" to "${System.currentTimeMillis() - startTime} ms"
            } catch (e: Exception) {
                "Offline" to "--"
            } finally {
                connection?.disconnect()
            }
            withContext(Dispatchers.Main) {
                awakeStatus.value = result.first
                pingTimeMs.value = result.second
            }
        }
    }
}
