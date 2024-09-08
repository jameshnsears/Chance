package com.github.jameshnsears.chance.data.domain.utility.svg

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.github.jameshnsears.chance.ui.theme.ChanceTheme
import com.github.jameshnsears.chance.utility.logging.UtilityLoggingInstrumentedHelper
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UtilitySvgSerializerInstrumentedTest : UtilityLoggingInstrumentedHelper() {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun encodeDecodeDisplaySvgFile() = runTest {
        val context = InstrumentationRegistry.getInstrumentation().targetContext

        val assetFilename = "data/svg/dN/d2.svg"
        val inputStream = context.assets.open(assetFilename)

        val encoded = UtilitySvgSerializer.encodeIntoBase64String(
            inputStream.bufferedReader().use {
                it.readText()
            }
        )
        val decodedByteArray = UtilitySvgSerializer.decodeBase64StringIntoByteArray(encoded)

        composeRule.setContent {
            val model = ImageRequest.Builder(context)
                .data(decodedByteArray)
                .decoderFactory(SvgDecoder.Factory())
                .build()

            val painter = rememberAsyncImagePainter(model)

            ChanceTheme {
                Column {
                    Image(
                        painter = painter,
                        contentDescription = "",
                        modifier = Modifier
                            .size(width = 200.dp, height = 200.dp)
                            .testTag(assetFilename),
                    )
                }
            }
        }

        composeRule.waitForIdle()
        composeRule.onNodeWithTag(assetFilename).assertIsDisplayed()
        Thread.sleep(1000)
    }
}
