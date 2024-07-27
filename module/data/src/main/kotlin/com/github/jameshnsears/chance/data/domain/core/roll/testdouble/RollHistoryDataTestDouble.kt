package com.github.jameshnsears.chance.data.domain.core.roll.testdouble

import com.github.jameshnsears.chance.data.domain.core.bag.BagDataInterface
import com.github.jameshnsears.chance.data.domain.core.roll.Roll
import com.github.jameshnsears.chance.data.domain.core.roll.RollHistoryDataInterface
import com.github.jameshnsears.chance.data.domain.utility.epoch.UtilityEpochTimeGenerator

class RollHistoryDataTestDouble(bagData: BagDataInterface) : RollHistoryDataInterface {
    override var rollHistory = linkedMapOf(
        UtilityEpochTimeGenerator.now() to listOf(
            Roll(
                bagData.allDice[0].epoch,
                bagData.allDice[0].sides[0],
                score = 2
            ),
            Roll(
                bagData.allDice[1].epoch,
                bagData.allDice[1].sides[1],
                score = 3
            ),
            Roll(
                bagData.allDice[4].epoch,
                bagData.allDice[4].sides[3],
                score = 5
            ),
            Roll(
                bagData.allDice[5].epoch,
                bagData.allDice[5].sides[4],
                score = 6
            ),
            Roll(
                bagData.allDice[6].epoch,
                bagData.allDice[6].sides[5],
                score = 7
            ),
            Roll(
                bagData.allDice[7].epoch,
                bagData.allDice[7].sides[6],
                score = 14
            ),
        ),

        UtilityEpochTimeGenerator.now() to listOf(
            Roll(
                bagData.allDice[1].epoch,
                bagData.allDice[1].sides[1],
                score = 3
            ),
            Roll(
                bagData.allDice[1].epoch,
                bagData.allDice[1].sides[2],
                score = 2
            ),
            Roll(
                bagData.allDice[1].epoch,
                bagData.allDice[1].sides[3],
                score = 1
            ),
        ),
    )
}
