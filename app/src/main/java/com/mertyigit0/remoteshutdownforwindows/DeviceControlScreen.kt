package com.mertyigit0.remoteshutdownforwindows

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import android.widget.Toast
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mertyigit0.remoteshutdownforwindows.data.DataStoreManager
import com.mertyigit0.remoteshutdownforwindows.viewmodel.DeviceViewModel
import com.mertyigit0.remoteshutdownforwindows.viewmodel.DeviceViewModelFactory
import kotlinx.coroutines.delay

@Composable
fun DeviceControlScreen() {
    val context = LocalContext.current
    val dataStoreManager = remember { DataStoreManager(context.applicationContext) }
    val deviceViewModel: DeviceViewModel = viewModel(
        factory = DeviceViewModelFactory(dataStoreManager)
    )

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "deviceControl") {
        composable("deviceControl") {
            DeviceControlContent(navController, deviceViewModel)
        }
        composable("addDevice") {
            AddDeviceScreen(deviceViewModel, navController)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeviceControlContent(navController: NavHostController, viewModel: DeviceViewModel) {
    val savedDevice by viewModel.savedDevice.collectAsState()
    val context = LocalContext.current
    var pendingAction by remember { mutableStateOf<Pair<String, () -> Unit>?>(null) }

    LaunchedEffect(Unit) {
        viewModel.messages.collect { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() }
    }

    // IP değişince hemen yeniden ping at, sonra 10 sn'de bir tekrarla.
    // Yalnızca uygulama ekrandayken (RESUMED) çalışır; arka plana atılınca durur, dönünce hemen ping atar.
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(savedDevice.ipAddress, lifecycleOwner) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            while (true) {
                viewModel.pingDevice()
                delay(10000)
            }
        }
    }

    pendingAction?.let { (label, action) ->
        AlertDialog(
            onDismissRequest = { pendingAction = null },
            title = { Text("$label PC?") },
            text = { Text("Are you sure you want to ${label.lowercase()} ${savedDevice.deviceName.ifBlank { "the PC" }}?") },
            confirmButton = {
                TextButton(onClick = { pendingAction = null; action() }) { Text(label) }
            },
            dismissButton = {
                TextButton(onClick = { pendingAction = null }) { Text("Cancel") }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF23272A))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopAppBar(
            title = { Text("PC Control", color = Color.White) },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = (Color(0xFF23272A))),
            navigationIcon = {
                IconButton(onClick = { navController.navigate("addDevice") }) {
                    Icon(Icons.Default.Settings, contentDescription = "Settings", tint = Color.White)
                }
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Image(
            painter = painterResource(id = R.drawable.switc),
            contentDescription = "Power",
            modifier = Modifier
                .size(100.dp)
                .clickable { pendingAction = "Shut down" to viewModel::shutdownPC }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.DarkGray)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.computer),
                        contentDescription = "Device",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text("Selected Device", color = Color.LightGray, fontSize = 12.sp)
                        Text(
                            savedDevice.deviceName.ifBlank { "None" },
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    //Icon(Icons.Default.Settings, contentDescription = "More", tint = Color.White)
                }

                Spacer(modifier = Modifier.height(16.dp))


                val awakeStatus by viewModel.awakeStatus.collectAsState()
                val pingTimeMs by viewModel.pingTimeMs.collectAsState()

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.wifi),
                        contentDescription = "WiFi",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text("Awake Status", color = Color.LightGray, fontSize = 12.sp)
                        Text(
                            text = awakeStatus,
                            fontWeight = FontWeight.Bold,
                            color = if (awakeStatus == "Awake") Color.Green else Color.Red
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = pingTimeMs,
                        color = Color.Green,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        ActionButtons(viewModel, onReboot = { pendingAction = "Reboot" to viewModel::rebootPC })
    }
}

@Composable
fun ActionButtons(viewModel: DeviceViewModel, onReboot: () -> Unit) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ActionButton("Turn On", R.drawable.powerbutton) { viewModel.turnOnPC() }
            ActionButton("Reboot", R.drawable.refresh, onReboot)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ActionButton("Sleep", R.drawable.zzz) { viewModel.putPCToSleep() }
            ActionButton("Lock", R.drawable.lock) { viewModel.lockPC() }
        }
    }
}

@Composable
fun ActionButton(text: String, iconRes: Int, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
        modifier = Modifier.size(150.dp, 50.dp)
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = text,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text, color = Color.White)
    }
}

@Preview(showBackground = true)
@Composable
fun DeviceControlScreenPreview() {
    DeviceControlScreen()
}