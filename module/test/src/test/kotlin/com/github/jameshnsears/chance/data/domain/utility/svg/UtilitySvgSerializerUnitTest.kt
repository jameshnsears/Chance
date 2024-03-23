package com.github.jameshnsears.chance.data.domain.utility.svg

import com.github.jameshnsears.chance.utility.android.UtilityAndroid
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertTrue
import org.junit.Test


class UtilitySvgSerializerUnitTest : UtilityAndroid() {
    @Test
    fun isUrlSvg() {
        val url = this::class.java.getResource("/data/svg/dN/d2.svg")
        assertTrue(UtilitySvgSerializer.isUrlSvg(url!!))
    }

    @Test
    fun encodeDecode() {
        val svgByteArray = getResourceAsByteArray("/data/svg/dN/d2.svg")

        val base64String = UtilitySvgSerializer.encodeIntoBase64String(svgByteArray)

        val decodedByteArray = UtilitySvgSerializer.decodeBase64StringIntoByteArray(base64String)

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

//            val file = File(System.getProperty("user.home")?.plus("/Desktop/$diceFilename.base64"))
//            file.writeText(UtilitySvgSerializer.encode(svgFileAsByteArray))

            val base64File = getResourceAsString("/data/base64/$diceFilename.base64")
            val base64FileAsByteArray =
                UtilitySvgSerializer.decodeBase64StringIntoByteArray(base64File)

            assertArrayEquals(svgFileAsByteArray, base64FileAsByteArray)
        }
    }
}
