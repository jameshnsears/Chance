package com.github.jameshnsears.chance.data.bag.repository

import com.github.jameshnsears.chance.data.bag.demo.BagDemo
import com.github.jameshnsears.chance.data.domain.Dice

interface BagRepositoryInterface {
    val bagDemo: List<Dice>
        get() = BagDemo.dice

    fun fetch(): List<Dice>
    fun store(newDice: List<Dice>)
}