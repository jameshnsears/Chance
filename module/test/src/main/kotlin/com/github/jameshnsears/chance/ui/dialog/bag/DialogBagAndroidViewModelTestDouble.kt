package com.github.jameshnsears.chance.ui.dialog.bag

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.github.jameshnsears.chance.data.domain.Dice
import com.github.jameshnsears.chance.data.domain.Side
import com.github.jameshnsears.chance.data.repository.bag.BagModel
import com.github.jameshnsears.chance.data.repository.bag.BagRepositoryTestDouble
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class DialogBagAndroidViewModelTestDouble(
    application: Application,
    bagRepository: BagRepositoryTestDouble,
    val dice: Dice,
    val side: Side
) : AndroidViewModel(
    application
), DialogBagAndroidViewModelInterface {
    override var bagModel: BagModel = BagModel(bagRepository)

    override var sideNumber: StateFlow<Int> = MutableStateFlow(side.number)

    override var sideColour: StateFlow<String> = MutableStateFlow(side.colour)

    override var sideImageFilename: StateFlow<String> = MutableStateFlow(side.imageFilename)

    override var sideDescription: StateFlow<String> = MutableStateFlow(mapSideDescription())

    override fun mapSideDescription(): String {
        return if (side.description != "")
            side.description
        else
            side.descriptionStringsId.toString()
    }

    override var diceSidesSliderPosition: StateFlow<Float> =
        MutableStateFlow(diceSidesSliderInitialPosition(dice.sides.size))

    override var diceTitle: StateFlow<String> = MutableStateFlow(mapDiceTitle())

    override fun mapDiceTitle(): String {
        return if (dice.title != "")
            dice.title
        else
            dice.titleStringsId.toString()
    }

    override var diceColour: StateFlow<String> = MutableStateFlow(dice.colour)

    private var _diceCanBeDeleted = MutableStateFlow(bagModel.diceCanBeDeleted())
    override var diceCanBeDeleted: StateFlow<Boolean> = _diceCanBeDeleted

    override fun sideColour(colour: String) {}

    override fun sideImageFilename(imageFilename: String) {}

    override fun sideDescription(sideDescription: String) {}

    override fun diceSidesSliderPosition(position: Float) {}

    override fun diceTitle(diceTitle: String) {}

    override fun diceColour(diceColour: String) {}

    override fun getString(stringsId: Int): String {
        return ""
    }

    override fun save() {}
}
