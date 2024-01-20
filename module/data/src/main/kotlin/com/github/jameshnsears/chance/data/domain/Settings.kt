package com.github.jameshnsears.chance.data.domain

data class Settings(
    var tabRowChance: Int = 0,

    var bagDemoBag: Boolean = true,

    var rollSequentially: Boolean = false,
    var rollHistory: Boolean = false,
    var rollScore: Boolean = false,
    var rollDiceTitle: Boolean = false,
    var rollSideNumber: Boolean = false,
    var rollSound: Boolean = false
)
