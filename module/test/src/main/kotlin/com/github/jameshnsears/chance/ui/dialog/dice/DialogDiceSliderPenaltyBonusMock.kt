package com.github.jameshnsears.chance.ui.dialog.dice

class DialogDiceSliderPenaltyBonusMock(override val defaultValue: Float = 3.0f) :
    DialogDiceSliderValues() {
    override fun values(): List<String> {
        return listOf(
            "-3",
            "-2",
            "-1",
            "0",
            "+1",
            "+2",
            "+3"
        )
    }
}
