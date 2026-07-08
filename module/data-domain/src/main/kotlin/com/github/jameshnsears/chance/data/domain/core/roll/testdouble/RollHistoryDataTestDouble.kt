package com.github.jameshnsears.chance.data.domain.core.roll.testdouble

import com.github.jameshnsears.chance.common.utility.epoch.UtilityEpochTimeGenerator
import com.github.jameshnsears.chance.data.domain.core.bag.BagDataInterface
import com.github.jameshnsears.chance.data.domain.core.group.GroupDataInterface
import com.github.jameshnsears.chance.data.domain.core.roll.Roll
import com.github.jameshnsears.chance.data.domain.core.roll.RollHistoryDataInterface

class RollHistoryDataTestDouble(
    bagData: BagDataInterface,
    groupData: GroupDataInterface,
) : RollHistoryDataInterface {
    override var rollHistory = linkedMapOf(
        UtilityEpochTimeGenerator.currentEpochMillis() to listOf(
            Roll(
                bagData.allDice[0].uuid,
                bagData.allDice[0].sides[0],
                score = 2,
                multiplierIndex = 1
            ),
        ),

        UtilityEpochTimeGenerator.currentEpochMillis() to listOf(
            Roll(
                bagData.allDice[1].uuid,
                bagData.allDice[1].sides[0],
                score = 4,
                multiplierIndex = 1
            ),
            Roll(
                bagData.allDice[1].uuid,
                bagData.allDice[1].sides[1],
                score = 3,
                multiplierIndex = 2
            ),
        ),
    )
}
