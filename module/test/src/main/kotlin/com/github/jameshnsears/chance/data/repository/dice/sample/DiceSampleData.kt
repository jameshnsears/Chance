package com.github.jameshnsears.chance.data.repository.dice.sample

import com.github.jameshnsears.chance.data.domain.Dice
import com.github.jameshnsears.chance.data.domain.Side

class DiceSampleData {
    companion object {
        val singleDice = listOf(
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

            Dice(
                1,
                penaltyBonus = -2
            )
        )
    }
}