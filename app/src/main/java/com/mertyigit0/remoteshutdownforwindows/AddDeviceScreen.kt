package com.mertyigit0.remoteshutdownforwindows

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.mertyigit0.remoteshutdownforwindows.viewmodel.DeviceViewModel
import androidx.compose.foundation.layout.Arrangement
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.mertyigit0.remoteshutdownforwindows.data.DataStoreManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource

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
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.add_device_title),
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(id = R.string.add_device_info),
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.fillMaxWidth(0.95f),
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(24.dp))
        OutlinedTextField(
            value = deviceName.value,
            onValueChange = { viewModel.deviceName.value = it },
            label = { Text("Enter the name of the device") },
            modifier = Modifier.fillMaxWidth(0.9f)
        )
        Spacer(modifier = Modifier.height(24.dp))
        OutlinedTextField(
            value = macAddress.value,
            onValueChange = { viewModel.macAddress.value = it },
            label = { Text("Enter the MAC address") },
            modifier = Modifier.fillMaxWidth(0.9f)
        )
        Spacer(modifier = Modifier.height(24.dp))
        OutlinedTextField(
            value = ipAddress.value,
            onValueChange = { viewModel.ipAddress.value = it },
            label = { Text("Enter the IP address") },
            modifier = Modifier.fillMaxWidth(0.9f)
        )
        Spacer(modifier = Modifier.height(24.dp))
        OutlinedTextField(
            value = networkIpAddress.value,
            onValueChange = { viewModel.networkIpAddress.value = it },
            label = { Text("Enter the network interface IP address") },
            modifier = Modifier.fillMaxWidth(0.9f)
        )
        Spacer(modifier = Modifier.height(24.dp))
        OutlinedTextField(
            value = wakeOnLanPort.value,
            onValueChange = { viewModel.wakeOnLanPort.value = it },
            label = { Text("Enter the wake on LAN port") },
            modifier = Modifier.fillMaxWidth(0.9f)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = {
                viewModel.saveDevice()
                navController.popBackStack() // Navigate back to the previous screen
            },
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .align(Alignment.CenterHorizontally)
        ) {
            Text("Add")
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = stringResource(id = R.string.add_device_note),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 8.dp)
        )
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

    AddDeviceScreen(viewModel = viewModel, navController = navController)
}