package com.github.jameshnsears.chance.data.domain

data class Dice(
    var diceIndex: Int = 0,
    var sides: List<Side> = listOf(
        Side(sideIndex = 6),
        Side(sideIndex = 5),
        Side(sideIndex = 4),
        Side(sideIndex = 3),
        Side(sideIndex = 2),
        Side(sideIndex = 1)
    ),
    var colour: Colour = Colour(),
    var description: String = "",
    var descriptionStringsId: Int = 0,
    var penaltyBonus: Int = 0
)
