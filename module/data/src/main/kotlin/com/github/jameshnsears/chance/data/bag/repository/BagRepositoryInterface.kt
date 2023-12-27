package com.github.jameshnsears.chance.data.bag.repository

import com.github.jameshnsears.chance.data.domain.Dice

interface BagRepositoryInterface {
    var bag: List<Dice>
    fun fetch(): List<Dice>
    fun store(newDice: List<Dice>)
}