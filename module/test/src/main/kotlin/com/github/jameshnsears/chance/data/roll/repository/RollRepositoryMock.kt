package com.github.jameshnsears.chance.data.roll.repository

import com.github.jameshnsears.chance.data.roll.sample.RollSampleData
import com.github.jameshnsears.chance.utils.epoch.Epoch

object RollRepositoryMock : RollRepositoryInterface {
    override var rollHistory = RollSampleData.oneRollOneDice

    override fun fetch(): RollHistory {
        return rollHistory
    }

    override fun store(rollSequence: RollSequence) {
        rollHistory.put(Epoch.now(), rollSequence)
    }
}
