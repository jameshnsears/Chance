package com.github.jameshnsears.chance.data.repository.settings

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.github.jameshnsears.chance.data.protocolbuffer.SettingsProtocolBuffer
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

object SettingsProtocolBufferSerializer : Serializer<SettingsProtocolBuffer> {
    override val defaultValue: SettingsProtocolBuffer = SettingsProtocolBuffer.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): SettingsProtocolBuffer {
        try {
            return SettingsProtocolBuffer.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(
        t: SettingsProtocolBuffer,
        output: OutputStream,
    ) = t.writeTo(output)
}

val android.content.Context.settingsDataStore: DataStore<SettingsProtocolBuffer> by dataStore(
    // /data/data/com.github.jameshnsears.chance.test.test/files/datastore/settings.pb
    fileName = "settings.pb",
    serializer = SettingsProtocolBufferSerializer,
)
