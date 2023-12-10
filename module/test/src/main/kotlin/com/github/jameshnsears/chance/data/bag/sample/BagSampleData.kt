package com.github.jameshnsears.chance.data.bag.sample

import com.github.jameshnsears.chance.data.R
import com.github.jameshnsears.chance.data.domain.Dice
import com.github.jameshnsears.chance.data.domain.Side

class BagSampleData {
    companion object {
        val oneDice = listOf(
            Dice(
                1,
                penaltyBonus = -2
            )
        )

        val twoDice = listOf(
            Dice(
                sides = listOf(
                    Side(sideIndex = 2),
                    Side(sideIndex = 1)
                ),
                description = "d2",
                penaltyBonus = 3
            ),
            oneDice[0]
        )

        val allDice = listOf(
            Dice(
                1,
                sides = (2 downTo 1).map { index -> Side(sideIndex = index) },
                description = "d2"
            ),
            Dice(
                2,
                sides = (4 downTo 1).map { index -> Side(sideIndex = index) },
                description = "d4"
            ),
            Dice(
                3,
                sides = (6 downTo 1).map { index -> Side(sideIndex = index) },
                description = "d6",
            ),
            Dice(
                4,
                sides = (8 downTo 1).map { index -> Side(sideIndex = index) },
                description = "d8",
            ),
            Dice(
                5,
                sides = (10 downTo 1).map { index -> Side(sideIndex = index) },
                description = "d10",
            ),
            Dice(
                6,
                sides = (12 downTo 1).map { index -> Side(sideIndex = index) },
                descriptionStringsId = R.string.demo_bag_d12,
            ),
            Dice(
                7,
                sides = (20 downTo 1).map { index -> Side(sideIndex = index) },
                descriptionStringsId = R.string.demo_bag_d20,
            )
        )
    }
}