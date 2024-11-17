package com.github.jameshnsears.chance.ui.dialog.bag.card.dice.composable

import android.content.Context
import com.github.jameshnsears.chance.ui.dialog.bag.R

class BagCardDiceSliderSides(
    private val context: Context
) {
    fun values(): List<String> {
        return context.resources.getStringArray(R.array.dialog_bag_dice_sides).toList()
    }
}
