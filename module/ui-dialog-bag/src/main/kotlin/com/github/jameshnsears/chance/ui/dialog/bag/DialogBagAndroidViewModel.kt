package com.github.jameshnsears.chance.ui.dialog.bag

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.viewModelScope
import com.github.jameshnsears.chance.data.domain.core.Dice
import com.github.jameshnsears.chance.data.domain.core.Side
import com.github.jameshnsears.chance.data.domain.core.bag.DiceBag
import com.github.jameshnsears.chance.data.domain.core.roll.RollHistory
import com.github.jameshnsears.chance.data.domain.utility.epoch.UtilityEpochTimeGenerator
import com.github.jameshnsears.chance.data.repository.RepositoryFactory
import com.github.jameshnsears.chance.data.repository.bag.RepositoryBagInterface
import com.github.jameshnsears.chance.ui.dialog.bag.card.dice.CardDiceViewModel
import com.github.jameshnsears.chance.ui.dialog.bag.card.dice.CardDiceViewModelFactory
import com.github.jameshnsears.chance.ui.dialog.bag.card.roll.CardRollViewModel
import com.github.jameshnsears.chance.ui.dialog.bag.card.roll.CardRollViewModelFactory
import com.github.jameshnsears.chance.ui.dialog.bag.card.side.CardSideAndroidViewModel
import com.github.jameshnsears.chance.ui.dialog.bag.card.side.CardSideAndroidViewModelFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber

