package com.github.jameshnsears.chance.data.repository.roll

import com.github.jameshnsears.chance.data.domain.Roll
import com.github.jameshnsears.chance.data.repository.bag.BagDemoSampleData
import com.github.jameshnsears.chance.utils.epoch.EpochTime

class RollSampleData {
    companion object {
        private val diceStory = BagDemoSampleData.diceStory
        private val diceHeadsTails = BagDemoSampleData.diceHeadsTails
        val diceBag = listOf(diceStory, diceHeadsTails)

        private val firstRoll = EpochTime.now()
        private val secondRoll = EpochTime.now()
        private val thirdRoll = EpochTime.now()
        private val fourthRoll = EpochTime.now()

        var rollHistory = linkedMapOf(
            fourthRoll to listOf(
                Roll(diceStory.epoch, diceStory.sides[5]),
                Roll(diceStory.epoch, diceStory.sides[6]),
                Roll(diceStory.epoch, diceStory.sides[7]),
                Roll(diceStory.epoch, diceStory.sides[8]),
                Roll(diceStory.epoch, diceStory.sides[9]),
            ),

            thirdRoll to listOf(
                Roll(diceStory.epoch, diceStory.sides[4]),
            ),

            secondRoll to listOf(
                Roll(diceHeadsTails.epoch, diceHeadsTails.sides[0]),
            ),

            firstRoll to listOf(
                Roll(diceStory.epoch, diceStory.sides[0]),
                Roll(diceHeadsTails.epoch, diceHeadsTails.sides[1]),
                Roll(diceStory.epoch, diceStory.sides[1]),
                Roll(diceHeadsTails.epoch, diceHeadsTails.sides[0]),
                Roll(diceStory.epoch, diceStory.sides[2]),
                Roll(diceHeadsTails.epoch, diceHeadsTails.sides[1]),
                Roll(diceStory.epoch, diceStory.sides[3]),
            ),
        )
    }
}
