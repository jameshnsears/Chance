package com.github.jameshnsears.chance.data.domain

data class Settings(
    var tabRowChance: Int = 0,

    var bagZoom: Int = 0,
    var bagDemoBag: Boolean = true,

    var rollSelectedDice: List<Dice> = emptyList(),
    var rollSequentially: Boolean = false,
    var rollZoom: Int = 0,
    var rollHistory: Boolean = false,
    var rollTitle: Boolean = false,
    var rollSideNumber: Boolean = false,
    var rollTotal: Boolean = false,
    var rollSound: Boolean = false
)
