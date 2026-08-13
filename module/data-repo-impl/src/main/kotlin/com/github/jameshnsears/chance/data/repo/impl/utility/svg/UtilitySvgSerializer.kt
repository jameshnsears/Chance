package com.github.jameshnsears.chance.data.repo.impl.utility.svg

import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

class UtilitySvgSerializer {
    companion object {
        @OptIn(ExperimentalEncodingApi::class)
        fun decodeBase64StringIntoByteArray(base64String: String) = Base64.decode(base64String)
    }
}
