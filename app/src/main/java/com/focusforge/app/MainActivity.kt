package com.focusforge.app

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.focusforge.app.core.permissions.PermissionChecker
import com.focusforge.app.core.permissions.PermissionIntents

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(Modifier.fillMaxSize()) { SetupScreen() }
            }
        }
    }

    @Composable
    private fun SetupScreen() {
        var state by remember { mutableStateOf(PermissionChecker(this).current()) }
        val notificationLauncher = rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { state = PermissionChecker(this).current() }

        Column(
            Modifier.fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("FocusForge", style = MaterialTheme.typography.headlineLarge)
            Text("Foundation setup", modifier = Modifier.padding(top = 8.dp, bottom = 24.dp))

            Text("Usage Access: " + if (state.usageAccessGranted) "Ready" else "Required")
            Button(
                onClick = { startActivity(PermissionIntents.usageAccess(this@MainActivity)) },
                modifier = Modifier.padding(top = 8.dp)
            ) { Text("Grant Usage Access") }

            Text("Overlay: " + if (state.overlayGranted) "Ready" else "Not enabled")
            Button(
                onClick = { startActivity(PermissionIntents.overlay(this@MainActivity)) },
                modifier = Modifier.padding(top = 8.dp)
            ) { Text("Open Overlay Settings") }

            if (Build.VERSION.SDK_INT >= 33 && !state.notificationsGranted) {
                Button(
                    onClick = { notificationLauncher.launch(Manifest.permission.POST_NOTIFICATIONS) },
                    modifier = Modifier.padding(top = 8.dp)
                ) { Text("Allow Notifications") }
            }

            Button(
                onClick = { state = PermissionChecker(this@MainActivity).current() },
                modifier = Modifier.padding(top = 16.dp)
            ) { Text("Refresh Setup State") }
        }
    }
}
