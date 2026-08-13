package com.github.jameshnsears.chance.data.domain.core.bag.impl

import android.content.Context
import com.github.jameshnsears.chance.common.R
import com.github.jameshnsears.chance.data.domain.core.Dice
import com.github.jameshnsears.chance.data.domain.core.Side

class PlayingCards(val context: Context? = null) {
    private val utilityBagData = UtilityBagData(context)

    val dice
        get() = Dice(
            sides = generateSides(),
            title = utilityBagData.getString(R.string.bag_playing_cards),
            multiplierValue = 1,
        )

    private fun generateSides(): List<Side> {
        val sides = mutableListOf<Side>()
        var sideNumber = 54


        sides.add(createSide(sideNumber--, "black_joker"))
        sides.add(createSide(sideNumber--, "red_joker"))

        val suits = listOf("clubs", "diamonds", "hearts", "spades")
        val values = listOf("king", "queen", "jack", "10", "9", "8", "7", "6", "5", "4", "3", "2", "ace", )

        for (suit in suits) {
            for (value in values) {
                sides.add(createSide(sideNumber--, "${value}_of_${suit}"))
            }
        }

        return sides
    }

    private fun createSide(number: Int, fileName: String): Side {
        return Side(
            number = number,
            imageBase64 = utilityBagData.readBase64File("data/base64/cards/$fileName.base64"),
            descriptionColour = "FF6650a4",
        )
    }
}
