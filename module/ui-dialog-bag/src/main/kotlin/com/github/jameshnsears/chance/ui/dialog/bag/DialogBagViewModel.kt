package com.github.jameshnsears.chance.ui.dialog.bag

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.jameshnsears.chance.data.bag.model.BagModel
import com.github.jameshnsears.chance.data.bag.repository.BagRepositoryInterface
import com.github.jameshnsears.chance.data.domain.Colour
import com.github.jameshnsears.chance.data.domain.Dice
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.math.round

open class DialogBagViewModel(
    bagRepository: BagRepositoryInterface,
    var diceIndex: Int
) : ViewModel() {
    private val bagModel = BagModel(bagRepository)

    var _sliderSidesPosition = MutableStateFlow(fetchInitialSliderSidesPosition())
    var sliderSidesPosition: StateFlow<Float> = _sliderSidesPosition

    var _diceTitle = MutableStateFlow(fetchInitialDiceTitle())
    var diceTitle: StateFlow<String> = _diceTitle

    fun fetchInitialSliderSidesPosition(): Float {
        return when (bagModel.sides(diceIndex).size) {
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

    private fun fetchInitialDiceTitle(): String = bagModel.diceDescription(diceIndex)

    fun updateDiceTitle(diceTitle: String) {
        Timber.d("diceTitle=", diceTitle)
        _diceTitle.value = diceTitle
    }

    fun ok() {
        viewModelScope.launch {
            bagModel.store(
                diceIndex,
                fetchCurrentSliderSidesPosition(),
                diceTitle.value,
            )
        }
    }

    fun fetchDice(diceIndex: Int): Dice = bagModel.dice(diceIndex)

    /////////

    fun colour(colour: Colour) {
        TODO("Not yet implemented")
    }

    fun clone() {
        TODO("Not yet implemented")
    }

    fun canBeDeleted(): Boolean = bagModel.diceCanBeDeleted()

    fun delete() {
        TODO("Not yet implemented")
    }
}
