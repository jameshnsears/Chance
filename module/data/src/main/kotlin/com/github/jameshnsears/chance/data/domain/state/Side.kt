package com.github.jameshnsears.chance.data.domain.state

import java.util.UUID

data class Side(
    var uuid: String = UUID.randomUUID().toString(),
    var number: Int = 0,
    var numberColour: String = "FFFFFFFF",
    var imageBase64: String = "",
    var imageDrawableId: Int = 0,
    var description: String = "",
    var descriptionStringsId: Int = 0,
    var descriptionColour: String = "FF000000",
)
