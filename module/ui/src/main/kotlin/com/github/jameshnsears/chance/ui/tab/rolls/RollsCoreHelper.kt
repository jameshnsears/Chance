package com.github.jameshnsears.chance.ui.tab.rolls

import com.github.jameshnsears.chance.common.utility.epoch.UtilityEpochTimeGenerator
import com.github.jameshnsears.chance.data.domain.core.Dice
import com.github.jameshnsears.chance.data.domain.core.DiceRollValues
import com.github.jameshnsears.chance.data.domain.core.Side
import com.github.jameshnsears.chance.data.domain.core.bag.DiceBag
import com.github.jameshnsears.chance.data.domain.core.group.GroupHistory
import com.github.jameshnsears.chance.data.domain.core.roll.Roll
import com.github.jameshnsears.chance.data.repo.api.roll.RepositoryRollInterface
import java.security.SecureRandom

class RollsCoreHelper(
    private val repositoryRoll: RepositoryRollInterface,
    private val secureRandom: SecureRandom = SecureRandom()
) {
    fun diceCanExplode(
        dice: Dice,
        side: Side
    ): Boolean {
        var explode = false

        when (dice.explodeWhen) {
            DiceRollValues.EXPLODE_WHEN_VALUES[0] -> {
                // "="
                if (side.number == dice.explodeValue) {
                    explode = true
                }
            }

            DiceRollValues.EXPLODE_WHEN_VALUES[1] -> {
                // "<"
                if (side.number < dice.explodeValue) {
                    explode = true
                }
            }

            DiceRollValues.EXPLODE_WHEN_VALUES[2] -> {
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
        repositoryRoll.store(UtilityEpochTimeGenerator.currentEpochMillis(), newRollSequence)
    }

    fun rollSequenceScore(rollSequence: MutableMap.MutableEntry<Long, List<Roll>>): String {
        var score = 0
        rollSequence.value.forEach {
            score += it.score
        }
        return "$score"
    }

    fun generateRollDiceSequence(
        diceBag: DiceBag,
        groupHistory: GroupHistory,
        newRollSequence: MutableList<Roll>
    ) {
        val diceToRoll = getDiceToRoll(diceBag, groupHistory)
        val diceCountMap = mutableMapOf<String, Int>()

        diceToRoll.forEach { (dice, uuidGroup) ->
            val count = diceCountMap.getOrDefault(dice.uuid, 0)
            newRollSequence.addAll(rollDice(dice, count * dice.multiplierValue, uuidGroup))
            diceCountMap[dice.uuid] = count + 1
        }
    }

    private fun getDiceToRoll(diceBag: DiceBag, groupHistory: GroupHistory): List<Pair<Dice, String>> {
        val selectedDice = diceBag.filter { it.selected }.map { it to "" }

        val diceMap = diceBag.associateBy { it.uuid }
        val groupDice = groupHistory
            .filter { it.selected }
            .flatMap { group ->
                group.uuidDice.mapNotNull { diceMap[it]?.to(group.uuid) }
            }

        return selectedDice + groupDice
    }

    private fun rollDice(dice: Dice, indexOffset: Int = 0, uuidGroup: String = ""): List<Roll> {
        val diceRolls = mutableListOf<Roll>()
        for (indexMultiplier in 1..dice.multiplierValue) {
            var randomSide = randomSide(dice)
            diceRolls.add(
                Roll(
                    uuidDice = dice.uuid,
                    side = randomSide,
                    multiplierIndex = indexMultiplier + indexOffset,
                    score = randomSide.number,
                    uuidGroup = uuidGroup
                )
            )

            if (dice.explode) {
                var indexExplode = 0
                val explosionDepth = 5
                while (indexExplode < explosionDepth && diceCanExplode(dice, randomSide)) {
                    indexExplode++
                    randomSide = randomSide(dice)
                    diceRolls.add(
                        Roll(
                            uuidDice = dice.uuid,
                            side = randomSide,
                            multiplierIndex = indexMultiplier,
                            explodeIndex = indexExplode,
                            score = randomSide.number,
                            uuidGroup = uuidGroup
                        )
                    )
                }
            }
        }

        if (dice.modifyScore && diceRolls.isNotEmpty()) {
            val lastRoll = diceRolls.last()
            lastRoll.scoreAdjustment = dice.modifyScoreValue
            lastRoll.score += dice.modifyScoreValue
        }

        return diceRolls
    }

    fun randomSide(dice: Dice) = dice.sides[secureRandom.nextInt(dice.sides.size)]

    fun shuffleRollSequence(rollSequence: MutableList<Roll>, shuffle: Boolean) {
        if (shuffle) {
            if (rollSequence.size > 1) {
                rollSequence.shuffle()

                // order the multipleIndex into ASC order for any dice cluster
                rollSequence
                    .groupBy { it.uuidDice }
                    .forEach { (_, rolls) ->
                        rolls.forEachIndexed { index, roll ->
                            roll.multiplierIndex = index + 1
                        }
                    }
            }
        }
    }
}
