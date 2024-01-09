package com.github.jameshnsears.chance.ui.dialog.bag.card

class BagCardDiceSliderSidesTestDouble(override val defaultValue: Float = 2.0f) :
    BagCardDiceSliderValues() {
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
