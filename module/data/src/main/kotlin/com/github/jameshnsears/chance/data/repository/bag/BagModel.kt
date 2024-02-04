package com.github.jameshnsears.chance.data.repository.bag

import com.github.jameshnsears.chance.data.domain.Dice
import timber.log.Timber

class BagModel(
    private val bagRepository: DiceBagRepositoryInterface,
) {
    suspend fun save(diceToSave: Dice) {
        Timber.d("diceToSave=$diceToSave")

        val diceBag = bagRepository.fetch()

        for (dice: Dice in diceBag) {
            if (dice.epoch == diceToSave.epoch) {
                dice.sides = diceToSave.sides
                dice.title = diceToSave.title
                dice.titleStringsId = diceToSave.titleStringsId
                dice.colour = diceToSave.colour
                dice.selected = diceToSave.selected
            }
        }

        bagRepository.store(diceBag)
    }

    suspend fun clone(diceToClone: Dice) {
        Timber.d("diceToClone=$diceToClone")

        var diceBag = bagRepository.fetch()

        diceBag += Dice(
            sides = diceToClone.sides,
            title = diceToClone.title,
            titleStringsId = diceToClone.titleStringsId,
            colour = diceToClone.colour,
            selected = diceToClone.selected,
        )

        bagRepository.store(diceBag)
    }

    suspend fun canBeDeleted() = bagRepository.fetch().size > 1

    suspend fun delete(diceToDelete: Dice) {
        Timber.d("diceToDelete=$diceToDelete")

        if (canBeDeleted()) {
            var diceBag = bagRepository.fetch()
            diceBag -= diceToDelete
            bagRepository.store(diceBag)
        }
    }
}
