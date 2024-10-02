package com.github.jameshnsears.chance.data.domain.core.bag.impl

import android.content.Context

import com.github.jameshnsears.chance.data.domain.core.Dice
import com.github.jameshnsears.chance.data.domain.core.Side
import com.github.jameshnsears.chance.data.domain.core.bag.BagDataInterface
import timber.log.Timber

class BagDataImpl(val context: Context? = null) : BagDataInterface {
    @Synchronized
    private fun readBase64File(fileName: String): String {
        try {
            return context!!.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (e: Exception) {
            // caused by unit tests using mock context
            Timber.w(e.message)
            return ""
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
        title = "Story",
        multiplierValue = 3,
    )

    override val allDice = mutableListOf(
        d6,
        diceStory,
    )
}
