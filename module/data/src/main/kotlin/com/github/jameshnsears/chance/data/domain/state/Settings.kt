package com.github.jameshnsears.chance.data.domain.state

data class Settings(
    var tabRowChance: Int = 0,
    var scale: Float = 0f,
    var rollSequentially: Boolean = false,
    var rollTime: Boolean = false,
    var rollScore: Boolean = false,
    var rollDiceTitle: Boolean = false,
    var rollSideNumber: Boolean = false,
    var rollSound: Boolean = false,
)
