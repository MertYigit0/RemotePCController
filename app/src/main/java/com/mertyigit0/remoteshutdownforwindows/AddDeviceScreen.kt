package com.mertyigit0.remoteshutdownforwindows

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mertyigit0.remoteshutdownforwindows.viewmodel.DeviceViewModel
import androidx.compose.foundation.layout.Arrangement
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.mertyigit0.remoteshutdownforwindows.data.DataStoreManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.painterResource
import androidx.compose.material.icons.Icons
import androidx.compose.material3.OutlinedTextFieldDefaults

@Composable
fun AddDeviceScreen(viewModel: DeviceViewModel, navController: NavHostController) {
    val deviceName = viewModel.deviceName.collectAsState()
    val macAddress = viewModel.macAddress.collectAsState()
    val ipAddress = viewModel.ipAddress.collectAsState()
    val networkIpAddress = viewModel.networkIpAddress.collectAsState()
    val wakeOnLanPort = viewModel.wakeOnLanPort.collectAsState()
    var showHelpDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF23272A))
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(top = 4.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.add_device_title),
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White,
                modifier = Modifier.weight(1f)
            )
            IconButton(
                onClick = { showHelpDialog = true }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.baseline_question_mark_24),
                    contentDescription = "Help"
                )
            }
        }
        OutlinedTextField(
            value = deviceName.value,
            onValueChange = { viewModel.deviceName.value = it },
            label = { Text("Enter the name of the device", color = Color(0xFF1098F7)) },
            modifier = Modifier.fillMaxWidth(0.9f),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color(0xFF1098F7),
                focusedBorderColor = Color(0xFF1098F7),
                unfocusedLabelColor = Color(0xFF1098F7),
                focusedLabelColor = Color(0xFF1098F7)
            )
        )
        Spacer(modifier = Modifier.height(24.dp))
        OutlinedTextField(
            value = macAddress.value,
            onValueChange = { viewModel.macAddress.value = it },
            label = { Text("Enter the MAC address", color = Color(0xFF1098F7)) },
            modifier = Modifier.fillMaxWidth(0.9f),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color(0xFF1098F7),
                focusedBorderColor = Color(0xFF1098F7),
                unfocusedLabelColor = Color(0xFF1098F7),
                focusedLabelColor = Color(0xFF1098F7)
            )
        )
        Spacer(modifier = Modifier.height(24.dp))
        OutlinedTextField(
            value = ipAddress.value,
            onValueChange = { viewModel.ipAddress.value = it },
            label = { Text("Enter the IP address", color = Color(0xFF1098F7)) },
            modifier = Modifier.fillMaxWidth(0.9f),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color(0xFF1098F7),
                focusedBorderColor = Color(0xFF1098F7),
                unfocusedLabelColor = Color(0xFF1098F7),
                focusedLabelColor = Color(0xFF1098F7)
            )
        )
        Spacer(modifier = Modifier.height(24.dp))
        OutlinedTextField(
            value = networkIpAddress.value,
            onValueChange = { viewModel.networkIpAddress.value = it },
            label = { Text("Enter the network interface IP address", color = Color(0xFF1098F7)) },
            modifier = Modifier.fillMaxWidth(0.9f),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color(0xFF1098F7),
                focusedBorderColor = Color(0xFF1098F7),
                unfocusedLabelColor = Color(0xFF1098F7),
                focusedLabelColor = Color(0xFF1098F7)
            )
        )
        Spacer(modifier = Modifier.height(24.dp))
        OutlinedTextField(
            value = wakeOnLanPort.value,
            onValueChange = { viewModel.wakeOnLanPort.value = it },
            label = { Text("Enter the wake on LAN port", color = Color(0xFF1098F7)) },
            modifier = Modifier.fillMaxWidth(0.9f),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color(0xFF1098F7),
                focusedBorderColor = Color(0xFF1098F7),
                unfocusedLabelColor = Color(0xFF1098F7),
                focusedLabelColor = Color(0xFF1098F7)
            )
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = {
                viewModel.saveDevice()
                navController.popBackStack() // Navigate back to the previous screen
            },
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .align(Alignment.CenterHorizontally),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF1098F7),
                contentColor = Color.White
            )
        ) {
            Text("Add", color = Color.White)
        }
        if (showHelpDialog) {
            AlertDialog(
                onDismissRequest = { showHelpDialog = false },
                confirmButton = {
                    TextButton(onClick = { showHelpDialog = false }) {
                        Text("OK", color = Color(0xFF1098F7))
                    }
                },
                title = {
                    Text(
                        text = stringResource(id = R.string.add_device_title),
                        color = Color(0xFF1098F7)
                    )
                },
                containerColor = Color(0xFF23272A),
                textContentColor = Color.White,
                text = {
                    Column {
                        Text(stringResource(id = R.string.network_info_title))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            stringResource(id = R.string.network_mac_title),
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(stringResource(id = R.string.network_mac_desc))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            stringResource(id = R.string.network_ip_title),
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(stringResource(id = R.string.network_ip_desc))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            stringResource(id = R.string.network_netip_title),
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(stringResource(id = R.string.network_netip_desc))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            stringResource(id = R.string.network_wol_title),
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(stringResource(id = R.string.network_wol_desc))
                    }
                }
            )
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

    AddDeviceScreen(viewModel = viewModel, navController = navController)
}