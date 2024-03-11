package com.github.jameshnsears.chance.data.repository.roll

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.github.jameshnsears.chance.data.domain.proto.RollHistoryProtocolBuffer
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

object RollHistoryProtocolBufferSerializer : Serializer<RollHistoryProtocolBuffer> {
    override val defaultValue: RollHistoryProtocolBuffer =
        RollHistoryProtocolBuffer.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): RollHistoryProtocolBuffer {
        try {
            return RollHistoryProtocolBuffer.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(
        t: RollHistoryProtocolBuffer,
        output: OutputStream,
    ) = t.writeTo(output)
}
