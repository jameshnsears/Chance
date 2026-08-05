package com.github.jameshnsears.chance.ui.tab.rolls

import com.github.jameshnsears.chance.data.common.repo.RepositoryFactory
import com.github.jameshnsears.chance.data.domain.core.DiceRollValues
import com.github.jameshnsears.chance.data.domain.core.bag.testdouble.BagDataTestDouble
import com.github.jameshnsears.chance.data.domain.core.group.testdouble.GroupDataTestDouble
import com.github.jameshnsears.chance.data.domain.core.roll.Roll
import io.mockk.every
import io.mockk.spyk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Test

class RollsCoreHelperTest {
    @Test
    fun diceCanExplode() {
        val helper = RollsCoreHelper(RepositoryFactory().repositoryRoll)
        val dice = BagDataTestDouble().d6
        dice.explode = true
        dice.explodeValue = 6

        // Equals
        dice.explodeWhen = DiceRollValues.explodeWhenValues[0]
        assertTrue(helper.diceCanExplode(dice, dice.sides[0])) // side 6

        // Less than
        dice.explodeWhen = DiceRollValues.explodeWhenValues[1]
        assertTrue(helper.diceCanExplode(dice, dice.sides[5])) // side 1

        // Greater than
        dice.explodeWhen = DiceRollValues.explodeWhenValues[2]
        dice.explodeValue = 1
        assertTrue(helper.diceCanExplode(dice, dice.sides[0])) // side 6
    }

    @Test
    fun shuffleRollSequence() {
        val helper = RollsCoreHelper(RepositoryFactory().repositoryRoll)
        val diceBag = BagDataTestDouble()
        val rolls = mutableListOf(
            Roll(uuidDice = diceBag.d6.uuid, multiplierIndex = 3, side = diceBag.d6.sides[0]),
            Roll(uuidDice = diceBag.d6.uuid, multiplierIndex = 1, side = diceBag.d6.sides[1]),
            Roll(uuidDice = diceBag.d6.uuid, multiplierIndex = 2, side = diceBag.d6.sides[2])
        )

        helper.shuffleRollSequence(rolls, true)

        // Verify multiplier indices are re-assigned in order 1, 2, 3
        assertEquals(1, rolls[0].multiplierIndex)
        assertEquals(2, rolls[1].multiplierIndex)
        assertEquals(3, rolls[2].multiplierIndex)
    }

    @Test
    fun generateRollDiceSequenceExplosionLimit() = runTest {
        val repositoryRoll = RepositoryFactory().repositoryRoll
        val helper = spyk(RollsCoreHelper(repositoryRoll))

        val diceBag = BagDataTestDouble()
        val d6 = diceBag.d6
        d6.selected = true
        d6.multiplierValue = 1
        d6.explode = true
        d6.explodeWhen = DiceRollValues.explodeWhenValues[0]
        d6.explodeValue = 6

        // Force explosion: return side with number 6
        every { helper.randomSide(any()) } returns d6.sides[0]

        val rolls = mutableListOf<Roll>()
        helper.generateRollDiceSequence(mutableListOf(d6), emptyList(), rolls)

        // Expect 6 rolls: 1 initial + 5 explosions
        assertEquals(6, rolls.size)
    }

    @Test
    fun generateRollDiceSequenceWithGroups() = runTest {
        val bagData = BagDataTestDouble()
        bagData.allDice().forEach { it.selected = false }
        val groupData = GroupDataTestDouble(bagData)
        val group = groupData.groupHistory[0].copy(selected = true) // tg1: d2 (mult 1), d4 (mult 2)

        val helper = spyk(RollsCoreHelper(RepositoryFactory().repositoryRoll))
        every { helper.randomSide(any()) } answers {
            (it.invocation.args[0] as com.github.jameshnsears.chance.data.domain.core.Dice).sides[0]
        }

        val rolls = mutableListOf<Roll>()
        helper.generateRollDiceSequence(bagData.allDice(), listOf(group), rolls)

        // d2 (1) + d4 (2) = 3 rolls
        assertEquals(3, rolls.size)
    }
}
