package goose.riju.carusagelog.receiver.calendarreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import dagger.hilt.android.AndroidEntryPoint
import goose.riju.carusagelog.receiver.extension.goAsync
import javax.inject.Inject

@AndroidEntryPoint
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
class CalendarReceiver : BroadcastReceiver() {
    @Inject
    lateinit var calendarReceiverManager: CalendarReceiverManager
    override fun onReceive(context: Context?, intent: Intent?) = goAsync {
        calendarReceiverManager.drivingEnded()
    }
}