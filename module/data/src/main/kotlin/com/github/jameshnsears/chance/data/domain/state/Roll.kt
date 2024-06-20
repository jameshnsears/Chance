package com.github.jameshnsears.chance.data.domain.state

data class Roll(
    val diceEpoch: Long,
    val side: Side,
    val multiplierIndex: Int = 1,
    val explodeIndex: Int = 0,
    var scoreAdjustment: Int = 0,
    var score: Int = 0,
)
