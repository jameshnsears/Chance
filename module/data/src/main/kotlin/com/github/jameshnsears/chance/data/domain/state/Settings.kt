package com.github.jameshnsears.chance.data.domain.state

data class Settings(
    var tabRowChance: Int = 0,

    var resize: Float = 4f,

    var rollIndexTime: Boolean = false,
    var rollScore: Boolean = true,

    var diceTitle: Boolean = false,
    var sideNumber: Boolean = true,
    var behaviour: Boolean = false,
    var sideDescription: Boolean = false,
    var sideSVG: Boolean = true,

    var rollSound: Boolean = false,
)
