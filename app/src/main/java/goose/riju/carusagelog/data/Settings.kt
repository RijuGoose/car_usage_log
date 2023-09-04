package goose.riju.carusagelog.data

import kotlinx.serialization.Serializable

@Serializable
data class Settings(
    val startMillis: Long = 0,
    val endMillis: Long = 0,
    val calendarName: String = "",
    val btDeviceName: String = "",
    val btDeviceMAC: String = "",
    val showNotification: Boolean = false
)