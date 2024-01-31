package goose.riju.carusagelog.ui

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
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
                .fillMaxSize()
                .padding(contentPadding)
                .padding(horizontal = 16.dp, vertical = 32.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    label = {
                        Text(
                            text = "Calendar name"
                        )
                    },
                    value = viewModel.calendarName,
                    onValueChange = { viewModel.calendarName = it },
                    modifier = Modifier
                        .fillMaxWidth()
                )
                OutlinedTextField(
                    label = {
                        Text(
                            text = "Bluetooth device name"
                        )
                    },
                    value = viewModel.btDeviceName,
                    onValueChange = { viewModel.btDeviceName = it },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedCard {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier.weight(1f),
                            text = "Send notification when the driving ended"
                        )
                        Switch(
                            checked = viewModel.notificationCheck,
                            onCheckedChange = { viewModel.notificationCheck = it })
                    }
                }
            }

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
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
// https://google.com/maps/dir/47.544092,+19.043661/47.532791,+19.072124