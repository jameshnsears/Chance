package com.github.jameshnsears.chance.data.sample.roll

import com.github.jameshnsears.chance.data.domain.state.Roll
import com.github.jameshnsears.chance.data.domain.utility.epoch.UtilityEpochTimeGenerator
import com.github.jameshnsears.chance.data.sample.bag.SampleBagTestData

class SampleRollTestData(sampleBagTestData: SampleBagTestData) {
    var rollHistory = linkedMapOf(
        UtilityEpochTimeGenerator.now() to listOf(
            Roll(sampleBagTestData.d2.epoch, sampleBagTestData.d2.sides[0], score = 2),
            Roll(sampleBagTestData.d4.epoch, sampleBagTestData.d4.sides[1], score = 3),
            Roll(sampleBagTestData.d8.epoch, sampleBagTestData.d8.sides[3], score = 5),
            Roll(sampleBagTestData.d10.epoch, sampleBagTestData.d10.sides[4], score = 6),
            Roll(sampleBagTestData.d12.epoch, sampleBagTestData.d12.sides[5], score = 7),
            Roll(sampleBagTestData.d20.epoch, sampleBagTestData.d20.sides[6], score = 14),
        ),

        UtilityEpochTimeGenerator.now() to listOf(
            Roll(sampleBagTestData.d4.epoch, sampleBagTestData.d4.sides[1], score = 3),
            Roll(
                sampleBagTestData.d4.epoch,
                sampleBagTestData.d4.sides[2],
                score = 2,
                multiplierIndex = 2
            ),
            Roll(
                sampleBagTestData.d4.epoch,
                sampleBagTestData.d4.sides[3],
                score = 1,
                multiplierIndex = 3
            ),
        ),
    )
}
