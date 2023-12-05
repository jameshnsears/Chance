package com.github.jameshnsears.chance.ui.dialog.dice.slider

import com.github.jameshnsears.chance.ui.dialog.dice.DialogDiceSliderValues

class DiceSliderSidesMock(override val defaultValue: Float = 2.0f) :
    DialogDiceSliderValues() {
    override fun values(): List<String> {
        return listOf(
            "d2",
            "d4",
            "d6",
            "d8",
            "d10",
            "d12",
            "d20"
        )
    }
}
