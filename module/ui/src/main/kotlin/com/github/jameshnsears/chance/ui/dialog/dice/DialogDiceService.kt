package com.github.jameshnsears.chance.ui.dialog.dice

import com.github.jameshnsears.chance.data.domain.core.Dice
import com.github.jameshnsears.chance.data.domain.core.Side
import com.github.jameshnsears.chance.data.domain.core.bag.DiceBag
import com.github.jameshnsears.chance.data.domain.core.group.Group
import com.github.jameshnsears.chance.data.domain.core.roll.RollHistory
import com.github.jameshnsears.chance.data.repo.api.bag.RepositoryBagInterface
import com.github.jameshnsears.chance.data.repo.api.group.RepositoryGroupInterface
import com.github.jameshnsears.chance.data.repo.api.roll.RepositoryRollInterface
import com.github.jameshnsears.chance.ui.dialog.dice.card.dice.CardDiceState
import com.github.jameshnsears.chance.ui.dialog.dice.card.face.CardSideState
import com.github.jameshnsears.chance.ui.dialog.dice.card.roll.CardRollState
import com.github.jameshnsears.chance.ui.tab.DisplayIndexEvent
import com.github.jameshnsears.chance.ui.tab.GroupEvent
import kotlinx.coroutines.flow.first
import timber.log.Timber
import java.util.UUID

