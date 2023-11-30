package com.github.jameshnsears.chance.ui.dialog.dice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.jameshnsears.chance.data.domain.Colour
import com.github.jameshnsears.chance.data.model.DiceModel
import com.github.jameshnsears.chance.data.repository.dice.DiceRepositoryInterface
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.math.round

class DialogDiceViewModel(
    diceRepository: DiceRepositoryInterface,
    val diceIndex: Int = 0
) : ViewModel() {
    val diceModel = DiceModel(diceRepository)

    private val _sliderSidesPosition = MutableStateFlow(fetchInitialSliderSidesPosition())
    var sliderSidesPosition: StateFlow<Float> = _sliderSidesPosition

    private val _description = MutableStateFlow(fetchInitialDescription())
    var description: StateFlow<String> = _description

    private val _sliderPenaltyBonusPosition =
        MutableStateFlow(fetchInitialSliderPenaltyBonusPosition())
    var sliderPenaltyBonusPosition: StateFlow<Float> = _sliderPenaltyBonusPosition


    private fun fetchInitialSliderSidesPosition(): Float {
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
        _sliderSidesPosition.value = round(position)
    }

    private fun fetchInitialDescription(): String = diceModel.fetchDiceDescription(diceIndex)

    private fun fetchInitialSliderPenaltyBonusPosition(): Float {
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
        _description.value = description
    }

    fun updateCurrentSliderPenaltyBonusPosition(position: Float) {
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

    /////////

    fun colour(colour: Colour) {
        TODO("Not yet implemented")
    }

    fun penaltyBonus(penaltyBonus: Int) {
        TODO("Not yet implemented")
    }

    fun canBeCloned(): Boolean {
        TODO("Not yet implemented")
    }

    fun clone() {
        TODO("Not yet implemented")
    }

    fun canBeDeleted(): Boolean {
        TODO("Not yet implemented")
    }

    fun delete() {
        TODO("Not yet implemented")
    }
}
