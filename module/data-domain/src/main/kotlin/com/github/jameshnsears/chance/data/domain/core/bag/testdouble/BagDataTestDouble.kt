package com.github.jameshnsears.chance.data.domain.core.bag.testdouble

import com.github.jameshnsears.chance.data.domain.R
import com.github.jameshnsears.chance.data.domain.core.Dice
import com.github.jameshnsears.chance.data.domain.core.Side
import com.github.jameshnsears.chance.data.domain.core.bag.BagDataInterface

class BagDataTestDouble : BagDataInterface {
    override suspend fun allDice(): MutableList<Dice> {
        return allDiceList()
    }

    fun allDiceList(): MutableList<Dice> {
        return mutableListOf(
            d2,
            d4,
            d6,
            d8,
            d10,
            d12,
            d20,
        )
    }

    val d2 = Dice(
        uuid = "d2",
        sides = (2 downTo 1).map { index -> Side(uuid = "d2-$index", number = index) },
        title = "d2",
        multiplierValue = 1
    )

    val d4 = Dice(
        uuid = "d4",
        sides = (4 downTo 1).map { index -> Side(uuid = "d4-$index", number = index) },
        title = "d4",
        explode = true,
        explodeValue = 2,
        multiplierValue = 2
    )

    val d6 = Dice(
        uuid = "d6",
        sides = (6 downTo 1).map { index -> Side(uuid = "d6-$index", number = index) },
        title = "d6",
        multiplierValue = 3,
    )

    val d8 = Dice(
        uuid = "d8",
        sides = (8 downTo 1).map { index -> Side(uuid = "d8-$index", number = index) },
        title = "d8",
        explode = true,
    )

    val d10 = Dice(
        uuid = "d10",
        sides = (10 downTo 1).map { index -> Side(uuid = "d10-$index", number = index) },
        title = "d10",
    )

    val d12 = Dice(
        uuid = "d12",
        sides = (12 downTo 1).map { index -> Side(uuid = "d12-$index", number = index) },
        title = "d12",
    )

    val d20 = Dice(
        uuid = "d20",
        sides = (20 downTo 1).map { index -> Side(uuid = "d20-$index", number = index) },
        title = "d20",
        explode = true,
        explodeValue = 20,
        multiplierValue = 20,
        modifyScore = true,
        modifyScoreValue = -5
    )

    val diceStory = Dice(
        sides = listOf(
            Side(
                number = 6,
                description = "Knight",
                descriptionColour = "FF6650a4",
                imageDrawableId = R.drawable.story_knight,
            ),
            Side(
                number = 5,
                description = "Lion",
                descriptionColour = "FF6650a4",
                imageDrawableId = R.drawable.story_lion,
            ),
            Side(
                number = 4,
                description = "Pirate",
                descriptionColour = "FF6650a4",
                imageDrawableId = R.drawable.story_pirate,
            ),
            Side(
                number = 3,
                description = "Crocodile",
                descriptionColour = "FF6650a4",
                imageDrawableId = R.drawable.story_crocodile,
            ),
            Side(
                number = 2,
                description = "Queen",
                descriptionColour = "FF6650a4",
                imageDrawableId = R.drawable.story_queen,
            ),
            Side(
                number = 1,
                description = "Spaceship",
                descriptionColour = "FF6650a4",
                imageDrawableId = R.drawable.story_spaceship,
            ),
        ),
        title = "Story",
        multiplierValue = 3,
    )
}
