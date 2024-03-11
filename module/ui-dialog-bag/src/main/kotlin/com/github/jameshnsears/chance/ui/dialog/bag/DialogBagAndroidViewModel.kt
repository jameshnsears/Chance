package com.github.jameshnsears.chance.ui.dialog.bag

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.github.jameshnsears.chance.data.domain.state.Dice
import com.github.jameshnsears.chance.data.domain.state.DiceBag
import com.github.jameshnsears.chance.data.domain.state.Side
import com.github.jameshnsears.chance.data.domain.utility.epoch.UtilityEpochTimeGenerator
import com.github.jameshnsears.chance.data.repository.bag.RepositoryBagInterface
import com.github.jameshnsears.chance.ui.dialog.bag.card.dice.BagCardDiceAndroidViewModel
import com.github.jameshnsears.chance.ui.dialog.bag.card.roll.BagCardRollViewModel
import com.github.jameshnsears.chance.ui.dialog.bag.card.side.BagCardSideAndroidViewModel
import kotlinx.coroutines.launch
import timber.log.Timber

class DialogBagAndroidViewModel(
    application: Application,
    val repositoryBag: RepositoryBagInterface,
    val dice: Dice,
    val side: Side,
) : AndroidViewModel(application) {
    var bagCardSideAndroidViewModel = BagCardSideAndroidViewModel(application, side)
    var bagCardDiceAndroidViewModel = BagCardDiceAndroidViewModel(application, repositoryBag, dice)
    var bagCardRollViewModel = BagCardRollViewModel(dice)

    fun save() {
        if (bagCardDiceAndroidViewModel.stateFlowDice.value.diceClone) {
            Timber.d("clone")
        } else if (bagCardDiceAndroidViewModel.stateFlowDice.value.diceDelete) {
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
                it.forEach { dice ->
                    if (dice.epoch == dice.epoch) {
                        dice.sides = dice.sides
                        dice.title = dice.title
                        dice.titleStringsId = dice.titleStringsId
                        dice.colour = dice.colour
                        dice.selected = dice.selected
                    }

                    updatedDiceBag.add(dice)
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
