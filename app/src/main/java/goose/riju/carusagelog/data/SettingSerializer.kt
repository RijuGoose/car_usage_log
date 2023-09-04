package goose.riju.carusagelog.data

import androidx.datastore.core.Serializer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

object SettingsSerializer : Serializer<Settings> {
    override val defaultValue: Settings
        get() = Settings()

    override suspend fun readFrom(input: InputStream): Settings =
        Json.decodeFromString(input.readBytes().decodeToString())


    override suspend fun writeTo(t: Settings, output: OutputStream) {
        withContext(Dispatchers.IO){
            output.write(
                Json.encodeToString(
                    serializer = Settings.serializer(),
                    value = t
                ).encodeToByteArray()
            )
        }
    }
}