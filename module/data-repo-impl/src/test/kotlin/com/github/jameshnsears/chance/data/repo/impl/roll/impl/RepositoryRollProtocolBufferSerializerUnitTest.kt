package com.github.jameshnsears.chance.data.repo.impl.roll.impl

import androidx.datastore.core.CorruptionException
import com.github.jameshnsears.chance.common.utility.UtilityAndroidUnitTestHelper
import com.github.jameshnsears.chance.data.domain.proto.RollHistoryProtocolBuffer
import com.github.jameshnsears.chance.data.domain.proto.RollListProtocolBuffer
import com.github.jameshnsears.chance.data.domain.proto.RollProtocolBuffer
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

class RepositoryRollProtocolBufferSerializerUnitTest : UtilityAndroidUnitTestHelper() {

    @Test
    fun defaultValue() {
        assertEquals(
            RollHistoryProtocolBuffer.getDefaultInstance(),
            RepositoryRollProtocolBufferSerializer.defaultValue
        )
    }

    @Test
    fun readFrom() = runTest {
        val expected = RollHistoryProtocolBuffer.newBuilder()
            .putValues(
                123L, RollListProtocolBuffer.newBuilder()
                    .addRoll(RollProtocolBuffer.newBuilder().setUuidDice("uuid").build())
                    .build()
            )
            .build()
        val inputStream = ByteArrayInputStream(expected.toByteArray())

        val actual = RepositoryRollProtocolBufferSerializer.readFrom(inputStream)

        assertEquals(expected, actual)
    }

    @Test(expected = CorruptionException::class)
    fun readFromCorruptionException() = runTest {
        val inputStream = ByteArrayInputStream(byteArrayOf(0xFF.toByte()))
        RepositoryRollProtocolBufferSerializer.readFrom(inputStream)
    }

    @Test
    fun writeTo() = runTest {
        val rollHistory = RollHistoryProtocolBuffer.newBuilder()
            .putValues(
                456L, RollListProtocolBuffer.newBuilder()
                    .addRoll(RollProtocolBuffer.newBuilder().setUuidDice("another-uuid").build())
                    .build()
            )
            .build()
        val outputStream = ByteArrayOutputStream()

        RepositoryRollProtocolBufferSerializer.writeTo(rollHistory, outputStream)

        val actual = RollHistoryProtocolBuffer.parseFrom(ByteArrayInputStream(outputStream.toByteArray()))
        assertEquals(rollHistory, actual)
    }

    @Test
    fun readFromConcatenated() = runTest {
        val msg1 = RollHistoryProtocolBuffer.newBuilder()
            .putValues(
                1L, RollListProtocolBuffer.newBuilder()
                    .addRoll(RollProtocolBuffer.newBuilder().setUuidDice("d1").build()).build()
            )
            .build()
        val msg2 = RollHistoryProtocolBuffer.newBuilder()
            .putValues(
                2L, RollListProtocolBuffer.newBuilder()
                    .addRoll(RollProtocolBuffer.newBuilder().setUuidDice("d2").build()).build()
            )
            .build()

        val outputStream = ByteArrayOutputStream()
        msg1.writeTo(outputStream)
        msg2.writeTo(outputStream)

        val inputStream = ByteArrayInputStream(outputStream.toByteArray())
        val actual = RepositoryRollProtocolBufferSerializer.readFrom(inputStream)

        assertEquals(2, actual.valuesCount)
        assertTrue(actual.containsValues(1L))
        assertTrue(actual.containsValues(2L))
    }
}
