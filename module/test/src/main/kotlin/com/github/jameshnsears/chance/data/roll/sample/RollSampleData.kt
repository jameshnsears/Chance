package com.github.jameshnsears.chance.data.roll.sample

import com.github.jameshnsears.chance.data.domain.Dice
import com.github.jameshnsears.chance.data.domain.Roll
import com.github.jameshnsears.chance.data.domain.Side
import com.github.jameshnsears.chance.utils.epoch.Epoch

class RollSampleData {
    companion object {
        private val d6 = Dice(
            epoch = Epoch.now(),
            sides = (6 downTo 1).map { index -> Side(number = index) },
            title = "d6",
        )


        var oneRollOneDice = linkedMapOf(
            Epoch.now() to listOf(
                Roll(d6, d6.sides[0])
            )
        )
    }
}
