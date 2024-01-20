package com.github.jameshnsears.chance.ui.dialog.bag

import com.github.jameshnsears.chance.data.domain.Dice
import com.github.jameshnsears.chance.data.domain.Side
import com.github.jameshnsears.chance.data.repository.bag.BagModel
import kotlinx.coroutines.flow.StateFlow

interface DialogBagAndroidViewModelInterface {
    var bagModel: BagModel
    var sideNumber: StateFlow<Int>
    var sideColour: StateFlow<String>
    var sideDescription: StateFlow<String>
    var sideDescriptionColour: StateFlow<String>
    var diceSidesSliderPosition: StateFlow<Float>
    var diceTitle: StateFlow<String>
    var diceColour: StateFlow<String>
    fun dice(): Dice
    fun side(): Side
    fun sideColour(colour: String)
    fun mapSideDescription(): String
    fun sideDescription(sideDescription: String)
    fun sideDescriptionColour(colour: String)

    fun diceSidesSliderInitialPosition(diceSidesSize: Int): Float {
        return when (diceSidesSize) {
            2 -> 0.0f
            4 -> 1.0f
            6 -> 2.0f
            8 -> 3.0f
            10 -> 4.0f
            12 -> 5.0f
            else -> 6.0f
        }
    }

    fun diceSidesSliderPosition(position: Float)
    fun mapDiceTitle(): String
    fun diceTitle(title: String)
    fun diceColour(colour: String)
    fun diceCanBeDeleted(): Boolean
    fun getString(stringsId: Int): String
    fun save()
}
