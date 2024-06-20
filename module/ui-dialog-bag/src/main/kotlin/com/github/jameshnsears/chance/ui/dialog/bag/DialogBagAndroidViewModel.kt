package com.github.jameshnsears.chance.ui.dialog.bag

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.github.jameshnsears.chance.data.domain.state.Dice
import com.github.jameshnsears.chance.data.domain.state.DiceBag
import com.github.jameshnsears.chance.data.domain.state.Side
import com.github.jameshnsears.chance.data.domain.utility.epoch.UtilityEpochTimeGenerator
import com.github.jameshnsears.chance.data.repository.bag.RepositoryBagInterface
import com.github.jameshnsears.chance.ui.dialog.bag.card.dice.CardDiceViewModel
import com.github.jameshnsears.chance.ui.dialog.bag.card.roll.CardRollViewModel
import com.github.jameshnsears.chance.ui.dialog.bag.card.side.CardSideAndroidViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.UUID

class DialogBagAndroidViewModel(
    application: Application,
    val repositoryBag: RepositoryBagInterface,
    val dice: Dice,
    val side: Side,
) : AndroidViewModel(application) {
    var cardSideAndroidViewModel = CardSideAndroidViewModel(application, side)

    var cardDiceViewModel = CardDiceViewModel(
        repositoryBag,
        dice
    )

    var cardRollViewModel = CardRollViewModel(dice)

    fun delete() {
        viewModelScope.launch {
            val updatedDiceBag: DiceBag = mutableListOf()

            repositoryBag.fetch().collect {
                it.forEach { diceInBag ->
                    if (diceInBag.epoch != dice.epoch) {
                        updatedDiceBag.add(diceInBag)
                    } else
                        Timber.d("delete.dice.epoch=${dice.epoch}")
                }
            }

            repositoryBag.store(updatedDiceBag)

            DialogBagCloseEvent.emit()
        }
    }

    fun clone() {
        viewModelScope.launch {

            val clonedDiceBag: DiceBag = mutableListOf()

            repositoryBag.fetch().collect {
                it.forEach { diceInBag ->
                    clonedDiceBag.add(diceInBag)

                    if (diceInBag.epoch == dice.epoch) {
                        val cardDice = cardDiceViewModel.stateFlowCardDice.value
                        val cardRoll = cardRollViewModel.stateFlowCardRoll.value

                        val newClonedDiceEpoch = UtilityEpochTimeGenerator.now()

                        Timber.d("clone.dice.epoch=${dice.epoch} -> $newClonedDiceEpoch")

                        clonedDiceBag.add(
                            Dice(
                                epoch = newClonedDiceEpoch,

                                sides = alignDiceSidesWithCardDice(),
                                title = cardDice.diceTitle,
                                colour = cardDice.diceColour,
                                selected = dice.selected,

                                multiplierValue = cardRoll.rollMultiplierValue,
                                explode = cardRoll.rollExplode,
                                explodeWhen = cardRoll.rollExplodeWhen,
                                explodeValue = cardRoll.rollExplodeValue,
                                modifyScore = cardRoll.rollModifyScore,
                                modifyScoreValue = cardRoll.rollModifyScoreValue
                            )
                        )
                    }
                }

                repositoryBag.store(clonedDiceBag)
            }

            DialogBagCloseEvent.emit()
        }
    }

    fun save() {
        viewModelScope.launch {

            val updatedDiceBag: DiceBag = mutableListOf()

            repositoryBag.fetch().collect {
                it.forEach { diceInBag ->

                    if (diceInBag.epoch == dice.epoch) {
                        Timber.d("save.dice.epoch=${dice.epoch}")

                        val cardDice = cardDiceViewModel.stateFlowCardDice.value
                        val cardRoll = cardRollViewModel.stateFlowCardRoll.value

                        val modifiedDice = Dice(
                            epoch = dice.epoch,

                            sides = alignDiceSidesWithCardDice(),
                            title = cardDice.diceTitle,
                            colour = cardDice.diceColour,

                            // user might have selected in roll selection
                            selected = diceInBag.selected,

                            multiplierValue = cardRoll.rollMultiplierValue,
                            explode = cardRoll.rollExplode,
                            explodeWhen = cardRoll.rollExplodeWhen,
                            explodeValue = cardRoll.rollExplodeValue,
                            modifyScore = cardRoll.rollModifyScore,
                            modifyScoreValue = cardRoll.rollModifyScoreValue
                        )

                        // if the # of sides has changed, then we've got a new dice
                        if (modifiedDice.sides.size != dice.sides.size) {
                            modifiedDice.epoch = UtilityEpochTimeGenerator.now()
                            Timber.d("save.dice.epoch=${dice.epoch} -> ${modifiedDice.epoch}")
                        }

                        updatedDiceBag.add(modifiedDice)

                    } else
                        updatedDiceBag.add(diceInBag)
                }
            }

            repositoryBag.store(updatedDiceBag)

            DialogBagCloseEvent.emit()
        }
    }

    fun alignDiceSidesWithCardDice(): List<Side> {
        val alignedSides: MutableList<Side>

        val originalDiceSides = dice.sides
        val dialogBagDiceSides = cardDiceViewModel.stateFlowCardDice.value.diceSidesSize

        if (dialogBagDiceSides == originalDiceSides.size) {
            alignedSides = originalDiceSides.toMutableList()

        } else if (dialogBagDiceSides < originalDiceSides.size) {
            alignedSides = originalDiceSides.reversed()
                .subList(0, dialogBagDiceSides)
                .reversed()
                .toMutableList()

        } else {
            alignedSides = originalDiceSides.toMutableList()
            for (newSideIndex in alignedSides.size + 1..dialogBagDiceSides) {
                alignedSides.add(
                    0,
                    Side(number = newSideIndex)
                )
            }
        }

        patchCurrentSideIfExists(alignedSides)

        return alignedSides
    }

    private fun patchCurrentSideIfExists(alignedSides: MutableList<Side>) {
        val cardSide = cardSideAndroidViewModel.stateFlowCardSide.value

        for (alignedSide in alignedSides) {
            if (alignedSide.uuid == side.uuid || cardSideAndroidViewModel.stateFlowCardSide.value.sideApplyToAll) {
                // force  compose recomposition
                alignedSide.uuid = UUID.randomUUID().toString()

                alignedSide.numberColour = cardSide.sideNumberColour
                alignedSide.imageDrawableId = cardSide.sideImageDrawableId
                alignedSide.imageBase64 = cardSide.sideImageBase64

                if (!cardSideAndroidViewModel.stateFlowCardSide.value.sideApplyToAll)
                    alignedSide.description = cardSide.sideDescription

                alignedSide.descriptionColour = cardSide.sideDescriptionColour
            }
        }
    }
}
