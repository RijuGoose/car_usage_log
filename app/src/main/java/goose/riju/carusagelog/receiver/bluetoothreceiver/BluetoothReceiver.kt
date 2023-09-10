package goose.riju.carusagelog.receiver.bluetoothreceiver

import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import dagger.hilt.android.AndroidEntryPoint
import goose.riju.carusagelog.extension.parcelable
import goose.riju.carusagelog.receiver.common.CommonReceiverManager
import goose.riju.carusagelog.receiver.extension.goAsync
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@AndroidEntryPoint
class BluetoothReceiver : BroadcastReceiver() {
    @Inject
    lateinit var bluetoothReceiverManager: BluetoothReceiverManager
    @Inject
    lateinit var commonReceiverManager: CommonReceiverManager

    override fun onReceive(context: Context?, intent: Intent?) = goAsync {
        val action: String? = intent?.action
        val btDevice: BluetoothDevice? = intent?.parcelable(BluetoothDevice.EXTRA_DEVICE)

        when (action) {
            BluetoothDevice.ACTION_ACL_CONNECTED -> {
                if (commonReceiverManager.isGivenBtDevice(btDevice)) {
                    bluetoothReceiverManager.setBtDeviceMAC(btDevice.toString())
                    bluetoothReceiverManager.drivingStarted()
                }
            }

            BluetoothDevice.ACTION_ACL_DISCONNECTED -> {
                if (commonReceiverManager.isGivenBtDevice(btDevice)) {
                    bluetoothReceiverManager.endDriveDelay()
                }
            }
        }
    }
}

