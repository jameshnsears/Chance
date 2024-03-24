package com.github.jameshnsears.chance.ui.dialog.bag.card.roll

import androidx.lifecycle.ViewModel
import com.github.jameshnsears.chance.data.domain.state.Dice
import com.github.jameshnsears.chance.data.domain.state.DiceRollValues
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

data class CardRollState(
    var rollMultiplier: Boolean,
    var rollMultiplierValue: Int,
    var rollExplode: Boolean,
    var rollExplodeWhen: String,
    var rollExplodeAvailableValues: List<String>,
    var rollExplodeValue: Int,
    var rollModifyScore: Boolean,
    var rollModifyScoreValue: Int
)

class CardRollViewModel(
    val dice: Dice
) : ViewModel() {

    private val _stateFlow = MutableStateFlow(
        CardRollState(
            rollMultiplier = dice.multiplier,
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

    fun rollMultiplier(ticked: Boolean) {
        _stateFlow.update { it.copy(rollMultiplier = ticked) }
    }

    fun rollMultiplierValue(value: String) {
        _stateFlow.update { it.copy(rollMultiplierValue = value.toInt()) }
    }

    fun rollExplode(ticked: Boolean) {
        _stateFlow.update { it.copy(rollExplode = ticked) }
    }

    private fun rollExplodeSidesEquals()
        = (1..dice.sides.size).toList().map { it.toString() }

    fun rollExplodeWhen(equalityValue: String) {
        when (equalityValue) {
            DiceRollValues.explodeWhenValues[1] -> {
                _stateFlow.update {
                    it.copy(
                        rollExplodeWhen = equalityValue,
                        rollExplodeAvailableValues = (2..dice.sides.size).toList()
                            .map { it.toString() },
                        rollExplodeValue = 2
                    )
                }
            }

            DiceRollValues.explodeWhenValues[2] -> {
                _stateFlow.update {
                    it.copy(
                        rollExplodeWhen = equalityValue,
                        rollExplodeAvailableValues = (1..(dice.sides.size - 1)).toList()
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
