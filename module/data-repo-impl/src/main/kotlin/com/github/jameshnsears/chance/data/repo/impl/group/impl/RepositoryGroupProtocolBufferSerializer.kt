package com.github.jameshnsears.chance.data.repo.impl.group.impl

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.github.jameshnsears.chance.data.domain.proto.GroupHistoryProtocolBuffer
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

object RepositoryGroupProtocolBufferSerializer : Serializer<GroupHistoryProtocolBuffer> {
    override val defaultValue: GroupHistoryProtocolBuffer = GroupHistoryProtocolBuffer.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): GroupHistoryProtocolBuffer {
        try {
            return GroupHistoryProtocolBuffer.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(
        t: GroupHistoryProtocolBuffer,
        output: OutputStream,
    ) = t.writeTo(output)
}
