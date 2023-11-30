package com.github.jameshnsears.chance.ui.dialog.dice

import android.content.Context

class DialogDiceSliderPenaltyBonus(val context: Context, override val defaultValue: Float = 3.0f) :
    DialogDiceSliderValues() {
    override fun values(): List<String> {
        return context.resources.getStringArray(R.array.dialog_dice_penalty_bonus).toList()
    }
}
