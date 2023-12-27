package com.github.jameshnsears.chance.data.roll.repository

import com.github.jameshnsears.chance.data.domain.Roll

typealias RollSequenceEpoch = Long

typealias RollSequence = List<Roll>

typealias RollHistory = LinkedHashMap<RollSequenceEpoch, RollSequence>

interface RollRepositoryInterface {
    var rollHistory: RollHistory
    fun fetch(): RollHistory
    fun store(rollSequence: RollSequence)
}