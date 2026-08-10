package com.github.jameshnsears.chance.data.repo.impl.locked.impl

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.github.jameshnsears.chance.data.domain.proto.LockedProtocolBuffer
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

object RepositoryLockedProtocolBufferSerializer : Serializer<LockedProtocolBuffer> {
    override val defaultValue: LockedProtocolBuffer = LockedProtocolBuffer.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): LockedProtocolBuffer {
        try {
            return LockedProtocolBuffer.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(
        t: LockedProtocolBuffer,
        output: OutputStream,
    ) = t.writeTo(output)
}
