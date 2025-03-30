package com.mertyigit0.remoteshutdownforwindows

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.mertyigit0.remoteshutdownforwindows.ui.theme.RemoteShutdownForWindowsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RemoteShutdownForWindowsTheme {
                // Ana ekranı burada gösteriyoruz
                Surface(color = MaterialTheme.colorScheme.background) {
                    DeviceControlScreen() // Ana ekranı çağırıyoruz
                }
            }
        }
    }
}
