package com.github.jameshnsears.chance.data.domain

data class Dice(
    var index: Int = 1,
    var sides: List<Side> = (2 downTo 1).map { i -> Side(index = i) },
    var colour: Colour = Colour(),
    var title: String = "",
    var titleStringsId: Int = 0,
)
