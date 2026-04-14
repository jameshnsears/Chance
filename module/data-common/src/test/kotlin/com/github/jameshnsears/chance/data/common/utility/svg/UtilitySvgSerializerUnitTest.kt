package com.github.jameshnsears.chance.data.common.utility.svg

import com.github.jameshnsears.chance.common.utility.UtilityAndroidUnitTestHelper
import com.github.jameshnsears.chance.data.domain.core.Side
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertTrue
import org.junit.Test


class UtilitySvgSerializerUnitTest : UtilityAndroidUnitTestHelper() {
    private val diceResources = setOf(
        "d2"
    )

    @Test
    fun isUrlSvg() {
        val url = this::class.java.getResource("/data/svg/d2.svg")
        assertTrue(UtilitySvgSerializer.isUrlSvg(url!!))
    }

    @Test
    fun encodeDecode() {
        val svgByteArray = resourceAsByteArray("/data/svg/d2.svg")

        val base64String = UtilitySvgSerializer.encodeIntoBase64String(svgByteArray)

        val decodedByteArray = UtilitySvgSerializer.decodeBase64StringIntoByteArray(base64String)

        assertArrayEquals(svgByteArray, decodedByteArray)
    }

    @Test
    fun checkBase64FilesMatchSvgFiles() {
        for (diceFilename: String in diceResources) {
            val svgFileAsByteArray = resourceAsByteArray("/data/svg/$diceFilename.svg")

            val base64File = resourceAsString("/data/base64/$diceFilename.base64")
            val base64FileAsByteArray =
                UtilitySvgSerializer.decodeBase64StringIntoByteArray(base64File)

            assertArrayEquals(svgFileAsByteArray, base64FileAsByteArray)
        }
    }

    @Test
    fun imageRequestFromBase64String() {
        val svgByteArray = resourceAsByteArray("/data/svg/d2.svg")

        val base64String = UtilitySvgSerializer.encodeIntoBase64String(svgByteArray)

        val side = Side(imageBase64 = base64String)

        val application = application()

        val imageRequest1 = UtilitySvgSerializer.imageRequestFromBase64String(application, side)
        val imageRequest2 = UtilitySvgSerializer.imageRequestFromBase64String(application, side)

        assertTrue(side.imageRequest != null)
        assertTrue(imageRequest1 === imageRequest2)
    }

    @Test
    fun imageRequestFromSvgString() {
        val svgString = resourceAsString("/data/svg/d2.svg")

        val application = application()

        val imageRequest1 = UtilitySvgSerializer.imageRequestFromSvgString(application, svgString)
        val imageRequest2 = UtilitySvgSerializer.imageRequestFromSvgString(application, svgString)

        assertTrue(imageRequest1 !== imageRequest2)
    }

    @Test
    fun encodeIntoBase64StringAsync() = runTest {
        val svgString = resourceAsString("/data/svg/d2.svg")

        val base64Sync = UtilitySvgSerializer.encodeIntoBase64String(svgString)
        val base64Async = UtilitySvgSerializer.encodeIntoBase64StringAsync(svgString)

        assertTrue(base64Sync == base64Async)
    }

    @Test
    fun imageRequestFromBase64StringAsync() = runTest {
        val svgByteArray = resourceAsByteArray("/data/svg/d2.svg")

        val base64String = UtilitySvgSerializer.encodeIntoBase64String(svgByteArray)

        val side = Side(imageBase64 = base64String)

        val application = application()

        val imageRequest = UtilitySvgSerializer.imageRequestFromBase64StringAsync(application, side)

        assertTrue(imageRequest != null)
        assertTrue(side.imageRequest != null)
    }

    @Test
    fun imageRequestFromBase64StringAsyncCaching() = runTest {
        val svgByteArray = resourceAsByteArray("/data/svg/d2.svg")

        val base64String = UtilitySvgSerializer.encodeIntoBase64String(svgByteArray)

        val side = Side(imageBase64 = base64String)

        val application = application()

        val imageRequest1 = UtilitySvgSerializer.imageRequestFromBase64StringAsync(application, side)
        val imageRequest2 = UtilitySvgSerializer.imageRequestFromBase64StringAsync(application, side)

        // Should return cached instance
        assertTrue(imageRequest1 === imageRequest2)
    }

    @Test
    fun imageRequestFromSvgStringAsync() = runTest {
        val svgString = resourceAsString("/data/svg/d2.svg")

        val application = application()

        val imageRequest = UtilitySvgSerializer.imageRequestFromSvgStringAsync(application, svgString)

        assertTrue(imageRequest != null)
    }
}
