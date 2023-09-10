package goose.riju.carusagelog.receiver.calendarreceiver

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.provider.CalendarContract
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import goose.riju.carusagelog.R
import goose.riju.carusagelog.repository.SettingsRepository
import kotlinx.coroutines.flow.first
import java.util.Calendar

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
class CalendarReceiverManager(
    private val settingsRepository: SettingsRepository,
    @ApplicationContext private val context: Context
) {
    private fun sendEndNotification(message: String) {
        val notificationPermission =
            ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)

        if (notificationPermission == PackageManager.PERMISSION_GRANTED) {
            val builder = NotificationCompat.Builder(context,
                BT_NOTIFICATION_CHANNEL
            )
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText(message)
                .setContentTitle("Drive ended")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setOnlyAlertOnce(true)
                .setAutoCancel(true)

            val notificationManager = NotificationManagerCompat.from(context)
            notificationManager.notify(1, builder.build())
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

    private suspend fun addEventToCalendar() {
        val calendarPermission =
            ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR)

        if (calendarPermission == PackageManager.PERMISSION_GRANTED) {
            val settings = settingsRepository.getSettings().first()

            val calID: Long = getCalendarId(context, settings.calendarName)

            if (calID == -1L) {
                sendEndNotification("The given calendar is not found.")
            }
            else {
                val cr = context.contentResolver
                val values = ContentValues()
                values.put(CalendarContract.Events.DTSTART, settings.startMillis)
                values.put(CalendarContract.Events.DTEND, settings.endMillis)
                values.put(CalendarContract.Events.TITLE, "Car usage")
                values.put(
                    CalendarContract.Events.EVENT_TIMEZONE,
                    Calendar.getInstance().timeZone.toString()
                )
                values.put(CalendarContract.Events.CALENDAR_ID, calID)
                cr.insert(CalendarContract.Events.CONTENT_URI, values)

                if(settingsRepository.getSettings().first().showNotification) {
                    sendEndNotification("Driving event automatically added to your calendar")
                }
            }
        } else {
            sendEndNotification("Event was not added because you did not give permission to it.")
        }
    }

    private fun getCalendarId(context: Context, calendarname: String): Long {
        val projection = arrayOf(CalendarContract.Calendars._ID)
        val selection = CalendarContract.Calendars.CALENDAR_DISPLAY_NAME + " = ? "

//        String[] selArgs = new String[]{"mail@mail.com", "com.google", calendarname};
        val selArgs = arrayOf(calendarname)
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_CALENDAR
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return -1
        }
        val cursor = context.contentResolver.query(
            CalendarContract.Calendars.CONTENT_URI, projection, selection,
            selArgs, null
        )
        return if (cursor!!.moveToFirst()) {
            cursor.getLong(0)
        } else -1
    }

    companion object {
        const val BT_NOTIFICATION_CHANNEL = "bt_notification"
    }
}