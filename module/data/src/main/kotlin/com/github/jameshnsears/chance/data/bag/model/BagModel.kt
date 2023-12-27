package com.github.jameshnsears.chance.data.bag.model

import com.github.jameshnsears.chance.data.bag.repository.BagRepositoryImpl
import com.github.jameshnsears.chance.data.bag.repository.BagRepositoryInterface
import com.github.jameshnsears.chance.data.domain.Dice
import com.github.jameshnsears.chance.data.domain.Side
import timber.log.Timber

class BagModel(
    private val bagRepository: BagRepositoryInterface = BagRepositoryImpl
) {
    fun diceClone(diceToClone: Dice) {
        var dice = dice()
        dice += diceToClone
        store(dice)
    }

    fun diceCanBeDeleted() = dice().size > 1

    fun dice(): List<Dice> {
        return bagRepository.fetch()
    }

    fun dice(diceIndex: Int): Dice {
        val dice = dice()
        if (diceIndex < 0 || diceIndex >= dice.size) {
            throw BagModelException()
        }
        return dice[diceIndex]
    }

    fun diceDescription(diceIndex: Int): String = dice(diceIndex).title

    fun sides(diceIndex: Int): List<Side> = dice(diceIndex).sides

    fun side(diceIndex: Int, sideIndex: Int): Side {
        val sides = dice(diceIndex).sides
        if (sideIndex < 0 || sideIndex >= sides.size) {
            throw BagModelException()
        }

        return sides[sideIndex]
    }

    fun store(
        diceIndex: Int,
        sides: Int,
        description: String,
    ) {
        Timber.d("diceIndex=$diceIndex")
        Timber.d("sids=$sides")
        Timber.d("description=", description)

        val dice = dice().map { it.copy() }.toList()

        dice[diceIndex].sides = (sides downTo 1).map { sideIndex ->
            Side(number = sideIndex)
        }
        dice[diceIndex].title = description

        store(dice)
    }

    fun store(allDice: List<Dice>) = bagRepository.store(allDice)
}
