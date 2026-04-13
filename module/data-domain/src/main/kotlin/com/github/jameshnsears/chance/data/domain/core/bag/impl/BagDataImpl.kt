package com.github.jameshnsears.chance.data.domain.core.bag.impl

import android.content.Context
import com.github.jameshnsears.chance.data.domain.core.Dice
import com.github.jameshnsears.chance.data.domain.core.Side
import com.github.jameshnsears.chance.data.domain.core.bag.BagDataInterface

class BagDataImpl(val context: Context? = null) : BagDataInterface {
    @Synchronized
    private fun readBase64File(fileName: String): String {
        return try {
            context!!.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (_: Exception) {
            // caused by unit tests using mock context
            ""
        }
    }

    val d6 = Dice(
        sides = listOf(
            Side(
                number = 6,
                imageBase64 = readBase64File("data/base64/d6/d6s6.base64"),
                descriptionColour = "FF6650a4",
            ),
            Side(
                number = 5,
                imageBase64 = readBase64File("data/base64/d6/d6s5.base64"),
                descriptionColour = "FF6650a4",
            ),
            Side(
                number = 4,
                imageBase64 = readBase64File("data/base64/d6/d6s4.base64"),
                descriptionColour = "FF6650a4",
            ),
            Side(
                number = 3,
                imageBase64 = readBase64File("data/base64/d6/d6s3.base64"),
                descriptionColour = "FF6650a4",
            ),
            Side(
                number = 2,
                imageBase64 = readBase64File("data/base64/d6/d6s2.base64"),
                descriptionColour = "FF6650a4",
            ),
            Side(
                number = 1,
                imageBase64 = readBase64File("data/base64/d6/d6s1.base64"),
                descriptionColour = "FF6650a4",
            ),
        ),
        title = "d6",
        multiplierValue = 3,
    )

    val hexagram = Dice(
        sides = listOf(
            Side(
                number = 8,
                imageBase64 = readBase64File("data/base64/heaxagram/yin.base64"),
                description = "Yin, 2",
                descriptionColour = "FF6650a4",
            ),
            Side(
                number = 7,
                imageBase64 = readBase64File("data/base64/heaxagram/yin.base64"),
                description = "Yin, 2",
                descriptionColour = "FF6650a4",
            ),
            Side(
                number = 6,
                imageBase64 = readBase64File("data/base64/heaxagram/yin.base64"),
                description = "Yin, 2",
                descriptionColour = "FF6650a4",
            ),
            Side(
                number = 5,
                imageBase64 = readBase64File("data/base64/heaxagram/yang.base64"),
                description = "Yang, 3",
                descriptionColour = "FF6650a4",
            ),
            Side(
                number = 4,
                imageBase64 = readBase64File("data/base64/heaxagram/yang.base64"),
                description = "Yang, 3",
                descriptionColour = "FF6650a4",
            ),
            Side(
                number = 3,
                imageBase64 = readBase64File("data/base64/heaxagram/yang.base64"),
                description = "Yang, 3",
                descriptionColour = "FF6650a4",
            ),
            Side(
                number = 2,
                imageBase64 = readBase64File("data/base64/heaxagram/yang.base64"),
                description = "Yang, 3",
                descriptionColour = "FF6650a4",
            ),
            Side(
                number = 1,
                imageBase64 = readBase64File("data/base64/heaxagram/yang.base64"),
                description = "Yang, 3",
                descriptionColour = "FF6650a4",
            ),
        ),
        title = "Yarrow Stalk, I Ching",
        multiplierValue = 3,
    )

    val diceStory = Dice(
        sides = listOf(
            Side(
                number = 6,
                description = "Knight",
                descriptionColour = "FF6650a4",
                imageBase64 = readBase64File("data/base64/story/knight.base64"),
            ),
            Side(
                number = 5,
                description = "Lion",
                descriptionColour = "FF6650a4",
                imageBase64 = readBase64File("data/base64/story/lion.base64"),
            ),
            Side(
                number = 4,
                description = "Pirate",
                descriptionColour = "FF6650a4",
                imageBase64 = readBase64File("data/base64/story/pirate.base64"),
            ),
            Side(
                number = 3,
                description = "Crocodile",
                descriptionColour = "FF6650a4",
                imageBase64 = readBase64File("data/base64/story/crocodile.base64"),
            ),
            Side(
                number = 2,
                description = "Queen",
                descriptionColour = "FF6650a4",
                imageBase64 = readBase64File("data/base64/story/queen.base64"),
            ),
            Side(
                number = 1,
                description = "Spaceship",
                descriptionColour = "FF6650a4",
                imageBase64 = readBase64File("data/base64/story/spaceship.base64"),
            ),
        ),
        title = "Mr Benn",
        multiplierValue = 3,
    )

    val diceMan = Dice(
        sides = listOf(
            Side(
                number = 10,
                description = "Draw / Paint your feelings",
                descriptionColour = "FF6650a4",
                imageBase64 = readBase64File("data/base64/diceman/diceman.base64"),
            ),
            Side(
                number = 9,
                description = "Act without introspection",
                descriptionColour = "FF6650a4",
                imageBase64 = readBase64File("data/base64/diceman/diceman.base64"),
            ),
            Side(
                number = 8,
                description = "Practise meditation",
                descriptionColour = "FF6650a4",
                imageBase64 = readBase64File("data/base64/diceman/diceman.base64"),
            ),
            Side(
                number = 7,
                description = "Seek external feedback on behavior",
                descriptionColour = "FF6650a4",
                imageBase64 = readBase64File("data/base64/diceman/diceman.base64"),
            ),
            Side(
                number = 6,
                description = "Spend time journaling",
                descriptionColour = "FF6650a4",
                imageBase64 = readBase64File("data/base64/diceman/diceman.base64"),
            ),
            Side(
                number = 5,
                description = "Work on a professional project",
                descriptionColour = "FF6650a4",
                imageBase64 = readBase64File("data/base64/diceman/diceman.base64"),
            ),
            Side(
                number = 4,
                description = "Talk to your partner",
                descriptionColour = "FF6650a4",
                imageBase64 = readBase64File("data/base64/diceman/diceman.base64"),
            ),
            Side(
                number = 3,
                description = "Visit a neighbour",
                descriptionColour = "FF6650a4",
                imageBase64 = readBase64File("data/base64/diceman/diceman.base64"),
            ),
            Side(
                number = 2,
                description = "Watch a movie",
                descriptionColour = "FF6650a4",
                imageBase64 = readBase64File("data/base64/diceman/diceman.base64"),
            ),
            Side(
                number = 1,
                description = "Go to bed early, read a book",
                descriptionColour = "FF6650a4",
                imageBase64 = readBase64File("data/base64/diceman/diceman.base64"),
            ),
        ),
        title = "The Dice Man",
        multiplierValue = 1,
    )

    override val allDice = mutableListOf(
        d6,
        hexagram,
        diceStory,
        diceMan
    )
}
