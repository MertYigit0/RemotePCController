package com.mertyigit0.remoteshutdownforwindows

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.mertyigit0.remoteshutdownforwindows.viewmodel.DeviceViewModel
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.mertyigit0.remoteshutdownforwindows.data.DataStoreManager

@Composable
fun AddDeviceScreen(viewModel: DeviceViewModel, navController: NavHostController) {
    val deviceName = viewModel.deviceName.collectAsState()
    val macAddress = viewModel.macAddress.collectAsState()
    val ipAddress = viewModel.ipAddress.collectAsState()
    val networkIpAddress = viewModel.networkIpAddress.collectAsState()
    val wakeOnLanPort = viewModel.wakeOnLanPort.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        OutlinedTextField(
            value = deviceName.value,
            onValueChange = { viewModel.deviceName.value = it },
            label = { Text("Enter the name of the device") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = macAddress.value,
            onValueChange = { viewModel.macAddress.value = it },
            label = { Text("Enter the MAC address") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = ipAddress.value,
            onValueChange = { viewModel.ipAddress.value = it },
            label = { Text("Enter the IP address") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = networkIpAddress.value,
            onValueChange = { viewModel.networkIpAddress.value = it },
            label = { Text("Enter the network interface IP address") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = wakeOnLanPort.value,
            onValueChange = { viewModel.wakeOnLanPort.value = it },
            label = { Text("Enter the wake on LAN port") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                viewModel.saveDevice()
                navController.popBackStack() // Navigate back to the previous screen
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add")
        }
    }
}


@SuppressLint("StateFlowValueCalledInComposition")
@Preview(showBackground = true)
@Composable
fun AddDeviceScreenPreview() {
    val context = LocalContext.current
    val dataStoreManager = DataStoreManager(context)
    val viewModel = DeviceViewModel(dataStoreManager)
    val navController = rememberNavController()

    /*
    // Create preview state with remember
    val deviceName = remember { mutableStateOf("Mock Device") }
    val macAddress = remember { mutableStateOf("00:00:00:00:00:00") }
    val ipAddress = remember { mutableStateOf("192.168.0.1") }
    val networkIpAddress = remember { mutableStateOf("192.168.0.255") }
    val wakeOnLanPort = remember { mutableStateOf("9") }

    // Set initial values for the ViewModel's StateFlow
    LaunchedEffect(Unit) {
        viewModel.deviceName.value = deviceName.value
        viewModel.macAddress.value = macAddress.value
        viewModel.ipAddress.value = ipAddress.value
        viewModel.networkIpAddress.value = networkIpAddress.value
        viewModel.wakeOnLanPort.value = wakeOnLanPort.value
    }
*/
    AddDeviceScreen(viewModel = viewModel, navController = navController)
}