package com.github.jameshnsears.chance.data.bag.model

import com.github.jameshnsears.chance.data.bag.repository.BagRepositoryInterface
import com.github.jameshnsears.chance.data.domain.Dice
import timber.log.Timber

class BagModel(
    private val bagRepository: BagRepositoryInterface
) {
    fun diceUpdate(dice: Dice) {
        Timber.d("dice=$dice")

        val diceBag = bagRepository.fetch().map { it.copy() }.toList()

//        dice[diceIndex].sides = (sides downTo 1).map { sideIndex ->
//            Side(number = sideIndex)
//        }

        bagRepository.store(diceBag)
    }

    fun diceClone(diceToClone: Dice) {
        val diceBag = bagRepository.fetch().toMutableList()
        diceBag += diceToClone
        bagRepository.store(diceBag)
    }

    fun diceCanBeDeleted() = bagRepository.fetch().size > 1

    fun diceDelete(dice: Dice) {
        // TODO
    }
}
