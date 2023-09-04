package goose.riju.carusagelog

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.AndroidEntryPoint
import goose.riju.carusagelog.ui.SettingsScreen
import goose.riju.carusagelog.ui.SettingsViewModel
import goose.riju.carusagelog.ui.theme.CarUsageLogTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.S)
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        ActivityCompat.requestPermissions(
//            this, arrayOf(
//                Manifest.permission.WRITE_CALENDAR,
//                Manifest.permission.READ_CALENDAR,
//                Manifest.permission.BLUETOOTH,
//                Manifest.permission.BLUETOOTH_CONNECT
//            ),
//            1
//        )

        setContent {
            CarUsageLogTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel: SettingsViewModel = hiltViewModel()
                    SettingsScreen(viewModel)
                }
            }
        }
    }
}