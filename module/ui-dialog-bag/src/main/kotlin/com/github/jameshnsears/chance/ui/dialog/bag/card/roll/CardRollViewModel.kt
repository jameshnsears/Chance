package com.github.jameshnsears.chance.ui.dialog.bag.card.roll

import androidx.lifecycle.ViewModel
import com.github.jameshnsears.chance.data.domain.state.Dice
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

data class CardRollState(
    var rollMultiplier: Boolean,
    var rollMultiplierValue: Int,
    var rollExplode: Boolean,
    var rollExplodeWhen: String,
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

    fun rollExplodeWhen(value: String) {
        _stateFlow.update { it.copy(rollExplodeWhen = value) }
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
