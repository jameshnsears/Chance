package com.github.jameshnsears.chance.data.domain.core.roll

import com.github.jameshnsears.chance.data.domain.core.Side

data class Roll(
    val uuidDice: String,
    val side: Side,
    var multiplierIndex: Int = 1,
    val explodeIndex: Int = 0,
    var scoreAdjustment: Int = 0,
    var score: Int = 0,
    var uuidGroup: String = "",
) {
    override fun toString(): String {
        return "uuidDice=$uuidDice; multiplierIndex=$multiplierIndex; side=$side; uuidGroup=$uuidGroup"
    }
}
