package com.github.jameshnsears.chance.data.repository.settings.impl

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.github.jameshnsears.chance.data.domain.proto.SettingsProtocolBuffer
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
