package com.github.jameshnsears.chance.data.bag.repository

import com.github.jameshnsears.chance.data.domain.Dice

object BagRepositoryMock : BagRepositoryInterface {
    private lateinit var bag: List<Dice>

    override fun fetch(): List<Dice> {
        return bag
    }

    override fun store(newBag: List<Dice>) {
        bag = newBag
    }
}
