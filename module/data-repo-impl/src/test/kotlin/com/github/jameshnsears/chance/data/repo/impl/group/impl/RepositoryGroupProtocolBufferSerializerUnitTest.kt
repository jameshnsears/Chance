package com.github.jameshnsears.chance.data.repo.impl.group.impl

import androidx.datastore.core.CorruptionException
import com.github.jameshnsears.chance.common.utility.UtilityAndroidUnitTestHelper
import com.github.jameshnsears.chance.data.domain.proto.GroupHistoryProtocolBuffer
import com.github.jameshnsears.chance.data.domain.proto.GroupProtocolBuffer
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

class RepositoryGroupProtocolBufferSerializerUnitTest : UtilityAndroidUnitTestHelper() {

    @Test
    fun defaultValue() {
        assertEquals(
            GroupHistoryProtocolBuffer.getDefaultInstance(),
            RepositoryGroupProtocolBufferSerializer.defaultValue
        )
    }

    @Test
    fun readFrom() = runTest {
        val expected = GroupHistoryProtocolBuffer.newBuilder()
            .addGroup(
                GroupProtocolBuffer.newBuilder()
                    .setUuid("uuid")
                    .setName("name")
                    .build()
            )
            .build()
        val inputStream = ByteArrayInputStream(expected.toByteArray())

        val actual = RepositoryGroupProtocolBufferSerializer.readFrom(inputStream)

        assertEquals(expected, actual)
    }

    @Test(expected = CorruptionException::class)
    fun readFromCorruptionException() = runTest {
        // 0xFF is an invalid tag in protobuf
        val inputStream = ByteArrayInputStream(byteArrayOf(0xFF.toByte()))
        RepositoryGroupProtocolBufferSerializer.readFrom(inputStream)
    }

    @Test
    fun writeTo() = runTest {
        val groupHistory = GroupHistoryProtocolBuffer.newBuilder()
            .addGroup(
                GroupProtocolBuffer.newBuilder()
                    .setUuid("uuid")
                    .setName("name")
                    .build()
            )
            .build()
        val outputStream = ByteArrayOutputStream()

        RepositoryGroupProtocolBufferSerializer.writeTo(groupHistory, outputStream)

        val actual = GroupHistoryProtocolBuffer.parseFrom(ByteArrayInputStream(outputStream.toByteArray()))
        assertEquals(groupHistory, actual)
    }
}
