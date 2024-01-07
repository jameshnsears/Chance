package com.github.jameshnsears.chance.data.domain

data class Dice(
    var epoch: Long = 0,
    var sides: List<Side> = emptyList(),
    var title: String = "",
    var titleStringsId: Int = 0,
    var colour: String = "",
    var selected: Boolean = false
)
