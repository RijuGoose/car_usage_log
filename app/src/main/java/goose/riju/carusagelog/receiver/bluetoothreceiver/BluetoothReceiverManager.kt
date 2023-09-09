package goose.riju.carusagelog.receiver.bluetoothreceiver

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import dagger.hilt.android.qualifiers.ApplicationContext
import goose.riju.carusagelog.receiver.calendarreceiver.CalendarReceiver
import goose.riju.carusagelog.repository.SettingsRepository
import kotlinx.coroutines.flow.first
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Calendar

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
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
        Log.d("libastart-info", settingsRepository.getSettings().first().toString())
    }

    fun endDriveDelay() {
        setAddEventDelay()
    }

    private fun setAddEventDelay() {
        val alarmManager = context.getSystemService(AlarmManager::class.java)

        val intent = Intent(context, CalendarReceiver::class.java)
        alarmManager.setAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            LocalDateTime.now().plusSeconds(30).atZone(
                ZoneId.systemDefault()
            ).toEpochSecond() * 1000L,
            PendingIntent.getBroadcast(
                context,
                1,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }

    suspend fun setBtDeviceMAC(deviceMAC: String) {
        settingsRepository.updateSettings {
            it.copy(
                btDeviceMAC = deviceMAC
            )
        }
    }
}