package com.mertyigit0.remoteshutdownforwindows.viewmodel

import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mertyigit0.remoteshutdownforwindows.data.ComputerModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import com.mertyigit0.remoteshutdownforwindows.data.DataStoreManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
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
    val deviceName = MutableStateFlow("")
    val macAddress = MutableStateFlow("")
    val ipAddress = MutableStateFlow("")
    val networkIpAddress = MutableStateFlow("")
    val wakeOnLanPort = MutableStateFlow("")

    init {
        viewModelScope.launch {
            dataStoreManager.deviceName.collect { deviceName.value = it }
            dataStoreManager.macAddress.collect { macAddress.value = it }
            dataStoreManager.ipAddress.collect { ipAddress.value = it }
            dataStoreManager.networkIpAddress.collect { networkIpAddress.value = it }
            dataStoreManager.wakeOnLanPort.collect { wakeOnLanPort.value = it }
        }
    }

    fun saveDevice() {
        val computerModel = ComputerModel(
            deviceName.value,
            macAddress.value,
            ipAddress.value,
            networkIpAddress.value,
            wakeOnLanPort.value
        )

        viewModelScope.launch {
            dataStoreManager.saveDevice(computerModel)
        }


    }




    // Retrofit interface
    interface ShutdownApi {
        @GET("shutdown")
        fun shutdownComputer(): Call<String>

        @GET("restart")
        fun restartComputer(): Call<String>

        @GET("sleep")
        fun sleepComputer(): Call<String>

        @GET("lock")
        fun lockComputer(): Call<String>
    }

    fun getApiService(): ShutdownApi {
        val ip = ipAddress.value
        val baseUrl = "http://$ip:5000/"

        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(ShutdownApi::class.java)
    }

    fun shutdownPC() {
        val api = getApiService()
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = api.shutdownComputer().execute()
                if (response.isSuccessful) {
                    // Başarılı durum
                } else {
                    // Hata durumu
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun rebootPC() {
        val api = getApiService()
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = api.restartComputer().execute()
                if (response.isSuccessful) {
                    // Başarılı durum
                } else {
                    // Hata durumu
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun putPCToSleep() {
        val api = getApiService()
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = api.sleepComputer().execute()
                if (response.isSuccessful) {
                    // Başarılı durum
                } else {
                    // Hata durumu
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun lockPC() {
        val api = getApiService()
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = api.lockComputer().execute()
                if (response.isSuccessful) {
                    // Başarılı durum
                } else {
                    // Hata durumu
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    fun turnOnPC() {
        val macAddress = macAddress.value.replace(":", "").replace("-", "")

        if (macAddress.length != 12) {
            println("Hatalı MAC adresi!")
            return
        }

        val bytes = ByteArray(102)
        for (i in 0 until 6) bytes[i] = 0xFF.toByte() // 6 byte FF
        val macBytes = macAddress.chunked(2).map { it.toInt(16).toByte() }.toByteArray()
        for (i in 6 until bytes.size step macBytes.size) {
            System.arraycopy(macBytes, 0, bytes, i, macBytes.size) // 16 kez MAC ekleniyor
        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val address = InetAddress.getByName(networkIpAddress.value)
                val port = wakeOnLanPort.value.toIntOrNull() ?: 9 // Default WOL port 9
                DatagramSocket().use { socket ->
                    val packet = DatagramPacket(bytes, bytes.size, address, port)
                    socket.send(packet)
                }
                println("Wake-on-LAN paketi gönderildi!")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


}