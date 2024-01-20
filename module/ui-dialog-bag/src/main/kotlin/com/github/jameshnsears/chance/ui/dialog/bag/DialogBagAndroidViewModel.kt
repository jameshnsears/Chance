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
    override fun dice() = dice
    override fun side() = side

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

    private var _sideDesciptionColour = MutableStateFlow(side.descriptionColour)
    override var sideDescriptionColour: StateFlow<String> = _sideDesciptionColour

    override fun sideDescriptionColour(colour: String) {
        _sideDesciptionColour.value = colour
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

    override fun diceTitle(title: String) {
        _diceTitle.value = title
    }

    ////////////////////////////////

    /*
    _diceColour is a private and mutable variable that can be changed within the class

    diceColour is a public and immutable variable that can be accessed and observed from
    outside the class, but cannot be changed from outside the class.
     */
    private var _diceColour = MutableStateFlow(dice.colour)
    override var diceColour: StateFlow<String> = _diceColour

    override fun diceColour(colour: String) {
        _diceColour.value = colour
    }

    ////////////////////////////////

    suspend fun diceClone() = bagModel.diceClone(dice)

    override fun diceCanBeDeleted() = bagModel.diceCanBeDeleted()

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
