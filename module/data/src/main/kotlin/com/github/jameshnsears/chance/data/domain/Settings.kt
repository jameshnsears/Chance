package com.github.jameshnsears.chance.data.domain

data class Settings(
    var tabRowChance: Int = 0,
    var bagDemoBag: Boolean = true,
    var bagZoom: Int = 0,
    var rollSelectedDice: List<Dice> = emptyList(),
    var rollSequentially: Boolean = true,
    var rollZoom: Int = 0,
    var rollTitle: Boolean = true,
    var rollSideNumber: Boolean = true,
    var rollTotal: Boolean = true,
    var rollHistory: Boolean = true,
    var rollSound: Boolean = true
)