class DialogBagAndroidViewModel(
    application: Application,
    val repositoryBag: RepositoryBagInterface,
    val dice: Dice,
    val side: Side,
) : AndroidViewModel(application) {
    val repositoryRoll = RepositoryFactory(application).repositoryRoll

    var cardSideAndroidViewModel =
        ViewModelProvider(
            ViewModelStore(), CardSideAndroidViewModelFactory(
                application,
                side
            )
        ).get(CardSideAndroidViewModel::class.java)

    var cardDiceViewModel =
        ViewModelProvider(
            ViewModelStore(), CardDiceViewModelFactory(
                repositoryBag,
                dice
            )
        ).get(CardDiceViewModel::class.java)

    var cardRollViewModel =
        ViewModelProvider(
            ViewModelStore(), CardRollViewModelFactory(
                dice
            )
        ).get(CardRollViewModel::class.java)

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

            Timber.d("repositoryBag.store")
            repositoryBag.store(updatedDiceBag)

            updateRepositoryRollWithDeletedDice()

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

                        Timber.d("clone: dice.epoch=${dice.epoch} -> $newClonedDiceEpoch")

                        clonedDiceBag.add(
                            Dice(
                                epoch = newClonedDiceEpoch,

                                sides = updateRepositoryBagWithNewSizedDice(dice),
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

                Timber.d("repositoryBag.store")
                repositoryBag.store(clonedDiceBag)
            }

            DialogBagCloseEvent.emit()
        }
    }

    fun save() {
        viewModelScope.launch {
            saveRepositoryBag()

            updateRepositoryRollWhereDiceBeenDeleted()

            updateRepositoryRollWhereDiceSizeChanged()

            DialogBagCloseEvent.emit()
        }
    }

    private suspend fun saveRepositoryBag() {
        val updatedDiceBag: DiceBag = mutableListOf()

        repositoryBag.fetch().collect {
            it.forEach { diceInBag ->

                if (diceInBag.epoch == dice.epoch) {
                    Timber.d("save: dice.epoch=${dice.epoch}")

                    val cardDice = cardDiceViewModel.stateFlowCardDice.value
                    val cardRoll = cardRollViewModel.stateFlowCardRoll.value

                    val modifiedDice = Dice(
                        epoch = dice.epoch,

                        sides = updateRepositoryBagWithNewSizedDice(dice),
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

        Timber.d("repositoryBag.store")
        repositoryBag.store(updatedDiceBag)
    }

    fun updateRepositoryBagWithNewSizedDice(dice: Dice): List<Side> {
        val alignedSides: MutableList<Side>

        val originalDiceSides = dice.sides

        for (side in originalDiceSides)
            Timber.d("originalDiceSides: dice.epoch=${dice.epoch}; side.uuid=${side.uuid}")

        val diceSidesSize = cardDiceViewModel.stateFlowCardDice.value.diceSidesSize

        if (diceSidesSize == originalDiceSides.size)
            alignedSides = originalDiceSides.toMutableList()
        else if (diceSidesSize < originalDiceSides.size)
            alignedSides = originalDiceSides.reversed()
                .subList(0, diceSidesSize)
                .reversed()
                .toMutableList()
        else {
            alignedSides = originalDiceSides.toMutableList()
            for (newSideIndex in alignedSides.size + 1..diceSidesSize) {
                alignedSides.add(
                    0,
                    Side(number = newSideIndex)
                )
            }
        }

        updateRepositoryBag(alignedSides)

        for (side in alignedSides)
            Timber.d("alignedSides: dice.epoch=${dice.epoch}; side.uuid=${side.uuid}")

        return alignedSides
    }

    private fun updateRepositoryBag(alignedSides: MutableList<Side>) {
        val cardSide = cardSideAndroidViewModel.stateFlowCardSide.value

        for (alignedSide in alignedSides) {
            if (alignedSide.uuid == side.uuid) {
                alignedSide.numberColour = cardSide.sideNumberColour
                alignedSide.imageDrawableId = cardSide.sideImageDrawableId
                alignedSide.imageBase64 = cardSide.sideImageBase64
                alignedSide.description = cardSide.sideDescription
                alignedSide.descriptionColour = cardSide.sideDescriptionColour
            }

            if (cardSideAndroidViewModel.stateFlowCardSide.value.sideApplyToAllNumberColour)
                alignedSide.numberColour = cardSide.sideNumberColour

            if (cardSideAndroidViewModel.stateFlowCardSide.value.sideApplyToAllDescription) {
                alignedSide.description = cardSide.sideDescription
                alignedSide.descriptionColour = cardSide.sideDescriptionColour
            }

            if (cardSideAndroidViewModel.stateFlowCardSide.value.sideApplyToAllSvg) {
                alignedSide.imageDrawableId = cardSide.sideImageDrawableId
                alignedSide.imageBase64 = cardSide.sideImageBase64
            }
        }
    }

    private suspend fun updateRepositoryRollWithDeletedDice() {
        val diceBagEpochs: MutableList<Long> = diceBagEpochs()

        val currentRollHistory = repositoryRoll.fetch().first()

        val rollHistoryWithValidDice = RollHistory()

        currentRollHistory.keys.forEach { rollSequenceEpoch ->

            val rolls = currentRollHistory.getValue(rollSequenceEpoch)

            var diceEpochMissing = false
            rolls.forEach { roll ->
                if (!diceBagEpochs.contains(roll.diceEpoch)) {
                    diceEpochMissing = true
                }
            }

            if (!diceEpochMissing)
                rollHistoryWithValidDice[rollSequenceEpoch] = rolls
        }

        if (currentRollHistory.size != rollHistoryWithValidDice.size) {
            Timber.d("repositoryRoll.store")
            repositoryRoll.store(rollHistoryWithValidDice)
        }
    }

    private suspend fun updateRepositoryRollWhereDiceBeenDeleted() {
        val diceEpochsThatNoLongerInDiceBag: List<Long> =
            diceRollEpochs().minus(diceBagEpochs().toSet())

        val currentRollHistory = repositoryRoll.fetch().first()

        val rollHistoryWithValidDice = RollHistory()

        currentRollHistory.keys.forEach { rollSequenceEpoch ->

            val rolls = currentRollHistory.getValue(rollSequenceEpoch)

            var diceEpochMissing = false
            rolls.forEach { roll ->
                if (diceEpochsThatNoLongerInDiceBag.contains(roll.diceEpoch)) {
                    diceEpochMissing = true
                }
            }

            if (!diceEpochMissing)
                rollHistoryWithValidDice[rollSequenceEpoch] = rolls
        }

        if (currentRollHistory.size != rollHistoryWithValidDice.size) {
            Timber.d("repositoryRoll.store")
            repositoryRoll.store(rollHistoryWithValidDice)
        }
    }

    private suspend fun updateRepositoryRollWhereDiceSizeChanged() {
        val rollHistory = repositoryRoll.fetch().first()

        for (rollSequence in rollHistory) {
            rollSequence.value.forEach { roll ->
                Timber.d("roll.diceEpoch=${roll.diceEpoch}; roll.side.uuid=${roll.side.uuid}")
            }
        }

        rollHistory.keys.forEach { rollSequenceEpoch ->
            rollHistory.getValue(rollSequenceEpoch).forEach { roll ->
                val diceSides = repositoryBag.fetch(roll.diceEpoch).first().sides

                for (diceSide in diceSides) {
                    if (diceSide.uuid == roll.side.uuid) {
                        roll.side.numberColour = diceSide.numberColour
                        roll.side.imageDrawableId = diceSide.imageDrawableId
                        roll.side.imageBase64 = diceSide.imageBase64
                        roll.side.description = diceSide.description
                        roll.side.descriptionColour = diceSide.descriptionColour
                        break
                    }
                }
            }
        }

        Timber.d("repositoryRoll.store")
        repositoryRoll.store(rollHistory)

        for (rollSequence in rollHistory) {
            rollSequence.value.forEach { roll ->
                Timber.d("roll.diceEpoch=${roll.diceEpoch}; roll.side.uuid=${roll.side.uuid}")
            }
        }
    }

    private suspend fun diceBagEpochs(): MutableList<Long> {
        val diceBagEpochs: MutableList<Long> = mutableListOf()
        repositoryBag.fetch().first().forEach {
            diceBagEpochs.add(it.epoch)
        }
        return diceBagEpochs
    }

    private suspend fun diceRollEpochs(): MutableList<Long> {
        val currentRollHistory = repositoryRoll.fetch().first()
        val diceRollEpochs: MutableList<Long> = mutableListOf()

        currentRollHistory.keys.forEach { rollSequenceEpoch ->
            val rolls = currentRollHistory.getValue(rollSequenceEpoch)
            rolls.forEach { roll ->
                diceRollEpochs.add(roll.diceEpoch)
            }
        }
        return diceRollEpochs
    }
}
