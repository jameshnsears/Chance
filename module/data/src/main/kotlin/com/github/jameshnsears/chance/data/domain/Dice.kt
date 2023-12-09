package com.github.jameshnsears.chance.data.domain

data class Dice(
    var diceIndex: Int = 0,
    var sides: List<Side> = (6 downTo 1).map { index -> Side(sideIndex = index) },
    var colour: Colour = Colour(),
    var description: String = "",
    var descriptionStringsId: Int = 0,
    var penaltyBonus: Int = 0
)
