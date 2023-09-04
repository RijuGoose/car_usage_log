package goose.riju.carusagelog.data

import kotlinx.coroutines.flow.Flow

interface SettingsDataSource {
    suspend fun updateSettings(update: (Settings) -> Settings)

    suspend fun getSettings(): Flow<Settings>
}