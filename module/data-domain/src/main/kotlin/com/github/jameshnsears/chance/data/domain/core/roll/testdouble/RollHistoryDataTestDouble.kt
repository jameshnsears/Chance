package com.github.jameshnsears.chance.data.domain.core.roll.testdouble

import com.github.jameshnsears.chance.common.utility.epoch.UtilityEpochTimeGenerator
import com.github.jameshnsears.chance.data.domain.core.bag.BagDataInterface
import com.github.jameshnsears.chance.data.domain.core.group.GroupDataInterface
import com.github.jameshnsears.chance.data.domain.core.roll.Roll
import com.github.jameshnsears.chance.data.domain.core.roll.RollHistory
import com.github.jameshnsears.chance.data.domain.core.roll.RollHistoryDataInterface
import kotlinx.coroutines.runBlocking

class RollHistoryDataTestDouble(
    bagData: BagDataInterface,
    groupData: GroupDataInterface,
) : RollHistoryDataInterface {
    override var rollHistory: RollHistory = runBlocking {
        val allDice = bagData.allDice()
        val now = UtilityEpochTimeGenerator.currentEpochMillis()
        linkedMapOf(
            now to listOf(
                Roll(
                    allDice[0].uuid,
                    allDice[0].sides[0],
                    score = 2,
                    multiplierIndex = 1,
                    uuidGroup = groupData.groupHistory[0].uuid
                ),
            ),

            now + 1000 to listOf(
                Roll(
                    allDice[1].uuid,
                    allDice[1].sides[0],
                    score = 4,
                    multiplierIndex = 1,
                    uuidGroup = groupData.groupHistory[1].uuid
                ),
                Roll(
                    allDice[1].uuid,
                    allDice[1].sides[1],
                    score = 3,
                    multiplierIndex = 2,
                    uuidGroup = groupData.groupHistory[1].uuid
                ),
            ),
        )
    }
}
