package com.github.jameshnsears.chance.ui.dialog.bag.card.dice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.jameshnsears.chance.data.domain.state.Dice
import com.github.jameshnsears.chance.data.repository.bag.RepositoryBagInterface
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CardDiceState(
    val diceTitle: String,
    val diceSidesSize: Int,
    var diceSidesPosition: Float,
    var diceColour: String,
    val diceCanBeDeleted: Boolean,
    val diceCanBeCloned: Boolean,
    val diceCanBeSaved: Boolean
)

class CardDiceViewModel(
    private val repositoryBag: RepositoryBagInterface,
    val dice: Dice
) : ViewModel() {

    private val _stateFlowCardDice = MutableStateFlow(
        CardDiceState(
            diceTitle = diceTitleInit(),
            diceSidesSize = dice.sides.size,
            diceSidesPosition = diceSidesPosition(dice.sides.size),
            diceColour = dice.colour,
            diceCanBeDeleted = false,
            diceCanBeCloned = false,
            diceCanBeSaved = true
        )
    )
    val stateFlowCardDice: StateFlow<CardDiceState> = _stateFlowCardDice

    private lateinit var diceTitlesHeldByOtherDice: List<String>

    init {
        viewModelScope.launch {
            _stateFlowCardDice.update {
                it.copy(
                    diceCanBeDeleted = diceCanBeDeleted(),
                )
            }

            diceTitlesHeldByOtherDice = diceTitlesUsedByOtherDice()
        }
    }

    private fun diceTitleInit() = dice.title

    fun diceTitle(title: String) {
        _stateFlowCardDice.update {
            it.copy(
                diceTitle = title,
                diceCanBeSaved = canDiceTitleBeSaved(title),
                diceCanBeCloned = canDiceTitleBeCloned(title)
            )
        }
    }

    private fun canDiceTitleBeSaved(diceTitle: String): Boolean {
        if (diceTitle.isBlank()) {
            return false
        }

        if (diceTitlesHeldByOtherDice.contains(diceTitle)) {
            return false
        }

        return true
    }

    private fun canDiceTitleBeCloned(diceTitle: String): Boolean {
        if (diceTitle.isBlank()) {
            return false
        }

        if (diceTitle == diceTitleInit()) {
            return false
        }

        if (diceTitlesHeldByOtherDice.contains(diceTitle)) {
            return false
        }

        return true
    }

    fun diceSidesPosition(diceSidesSize: Int): Float {
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

    fun diceSidesSize(sideSize: String) {
        val sideSizeInt = sideSize.toInt()

        _stateFlowCardDice.update {
            it.copy(
                diceSidesSize = sideSizeInt,
                diceSidesPosition = diceSidesPosition(sideSizeInt)
            )
        }

        viewModelScope.launch {
            CardDiceSideEvent.emit(sideSizeInt)
        }
    }

    fun diceColour(colour: String) {
        _stateFlowCardDice.update { it.copy(diceColour = colour) }
    }

    suspend fun diceCanBeDeleted() = repositoryBag.fetch().first().size > 1

    private suspend fun diceTitlesUsedByOtherDice(): List<String> {
        val diceTitlesUsedByOtherDice = mutableListOf<String>()

        repositoryBag.fetch().first().forEach {
            diceTitlesUsedByOtherDice.add(it.title)
        }

        diceTitlesUsedByOtherDice.remove(dice.title)

        return diceTitlesUsedByOtherDice
    }
}
