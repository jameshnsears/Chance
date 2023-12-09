package com.github.jameshnsears.chance.data.domain

data class Side(
    val diceIndex: Int = 0,
    val sideIndex: Int = 1,
    val unicodeCharacter: Int = 0,
    val unicodeCharacterColour: Colour = Colour(),
    val imagePath: String = "",
    val imageDrawableId: Int = 0,
    val text: String = "",
    val textStringsId: Int = 0,
    val textColour: Colour = Colour(),
)
