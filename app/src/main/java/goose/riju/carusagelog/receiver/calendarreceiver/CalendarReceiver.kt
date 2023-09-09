package goose.riju.carusagelog.receiver.calendarreceiver

import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import dagger.hilt.android.AndroidEntryPoint
import goose.riju.carusagelog.extension.parcelable
import goose.riju.carusagelog.receiver.common.CommonReceiverManager
import goose.riju.carusagelog.receiver.extension.goAsync
import javax.inject.Inject

@AndroidEntryPoint
class CalendarReceiver : BroadcastReceiver() {
    @Inject
    lateinit var calendarReceiverManager: CalendarReceiverManager
    @Inject
    lateinit var commonReceiverManager: CommonReceiverManager
    override fun onReceive(context: Context?, intent: Intent?) = goAsync {
        Log.d("libadelay", "delay received")
        val action: String? = intent?.action
        val btDevice: BluetoothDevice? = intent?.parcelable(BluetoothDevice.EXTRA_DEVICE)

        if (!commonReceiverManager.isGivenBtDevice(btDevice)) {
            Log.d("libaflow", "drive really ended")
            calendarReceiverManager.drivingEnded()
        }
    }
}