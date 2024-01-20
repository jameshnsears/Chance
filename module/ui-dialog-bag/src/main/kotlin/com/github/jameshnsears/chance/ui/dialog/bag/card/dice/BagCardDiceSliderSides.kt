package com.github.jameshnsears.chance.ui.dialog.bag.card.dice

import android.content.Context
import com.github.jameshnsears.chance.ui.dialog.dice.R

class BagCardDiceSliderSides(
    private val context: Context,
    override val defaultValue: Float = 2.0f
) :
    BagCardDiceSliderValues() {
    override fun values(): List<String> {
        return context.resources.getStringArray(R.array.dialog_bag_dice_sides).toList()
    }
}
