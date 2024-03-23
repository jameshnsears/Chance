package com.github.jameshnsears.chance.ui.dialog.bag.card.dice

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.github.jameshnsears.chance.data.domain.state.Dice
import com.github.jameshnsears.chance.data.repository.bag.RepositoryBagInterface
import com.github.jameshnsears.chance.ui.dialog.bag.card.CardAndroidViewModel
import com.github.jameshnsears.chance.ui.dialog.dice.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

data class CardDiceState(
    val diceTitle: String,
    val diceTitleIsUnique: Boolean,
    val diceSidesSize: Int,
    var diceSidesPosition: Float,
    var diceColour: String,
    var diceClone: Boolean,
    var diceDelete: Boolean,
    val diceCanBeDeleted: Boolean
)

class CardDiceAndroidViewModel(
    application: Application,
    private val repositoryBag: RepositoryBagInterface,
    val dice: Dice
) : CardAndroidViewModel(application) {

    private val _stateFlow = MutableStateFlow(
        CardDiceState(
            diceTitle = diceTitleInit(),
            diceTitleIsUnique = true,
            diceSidesSize = dice.sides.size,
            diceSidesPosition = diceSidesPosition(dice.sides.size),
            diceColour = dice.colour,
            diceClone = false,
            diceDelete = false,
            diceCanBeDeleted = false
        )
    )
    val stateFlowCardDice: StateFlow<CardDiceState> = _stateFlow

    private lateinit var diceTitlesHeldByOtherDice: List<String>

    init {
        viewModelScope.launch {
            _stateFlow.update {
                it.copy(
                    diceCanBeDeleted = diceCanBeDeleted(),
                )
            }
            diceTitlesHeldByOtherDice = diceTitlesHeldByOtherDice()
        }
    }

    private fun diceTitleInit(): String {
        return if (dice.title != "")
            dice.title
        else if (dice.titleStringsId != 0)
            getString(dice.titleStringsId)
        else
            "${getString(R.string.dialog_bag_dice_title_d)}$dice.sides.size"
    }

    fun diceTitle(title: String) {
        _stateFlow.update {
            it.copy(
                diceTitle = title,
                diceTitleIsUnique = !diceTitlesHeldByOtherDice.contains(title)
            )
        }
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
        Timber.d("sideSize=$sideSize")
        _stateFlow.update {
            it.copy(
                diceSidesSize = sideSize.toInt(),
                diceSidesPosition = diceSidesPosition(sideSize.toInt())
            )
        }
    }

    fun diceColour(colour: String) {
        _stateFlow.update { it.copy(diceColour = colour) }
    }

    suspend fun diceCanBeDeleted() = repositoryBag.fetch().first().size > 1

    fun diceClone(clone: Boolean) {
        _stateFlow.update { it.copy(diceClone = clone) }
    }

    fun diceDelete(delete: Boolean) {
        _stateFlow.update { it.copy(diceDelete = delete) }
    }

    private suspend fun diceTitlesHeldByOtherDice(): List<String> {
        val diceTitlesAlreadyPresent = mutableListOf<String>()

        repositoryBag.fetch().first().forEach {
            if (it.title == "")
                diceTitlesAlreadyPresent.add(getString(it.titleStringsId))
            else
                diceTitlesAlreadyPresent.add(it.title)
        }

        diceTitlesAlreadyPresent.remove(dice.title)

        return diceTitlesAlreadyPresent
    }
}
