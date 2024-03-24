package com.github.jameshnsears.chance.ui.dialog.bag

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.github.jameshnsears.chance.data.domain.state.Dice
import com.github.jameshnsears.chance.data.domain.state.DiceBag
import com.github.jameshnsears.chance.data.domain.state.Side
import com.github.jameshnsears.chance.data.domain.utility.epoch.UtilityEpochTimeGenerator
import com.github.jameshnsears.chance.data.repository.bag.RepositoryBagInterface
import com.github.jameshnsears.chance.ui.dialog.bag.card.dice.CardDiceAndroidViewModel
import com.github.jameshnsears.chance.ui.dialog.bag.card.roll.CardRollViewModel
import com.github.jameshnsears.chance.ui.dialog.bag.card.side.CardSideAndroidViewModel
import kotlinx.coroutines.launch
import timber.log.Timber

class DialogBagAndroidViewModel(
    application: Application,
    val repositoryBag: RepositoryBagInterface,
    val dice: Dice,
    val side: Side,
) : AndroidViewModel(application) {
    var cardSideAndroidViewModel = CardSideAndroidViewModel(application, side)
    var cardDiceAndroidViewModel = CardDiceAndroidViewModel(application, repositoryBag, dice)
    var cardRollViewModel = CardRollViewModel(dice)

    fun save() {
        if (cardDiceAndroidViewModel.stateFlowCardDice.value.diceClone) {
            Timber.d("clone")
        } else if (cardDiceAndroidViewModel.stateFlowCardDice.value.diceDelete) {
            Timber.d("delete")
        } else {
            Timber.d("update")
        }
    }

    fun update() {
        viewModelScope.launch {
            Timber.d("dice=$dice")

            val currentDiceBag = repositoryBag.fetch()

            val updatedDiceBag: DiceBag = mutableListOf()

            currentDiceBag.collect {
                it.forEach { diceUpdated ->
                    if (diceUpdated.epoch == dice.epoch) {
                        diceUpdated.sides = dice.sides
                        diceUpdated.title = dice.title
                        diceUpdated.titleStringsId = dice.titleStringsId
                        diceUpdated.colour = dice.colour
                        diceUpdated.selected = dice.selected
                    }

                    updatedDiceBag.add(diceUpdated)
                }
            }

            repositoryBag.store(updatedDiceBag)
        }
    }

    suspend fun clone(diceToClone: Dice) {
        Timber.d("diceToClone=$diceToClone")

        val clonedDiceBag: DiceBag = mutableListOf()

        repositoryBag.fetch().collect {
            it.forEach { dice ->
                clonedDiceBag.add(dice)

                if (dice.epoch == diceToClone.epoch) {
                    clonedDiceBag.add(
                        diceToClone.copy(
                            epoch = UtilityEpochTimeGenerator.now(),
                            title = dice.title + "+"
                        )
                    )
                }
            }

            repositoryBag.store(clonedDiceBag)
        }
    }

    suspend fun delete(diceToDelete: Dice) {
        Timber.d("diceToDelete=$diceToDelete")

        viewModelScope.launch {
            val updatedDiceBag: DiceBag = mutableListOf()

            repositoryBag.fetch().collect {
                it.forEach { dice ->
                    if (dice.epoch != diceToDelete.epoch) updatedDiceBag.add(dice)
                }
            }

            repositoryBag.store(updatedDiceBag)
        }
    }
}
