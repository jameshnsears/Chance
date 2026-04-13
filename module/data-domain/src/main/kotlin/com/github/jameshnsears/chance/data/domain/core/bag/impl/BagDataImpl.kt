package com.github.jameshnsears.chance.data.domain.core.bag.impl

import android.content.Context
import com.github.jameshnsears.chance.common.R
import com.github.jameshnsears.chance.data.domain.core.Dice
import com.github.jameshnsears.chance.data.domain.core.Side
import com.github.jameshnsears.chance.data.domain.core.bag.BagDataInterface

class BagDataImpl(val context: Context? = null) : BagDataInterface {
    private fun getString(resId: Int): String {
        return context?.getString(resId) ?: ""
    }

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
        title = getString(R.string.bag_d6),
        multiplierValue = 3,
    )

    val hexagram = Dice(
        sides = listOf(
            Side(
                number = 8,
                imageBase64 = readBase64File("data/base64/hexagram/yin.base64"),
                description = getString(R.string.bag_haxagram_yin),
                descriptionColour = "FF6650a4",
            ),
            Side(
                number = 7,
                imageBase64 = readBase64File("data/base64/hexagram/yin.base64"),
                description = getString(R.string.bag_haxagram_yin),
                descriptionColour = "FF6650a4",
            ),
            Side(
                number = 6,
                imageBase64 = readBase64File("data/base64/hexagram/yin.base64"),
                description = getString(R.string.bag_haxagram_yin),
                descriptionColour = "FF6650a4",
            ),
            Side(
                number = 5,
                imageBase64 = readBase64File("data/base64/hexagram/yang.base64"),
                description = getString(R.string.bag_haxagram_yang),
                descriptionColour = "FF6650a4",
            ),
            Side(
                number = 4,
                imageBase64 = readBase64File("data/base64/hexagram/yang.base64"),
                description = getString(R.string.bag_haxagram_yang),
                descriptionColour = "FF6650a4",
            ),
            Side(
                number = 3,
                imageBase64 = readBase64File("data/base64/hexagram/yang.base64"),
                description = getString(R.string.bag_haxagram_yang),
                descriptionColour = "FF6650a4",
            ),
            Side(
                number = 2,
                imageBase64 = readBase64File("data/base64/hexagram/yang.base64"),
                description = getString(R.string.bag_haxagram_yang),
                descriptionColour = "FF6650a4",
            ),
            Side(
                number = 1,
                imageBase64 = readBase64File("data/base64/hexagram/yang.base64"),
                description = getString(R.string.bag_haxagram_yang),
                descriptionColour = "FF6650a4",
            ),
        ),
        title = getString(R.string.bag_haxagram),
        multiplierValue = 3,
    )

    val diceStory = Dice(
        sides = listOf(
            Side(
                number = 6,
                description = getString(R.string.bag_mrbenn_knight),
                descriptionColour = "FF6650a4",
                imageBase64 = readBase64File("data/base64/story/knight.base64"),
            ),
            Side(
                number = 5,
                description = getString(R.string.bag_mrbenn_lion),
                descriptionColour = "FF6650a4",
                imageBase64 = readBase64File("data/base64/story/lion.base64"),
            ),
            Side(
                number = 4,
                description = getString(R.string.bag_mrbenn_pirate),
                descriptionColour = "FF6650a4",
                imageBase64 = readBase64File("data/base64/story/pirate.base64"),
            ),
            Side(
                number = 3,
                description = getString(R.string.bag_mrbenn_crocodile),
                descriptionColour = "FF6650a4",
                imageBase64 = readBase64File("data/base64/story/crocodile.base64"),
            ),
            Side(
                number = 2,
                description = getString(R.string.bag_mrbenn_queen),
                descriptionColour = "FF6650a4",
                imageBase64 = readBase64File("data/base64/story/queen.base64"),
            ),
            Side(
                number = 1,
                description = getString(R.string.bag_mrbenn_spaceship),
                descriptionColour = "FF6650a4",
                imageBase64 = readBase64File("data/base64/story/spaceship.base64"),
            ),
        ),
        title = getString(R.string.bag_mrbenn),
        multiplierValue = 3,
    )

    val diceMan = Dice(
        sides = listOf(
            Side(
                number = 10,
                description = getString(R.string.bag_diceman_draw),
                descriptionColour = "FF6650a4",
                imageBase64 = readBase64File("data/base64/diceman/diceman.base64"),
            ),
            Side(
                number = 9,
                description = getString(R.string.bag_diceman_introspection),
                descriptionColour = "FF6650a4",
                imageBase64 = readBase64File("data/base64/diceman/diceman.base64"),
            ),
            Side(
                number = 8,
                description = getString(R.string.bag_diceman_meditation),
                descriptionColour = "FF6650a4",
                imageBase64 = readBase64File("data/base64/diceman/diceman.base64"),
            ),
            Side(
                number = 7,
                description = getString(R.string.bag_diceman_feedback),
                descriptionColour = "FF6650a4",
                imageBase64 = readBase64File("data/base64/diceman/diceman.base64"),
            ),
            Side(
                number = 6,
                description = getString(R.string.bag_diceman_journaling),
                descriptionColour = "FF6650a4",
                imageBase64 = readBase64File("data/base64/diceman/diceman.base64"),
            ),
            Side(
                number = 5,
                description = getString(R.string.bag_diceman_project),
                descriptionColour = "FF6650a4",
                imageBase64 = readBase64File("data/base64/diceman/diceman.base64"),
            ),
            Side(
                number = 4,
                description = getString(R.string.bag_diceman_partner),
                descriptionColour = "FF6650a4",
                imageBase64 = readBase64File("data/base64/diceman/diceman.base64"),
            ),
            Side(
                number = 3,
                description = getString(R.string.bag_diceman_neighbour),
                descriptionColour = "FF6650a4",
                imageBase64 = readBase64File("data/base64/diceman/diceman.base64"),
            ),
            Side(
                number = 2,
                description = getString(R.string.bag_diceman_movie),
                descriptionColour = "FF6650a4",
                imageBase64 = readBase64File("data/base64/diceman/diceman.base64"),
            ),
            Side(
                number = 1,
                description = getString(R.string.bag_diceman_bed),
                descriptionColour = "FF6650a4",
                imageBase64 = readBase64File("data/base64/diceman/diceman.base64"),
            ),
        ),
        title = getString(R.string.bag_diceman),
        multiplierValue = 1,
    )

    override val allDice = mutableListOf(
        d6,
        hexagram,
        diceStory,
        diceMan
    )
}
