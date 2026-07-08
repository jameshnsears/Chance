package com.github.jameshnsears.chance.data.repo.impl.bag.impl

import androidx.datastore.core.CorruptionException
import com.github.jameshnsears.chance.common.utility.UtilityAndroidUnitTestHelper
import com.github.jameshnsears.chance.data.domain.proto.BagProtocolBuffer
import com.github.jameshnsears.chance.data.domain.proto.DiceProtocolBuffer
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

class RepositoryBagProtocolBufferSerializerUnitTest : UtilityAndroidUnitTestHelper() {

    @Test
    fun defaultValue() {
        assertEquals(
            BagProtocolBuffer.getDefaultInstance(),
            RepositoryBagProtocolBufferSerializer.defaultValue
        )
    }

    @Test
    fun readFrom() = runTest {
        val expected = BagProtocolBuffer.newBuilder()
            .addDice(DiceProtocolBuffer.newBuilder().setUuid("uuid").setTitle("d6").build())
            .build()
        val inputStream = ByteArrayInputStream(expected.toByteArray())

        val actual = RepositoryBagProtocolBufferSerializer.readFrom(inputStream)

        assertEquals(expected, actual)
    }

    @Test(expected = CorruptionException::class)
    fun readFromCorruptionException() = runTest {
        val inputStream = ByteArrayInputStream(byteArrayOf(0xFF.toByte()))
        RepositoryBagProtocolBufferSerializer.readFrom(inputStream)
    }

    @Test
    fun writeTo() = runTest {
        val bag = BagProtocolBuffer.newBuilder()
            .addDice(DiceProtocolBuffer.newBuilder().setUuid("uuid-write").setTitle("d20").build())
            .build()
        val outputStream = ByteArrayOutputStream()

        RepositoryBagProtocolBufferSerializer.writeTo(bag, outputStream)

        val actual = BagProtocolBuffer.parseFrom(ByteArrayInputStream(outputStream.toByteArray()))
        assertEquals(bag, actual)
    }
}
