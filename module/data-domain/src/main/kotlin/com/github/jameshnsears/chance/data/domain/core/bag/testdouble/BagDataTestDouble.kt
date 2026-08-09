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
        uuid = "22222222-2222-2222-2222-222222222222",
        sides = (2 downTo 1).map { index -> Side(uuid = "22222222-2222-2222-2222-22222222220$index", number = index) },
        title = "d2",
        multiplierValue = 1
    )

    val d4 = Dice(
        uuid = "44444444-4444-4444-4444-444444444444",
        sides = (4 downTo 1).map { index -> Side(uuid = "44444444-4444-4444-4444-44444444440$index", number = index) },
        title = "d4",
        explode = true,
        explodeValue = 2,
        multiplierValue = 2
    )

    val d6 = Dice(
        uuid = "66666666-6666-6666-6666-666666666666",
        sides = (6 downTo 1).map { index -> Side(uuid = "66666666-6666-6666-6666-66666666660$index", number = index) },
        title = "d6",
        multiplierValue = 3,
    )

    val d8 = Dice(
        uuid = "88888888-8888-8888-8888-888888888888",
        sides = (8 downTo 1).map { index -> Side(uuid = "88888888-8888-8888-8888-88888888880$index", number = index) },
        title = "d8",
        explode = true,
    )

    val d10 = Dice(
        uuid = "10101010-1010-1010-1010-101010101010",
        sides = (10 downTo 1).map { index -> Side(uuid = "10101010-1010-1010-1010-1010101010" + String.format("%02d", index), number = index) },
        title = "d10",
    )

    val d12 = Dice(
        uuid = "12121212-1212-1212-1212-121212121212",
        sides = (12 downTo 1).map { index -> Side(uuid = "12121212-1212-1212-1212-1212121212" + String.format("%02d", index), number = index) },
        title = "d12",
    )

    val d20 = Dice(
        uuid = "20202020-2020-2020-2020-202020202020",
        sides = (20 downTo 1).map { index -> Side(uuid = "20202020-2020-2020-2020-2020202020" + String.format("%02d", index), number = index) },
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
