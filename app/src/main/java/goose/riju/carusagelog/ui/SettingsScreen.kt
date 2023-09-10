package goose.riju.carusagelog.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat.checkSelfPermission
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@ExperimentalMaterial3Api
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    modifier: Modifier = Modifier
) {
    val permissions = arrayOf(
        Manifest.permission.WRITE_CALENDAR, Manifest.permission.READ_CALENDAR,
        Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_CONNECT,
        Manifest.permission.POST_NOTIFICATIONS,
        Manifest.permission.SCHEDULE_EXACT_ALARM
    )

    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val requestLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissionsMap ->
        val areGranted = permissionsMap.values.reduce { acc, next ->
            acc && next
        }
        if (areGranted) {
            viewModel.saveSettings()
            scope.launch {
                snackbarHostState.showSnackbar("Save successful")
            }
        } else {
            scope.launch {
                snackbarHostState.showSnackbar("Please grant every required permissions.")
            }
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { contentPadding ->
        Column(
            modifier = modifier
                .padding(contentPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.padding(12.dp))
            OutlinedTextField(
                label = {
                    Text(
                        text = "Calendar name"
                    )
                },
                value = viewModel.calendarName,
                onValueChange = { viewModel.calendarName = it },
                modifier = Modifier.padding(8.dp)
            )
            OutlinedTextField(
                label = {
                    Text(
                        text = "Bluetooth device name"
                    )
                },
                value = viewModel.btDeviceName,
                onValueChange = { viewModel.btDeviceName = it }
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = viewModel.notificationCheck,
                    onCheckedChange = { viewModel.notificationCheck = it }
                )
                Text(
                    text = "Send notification when the driving ended"
                )
            }

            Button(onClick = {
                if (checkAndRequestAllPermissions(
                        context,
                        permissions,
                        requestLauncher
                    )
                ) {
                    viewModel.saveSettings()
                    scope.launch {
                        snackbarHostState.showSnackbar("Save successful")
                    }
                }
            }) {
                Text(
                    text = "Save"
                )
            }
        }
    }
}

fun checkAndRequestAllPermissions(
    context: Context,
    permissions: Array<String>,
    launcher: ManagedActivityResultLauncher<Array<String>, Map<String, Boolean>>
): Boolean {
    return if (!permissions.all {
            checkSelfPermission(
                context,
                it
            ) == PackageManager.PERMISSION_GRANTED
        }
    ) {
        launcher.launch(permissions)
        false
    } else {
        true
    }
}