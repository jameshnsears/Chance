package com.github.jameshnsears.chance.data.repository.dice

import com.github.jameshnsears.chance.data.domain.Dice

interface DiceRepositoryInterface {
    fun fetch(): List<Dice>
    fun store(newDice: List<Dice>)
}