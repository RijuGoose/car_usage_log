package goose.riju.carusagelog.receiver.common

import android.Manifest
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import goose.riju.carusagelog.repository.SettingsRepository
import kotlinx.coroutines.flow.first

class CommonReceiverManager(
    private val settingsRepository: SettingsRepository,
    @ApplicationContext private val context: Context
) {
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