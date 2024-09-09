package com.github.jameshnsears.chance.data.repository.bag.impl

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.github.jameshnsears.chance.data.domain.proto.BagProtocolBuffer
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

object BagProtocolBufferSerializer : Serializer<BagProtocolBuffer> {
    override val defaultValue: BagProtocolBuffer = BagProtocolBuffer.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): BagProtocolBuffer {
        try {
            return BagProtocolBuffer.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(
        t: BagProtocolBuffer,
        output: OutputStream,
    ) = t.writeTo(output)
}
