package goose.riju.carusagelog.di

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import goose.riju.carusagelog.data.Settings
import goose.riju.carusagelog.data.SettingsDataSource
import goose.riju.carusagelog.data.SettingsDataSourceDataStore
import goose.riju.carusagelog.data.SettingsSerializer
import goose.riju.carusagelog.receiver.bluetoothreceiver.BluetoothReceiverManager
import goose.riju.carusagelog.receiver.calendarreceiver.CalendarReceiverManager
import goose.riju.carusagelog.repository.SettingsRepository
import goose.riju.carusagelog.repository.SettingsRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SettingsDependencies {

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @Provides
    @Singleton
    fun provideCalendarReceiverManager(
        settingsRepository: SettingsRepository,
        @ApplicationContext context: Context
    ) : CalendarReceiverManager = CalendarReceiverManager(settingsRepository, context)

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @Provides
    @Singleton
    fun provideBluetoothReceiverManager(
        settingsRepository: SettingsRepository,
        @ApplicationContext context: Context
    ) : BluetoothReceiverManager = BluetoothReceiverManager(settingsRepository, context)

    @Provides
    @Singleton
    fun provideSettingsRepository(
        settingsDataSource: SettingsDataSource
    ): SettingsRepository = SettingsRepositoryImpl(settingsDataSource)

    @Provides
    @Singleton
    fun provideSettingsDataSource(
        settingsDataStore: DataStore<Settings>
    ): SettingsDataSource = SettingsDataSourceDataStore(settingsDataStore)

    @Provides
    @Singleton
    fun provideSettingsDataStore(@ApplicationContext context: Context): DataStore<Settings> =
        DataStoreFactory.create(
            serializer = SettingsSerializer,
            produceFile = {
                context.dataStoreFile(SETTINGS)
            }
        )

    private const val SETTINGS = "settings"
}