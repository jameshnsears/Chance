package com.github.jameshnsears.chance.data.repo.impl.side.impl

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.github.jameshnsears.chance.data.domain.proto.SideCacheProtocolBuffer
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

object RepositorySideCacheProtocolBufferSerializer : Serializer<SideCacheProtocolBuffer> {
    override val defaultValue: SideCacheProtocolBuffer = SideCacheProtocolBuffer.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): SideCacheProtocolBuffer {
        try {
            return SideCacheProtocolBuffer.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: SideCacheProtocolBuffer, output: OutputStream) {
        t.writeTo(output)
    }
}
