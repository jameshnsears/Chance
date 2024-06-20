package com.github.jameshnsears.chance.data.repository.bag.impl

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.github.jameshnsears.chance.data.domain.proto.DiceBagProtocolBuffer
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

object DiceBagProtocolBufferSerializer : Serializer<DiceBagProtocolBuffer> {
    override val defaultValue: DiceBagProtocolBuffer = DiceBagProtocolBuffer.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): DiceBagProtocolBuffer {
        try {
            return DiceBagProtocolBuffer.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(
        t: DiceBagProtocolBuffer,
        output: OutputStream,
    ) = t.writeTo(output)
}
