package com.github.jameshnsears.chance.data.domain

import com.github.jameshnsears.chance.utils.epoch.EpochTime

data class Dice(
    var epoch: Long = EpochTime.now(),
    var sides: List<Side> = emptyList(),
    var title: String = "",
    var titleStringsId: Int = 0,
    var colour: String = "FF000000",
    var selected: Boolean = false,
)
