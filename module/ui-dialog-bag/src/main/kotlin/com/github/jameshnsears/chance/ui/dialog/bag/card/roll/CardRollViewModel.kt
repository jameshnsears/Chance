package com.github.jameshnsears.chance.ui.dialog.bag.card.roll

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.jameshnsears.chance.data.domain.core.Dice
import com.github.jameshnsears.chance.data.domain.core.DiceRollValues
import com.github.jameshnsears.chance.ui.dialog.bag.card.dice.CardDiceSideEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

data class CardRollState(
    var rollMultiplierValue: Int,
    var rollExplode: Boolean,
    var rollExplodeWhen: String,
    var rollExplodeAvailableValues: List<String>,
    var rollExplodeValue: Int,
    var rollModifyScore: Boolean,
    var rollModifyScoreValue: Int
)

class CardRollViewModel(
    val dice: Dice,
    private var diceSidesSize: Int = dice.sides.size
) : ViewModel() {
    private val _stateFlow = MutableStateFlow(
        CardRollState(
            rollMultiplierValue = dice.multiplierValue,

            rollExplode = dice.explode,
            rollExplodeWhen = dice.explodeWhen,
            rollExplodeAvailableValues = rollExplodeSidesEquals(),
            rollExplodeValue = dice.explodeValue,

            rollModifyScore = dice.modifyScore,
            rollModifyScoreValue = dice.modifyScoreValue
        )
    )

    val stateFlowCardRoll: StateFlow<CardRollState> = _stateFlow

    init {
        viewModelScope.launch {
            CardDiceSideEvent.sharedFlowDiceSide.collect {
                Timber.d("collect.CardDiceSideEvent=$it")
                diceSidesSize = it
                rollExplodeWhen(stateFlowCardRoll.value.rollExplodeWhen)
            }
        }
    }

    fun rollMultiplierValue(value: String) {
        _stateFlow.update { it.copy(rollMultiplierValue = value.toInt()) }
    }

    fun rollExplode(ticked: Boolean) {
        _stateFlow.update { it.copy(rollExplode = ticked) }
    }

    private fun rollExplodeSidesEquals() = (1..diceSidesSize).toList().map { it.toString() }

    fun rollExplodeWhen(equalityValue: String) {
        when (equalityValue) {
            DiceRollValues.explodeWhenValues[1] -> {
                _stateFlow.update { cardRollState ->
                    cardRollState.copy(
                        rollExplodeWhen = equalityValue,
                        rollExplodeAvailableValues = (2..diceSidesSize).toList()
                            .map { it.toString() },
                        rollExplodeValue = 2
                    )
                }
            }

            DiceRollValues.explodeWhenValues[2] -> {
                _stateFlow.update { cardRollState ->
                    cardRollState.copy(
                        rollExplodeWhen = equalityValue,
                        rollExplodeAvailableValues = (1..(diceSidesSize - 1)).toList()
                            .map { it.toString() },
                        rollExplodeValue = 1
                    )
                }
            }

            else -> {
                _stateFlow.update {
                    it.copy(
                        rollExplodeWhen = equalityValue,
                        rollExplodeAvailableValues = rollExplodeSidesEquals(),
                        rollExplodeValue = 1
                    )
                }
            }
        }
    }

    fun rollExplodeValue(value: String) {
        _stateFlow.update { it.copy(rollExplodeValue = value.toInt()) }
    }

    fun rollModifyScore(ticked: Boolean) {
        _stateFlow.update { it.copy(rollModifyScore = ticked) }
    }

    fun rollModifyScoreValue(value: String) {
        _stateFlow.update { it.copy(rollModifyScoreValue = value.toInt()) }
    }
}
