package com.github.jameshnsears.chance.data.sample.roll

import com.github.jameshnsears.chance.data.domain.state.Roll
import com.github.jameshnsears.chance.data.domain.utility.epoch.UtilityEpochTimeGenerator
import com.github.jameshnsears.chance.data.sample.bag.SampleBag

class SampleRollSampleBag {
    companion object {
        private val firstRoll = UtilityEpochTimeGenerator.now()
        private val secondRoll = UtilityEpochTimeGenerator.now()

        var rollHistory = linkedMapOf(
            secondRoll to listOf(
                Roll(SampleBag.d2.epoch, SampleBag.d2.sides[0]),
                Roll(SampleBag.d4.epoch, SampleBag.d4.sides[1]),
                Roll(SampleBag.d6.epoch, SampleBag.d6.sides[2]),
                Roll(SampleBag.d8.epoch, SampleBag.d8.sides[3]),
                Roll(SampleBag.d10.epoch, SampleBag.d10.sides[5]),
                Roll(SampleBag.d12.epoch, SampleBag.d12.sides[6]),
                Roll(SampleBag.d20.epoch, SampleBag.d20.sides[6]),
            ),

            firstRoll to listOf(
                Roll(SampleBag.d6.epoch, SampleBag.d6.sides[1]),
                Roll(SampleBag.d6.epoch, SampleBag.d6.sides[3]),
                Roll(SampleBag.d6.epoch, SampleBag.d6.sides[5]),
            ),
        )
    }
}
