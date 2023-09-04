package goose.riju.carusagelog.receiver.bluetoothreceiver

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import goose.riju.carusagelog.repository.SettingsRepository
import kotlinx.coroutines.flow.first
import java.util.Calendar

class BluetoothReceiverManager(
    private val settingsRepository: SettingsRepository,
    @ApplicationContext private val context: Context
) {
    suspend fun drivingStarted() {
        settingsRepository.updateSettings {
            val time: Calendar = Calendar.getInstance()
            it.copy(
                startMillis = time.timeInMillis
            )
        }
        Log.d("libastarted", settingsRepository.getSettings().first().startMillis.toString())
    }

    suspend fun drivingEnded() {
        settingsRepository.updateSettings {
            val time: Calendar = Calendar.getInstance()
            it.copy(
                endMillis = time.timeInMillis
            )
        }

        addEventToCalendar()
    }

    private fun addEventToCalendar() {
        val calendarPermission = ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR)

        if(calendarPermission == PackageManager.PERMISSION_GRANTED) {
            Log.d("libapermission", "calendar granted")
        }
        else{
            Log.d("libapermission", "calendar denied")
        }
    }
}