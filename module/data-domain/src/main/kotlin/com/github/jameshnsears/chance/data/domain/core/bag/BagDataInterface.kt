package com.github.jameshnsears.chance.data.domain.core.bag

import com.github.jameshnsears.chance.data.domain.core.Dice

interface BagDataInterface {
    suspend fun allDice(): MutableList<Dice>
}
