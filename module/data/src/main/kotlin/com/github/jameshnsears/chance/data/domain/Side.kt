package com.github.jameshnsears.chance.data.domain

data class Side(
    val index: Int = 1,
    val imagePath: String = "",
    val imageDrawableId: Int = 0,
    val description: String = "",
    val descriptionStringsId: Int = 0,
    val colour: Colour = Colour(),
)
