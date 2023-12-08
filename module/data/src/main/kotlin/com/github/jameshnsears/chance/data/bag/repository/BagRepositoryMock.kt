package com.github.jameshnsears.chance.data.bag.repository

import com.github.jameshnsears.chance.data.domain.Dice

object BagRepositoryMock : BagRepositoryInterface {
    private var dice: List<Dice> = emptyList()

    override fun fetch(): List<Dice> {
        return dice
    }

    override fun store(newDice: List<Dice>) {
        dice = newDice
    }
}
