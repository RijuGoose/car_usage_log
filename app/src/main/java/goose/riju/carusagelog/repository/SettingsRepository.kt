package goose.riju.carusagelog.repository

import goose.riju.carusagelog.data.Settings
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    suspend fun updateSettings(update: (Settings) -> Settings)
    suspend fun getSettings(): Flow<Settings>
}
