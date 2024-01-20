package com.github.jameshnsears.chance.data.repository.bag

import com.github.jameshnsears.chance.data.R
import com.github.jameshnsears.chance.data.domain.Dice
import com.github.jameshnsears.chance.data.domain.Side

class BagSampleData {
    companion object {
        val allDice = listOf(
            Dice(
                sides = (2 downTo 1).map { index -> Side(number = index) },
                title = "d2"
            ),
            Dice(
                sides = (4 downTo 1).map { index -> Side(number = index) },
                title = "d4"
            ),
            Dice(
                sides = (6 downTo 1).map { index -> Side(number = index) },
                title = "d6",
            ),
            Dice(
                sides = (8 downTo 1).map { index -> Side(number = index) },
                title = "d8",
            ),
            Dice(
                sides = (10 downTo 1).map { index -> Side(number = index) },
                title = "d10",
            ),
            Dice(
                sides = (12 downTo 1).map { index -> Side(number = index) },
                titleStringsId = R.string.bag_d12,
            ),
            Dice(
                sides = (20 downTo 1).map { index -> Side(number = index) },
                titleStringsId = R.string.bag_d20,
            )
        )
    }
}