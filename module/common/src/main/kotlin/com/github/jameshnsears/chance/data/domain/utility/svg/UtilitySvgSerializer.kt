package com.github.jameshnsears.chance.data.domain.utility.svg

import android.app.Application
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import java.net.URL
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

class UtilitySvgSerializer {
    companion object {
        fun isSVG(url: URL): Boolean {
            val contentType = url.toURI().toURL().openConnection().contentType

            return if (contentType.matches(Regex("^image/svg\\+xml$")))
                true
            else {
                val fileContent = url.readText()
                fileContent.contains("<svg ") && fileContent.contains("</svg>")
            }
        }

        @OptIn(ExperimentalEncodingApi::class)
        fun encode(byteArray: ByteArray) = Base64.encode(byteArray)

        @OptIn(ExperimentalEncodingApi::class)
        fun decode(encodedString: String) = Base64.decode(encodedString)

        fun decodeIntoImageRequest(application: Application, imageBase64: String) =
            ImageRequest.Builder(application)
                .data(decode(imageBase64))
                .decoderFactory(SvgDecoder.Factory())
                .build()
    }
}
