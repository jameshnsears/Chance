package com.github.jameshnsears.chance.data.repository.bag

import com.github.jameshnsears.chance.data.R
import com.github.jameshnsears.chance.data.domain.Dice
import com.github.jameshnsears.chance.data.domain.Side

class BagSampleData {
    companion object {
        val allDice = listOf(
            Dice(
                1,
                sides = (2 downTo 1).map { index -> Side(number = index) },
                title = "d2"
            ),
            Dice(
                2,
                sides = (4 downTo 1).map { index -> Side(number = index) },
                title = "d4"
            ),
            Dice(
                3,
                sides = (6 downTo 1).map { index -> Side(number = index) },
                title = "d6",
            ),
            Dice(
                4,
                sides = (8 downTo 1).map { index -> Side(number = index) },
                title = "d8",
            ),
            Dice(
                5,
                sides = (10 downTo 1).map { index -> Side(number = index) },
                title = "d10",
            ),
            Dice(
                6,
                sides = (12 downTo 1).map { index -> Side(number = index) },
                titleStringsId = R.string.bag_d12,
            ),
            Dice(
                7,
                sides = (20 downTo 1).map { index -> Side(number = index) },
                titleStringsId = R.string.bag_d20,
            )
        )
    }
}