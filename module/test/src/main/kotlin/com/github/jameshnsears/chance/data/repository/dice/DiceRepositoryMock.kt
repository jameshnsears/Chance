package com.github.jameshnsears.chance.data.repository.dice

import com.github.jameshnsears.chance.data.domain.Dice

object DiceRepositoryMock : DiceRepositoryInterface {
    private var dice: List<Dice> = emptyList()

    override fun fetch(): List<Dice> {
        return dice
    }

    override fun store(newDice: List<Dice>) {
        dice = newDice
    }
}
