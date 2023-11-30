package com.github.jameshnsears.chance.ui.dialog.dice

class DialogDiceSliderSidesMock(override val defaultValue: Float = 2.0f) :
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
