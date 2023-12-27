package com.github.jameshnsears.chance.data.bag.repository

import com.github.jameshnsears.chance.data.bag.demo.BagDemo
import com.github.jameshnsears.chance.data.domain.Dice

object BagRepositoryMock : BagRepositoryInterface {
    override var bag: List<Dice> = BagDemo.dice

    override fun fetch(): List<Dice> {
        return bag
    }

    override fun store(newBag: List<Dice>) {
        bag = newBag
    }
}
