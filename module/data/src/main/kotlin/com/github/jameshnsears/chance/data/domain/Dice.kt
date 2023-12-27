package com.github.jameshnsears.chance.data.domain

data class Dice(
    var epoch: Long = 0,
    var sides: List<Side> = (2 downTo 1).map { i -> Side(number = i) },
    var title: String = "",
    var titleStringsId: Int = 0,
    var colour: String = ""
)
