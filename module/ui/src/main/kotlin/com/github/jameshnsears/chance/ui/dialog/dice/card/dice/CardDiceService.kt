package com.github.jameshnsears.chance.ui.dialog.dice.card.dice

import com.github.jameshnsears.chance.data.domain.core.Dice
import com.github.jameshnsears.chance.data.domain.core.DiceRollValues
import com.github.jameshnsears.chance.data.repo.api.bag.RepositoryBagInterface
import com.github.jameshnsears.chance.data.repo.api.roll.RepositoryRollInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CardDiceState(
    val diceTitle: String,
    val diceSidesSize: Int,
    var diceSiderInfoColour: Float,
    var diceColour: String,
    val diceCanBeDeleted: Boolean,
    val diceCanBeCloned: Boolean,
    val diceCanBeSaved: Boolean
)

class CardDiceService(
    private val repositoryBag: RepositoryBagInterface,
    private val repositoryRoll: RepositoryRollInterface,
    val dice: Dice,
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    private val _stateFlowCardDice = MutableStateFlow(
        CardDiceState(
            diceTitle = diceTitleInit(),
            diceSidesSize = dice.sides.size,
            diceSiderInfoColour = 0.38f,
            diceColour = dice.colour,
            diceCanBeDeleted = false,
            diceCanBeCloned = false,
            diceCanBeSaved = true
        )
    )
    val stateFlowCardDice: StateFlow<CardDiceState> = _stateFlowCardDice

    private var diceTitlesHeldByOtherDice: List<String> = emptyList()

    private var isSidesValid = true

    init {
        scope.launch {
            _stateFlowCardDice.update {
                it.copy(
                    diceCanBeDeleted = diceCanBeDeleted(),
                    diceCanBeSaved = canDiceTitleBeSaved(it.diceTitle)
                )
            }

            diceTitlesHeldByOtherDice = diceTitlesUsedByOtherDice()
        }
    }

    fun refresh(dice: Dice) {
        isSidesValid = true
        _stateFlowCardDice.update {
            it.copy(
                diceTitle = dice.title,
                diceSidesSize = dice.sides.size,
                diceSiderInfoColour = 0.38f,
                diceColour = dice.colour,
                diceCanBeDeleted = false,
                diceCanBeCloned = false,
                diceCanBeSaved = true
            )
        }

        scope.launch {
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
                diceCanBeSaved = canDiceTitleBeSaved(title) && isSidesValid,
                diceCanBeCloned = canDiceTitleBeCloned(title) && isSidesValid
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

    fun diceSidesSize(sideSize: String) {
        val sideSizeInt = sideSize.toIntOrNull()
        val sideSizeChanged = sideSizeInt != null && sideSizeInt != dice.sides.size
        isSidesValid = sideSizeInt != null && sideSizeInt in DiceRollValues.SIDES_MIN..DiceRollValues.SIDES_MAX

        _stateFlowCardDice.update {
            it.copy(
                diceSidesSize = sideSizeInt ?: it.diceSidesSize,
                diceSiderInfoColour = if (isSidesValid && sideSizeInt != dice.sides.size) 1.0f else 0.38f,
                diceCanBeSaved = canDiceTitleBeSaved(it.diceTitle) && isSidesValid,
                diceCanBeCloned = canDiceTitleBeCloned(it.diceTitle) && isSidesValid
            )
        }

        if (isSidesValid) {
            scope.launch {
                CardDiceSideEvent.emit(sideSizeInt!!)

                if (sideSizeChanged) {
                    repositoryRoll.delete(dice.uuid)
                }
            }
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
