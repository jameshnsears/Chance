package com.github.jameshnsears.chance.data.domain.utility.svg

import android.app.Application
import coil.decode.SvgDecoder
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.github.jameshnsears.chance.data.domain.core.Side
import java.net.URL
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

class UtilitySvgSerializer {
    companion object {
        fun isUrlSvg(url: URL): Boolean {
            val contentType = url.toURI().toURL().openConnection().contentType

            return if (contentType.matches(Regex("^image/svg\\+xml$")))
                true
            else {
                isStringSvg(url.readText())
            }
        }

        fun isStringSvg(text: String) = text.contains("<svg") && text.contains("</svg>")

        @OptIn(ExperimentalEncodingApi::class)
        fun encodeIntoBase64String(string: String) = Base64.encode(string.toByteArray())

        @OptIn(ExperimentalEncodingApi::class)
        fun encodeIntoBase64String(byteArray: ByteArray) = Base64.encode(byteArray)

        @OptIn(ExperimentalEncodingApi::class)
        fun decodeBase64StringIntoByteArray(base64String: String) = Base64.decode(base64String)

        fun imageRequestFromBase64String(application: Application, side: Side): ImageRequest {
            if (side.imageRequest == null) {
                val imageRequest = ImageRequest.Builder(application)
                    .data(decodeBase64StringIntoByteArray(side.imageBase64))
                    .decoderFactory(SvgDecoder.Factory())
                    .memoryCachePolicy(CachePolicy.ENABLED)
                    .memoryCacheKey(side.imageBase64.hashCode().toString())
                    .build()
                side.imageRequest = imageRequest
                return side.imageRequest!!
            } else
                return side.imageRequest!!
        }

        fun imageRequestFromSvgString(application: Application, svgString: String) =
            ImageRequest.Builder(application)
                .data(svgString.toByteArray())
                .decoderFactory(SvgDecoder.Factory())
                .memoryCachePolicy(CachePolicy.ENABLED)
                .memoryCacheKey(svgString.hashCode().toString())
                .build()
    }
}
