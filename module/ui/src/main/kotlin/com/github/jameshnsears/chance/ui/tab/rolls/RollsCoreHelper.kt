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
            val countOffset = diceCountMap.getOrDefault(dice.uuid, 0)
            for (m in 1..dice.multiplierValue) {
                newRollSequence.addAll(rollDiceUnit(dice, countOffset + m, uuidGroup))
            }
            diceCountMap[dice.uuid] = countOffset + dice.multiplierValue
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

    fun reRollDiceSequence(
        diceBag: DiceBag,
        existingSequence: List<Roll>,
        lockedIndices: Set<Int>,
        newRollSequence: MutableList<Roll>,
        shuffle: Boolean
    ) {
        val diceMap = diceBag.associateBy { it.uuid }

        data class UnitKey(val uuidDice: String, val multiplierIndex: Int, val uuidGroup: String)

        val unitRootIndices = mutableMapOf<UnitKey, Int>()
        val unitRoots = mutableMapOf<UnitKey, Roll>()

        existingSequence.forEachIndexed { index, roll ->
            if (roll.explodeIndex == 0) {
                val key = UnitKey(roll.uuidDice, roll.multiplierIndex, roll.uuidGroup)
                unitRootIndices[key] = index
                unitRoots[key] = roll
            }
        }

        val unitKeys = existingSequence.map { UnitKey(it.uuidDice, it.multiplierIndex, it.uuidGroup) }.distinct()

        unitKeys.forEach { key ->
            val dice = diceMap[key.uuidDice]
            val rootIndex = unitRootIndices[key]
            val rootRoll = unitRoots[key]

            if (dice != null && rootRoll != null && rootIndex != null) {
                if (lockedIndices.contains(rootIndex)) {
                    newRollSequence.addAll(rollDiceStartingFromRoot(dice, rootRoll))
                } else {
                    newRollSequence.addAll(rollDiceUnit(dice, key.multiplierIndex, key.uuidGroup))
                }
            }
        }

        if (shuffle) {
            newRollSequence.shuffle()
        }
    }

    private fun rollDiceUnit(dice: Dice, multiplierIndex: Int, uuidGroup: String): List<Roll> {
        val diceRolls = mutableListOf<Roll>()
        var randomSide = randomSide(dice)
        diceRolls.add(
            Roll(
                uuidDice = dice.uuid,
                side = randomSide,
                multiplierIndex = multiplierIndex,
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
                        multiplierIndex = multiplierIndex,
                        explodeIndex = indexExplode,
                        score = randomSide.number,
                        uuidGroup = uuidGroup
                    )
                )
            }
        }

        if (dice.modifyScore && diceRolls.isNotEmpty()) {
            val lastRoll = diceRolls.last()
            lastRoll.scoreAdjustment = dice.modifyScoreValue
            lastRoll.score += dice.modifyScoreValue
        }

        return diceRolls
    }

    private fun rollDiceStartingFromRoot(dice: Dice, rootRoll: Roll): List<Roll> {
        val diceRolls = mutableListOf<Roll>()
        // Reset root roll score in case it was previously the last item with an adjustment
        val cleanRoot = rootRoll.copy(score = rootRoll.side.number, scoreAdjustment = 0)
        diceRolls.add(cleanRoot)

        var lastSide = cleanRoot.side
        if (dice.explode) {
            var indexExplode = 0
            val explosionDepth = 5
            while (indexExplode < explosionDepth && diceCanExplode(dice, lastSide)) {
                indexExplode++
                val randomSide = randomSide(dice)
                diceRolls.add(
                    Roll(
                        uuidDice = dice.uuid,
                        side = randomSide,
                        multiplierIndex = cleanRoot.multiplierIndex,
                        explodeIndex = indexExplode,
                        score = randomSide.number,
                        uuidGroup = cleanRoot.uuidGroup
                    )
                )
                lastSide = randomSide
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
