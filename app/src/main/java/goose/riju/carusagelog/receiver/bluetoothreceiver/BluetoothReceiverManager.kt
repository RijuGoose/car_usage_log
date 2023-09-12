package goose.riju.carusagelog.receiver.bluetoothreceiver

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
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
    }

    fun endDriveDelay() {
        setAddEventDelay()
    }

    fun driveResume() {
        val alarmManager = context.getSystemService(AlarmManager::class.java)
        val intent = Intent(context, CalendarReceiver::class.java)

        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                1,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
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

    suspend fun hasDrivingStartedAlready(): Boolean {
        val settings = settingsRepository.getSettings().first()
        return settings.endMillis < settings.startMillis
    }

    suspend fun isGivenBtDevice(btDevice: BluetoothDevice?): Boolean {
        val btPermission =
            ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT)
        val repository = settingsRepository.getSettings().first()
        if (btPermission == PackageManager.PERMISSION_GRANTED) {
            return btDevice?.name == repository.btDeviceName ||
                    btDevice.toString() == repository.btDeviceMAC
        }
        return false
    }
}