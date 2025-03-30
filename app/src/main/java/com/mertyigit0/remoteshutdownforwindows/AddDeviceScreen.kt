package com.mertyigit0.remoteshutdownforwindows

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.mertyigit0.remoteshutdownforwindows.viewmodel.DeviceViewModel

@Composable
fun AddDeviceScreen(viewModel: DeviceViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        OutlinedTextField(
            value = viewModel.deviceName.value,
            onValueChange = { viewModel.deviceName.value = it },
            label = { Text("Enter the name of the device") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = viewModel.macAddress.value,
            onValueChange = { viewModel.macAddress.value = it },
            label = { Text("Enter the MAC address") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = viewModel.ipAddress.value,
            onValueChange = { viewModel.ipAddress.value = it },
            label = { Text("Enter the IP address") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = viewModel.networkIpAddress.value,
            onValueChange = { viewModel.networkIpAddress.value = it },
            label = { Text("Enter the network interface IP address") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = viewModel.wakeOnLanPort.value,
            onValueChange = { viewModel.wakeOnLanPort.value = it },
            label = { Text("Enter the wake on LAN port") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { viewModel.saveDevice() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddDeviceScreenPreview() {
    // Mock DeviceViewModel oluştur
    val mockViewModel = DeviceViewModel().apply {
        deviceName.value = "Mock Device"
        macAddress.value = "00:00:00:00:00:00"
        ipAddress.value = "192.168.0.1"
        networkIpAddress.value = "192.168.0.255"
        wakeOnLanPort.value = "9"
    }

    AddDeviceScreen(viewModel = mockViewModel)
}
