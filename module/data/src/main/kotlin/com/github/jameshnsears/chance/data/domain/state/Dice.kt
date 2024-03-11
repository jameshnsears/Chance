package com.github.jameshnsears.chance.data.domain.state

import com.github.jameshnsears.chance.data.domain.utility.epoch.UtilityEpochTimeGenerator

data class Dice(
    var epoch: Long = UtilityEpochTimeGenerator.now(),
    var sides: List<Side> = emptyList(),
    var title: String = "",
    var titleStringsId: Int = 0,
    var colour: String = "FF000000",
    var selected: Boolean = false,

    var multiplier: Boolean = false,
    var multiplierValue: Int = 0,

    var explode: Boolean = false,
    var explodeWhen: String = "",
    var explodeValue: Int = 0,

    var modifyScore: Boolean = false,
    var modifyScoreValue: Int = 0
)
