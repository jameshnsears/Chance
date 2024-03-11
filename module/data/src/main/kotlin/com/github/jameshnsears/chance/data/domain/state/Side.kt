package com.github.jameshnsears.chance.data.domain.state

data class Side(
    var number: Int = 0,
    var numberColour: String = "FFFFFFFF",
    var imageBase64: String = "",
    var imageDrawableId: Int = 0,
    var description: String = "",
    var descriptionStringsId: Int = 0,
    var descriptionColour: String = "FF000000",
)
