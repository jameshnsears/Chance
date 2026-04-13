package com.github.jameshnsears.chance.data.common.utility.svg

import com.github.jameshnsears.chance.common.utility.UtilityAndroidUnitTestHelper
import org.junit.Test
import java.io.File


class CreateBase64FileFromSvgFileUnitTest : UtilityAndroidUnitTestHelper() {
    private val diceResources = setOf(
        "d2",
        "yang",
        "yin",
        "diceman"
    )

    @Test
    fun createBase64FileFromSvgFile() {
        for (svgFile: String in diceResources) {
            val svgFileAsByteArray = resourceAsByteArray("/data/svg/$svgFile.svg")

            val base65FilePath =
                "${System.getProperty("user.dir")}/src/test/resources/data/base64/$svgFile.base64"

            File(base65FilePath).createNewFile()
            File(base65FilePath)
                .writeText(UtilitySvgSerializer.encodeIntoBase64String(svgFileAsByteArray))

            // ...manually copy .base64 into module/common/src/main/assets/data/base64
        }
    }
}
