package com.github.jameshnsears.chance.utils.svg

import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File


class SvgSerializerUnitTest {
    private val serializer = SvgSerializer()

    @Test
    fun isSVG() {
        val url = this::class.java.getResource("/data/svg/dN/d2.svg")
        assertTrue(serializer.isSVG(url!!))
    }

    @Test
    fun encodeDecode() {
        val svgByteArray = getResourceAsByteArray("/data/svg/dN/d2.svg")

        val base64String = serializer.encode(svgByteArray)

        val decodedByteArray = serializer.decode(base64String)

        assertArrayEquals(svgByteArray, decodedByteArray)
    }

    @Test
    fun checkBase64FilesMatchSvgFiles() {
        val diceResources = setOf(
            "dN/d2",
            "dN/d4_d8_d20",
            "dN/d6",
            "dN/d10",
            "dN/d12",
            "MrBenn/centurion",
            "MrBenn/crocodile",
            "MrBenn/elephants",
            "MrBenn/hunter",
            "MrBenn/knight",
            "MrBenn/lion",
            "MrBenn/parrot",
            "MrBenn/pirate",
            "MrBenn/queen",
            "MrBenn/spaceship",
        )

        for (diceFilename: String in diceResources) {
            val svgFileAsByteArray = getResourceAsByteArray("/data/svg/$diceFilename.svg")

//            writeBase64ToFile(diceFilename, svgFileAsByteArray)

            val base64File = getResourceAsString("/data/base64/$diceFilename.base64")
            val base64FileAsByteArray = serializer.decode(base64File)

            assertArrayEquals(svgFileAsByteArray, base64FileAsByteArray)
        }
    }

    private fun writeBase64ToFile(diceFilename: String, svgFileAsByteArray: ByteArray) {
        val file = File(System.getProperty("user.home")?.plus("/Desktop/$diceFilename.base64"))
        file.writeText(serializer.encode(svgFileAsByteArray))
    }

    private fun getResourceAsByteArray(resource: String): ByteArray {
        val url = this::class.java.getResource(resource)
        return url!!.readBytes()
    }

    private fun getResourceAsString(resource: String): String {
        val url = this::class.java.getResource(resource)
        return url!!.readText()
    }
}
