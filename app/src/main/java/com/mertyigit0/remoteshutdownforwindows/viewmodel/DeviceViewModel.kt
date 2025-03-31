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
import java.net.HttpURLConnection
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
        val retrofit = Retrofit.Builder()
            .baseUrl("http://:5000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(ShutdownApi::class.java)
    }

    fun turnOnPC() {
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


}