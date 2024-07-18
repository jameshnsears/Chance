package com.github.jameshnsears.chance.data.domain.core.settings

data class Settings(
    var resize: Int = 4,

    var rollIndexTime: Boolean = false,
    var rollScore: Boolean = true,

    var diceTitle: Boolean = false,
    var sideNumber: Boolean = true,
    var behaviour: Boolean = false,
    var sideDescription: Boolean = false,
    var sideSVG: Boolean = true,

    var rollSound: Boolean = false,
)
