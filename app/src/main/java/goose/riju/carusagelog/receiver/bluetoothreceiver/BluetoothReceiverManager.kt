package goose.riju.carusagelog.receiver.bluetoothreceiver

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import goose.riju.carusagelog.R
import goose.riju.carusagelog.repository.SettingsRepository
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
            sendEndNotification()
        }
        else{
            Log.d("libapermission", "calendar denied")
        }
    }

    private fun sendEndNotification() {
        val notificationPermission = ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)

        if(notificationPermission == PackageManager.PERMISSION_GRANTED)
        {
            Log.d("libapermission", "noti permission granted")
            val builder = NotificationCompat.Builder(context, BT_NOTIFICATION_CHANNEL)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText("Driving event automatically added to your calendar")
                .setContentTitle("Drive ended")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setOnlyAlertOnce(true)
                .setAutoCancel(true)

            val notificationManager = NotificationManagerCompat.from(context)
            notificationManager.notify(1, builder.build())
        }
    }

    companion object {
        const val BT_NOTIFICATION_CHANNEL = "bt_notification"
    }
}