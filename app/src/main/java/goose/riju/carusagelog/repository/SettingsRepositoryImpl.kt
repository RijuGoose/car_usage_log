package goose.riju.carusagelog.repository

import goose.riju.carusagelog.data.Settings
import goose.riju.carusagelog.data.SettingsDataSource
import kotlinx.coroutines.flow.Flow

class SettingsRepositoryImpl(
    private val settingsDataSource: SettingsDataSource
) : SettingsRepository
{
    override suspend fun updateSettings(update: (Settings) -> Settings) {
        settingsDataSource.updateSettings(update)
    }

    override suspend fun getSettings(): Flow<Settings> =
        settingsDataSource.getSettings()
}