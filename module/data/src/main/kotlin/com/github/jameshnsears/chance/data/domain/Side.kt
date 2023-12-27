package com.github.jameshnsears.chance.data.domain

data class Side(
    val number: Int = 1,
    var colour: String = "",
    val imageBase64: String = "",
    val imageDrawableId: Int = 0,
    val description: String = "",
    val descriptionStringsId: Int = 0
)
