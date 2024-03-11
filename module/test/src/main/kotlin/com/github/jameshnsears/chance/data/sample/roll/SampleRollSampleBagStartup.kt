package com.github.jameshnsears.chance.data.sample.roll

import com.github.jameshnsears.chance.data.domain.state.Roll
import com.github.jameshnsears.chance.data.domain.utility.epoch.UtilityEpochTimeGenerator
import com.github.jameshnsears.chance.data.sample.bag.SampleBagStartup

class SampleRollSampleBagStartup {
    companion object {
        private val diceStory = SampleBagStartup.diceStory
        private val diceHeadsTails = SampleBagStartup.diceHeadsTails
        private val d6 = SampleBagStartup.d6

        private val firstRoll = UtilityEpochTimeGenerator.now()
        private val secondRoll = UtilityEpochTimeGenerator.now()
        private val thirdRoll = UtilityEpochTimeGenerator.now()

        var rollHistory = linkedMapOf(
            thirdRoll to listOf(
                Roll(diceStory.epoch, diceStory.sides[0]),
                Roll(diceHeadsTails.epoch, diceHeadsTails.sides[1]),
                Roll(diceStory.epoch, diceStory.sides[1]),
                Roll(diceHeadsTails.epoch, diceHeadsTails.sides[0]),
                Roll(diceStory.epoch, diceStory.sides[2]),
                Roll(diceHeadsTails.epoch, diceHeadsTails.sides[1]),
                Roll(diceStory.epoch, diceStory.sides[3]),
            ),

            secondRoll to listOf(
                Roll(diceHeadsTails.epoch, diceHeadsTails.sides[0]),
            ),

            firstRoll to listOf(
                Roll(d6.epoch, d6.sides[0]),
                Roll(d6.epoch, d6.sides[1]),
                Roll(d6.epoch, d6.sides[2]),
            ),
        )
    }
}
