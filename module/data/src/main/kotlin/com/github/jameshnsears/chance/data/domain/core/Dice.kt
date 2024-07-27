package com.github.jameshnsears.chance.data.domain.core

import com.github.jameshnsears.chance.data.domain.utility.epoch.UtilityEpochTimeGenerator
import java.util.UUID


data class Dice(
    var epoch: Long = UtilityEpochTimeGenerator.now(),

    var uuid: String = UUID.randomUUID().toString(),

    var sides: List<Side> = emptyList(),
    var title: String = "",
    var colour: String = "FF6650a4",    // Theme; primary
    var selected: Boolean = false,

    var multiplierValue: Int = DiceRollValues.multiplierValues[0].toInt(),

    var explode: Boolean = false,
    var explodeWhen: String = DiceRollValues.explodeWhenValues[0],
    var explodeValue: Int = 1,

    var modifyScore: Boolean = false,
    var modifyScoreValue: Int = DiceRollValues.modifyScoreValues[0].toInt(),
)

class DiceRollValues {
    companion object {
        val multiplierValues = listOf("1", "2", "3", "4", "5", "6")
        val explodeWhenValues = listOf("=", "<", ">")
        val modifyScoreValues = listOf("-5", "-4", "-3", "-2", "-1", "1", "2", "3", "4", "5")
    }
}
