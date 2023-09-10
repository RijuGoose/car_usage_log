package goose.riju.carusagelog.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import goose.riju.carusagelog.repository.SettingsRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    var calendarName by mutableStateOf("")
    var btDeviceName by mutableStateOf("")
    var notificationCheck by mutableStateOf(false)

    init {
        updateUiSettings()
    }

    fun saveSettings() {
        viewModelScope.launch {
            settingsRepository.updateSettings {
                it.copy(
                    calendarName = calendarName,
                    btDeviceName = btDeviceName,
                    showNotification = notificationCheck
                )
            }
        }
    }

    private fun updateUiSettings() {
        viewModelScope.launch {
            val settings = settingsRepository.getSettings().first()
            calendarName = settings.calendarName
            btDeviceName = settings.btDeviceName
            notificationCheck = settings.showNotification
        }
    }

    companion object {
        const val BT_NOTIFICATION_CHANNEL = "bt_notification"
    }
}