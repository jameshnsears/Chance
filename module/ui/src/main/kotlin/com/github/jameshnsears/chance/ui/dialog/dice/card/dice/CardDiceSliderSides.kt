package com.github.jameshnsears.chance.ui.dialog.dice.card.dice

import android.content.Context
import com.github.jameshnsears.chance.common.R

class CardDiceSliderSides(
    private val context: Context
) {
    fun values(): List<String> {
        return context.resources.getStringArray(R.array.dialog_bag_dice_sides).toList()
    }
}
