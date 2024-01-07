package com.github.jameshnsears.chance.data.roll.repository

import com.github.jameshnsears.chance.data.domain.RollHistory

interface RollRepositoryInterface {
    fun fetch(): RollHistory
    fun store(newRollHistory: RollHistory)
}