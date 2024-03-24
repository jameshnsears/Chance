package com.github.jameshnsears.chance.data.domain.state

import com.github.jameshnsears.chance.data.domain.utility.epoch.UtilityEpochTimeGenerator

data class Dice(
    var epoch: Long = UtilityEpochTimeGenerator.now(),
    var sides: List<Side> = emptyList(),
    var title: String = "",
    var titleStringsId: Int = 0,
    var colour: String = "FF000000",
    var selected: Boolean = false,

    var multiplier: Boolean = false,
    var multiplierValue: Int = DiceRollValues.multiplierValues[0].toInt(),

    var explode: Boolean = false,
    var explodeWhen: String = DiceRollValues.explodeWhenValues[0],
    var explodeValue: Int = 1,

    var modifyScore: Boolean = false,
    var modifyScoreValue: Int = DiceRollValues.modifyScoreValues[0].toInt(),
)

class DiceRollValues {
    companion object {
        val multiplierValues = listOf("2", "3", "4", "5")
        val explodeWhenValues = listOf("=", "<", ">")
        val modifyScoreValues = listOf("-5", "-4", "-3", "-2", "-1", "1", "2", "3", "4", "5")
    }
}
