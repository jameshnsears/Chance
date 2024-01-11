package com.github.jameshnsears.chance.data.repository.roll

import com.github.jameshnsears.chance.data.domain.Dice
import com.github.jameshnsears.chance.data.domain.Roll
import com.github.jameshnsears.chance.data.domain.Side
import com.github.jameshnsears.chance.utils.epoch.EpochTime

class RollSampleData {
    companion object {
        private val d6 = Dice(
            epoch = EpochTime.now(),
            sides = (6 downTo 1).map { index -> Side(number = index) },
            title = "d6",
        )

        var rollHistory_roll1Sequence1 = linkedMapOf(
            EpochTime.now() to listOf(Roll(d6, d6.sides[0]))
        )

        var rollHistory_roll2Sequence2 = linkedMapOf(
            EpochTime.now() to listOf(
                Roll(d6, d6.sides[4]),
                Roll(d6, d6.sides[2]),
                Roll(d6, d6.sides[0])
            ),
            EpochTime.now() - 10 to listOf(
                Roll(d6, d6.sides[5]),
                Roll(d6, d6.sides[3]),
                Roll(d6, d6.sides[1])
            )
        )
    }
}
