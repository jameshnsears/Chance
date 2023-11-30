package com.github.jameshnsears.chance.data.domain

data class Side(
    val diceIndex: Int = 0,
    val sideIndex: Int = 1,
    val defaultValue: Int = 1,
    val unicodeCharacter: String = "",
    val unicodeCharacterColour: Colour = Colour(),
    val imagePath: String = "",
    val description: String = "",
    val descriptionColour: Colour = Colour(),
    val soundPath: String = ""
)