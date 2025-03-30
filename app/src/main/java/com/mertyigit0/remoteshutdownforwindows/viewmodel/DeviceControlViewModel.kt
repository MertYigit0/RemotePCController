package com.mertyigit0.remoteshutdownforwindows.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class DeviceViewModel : ViewModel() {
    val deviceName = mutableStateOf("")
    val macAddress = mutableStateOf("")
    val ipAddress = mutableStateOf("")
    val networkIpAddress = mutableStateOf("")
    val wakeOnLanPort = mutableStateOf("")

    fun saveDevice() {
        // Burada cihazı kaydetme işlemlerini gerçekleştirin
        println("Device Saved: $deviceName, $macAddress, $ipAddress, $networkIpAddress, $wakeOnLanPort")
    }
}
