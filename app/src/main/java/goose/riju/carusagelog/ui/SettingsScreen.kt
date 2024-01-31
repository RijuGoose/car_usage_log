package goose.riju.carusagelog.ui

import android.Manifest
import android.os.Build
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
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kotlinx.coroutines.launch

@OptIn(ExperimentalPermissionsApi::class)
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@ExperimentalMaterial3Api
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    modifier: Modifier = Modifier
) {
    val permissions = listOf(
        Manifest.permission.WRITE_CALENDAR, Manifest.permission.READ_CALENDAR,
        Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_CONNECT,
        Manifest.permission.POST_NOTIFICATIONS
    )

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val permissionList = rememberMultiplePermissionsState(permissions = permissions,
        onPermissionsResult = { granted ->
            val anyPermissionsDenied = granted.any { perm ->
                !perm.value
            }
            if (anyPermissionsDenied) {
                scope.launch {
                    snackbarHostState.showSnackbar("Please grant every required permissions.")
                }
            } else {
                viewModel.saveSettings()
                scope.launch {
                    snackbarHostState.showSnackbar("Save successful")
                }
            }
        })

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
                if (permissionList.allPermissionsGranted) {
                    viewModel.saveSettings()
                    scope.launch {
                        snackbarHostState.showSnackbar("Save successful")
                    }
                } else {
                    permissionList.launchMultiplePermissionRequest()
                }

            }) {
                Text(
                    text = "Save"
                )
            }
        }
    }
}
