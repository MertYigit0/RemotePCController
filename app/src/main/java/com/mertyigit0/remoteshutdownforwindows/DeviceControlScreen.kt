package com.mertyigit0.remoteshutdownforwindows

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeviceControlScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopAppBar(
            title = { Text("PC Control", color = Color.White) },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black),
            navigationIcon = {
                IconButton(onClick = { /* Open drawer */ }) {
                    Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Color.White)
                }
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Image(
            painter = painterResource(id = R.drawable.switc), // PNG ikonunu kullan
            contentDescription = "Power",
            modifier = Modifier.size(100.dp)
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
                        painter = painterResource(id = R.drawable.computer), // PNG kullan
                        contentDescription = "Device",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text("Selected Device", color = Color.LightGray, fontSize = 12.sp)
                        Text("DEV-MACHINE-WIN", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(Icons.Default.Settings, contentDescription = "More", tint = Color.White)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.wifi), // PNG kullan
                        contentDescription = "WiFi",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text("Awake Status", color = Color.LightGray, fontSize = 12.sp)
                        Text("Awake", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Text("4ms", color = Color.Green, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        ActionButtons()
    }
}

@Composable
fun ActionButtons() {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ActionButton("Turn On", R.drawable.powerbutton)
            ActionButton("Reboot", R.drawable.refresh)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ActionButton("Sleep", R.drawable.zzz)
            ActionButton("Lock", R.drawable.lock)
        }
    }
}

@Composable
fun ActionButton(text: String, iconRes: Int) {
    Button(
        onClick = { /* Butona basılınca yapılacak işlem */ },
        colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
        modifier = Modifier.size(150.dp, 50.dp)
    ) {
        Image(
            painter = painterResource(id = iconRes), // PNG ikonunu yükle
            contentDescription = text,
            modifier = Modifier.size(24.dp) // Küçük ikon boyutu
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
