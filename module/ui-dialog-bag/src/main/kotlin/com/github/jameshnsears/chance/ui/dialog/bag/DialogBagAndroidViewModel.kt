package com.github.jameshnsears.chance.ui.dialog.bag

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.github.jameshnsears.chance.data.domain.Dice
import com.github.jameshnsears.chance.data.domain.Side
import com.github.jameshnsears.chance.data.repository.bag.BagModel
import com.github.jameshnsears.chance.data.repository.bag.BagRepositoryInterface
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.math.round

class DialogBagAndroidViewModel(
    application: Application,
    bagRepository: BagRepositoryInterface,
    val dice: Dice,
    val side: Side
) : AndroidViewModel(
    application
), DialogBagAndroidViewModelInterface {
    override var bagModel: BagModel = BagModel(bagRepository)

    ////////////////////////////////

    private var _sideNumber = MutableStateFlow(side.number)
    override var sideNumber: StateFlow<Int> = _sideNumber

    ////////////////////////////////

    private var _sideColour = MutableStateFlow(side.colour)
    override var sideColour: StateFlow<String> = _sideColour

    override fun sideColour(colour: String) {
        _sideColour.value = colour
    }

    ////////////////////////////////

    private var _sideImageFilename = MutableStateFlow(side.imageFilename)
    override var sideImageFilename: StateFlow<String> = _sideImageFilename

    override fun sideImageFilename(imageFilename: String) {
        _sideImageFilename.value = imageFilename
    }

    ////////////////////////////////

    private var _sideDescription = MutableStateFlow(mapSideDescription())
    override var sideDescription: StateFlow<String> = _sideDescription

    override fun mapSideDescription(): String {
        return if (side.description != "")
            side.description
        else
            getString(side.descriptionStringsId)
    }

    override fun sideDescription(sideDescription: String) {
        _sideDescription.value = sideDescription
    }

    ////////////////////////////////

    private var _diceSidesSliderPosition =
        MutableStateFlow(diceSidesSliderInitialPosition(dice.sides.size))
    override var diceSidesSliderPosition: StateFlow<Float> = _diceSidesSliderPosition

    override fun diceSidesSliderPosition(position: Float) {
        Timber.d("position=$position")
        _diceSidesSliderPosition.value = round(position)
    }

    ////////////////////////////////

    private var _diceTitle = MutableStateFlow(mapDiceTitle())
    override var diceTitle: StateFlow<String> = _diceTitle

    override fun mapDiceTitle(): String {
        return if (dice.title != "")
            dice.title
        else
            getString(dice.titleStringsId)
    }

    override fun diceTitle(diceTitle: String) {
        _diceTitle.value = diceTitle
    }

    ////////////////////////////////

    private var _diceColour = MutableStateFlow(dice.colour)
    override var diceColour: StateFlow<String> = _diceColour

    override fun diceColour(diceColour: String) {
        _diceColour.value = diceColour
    }

    ////////////////////////////////

    suspend fun diceClone() = bagModel.diceClone(dice)

    private var _diceCanBeDeleted = MutableStateFlow(bagModel.diceCanBeDeleted())
    override var diceCanBeDeleted: StateFlow<Boolean> = _diceCanBeDeleted

    fun diceDelete() = bagModel.diceDelete(dice)

    override fun save() {
        viewModelScope.launch {
            bagModel.diceUpdate(
                dice
            )
        }
    }

    ////////////////////////////////

    override fun getString(stringsId: Int): String {
        return try {
            getApplication<Application>().getString(stringsId)
        } catch (e: NullPointerException) {
            // support for @Preview
            stringsId.toString()
        }
    }
}
