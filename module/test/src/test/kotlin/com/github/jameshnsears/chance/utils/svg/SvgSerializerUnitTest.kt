package com.github.jameshnsears.chance.utils.svg

import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SvgSerializerUnitTest {
    private val serializer = SvgSerializer()

    @Test
    fun isSVG() {
        val url = this::class.java.getResource("/SvgSerializer-d2.svg")

        assertTrue(serializer.isSVG(url!!))
    }

    @Test
    fun encodeDecode() {
        val url = this::class.java.getResource("/SvgSerializer-d2.svg")

        val byteArray = url!!.readBytes()

        val base64String = serializer.encode(byteArray)

        val decodedByteArray = serializer.decode(base64String)

        assertArrayEquals(byteArray, decodedByteArray)
    }
}
