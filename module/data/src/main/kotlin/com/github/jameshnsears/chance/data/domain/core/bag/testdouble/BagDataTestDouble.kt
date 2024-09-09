package com.github.jameshnsears.chance.data.domain.core.bag.testdouble

import com.github.jameshnsears.chance.data.domain.core.Dice
import com.github.jameshnsears.chance.data.domain.core.Side
import com.github.jameshnsears.chance.data.domain.core.bag.BagDataInterface
import com.github.jameshnsears.chance.data.domain.core.bag.impl.BagDataImpl

class BagDataTestDouble : BagDataInterface {
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

    val d6 = BagDataImpl().d6

    val diceStory = BagDataImpl().diceStory

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

    override val allDice = mutableListOf(
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