class DialogDiceService(
    private val repositoryBag: RepositoryBagInterface,
    private val repositoryRoll: RepositoryRollInterface,
    private val repositoryGroup: RepositoryGroupInterface
) {

    suspend fun delete(dice: Dice) {
        val updatedDiceBag: DiceBag = mutableListOf()

        val diceBag = repositoryBag.fetch().first()
        diceBag.forEach { diceInBag ->
            if (diceInBag.uuid != dice.uuid) {
                updatedDiceBag.add(diceInBag)
            } else
                Timber.d("delete.dice.uuid=${dice.uuid}")
        }

        repositoryBag.store(updatedDiceBag)

        updateRepositoryRollWithDeletedDice()
        updateRepositoryGroupWhereDiceBeenDeleted()
    }

    suspend fun clone(dice: Dice, cardDiceState: CardDiceState, cardRollState: CardRollState) {
        val clonedDiceBag: DiceBag = mutableListOf()

        val diceBag = repositoryBag.fetch().first()
        diceBag.forEach { diceInBag ->
            clonedDiceBag.add(diceInBag)

            if (diceInBag.uuid == dice.uuid) {
                val newClonedDiceUuid = UUID.randomUUID().toString()

                Timber.d("clone: dice.uuid=${dice.uuid} -> $newClonedDiceUuid")

                clonedDiceBag.add(
                    Dice(
                        uuid = newClonedDiceUuid,
                        sides = updateRepositoryBagWithNewSizedDice(dice, cardDiceState.diceSidesSize),
                        title = cardDiceState.diceTitle,
                        colour = cardDiceState.diceColour,
                        selected = dice.selected,
                        multiplierValue = cardRollState.rollMultiplierValue,
                        explode = cardRollState.rollExplode,
                        explodeWhen = cardRollState.rollExplodeWhen,
                        explodeValue = cardRollState.rollExplodeValue,
                        modifyScore = cardRollState.rollModifyScore,
                        modifyScoreValue = cardRollState.rollModifyScoreValue,
                        displayIndex = diceInBag.displayIndex
                    )
                )
            }
        }

        // Update displayIndex for all dice in the bag
        clonedDiceBag.forEachIndexed { index, diceInBag ->
            diceInBag.displayIndex = index
        }

        repositoryBag.store(clonedDiceBag)

        DisplayIndexEvent.emit()
    }

    suspend fun save(
        dice: Dice,
        side: Side,
        cardDiceState: CardDiceState,
        cardRollState: CardRollState,
        cardSideState: CardSideState
    ) {
        val modifiedDice = saveRepositoryBag(dice, side, cardDiceState, cardRollState, cardSideState)

        if (modifiedDice != null && modifiedDice.uuid != dice.uuid) {
            migrateRepositoryGroupUuid(dice.uuid, modifiedDice.uuid)
        }

        updateRepositoryRollWhereDiceBeenDeleted()
        updateRepositoryGroupWhereDiceBeenDeleted()

        updateRepositoryRollWhereDiceSizeChanged()
    }

    private suspend fun saveRepositoryBag(
        dice: Dice,
        side: Side,
        cardDiceState: CardDiceState,
        cardRollState: CardRollState,
        cardSideState: CardSideState
    ): Dice? {
        val updatedDiceBag: DiceBag = mutableListOf()
        var modifiedDiceResult: Dice? = null

        val diceBag = repositoryBag.fetch().first()
        diceBag.forEach { diceInBag ->

            if (diceInBag.uuid == dice.uuid) {
                Timber.d("save: dice.uuid=${dice.uuid}")

                val modifiedDice = Dice(
                    uuid = dice.uuid,
                    sides = updateRepositoryBagWithNewSizedDice(
                        dice,
                        cardDiceState.diceSidesSize,
                        side,
                        cardSideState
                    ),
                    title = cardDiceState.diceTitle,
                    colour = cardDiceState.diceColour,

                    // user might have selected in roll selection
                    selected = diceInBag.selected,

                    multiplierValue = cardRollState.rollMultiplierValue,
                    explode = cardRollState.rollExplode,
                    explodeWhen = cardRollState.rollExplodeWhen,
                    explodeValue = cardRollState.rollExplodeValue,
                    modifyScore = cardRollState.rollModifyScore,
                    modifyScoreValue = cardRollState.rollModifyScoreValue,
                    displayIndex = diceInBag.displayIndex
                )

                // if the # of sides has changed, then we've got a new dice
                if (modifiedDice.sides.size != dice.sides.size) {
                    modifiedDice.uuid = UUID.randomUUID().toString()
                    Timber.d("save.dice.uuid=${dice.uuid} -> ${modifiedDice.uuid}")
                }

                modifiedDiceResult = modifiedDice
                updatedDiceBag.add(modifiedDice)

            } else
                updatedDiceBag.add(diceInBag)
        }

        repositoryBag.store(updatedDiceBag)

        return modifiedDiceResult
    }

    fun updateRepositoryBagWithNewSizedDice(
        dice: Dice,
        diceSidesSize: Int,
        side: Side? = null,
        cardSideState: CardSideState? = null
    ): List<Side> {
        val alignedSides: MutableList<Side>

        val originalDiceSides = dice.sides

        for (s in originalDiceSides)
            Timber.d("originalDiceSides: dice.uuid=${dice.uuid}; side.uuid=${s.uuid}")

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

        if (side != null && cardSideState != null) {
            updateRepositoryBag(alignedSides, side, cardSideState)
        }

        for (s in alignedSides)
            Timber.d("alignedSides: dice.uuid=${dice.uuid}; side.uuid=${s.uuid}")

        return alignedSides
    }

    private fun updateRepositoryBag(alignedSides: MutableList<Side>, side: Side, cardSideState: CardSideState) {
        for (alignedSide in alignedSides) {
            if (alignedSide.uuid == side.uuid) {
                alignedSide.numberColour = cardSideState.sideNumberColour
                alignedSide.imageDrawableId = cardSideState.sideImageDrawableId
                alignedSide.imageBase64 = cardSideState.sideImageBase64
                alignedSide.description = cardSideState.sideDescription
                alignedSide.descriptionColour = cardSideState.sideDescriptionColour
            }

            if (cardSideState.sideApplyToAllNumberColour)
                alignedSide.numberColour = cardSideState.sideNumberColour

            if (cardSideState.sideApplyToAllDescription) {
                alignedSide.description = cardSideState.sideDescription
                alignedSide.descriptionColour = cardSideState.sideDescriptionColour
            }

            if (cardSideState.sideApplyToAllSvg) {
                alignedSide.imageDrawableId = cardSideState.sideImageDrawableId
                alignedSide.imageBase64 = cardSideState.sideImageBase64
            }
        }
    }

    private suspend fun updateRepositoryRollWithDeletedDice() {
        val diceBagUuids: MutableList<String> = diceBagUuids()

        val currentRollHistory = repositoryRoll.fetch().first()

        val rollHistoryWithValidDice = RollHistory()

        currentRollHistory.keys.forEach { rollSequenceEpoch ->

            val rolls = currentRollHistory.getValue(rollSequenceEpoch)

            var diceUuidMissing = false
            rolls.forEach { roll ->
                if (!diceBagUuids.contains(roll.uuidDice)) {
                    diceUuidMissing = true
                }
            }

            if (!diceUuidMissing)
                rollHistoryWithValidDice[rollSequenceEpoch] = rolls
        }

        if (currentRollHistory.size != rollHistoryWithValidDice.size) {
            Timber.d("repositoryRoll.store")
            repositoryRoll.store(rollHistoryWithValidDice)
        }
    }

    private suspend fun updateRepositoryGroupWhereDiceBeenDeleted() {
        val diceBagUuids = diceBagUuids().toSet()
        val currentGroupHistory = repositoryGroup.fetch().first()
        val updatedGroupHistory = mutableListOf<Group>()

        currentGroupHistory.forEach { group ->
            val updatedUuidDice = group.uuidDice.filter { diceBagUuids.contains(it) }
            if (updatedUuidDice.isNotEmpty()) {
                updatedGroupHistory.add(group.copy(uuidDice = updatedUuidDice))
            }
        }

        if (currentGroupHistory.size != updatedGroupHistory.size ||
            currentGroupHistory.zip(updatedGroupHistory)
                .any { it.first.uuidDice.size != it.second.uuidDice.size }
        ) {
            Timber.d("repositoryGroup.store")
            repositoryGroup.store(updatedGroupHistory)
            GroupEvent.emit()
        }
    }

    private suspend fun migrateRepositoryGroupUuid(oldUuid: String, newUuid: String) {
        val currentGroupHistory = repositoryGroup.fetch().first()
        val updatedGroupHistory = currentGroupHistory.map { group ->
            val updatedUuidDice = group.uuidDice.map { uuid ->
                if (uuid == oldUuid) newUuid else uuid
            }
            group.copy(uuidDice = updatedUuidDice)
        }

        if (currentGroupHistory != updatedGroupHistory) {
            Timber.d("migrateRepositoryGroupUuid.store")
            repositoryGroup.store(updatedGroupHistory)
            GroupEvent.emit()
        }
    }

    private suspend fun updateRepositoryRollWhereDiceBeenDeleted() {
        val diceUuidsThatNoLongerInDiceBag: List<String> =
            diceRollUuids().minus(diceBagUuids().toSet())

        val currentRollHistory = repositoryRoll.fetch().first()

        val rollHistoryWithValidDice = RollHistory()

        currentRollHistory.keys.forEach { rollSequenceEpoch ->

            val rolls = currentRollHistory.getValue(rollSequenceEpoch)

            var diceUuidMissing = false
            rolls.forEach { roll ->
                if (diceUuidsThatNoLongerInDiceBag.contains(roll.uuidDice)) {
                    diceUuidMissing = true
                }
            }

            if (!diceUuidMissing)
                rollHistoryWithValidDice[rollSequenceEpoch] = rolls
        }

        if (currentRollHistory.size != rollHistoryWithValidDice.size) {
            Timber.d("repositoryRoll.store")
            repositoryRoll.store(rollHistoryWithValidDice)
        }
    }

    private suspend fun updateRepositoryRollWhereDiceSizeChanged() {
        val rollHistory = repositoryRoll.fetch().first()

        val diceBag = repositoryBag.fetch().first()

        rollHistory.keys.forEach { rollSequenceEpoch ->
            rollHistory.getValue(rollSequenceEpoch).forEach { roll ->
                val diceSides = diceBag.find { it.uuid == roll.uuidDice }?.sides ?: emptyList()

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
    }

    private suspend fun diceBagUuids(): MutableList<String> {
        val diceBagUuids: MutableList<String> = mutableListOf()
        repositoryBag.fetch().first().forEach {
            diceBagUuids.add(it.uuid)
        }
        return diceBagUuids
    }

    private suspend fun diceRollUuids(): MutableList<String> {
        val currentRollHistory = repositoryRoll.fetch().first()
        val diceRollUuids: MutableList<String> = mutableListOf()

        currentRollHistory.keys.forEach { rollSequenceEpoch ->
            val rolls = currentRollHistory.getValue(rollSequenceEpoch)
            rolls.forEach { roll ->
                diceRollUuids.add(roll.uuidDice)
            }
        }
        return diceRollUuids
    }
}
