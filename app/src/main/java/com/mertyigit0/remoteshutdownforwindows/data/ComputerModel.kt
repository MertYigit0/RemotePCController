package com.mertyigit0.remoteshutdownforwindows.data

data class ComputerModel(
    var deviceName: String = "",
    var macAddress: String = "",
    var ipAddress: String = "",
    var networkIpAddress: String = "",
    var wakeOnLanPort: String = ""
)