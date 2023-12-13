package com.github.jameshnsears.chance.ui.dialog.dice

import android.content.Context

class DialogDiceSliderSides(private val context: Context, override val defaultValue: Float = 2.0f) :
    DialogDiceSliderValues() {
    override fun values(): List<String> {
        return context.resources.getStringArray(R.array.dialog_dice_sides).toList()
    }
}
