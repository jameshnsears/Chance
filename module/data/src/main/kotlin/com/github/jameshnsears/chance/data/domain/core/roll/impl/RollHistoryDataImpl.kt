package com.github.jameshnsears.chance.data.domain.core.roll.impl

import com.github.jameshnsears.chance.data.domain.core.bag.BagDataInterface
import com.github.jameshnsears.chance.data.domain.core.roll.Roll
import com.github.jameshnsears.chance.data.domain.core.roll.RollHistory
import com.github.jameshnsears.chance.data.domain.core.roll.RollHistoryDataInterface
import com.github.jameshnsears.chance.data.domain.utility.epoch.UtilityEpochTimeGenerator

class RollHistoryDataImpl(bagData: BagDataInterface) : RollHistoryDataInterface {
    override var rollHistory: RollHistory = linkedMapOf(
        UtilityEpochTimeGenerator.now() to listOf(
            Roll(
                bagData.allDice[1].epoch,
                bagData.allDice[1].sides[0],
                score = 6
            ),
            Roll(
                bagData.allDice[1].epoch,
                bagData.allDice[1].sides[1],
                score = 5
            ),
            Roll(
                bagData.allDice[1].epoch,
                bagData.allDice[1].sides[2],
                score = 4
            ),
        )
    )
}
