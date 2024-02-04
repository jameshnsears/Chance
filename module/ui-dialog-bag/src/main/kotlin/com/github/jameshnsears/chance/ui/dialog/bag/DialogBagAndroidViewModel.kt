package com.github.jameshnsears.chance.ui.dialog.bag

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.github.jameshnsears.chance.data.domain.Dice
import com.github.jameshnsears.chance.data.domain.Side
import com.github.jameshnsears.chance.data.repository.bag.BagModel
import com.github.jameshnsears.chance.data.repository.bag.DiceBagRepositoryInterface
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.math.round

class DialogBagAndroidViewModel(
    application: Application,
    bagRepository: DiceBagRepositoryInterface,
    val dice: Dice,
    val side: Side,
) : AndroidViewModel(application) {
    private var bagModel = BagModel(bagRepository)

    private var _diceCanBeDeleted = MutableStateFlow(false)
    var diceCanBeDeleted: StateFlow<Boolean> = _diceCanBeDeleted

    ////////////////////////////////

    init {
        viewModelScope.launch {
            _diceCanBeDeleted.value = bagModel.canBeDeleted()
        }
    }

    ////////////////////////////////

    private var _sideDescription = MutableStateFlow(mapSideDescription())
    var sideDescription: StateFlow<String> = _sideDescription

    private fun mapSideDescription(): String {
        return if (side.description != "")
            side.description
        else
            getString(side.descriptionStringsId)
    }

    fun sideDescription(sideDescription: String) {
        _sideDescription.value = sideDescription
    }

    ////////////////////////////////

    private fun diceSidesSliderInitialPosition(diceSidesSize: Int): Float {
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

    ////////////////////////////////

    private var _sideNumber = MutableStateFlow(side.number)
    var sideNumber: StateFlow<Int> = _sideNumber

    ////////////////////////////////

    private var _sideColour = MutableStateFlow(side.colour)
    var sideColour: StateFlow<String> = _sideColour

    fun sideColour(colour: String) {
        _sideColour.value = colour
    }

    ////////////////////////////////

    private var _sideDescriptionColour = MutableStateFlow(side.descriptionColour)
    var sideDescriptionColour: StateFlow<String> = _sideDescriptionColour

    fun sideDescriptionColour(colour: String) {
        _sideDescriptionColour.value = colour
    }

    ////////////////////////////////

    private var _diceSidesSliderPosition =
        MutableStateFlow(diceSidesSliderInitialPosition(dice.sides.size))
    var diceSidesSliderPosition: StateFlow<Float> = _diceSidesSliderPosition

    fun diceSidesSliderPosition(position: Float) {
        Timber.d("position=$position")
        _diceSidesSliderPosition.value = round(position)
    }

    ////////////////////////////////

    private var _diceTitle = MutableStateFlow(mapDiceTitle())
    var diceTitle: StateFlow<String> = _diceTitle

    private fun mapDiceTitle(): String {
        return if (dice.title != "")
            dice.title
        else
            getString(dice.titleStringsId)
    }

    fun diceTitle(title: String) {
        _diceTitle.value = title
    }

    ////////////////////////////////

    private var _diceColour = MutableStateFlow(dice.colour)
    var diceColour: StateFlow<String> = _diceColour

    fun diceColour(colour: String) {
        _diceColour.value = colour
    }

    ////////////////////////////////

    suspend fun diceClone() = bagModel.clone(dice)

    ////////////////////////////////

    fun diceDelete() {
        viewModelScope.launch {
            bagModel.delete(dice)
        }
    }

    fun save() {
        viewModelScope.launch {
            bagModel.save(
                dice,
            )
        }
    }

    ////////////////////////////////

    private fun getString(stringsId: Int): String {
        return try {
            getApplication<Application>().getString(stringsId)
        } catch (e: NullPointerException) {
            // support for @Preview
            stringsId.toString()
        }
    }
}
