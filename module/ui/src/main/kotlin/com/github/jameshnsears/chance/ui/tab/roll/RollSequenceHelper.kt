package com.github.jameshnsears.chance.ui.tab.roll

import com.github.jameshnsears.chance.common.utility.epoch.UtilityEpochTimeGenerator
import com.github.jameshnsears.chance.data.domain.core.Dice
import com.github.jameshnsears.chance.data.domain.core.DiceRollValues
import com.github.jameshnsears.chance.data.domain.core.Side
import com.github.jameshnsears.chance.data.domain.core.roll.Roll
import com.github.jameshnsears.chance.data.domain.core.roll.RollHistory
import com.github.jameshnsears.chance.data.repo.api.roll.RepositoryRollInterface
import kotlinx.coroutines.flow.first

class RollSequenceHelper(
    val repositoryRoll: RepositoryRollInterface
) {
    fun diceCanExplode(
        dice: Dice,
        side: Side
    ): Boolean {
        var explode = false

        when (dice.explodeWhen) {
            DiceRollValues.explodeWhenValues[0] -> {
                // "="
                if (side.number == dice.explodeValue) {
                    explode = true
                }
            }

            DiceRollValues.explodeWhenValues[1] -> {
                // "<"
                if (side.number < dice.explodeValue) {
                    explode = true
                }
            }

            DiceRollValues.explodeWhenValues[2] -> {
                // ">"
                if (side.number > dice.explodeValue) {
                    explode = true
                }
            }
        }
        return explode
    }

    suspend fun saveNewRollSequence(
        newRollSequence: MutableList<Roll>
    ) {
        val rollHistory: RollHistory = LinkedHashMap()
        rollHistory[UtilityEpochTimeGenerator.currentEpochMillis()] = newRollSequence

        rollHistory.putAll(repositoryRoll.fetch().first())
        repositoryRoll.store(rollHistory)
    }

    fun rollSequenceScore(rollSequence: MutableMap.MutableEntry<Long, List<Roll>>): String {
        var score = 0
        rollSequence.value.forEach {
            score += it.score
        }
        return "$score"
    }
}
