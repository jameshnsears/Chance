package com.github.jameshnsears.chance.data.domain.core

import com.github.jameshnsears.chance.common.utility.epoch.UtilityEpochTimeGenerator
import java.util.UUID


data class Dice(
    var epoch: Long = UtilityEpochTimeGenerator.currentEpochMillis(),

    var uuid: String = UUID.randomUUID().toString(),

    var sides: List<Side> = emptyList(),
    var title: String = "",
    var colour: String = "FF6650a4",    // Theme; primary
    var selected: Boolean = false,

    var multiplierValue: Int = DiceRollValues.MULTIPLIER_VALUES[0].toInt(),

    var explode: Boolean = false,
    var explodeWhen: String = DiceRollValues.EXPLODE_WHEN_VALUES[0],
    var explodeValue: Int = 1,

    var modifyScore: Boolean = false,
    var modifyScoreValue: Int = DiceRollValues.MODIFY_SCORE_VALUES[0].toInt(),

    var displayIndex: Int = 0,
)

class DiceRollValues {
    companion object {
        const val SIDES_MIN = 2
        const val SIDES_MAX = 1000

        val MULTIPLIER_VALUES = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "12", "20", "50")
        val EXPLODE_WHEN_VALUES = listOf("=", "<", ">")
        val MODIFY_SCORE_VALUES =
            listOf("-5", "-4", "-3", "-2", "-1", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "12", "15", "20")
    }
}
