package goose.riju.carusagelog.receiver.bluetoothreceiver

import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import dagger.hilt.android.AndroidEntryPoint
import goose.riju.carusagelog.extension.parcelable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

@AndroidEntryPoint
class BluetoothReceiver : BroadcastReceiver() {
    @Inject
    lateinit var bluetoothReceiverManager: BluetoothReceiverManager

    override fun onReceive(context: Context?, intent: Intent?) = goAsync {
        val action: String? = intent?.action
        val btDevice: BluetoothDevice? = intent?.parcelable(BluetoothDevice.EXTRA_DEVICE)
        when (action){
            BluetoothDevice.ACTION_ACL_CONNECTED -> {
                Log.d("liba3", action.toString())

                bluetoothReceiverManager.drivingStarted()
                //toastot hagyni kéne ideiglenesen vagy véglegesen
                //Toast.makeText(context, "Driving started", Toast.LENGTH_SHORT).show()
            }
            BluetoothDevice.ACTION_ACL_DISCONNECTED -> {
                Log.d("liba2", action.toString())
                bluetoothReceiverManager.drivingEnded()
                //Toast.makeText(context, "Driving ended", Toast.LENGTH_SHORT).show()
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