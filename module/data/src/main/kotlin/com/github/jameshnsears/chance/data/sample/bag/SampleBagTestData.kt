package com.github.jameshnsears.chance.data.sample.bag

import com.github.jameshnsears.chance.data.domain.state.Dice
import com.github.jameshnsears.chance.data.domain.state.Side

class SampleBagTestData {
    val d2 = Dice(
        sides = listOf(
            Side(
                number = 2,
            ),
            Side(
                number = 1,
            ),
        ),
        title = "d2",
        multiplierValue = 1
    )

    val d4 = Dice(
        sides = listOf(
            Side(
                number = 4,
            ),
            Side(
                number = 3,
            ),
            Side(
                number = 2,
            ),
            Side(
                number = 1,
            ),
        ),
        title = "d4",
        explode = true,
        explodeValue = 2,
        multiplierValue = 2
    )

    val d6 = SampleBagStartup().d6

    val diceStory = SampleBagStartup().diceStory

    val d8 = Dice(
        sides = (8 downTo 1).map { index -> Side(number = index) },
        title = "d8",
        explode = true,
    )

    val d10 = Dice(
        sides = (10 downTo 1).map { index -> Side(number = index) },
        title = "d10",
    )

    val d12 = Dice(
        sides = (12 downTo 1).map { index -> Side(number = index) },
        title = "d12",
    )

    val d20 = Dice(
        sides = (20 downTo 1).map { index -> Side(number = index) },
        title = "d20",
        explode = true,
        explodeValue = 20,
        multiplierValue = 20,
        modifyScore = true,
        modifyScoreValue = -5
    )

    val allDice = mutableListOf(
        d2,
        d4,
        d6,
        diceStory,
        d8,
        d10,
        d12,
        d20,
    )
}
