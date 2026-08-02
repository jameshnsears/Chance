package com.github.jameshnsears.chance.data.domain.core.group.testdouble

import com.github.jameshnsears.chance.data.domain.core.bag.BagDataInterface
import com.github.jameshnsears.chance.data.domain.core.group.Group
import com.github.jameshnsears.chance.data.domain.core.group.GroupDataInterface
import com.github.jameshnsears.chance.data.domain.core.group.GroupHistory
import kotlinx.coroutines.runBlocking

class GroupDataTestDouble(bagData: BagDataInterface) : GroupDataInterface {
    override val groupHistory: GroupHistory = runBlocking {
        val allDice = bagData.allDice()
        listOf(
            Group(
                name = "tg1",
                uuidDice = listOf(
                    allDice[0].uuid,
                    allDice[1].uuid,
                ),
                notes = "Test Note 1"
            ),
            Group(
                name = "tg2",
                uuidDice = listOf(
                    allDice[1].uuid,
                ),
                notes = "Test Note 2"
            )
        )
    }
}
