package com.github.jameshnsears.chance.data.repo.impl.settings.impl

import androidx.datastore.core.CorruptionException
import com.github.jameshnsears.chance.common.utility.UtilityAndroidUnitTestHelper
import com.github.jameshnsears.chance.data.domain.proto.SettingsProtocolBuffer
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

class RepositorySettingsProtocolBufferSerializerUnitTest : UtilityAndroidUnitTestHelper() {

    @Test
    fun defaultValue() {
        assertEquals(
            SettingsProtocolBuffer.getDefaultInstance(),
            RepositorySettingsProtocolBufferSerializer.defaultValue
        )
    }

    @Test
    fun readFrom() = runTest {
        val expected = SettingsProtocolBuffer.newBuilder()
            .setResizeZoom(2.0f)
            .build()
        val inputStream = ByteArrayInputStream(expected.toByteArray())

        val actual = RepositorySettingsProtocolBufferSerializer.readFrom(inputStream)

        assertEquals(expected, actual)
    }

    @Test(expected = CorruptionException::class)
    fun readFromCorruptionException() = runTest {
        // 0xFF is an invalid tag in protobuf
        val inputStream = ByteArrayInputStream(byteArrayOf(0xFF.toByte()))
        RepositorySettingsProtocolBufferSerializer.readFrom(inputStream)
    }

    @Test
    fun writeTo() = runTest {
        val settings = SettingsProtocolBuffer.newBuilder()
            .setResizeZoom(3.0f)
            .build()
        val outputStream = ByteArrayOutputStream()

        RepositorySettingsProtocolBufferSerializer.writeTo(settings, outputStream)

        val actual = SettingsProtocolBuffer.parseFrom(ByteArrayInputStream(outputStream.toByteArray()))
        assertEquals(settings, actual)
    }
}
