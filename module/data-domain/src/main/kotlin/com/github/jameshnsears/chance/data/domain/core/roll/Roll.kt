package com.github.jameshnsears.chance.data.domain.core.roll

import com.github.jameshnsears.chance.data.domain.core.Side

data class Roll(
    val diceEpoch: Long,
    val side: Side,
    var multiplierIndex: Int = 1,
    val explodeIndex: Int = 0,
    var scoreAdjustment: Int = 0,
    var score: Int = 0,
) {
    override fun toString(): String {
        return "diceEpoch=$diceEpoch; multiplierIndex=$multiplierIndex; side=$side"
    }
}
