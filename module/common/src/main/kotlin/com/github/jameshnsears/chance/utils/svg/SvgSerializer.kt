package com.github.jameshnsears.chance.utils.svg

import java.net.URL
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

class SvgSerializer {
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
}
