package com.github.jameshnsears.chance.data.domain.utility.svg

import com.github.jameshnsears.chance.utility.android.UtilityAndroidHelper
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertTrue
import org.junit.Ignore
import org.junit.Test
import java.io.File


class UtilitySvgSerializerUnitTest : UtilityAndroidHelper() {
    private val diceResources = setOf(
        "d6/d6s1",
        "d6/d6s2",
        "d6/d6s3",
        "d6/d6s4",
        "d6/d6s5",
        "d6/d6s6",
        "story/crocodile",
        "story/knight",
        "story/lion",
        "story/pirate",
        "story/queen",
        "story/spaceship"
    )

    @Test
    fun isUrlSvg() {
        val url = this::class.java.getResource("/data/svg/d6/d6s1.svg")
        assertTrue(UtilitySvgSerializer.isUrlSvg(url!!))
    }

    @Test
    fun encodeDecode() {
        val svgByteArray = getResourceAsByteArray("/data/svg/d6/d6s1.svg")

        val base64String = UtilitySvgSerializer.encodeIntoBase64String(svgByteArray)

        val decodedByteArray = UtilitySvgSerializer.decodeBase64StringIntoByteArray(base64String)

        assertArrayEquals(svgByteArray, decodedByteArray)
    }

    @Test
    fun checkBase64FilesMatchSvgFiles() {
        for (diceFilename: String in diceResources) {
            val svgFileAsByteArray = getResourceAsByteArray("/data/svg/$diceFilename.svg")

            val base64File = getResourceAsString("/data/base64/$diceFilename.base64")
            val base64FileAsByteArray =
                UtilitySvgSerializer.decodeBase64StringIntoByteArray(base64File)

            assertArrayEquals(svgFileAsByteArray, base64FileAsByteArray)
        }
    }

    @Test
//    @Ignore("run adhoc manually")
    fun createBase64FileFromSvgFile() {
        for (svgFile: String in diceResources) {
            val svgFileAsByteArray = getResourceAsByteArray("/data/svg/$svgFile.svg")

            val base65FilePath =
                "${System.getProperty("user.dir")}/src/test/resources/data/base64/$svgFile.base64"

            File(base65FilePath).createNewFile()
            File(base65FilePath)
                .writeText(UtilitySvgSerializer.encodeIntoBase64String(svgFileAsByteArray))
        }
    }
}
