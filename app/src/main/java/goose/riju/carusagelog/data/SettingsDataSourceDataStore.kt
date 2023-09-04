package goose.riju.carusagelog.data

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.Flow

class SettingsDataSourceDataStore(
    private val settingsDataStore: DataStore<Settings>
) : SettingsDataSource {
    override suspend fun updateSettings(update: (Settings) -> Settings) {
        settingsDataStore.updateData { settingsModel ->
            update(settingsModel)
        }
    }

    override suspend fun getSettings(): Flow<Settings> =
        settingsDataStore.data

}