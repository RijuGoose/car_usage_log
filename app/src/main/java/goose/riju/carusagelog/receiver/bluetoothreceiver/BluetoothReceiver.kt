package goose.riju.carusagelog.receiver.bluetoothreceiver

import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import dagger.hilt.android.AndroidEntryPoint
import goose.riju.carusagelog.extension.parcelable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@AndroidEntryPoint
class BluetoothReceiver : BroadcastReceiver() {
    @Inject
    lateinit var bluetoothReceiverManager: BluetoothReceiverManager

    override fun onReceive(context: Context?, intent: Intent?) = goAsync {
        val action: String? = intent?.action
        val btDevice: BluetoothDevice? = intent?.parcelable(BluetoothDevice.EXTRA_DEVICE)

        when (action){
            BluetoothDevice.ACTION_ACL_CONNECTED -> {
                bluetoothReceiverManager.drivingStarted()
            }
            BluetoothDevice.ACTION_ACL_DISCONNECTED -> {
                bluetoothReceiverManager.drivingEnded()
            }
        }
    }
}

fun BroadcastReceiver.goAsync(
    context: CoroutineContext = EmptyCoroutineContext,
    block: suspend CoroutineScope.() -> Unit
) {
    val pendingResult = goAsync()
    GlobalScope.launch(context){
        try{
            block()
        }
        finally{
            pendingResult.finish()
        }
    }
}