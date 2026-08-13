package com.github.jameshnsears.chance.data.repo.impl.side.impl

import androidx.datastore.core.Serializer
import com.github.jameshnsears.chance.data.domain.proto.SideCacheProtocolBuffer
import java.io.InputStream
import java.io.OutputStream

object RepositorySideCacheProtocolBufferSerializer : Serializer<SideCacheProtocolBuffer> {
    override val defaultValue: SideCacheProtocolBuffer = SideCacheProtocolBuffer.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): SideCacheProtocolBuffer {
        return try {
            SideCacheProtocolBuffer.parseFrom(input)
        } catch (exception: Exception) {
            defaultValue
        }
    }

    override suspend fun writeTo(t: SideCacheProtocolBuffer, output: OutputStream) {
        t.writeTo(output)
    }
}
