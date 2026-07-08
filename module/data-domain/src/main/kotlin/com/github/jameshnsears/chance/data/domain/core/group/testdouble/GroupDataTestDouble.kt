package com.github.jameshnsears.chance.data.domain.core.group.testdouble

import com.github.jameshnsears.chance.data.domain.core.bag.BagDataInterface
import com.github.jameshnsears.chance.data.domain.core.group.Group
import com.github.jameshnsears.chance.data.domain.core.group.GroupDataInterface
import com.github.jameshnsears.chance.data.domain.core.group.GroupHistory

class GroupDataTestDouble(bagData: BagDataInterface) : GroupDataInterface {
    override val groupHistory: GroupHistory = listOf(
        Group(
            name = "tg1",
            uuidDice = listOf(
                bagData.allDice[0].uuid,
                bagData.allDice[1].uuid,
            ),
            notes = "Test Note 1"
        ),
        Group(
            name = "tg2",
            uuidDice = listOf(
                bagData.allDice[1].uuid,
                bagData.allDice[2].uuid,
            ),
            notes = "Test Note 2"
        )
    )
}
