package com.github.jameshnsears.chance.data.bag.repository

import com.github.jameshnsears.chance.data.domain.Dice

object BagRepositoryImpl : BagRepositoryInterface {
    override var bag: List<Dice>
        get() = TODO("Not yet implemented")
        set(value) {}

    override fun fetch(): List<Dice> {
        TODO("Not yet implemented")
    }

    override fun store(newDice: List<Dice>) {
        TODO("Not yet implemented")
    }
}
