package com.github.jameshnsears.chance.ui.dialog.dice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.jameshnsears.chance.data.domain.Colour
import com.github.jameshnsears.chance.data.domain.Dice
import com.github.jameshnsears.chance.data.model.DiceModel
import com.github.jameshnsears.chance.data.repository.dice.DiceRepositoryInterface
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.math.round

open class DialogDiceViewModel(
    diceRepository: DiceRepositoryInterface,
    var diceIndex: Int
) : ViewModel() {
    private val diceModel = DiceModel(diceRepository)

    var _sliderSidesPosition = MutableStateFlow(fetchInitialSliderSidesPosition())
    var sliderSidesPosition: StateFlow<Float> = _sliderSidesPosition

    var _description = MutableStateFlow(fetchInitialDescription())
    var description: StateFlow<String> = _description

    var _sliderPenaltyBonusPosition =
        MutableStateFlow(fetchInitialSliderPenaltyBonusPosition())
    var sliderPenaltyBonusPosition: StateFlow<Float> = _sliderPenaltyBonusPosition

    fun fetchInitialSliderSidesPosition(): Float {
        return when (diceModel.fetchSides(diceIndex).size) {
            2 -> 0.0f
            4 -> 1.0f
            6 -> 2.0f
            8 -> 3.0f
            10 -> 4.0f
            12 -> 5.0f
            else -> 6.0f
        }
    }

    fun fetchCurrentSliderSidesPosition(): Int {
        return when (sliderSidesPosition.value) {
            0.0f -> 2
            1.0f -> 4
            2.0f -> 6
            3.0f -> 8
            4.0f -> 10
            5.0f -> 12
            else -> 20
        }
    }

    fun updateCurrentSliderSidesPosition(position: Float) {
        Timber.d("position=$position")
        _sliderSidesPosition.value = round(position)
    }

    fun fetchInitialDescription(): String = diceModel.fetchDiceDescription(diceIndex)

    fun fetchInitialSliderPenaltyBonusPosition(): Float {
        return when (diceModel.fetchDicePenaltyBonus(diceIndex)) {
            -3 -> 0.0f
            -2 -> 1.0f
            -1 -> 2.0f
            0 -> 3.0f
            1 -> 4.0f
            2 -> 5.0f
            else -> 6.0f
        }
    }

    fun fetchCurrentSliderPenaltyBonusPosition(): Int {
        return when (sliderPenaltyBonusPosition.value) {
            0.0f -> -3
            1.0f -> -2
            2.0f -> -1
            3.0f -> 0
            4.0f -> 1
            5.0f -> 2
            else -> 3
        }
    }

    fun updateCurrentDescription(description: String) {
        Timber.d("description=", description)
        _description.value = description
    }

    fun updateCurrentSliderPenaltyBonusPosition(position: Float) {
        Timber.d("position=$position")
        _sliderPenaltyBonusPosition.value = round(position)
    }

    fun ok() {
        viewModelScope.launch {
            diceModel.store(
                diceIndex,
                fetchCurrentSliderSidesPosition(),
                description.value,
                fetchCurrentSliderPenaltyBonusPosition()
            )
        }
    }

    fun fetchDice(diceIndex: Int): Dice = diceModel.fetchDice(diceIndex)

    /////////

    fun colour(colour: Colour) {
        TODO("Not yet implemented")
    }

    fun penaltyBonus(penaltyBonus: Int) {
        TODO("Not yet implemented")
    }

    fun clone() {
        TODO("Not yet implemented")
    }

    fun canBeDeleted(): Boolean = diceModel.canBeDeleted()

    fun delete() {
        TODO("Not yet implemented")
    }
}
