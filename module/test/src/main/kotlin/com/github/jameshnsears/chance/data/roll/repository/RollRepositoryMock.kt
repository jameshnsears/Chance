package com.github.jameshnsears.chance.data.roll.repository

import com.github.jameshnsears.chance.data.domain.RollHistory

object RollRepositoryMock : RollRepositoryInterface {
    private lateinit var rollHistory: RollHistory

    override fun fetch(): RollHistory {
        return rollHistory
    }

    override fun store(newRollHistory: RollHistory) {
        rollHistory = newRollHistory
    }
}
