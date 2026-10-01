package com.focusforge.app

import android.Manifest
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
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
import androidx.compose.runtime.collectAsState
import com.focusforge.app.core.focus.AndroidFocusPolicy
import com.focusforge.app.core.focus.FocusAdminReceiver
import com.focusforge.app.core.focus.FocusState
import com.focusforge.app.core.focus.FocusViewModel
import com.focusforge.app.core.permissions.PermissionChecker
import com.focusforge.app.core.permissions.PermissionIntents
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val focusViewModel: FocusViewModel by viewModels()

    private val focusPolicy by lazy {
        AndroidFocusPolicy(
            this,
            ComponentName(this, FocusAdminReceiver::class.java)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(Modifier.fillMaxSize()) { FocusForgeScreen() }
            }
        }
    }

    @Composable
    private fun FocusForgeScreen() {
        var permissionState by remember { mutableStateOf(PermissionChecker(this).current()) }
        val active by focusViewModel.active.collectAsState()
        val notificationLauncher = rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { permissionState = PermissionChecker(this).current() }
        val lockdownReady = focusPolicy.canEnforce()

        Column(
            Modifier.fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("FocusForge", style = MaterialTheme.typography.headlineLarge)
            Text(if (active == null) "Ready" else "Focus: " + active!!.state.name)

            if (!lockdownReady && active == null) {
                Text(
                    "Lockdown is unavailable until this installation is configured as Device Owner.",
                    modifier = Modifier.padding(top = 12.dp)
                )
            }

            if (active == null) {
                Button(
                    enabled = lockdownReady,
                    onClick = { focusViewModel.start(setOf(packageName), focusPolicy) },
                    modifier = Modifier.padding(top = 16.dp)
                ) { Text("Start Enforced Focus") }
            } else {
                val session = active!!
                when (session.state) {
                    FocusState.RUNNING -> Button(
                        onClick = { focusViewModel.pause(session) },
                        modifier = Modifier.padding(top = 16.dp)
                    ) { Text("Pause") }
                    FocusState.PAUSED -> Button(
                        onClick = { focusViewModel.resume(session) },
                        modifier = Modifier.padding(top = 16.dp)
                    ) { Text("Resume") }
                    FocusState.ENDED -> Unit
                }
                Button(
                    onClick = { focusViewModel.end(session, focusPolicy) },
                    modifier = Modifier.padding(top = 8.dp)
                ) { Text("End Focus") }
            }

            Text(
                "Usage Access: " + if (permissionState.usageAccessGranted) "Ready" else "Required",
                modifier = Modifier.padding(top = 24.dp)
            )
            Button(
                onClick = { startActivity(PermissionIntents.usageAccess(this@MainActivity)) },
                modifier = Modifier.padding(top = 8.dp)
            ) { Text("Open Usage Access") }

            if (Build.VERSION.SDK_INT >= 33 && !permissionState.notificationsGranted) {
                Button(
                    onClick = { notificationLauncher.launch(Manifest.permission.POST_NOTIFICATIONS) },
                    modifier = Modifier.padding(top = 8.dp)
                ) { Text("Allow Notifications") }
            }
        }
    }
}
