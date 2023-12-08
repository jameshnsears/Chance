package com.github.jameshnsears.chance.data.model

import com.github.jameshnsears.chance.data.domain.Dice
import com.github.jameshnsears.chance.data.domain.Side
import com.github.jameshnsears.chance.data.repository.dice.DiceRepository
import com.github.jameshnsears.chance.data.repository.dice.DiceRepositoryInterface
import timber.log.Timber

class DiceModel(
    private val diceRepository: DiceRepositoryInterface = DiceRepository
) {
    fun cloneDice(diceToClone: Dice) {
        var dice = fetchDice()
        dice += diceToClone
        store(dice)
    }

    fun canBeDeleted() = fetchDice().size > 1

    fun fetchDice(): List<Dice> {
        return diceRepository.fetch()
    }

    fun fetchDice(diceIndex: Int): Dice {
        val dice = fetchDice()
        if (diceIndex < 0 || diceIndex >= dice.size) {
            throw DiceIndexException()
        }
        return dice[diceIndex]
    }

    fun fetchDiceDescription(diceIndex: Int): String = fetchDice(diceIndex).description

    fun fetchSides(diceIndex: Int): List<Side> = fetchDice(diceIndex).sides

    fun fetchSide(diceIndex: Int, sideIndex: Int): Side {
        val sides = fetchDice(diceIndex).sides
        if (sideIndex < 0 || sideIndex >= sides.size) {
            throw SideIndexException()
        }

        return sides[sideIndex]
    }

    fun fetchDicePenaltyBonus(diceIndex: Int): Int = fetchDice(diceIndex).penaltyBonus

    fun store(
        diceIndex: Int,
        sides: Int,
        description: String,
        penaltyBonus: Int
    ) {
        Timber.d("diceIndex=$diceIndex")
        Timber.d("sids=$sides")
        Timber.d("description=", description)
        Timber.d("penaltyBonus=$penaltyBonus")

        val dice = fetchDice().map { it.copy() }.toList()

        dice[diceIndex].sides = (sides downTo 1).map { sideIndex ->
            Side(sideIndex = sideIndex)
        }
        dice[diceIndex].description = description
        dice[diceIndex].penaltyBonus = penaltyBonus

        store(dice)
    }

    fun store(allDice: List<Dice>) = diceRepository.store(allDice)
}
