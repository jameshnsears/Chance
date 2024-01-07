package com.github.jameshnsears.chance.data.domain

data class Side(
    val number: Int = 0,
    var colour: String = "",
    val imageFilename: String = "",
    val imageBase64: String = "",
    val imageDrawableId: Int = 0,
    val description: String = "",
    val descriptionStringsId: Int = 0
)
