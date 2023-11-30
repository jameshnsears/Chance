package com.github.jameshnsears.chance.data.domain

data class Dice(
    var diceIndex: Int = 0,
    var sides: List<Side> = listOf(
        Side(sideIndex = 6, defaultValue = 6),
        Side(sideIndex = 5, defaultValue = 5),
        Side(sideIndex = 4, defaultValue = 4),
        Side(sideIndex = 3, defaultValue = 3),
        Side(sideIndex = 2, defaultValue = 2),
        Side(sideIndex = 1, defaultValue = 1)
    ),
    var colour: Colour = Colour(),
    var description: String = "d6",
    var penaltyBonus: Int = 0
)
