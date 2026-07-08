package com.github.jameshnsears.chance.ui.tab.rolls

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.github.jameshnsears.chance.common.utility.UtilityAndroidUnitTestHelper
import com.github.jameshnsears.chance.data.common.repo.RepositoryFactory
import com.github.jameshnsears.chance.data.domain.core.Dice
import com.github.jameshnsears.chance.data.domain.core.DiceRollValues
import com.github.jameshnsears.chance.data.domain.core.bag.testdouble.BagDataTestDouble
import com.github.jameshnsears.chance.data.domain.core.group.Group
import com.github.jameshnsears.chance.data.domain.core.group.testdouble.GroupDataTestDouble
import com.github.jameshnsears.chance.data.domain.core.roll.Roll
import com.github.jameshnsears.chance.data.domain.core.roll.testdouble.RollHistoryDataTestDouble
import com.github.jameshnsears.chance.data.domain.core.settings.testdouble.SettingsDataTestDouble
import io.mockk.coEvery
import io.mockk.every
import io.mockk.slot
import io.mockk.spyk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class RollsAndroidViewModelUnitTest : UtilityAndroidUnitTestHelper() {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @OptIn(DelicateCoroutinesApi::class)
    @Test
    fun rollDiceSequenceNoExplosionNoScore() = runTest {
        val tabRollAndroidViewModel = tabRollAndroidViewModel()

        coEvery { tabRollAndroidViewModel.playRollSound() } returns Unit

        val rollHistory = tabRollAndroidViewModel.repositoryRoll.fetch().first()
        assertEquals(2, rollHistory.size)

        val rolls = mutableListOf<Roll>()

        tabRollAndroidViewModel.rollDiceSequence(rolls)

        val diceSelected = mutableListOf<String>()
        tabRollAndroidViewModel.diceBag.value.forEach {
            if (it.selected)
                diceSelected.add(it.uuid)
        }

        assertEquals(1, diceSelected.size)

        assertEquals(3, rolls.size)

        rolls.forEach {
            assertEquals(diceSelected[0], it.uuidDice)
        }

        tabRollAndroidViewModel.rollsSequenceHelper.saveNewRollSequence(rolls)
        assertEquals(3, tabRollAndroidViewModel.repositoryRoll.fetch().first().size)

        val job = launch {
            tabRollAndroidViewModel.rollDiceSequence()
        }
        job.join()

        assertEquals(4, tabRollAndroidViewModel.repositoryRoll.fetch().first().size)
    }


    @OptIn(DelicateCoroutinesApi::class)
    @Test
    fun shuffleRollSequence() = runTest {
        val repositorySettings = RepositoryFactory().repositorySettings
        repositorySettings.store(SettingsDataTestDouble())

        val bagDataTestDouble = BagDataTestDouble()
        bagDataTestDouble.d4.selected = true
        bagDataTestDouble.d4.multiplierValue = 3
        bagDataTestDouble.d4.explode = false
        bagDataTestDouble.d6.selected = true
        bagDataTestDouble.d6.multiplierValue = 5
        bagDataTestDouble.d6.explode = false

        val repositoryBag = RepositoryFactory().repositoryBag
        repositoryBag.store(
            mutableListOf(
                bagDataTestDouble.d4,
                bagDataTestDouble.d6
            )
        )

        val repositoryRoll = RepositoryFactory().repositoryRoll
        val groupDataTestDouble = GroupDataTestDouble(bagDataTestDouble)
        val rollDataTestDouble = RollHistoryDataTestDouble(bagDataTestDouble, groupDataTestDouble)
        repositoryRoll.store(rollDataTestDouble.rollHistory)

        val rollsAndroidViewModel = spyk<RollsAndroidViewModel>(
            RollsAndroidViewModel(
                application(),
                repositorySettings,
                repositoryBag,
                repositoryRoll,
                RepositoryFactory().repositoryGroup
            )
        )

        coEvery { rollsAndroidViewModel.playRollSound() } returns Unit

        ////////////////

        val rolls = mutableListOf<Roll>()

        rollsAndroidViewModel.rollDiceSequence(rolls)
        assertEquals(8, rolls.size)

        rollsAndroidViewModel._stateFlowSettingsData.value.shuffle = true
        rollsAndroidViewModel.shuffleRollSequence(rolls)

        assertEquals(8, rolls.size)

        val diceUuidGroupRolls = rolls.groupBy { it.uuidDice }
        diceUuidGroupRolls.forEach { (_, diceUuidGroup) ->
            val sortedDiceUuidGroup = diceUuidGroup.sortedBy { it.multiplierIndex }
            for (i in sortedDiceUuidGroup.indices) {
                assertEquals(i + 1, sortedDiceUuidGroup[i].multiplierIndex)
            }
        }
    }

    @Test
    fun rollDiceSequenceWithExplosionEquals() = runTest {
        val diceBag = BagDataTestDouble()
        diceBag.d6.selected = true

        // d6
        diceBag.allDice.forEach {
            if (it.selected)
                it.explode = true
            it.explodeWhen = DiceRollValues.explodeWhenValues[0]
            it.explodeValue = 2
        }

        // s2
        val tabRollAndroidViewModel = deterministicTabRollAndroidViewModel(diceBag)

        val rolls = mutableListOf<Roll>()

        tabRollAndroidViewModel.rollDiceSequence(rolls)

        assertEquals(18, rolls.size)

        val rollSequence: MutableMap.MutableEntry<Long, List<Roll>> =
            mutableMapOf(1L to rolls.toList()).entries.first()

        assertEquals(
            "36", tabRollAndroidViewModel.rollsSequenceHelper.rollSequenceScore(
                rollSequence
            )
        )
    }

    @Test
    fun rollDiceSequenceWithExplosionLessThan() = runTest {
        val diceBag = BagDataTestDouble()
        diceBag.d6.selected = true

        // d6
        diceBag.allDice.forEach {
            if (it.selected)
                it.explode = true
            it.explodeWhen = DiceRollValues.explodeWhenValues[1]
            it.explodeValue = 3
        }

        // s2
        val tabRollAndroidViewModel = deterministicTabRollAndroidViewModel(diceBag)

        val rolls = mutableListOf<Roll>()

        tabRollAndroidViewModel.rollDiceSequence(rolls)

        assertEquals(18, rolls.size)
    }

    @Test
    fun rollDiceSequenceWithExplosionGreaterThan() = runTest {
        val diceBag = BagDataTestDouble()
        diceBag.d6.selected = true

        // d6
        diceBag.allDice.forEach {
            if (it.selected)
                it.explode = true
            it.explodeWhen = DiceRollValues.explodeWhenValues[2]
            it.explodeValue = 1
        }

        // s2
        val tabRollAndroidViewModel = deterministicTabRollAndroidViewModel(diceBag)

        val rolls = mutableListOf<Roll>()

        tabRollAndroidViewModel.rollDiceSequence(rolls)

        assertEquals(18, rolls.size)
    }

    @Test
    fun rollDiceSequenceWithScore() = runTest {
        val diceBag = BagDataTestDouble()
        diceBag.d6.selected = true

        // d6 only
        diceBag.allDice.forEach {
            if (it.selected)
                it.modifyScore = true
            it.modifyScoreValue = DiceRollValues.modifyScoreValues[0].toInt()
        }

        // s2
        val tabRollAndroidViewModel = deterministicTabRollAndroidViewModel(diceBag)

        val rolls = mutableListOf<Roll>()

        tabRollAndroidViewModel.rollDiceSequence(rolls)

        val rollSequence: MutableMap.MutableEntry<Long, List<Roll>> =
            mutableMapOf(1L to rolls.toList()).entries.first()

        assertEquals(
            "1", tabRollAndroidViewModel.rollsSequenceHelper.rollSequenceScore(
                rollSequence
            )
        )
    }

    @Test
    fun markDiceAsSelected() = runTest {
        val tabRollAndroidViewModel = tabRollAndroidViewModel()

        assertFalse(tabRollAndroidViewModel.diceBag.value[0].selected)
        assertFalse(tabRollAndroidViewModel.diceBag.value[1].selected)
        assertTrue(tabRollAndroidViewModel.diceBag.value[2].selected)
        assertFalse(tabRollAndroidViewModel.diceBag.value[3].selected)
        assertFalse(tabRollAndroidViewModel.diceBag.value[4].selected)
        assertFalse(tabRollAndroidViewModel.diceBag.value[5].selected)
        assertFalse(tabRollAndroidViewModel.diceBag.value[6].selected)

        tabRollAndroidViewModel.diceBag.value.forEach {
            tabRollAndroidViewModel.markDiceAsSelected(it, true)
        }

        tabRollAndroidViewModel.diceBag.value.forEach {
            assertTrue(it.selected)
        }
    }

    @Test
    fun dialogSettings() = runTest {
        val tabRollAndroidViewModel = tabRollAndroidViewModel()

        tabRollAndroidViewModel.settingsIndexTime(false)
        assertFalse(tabRollAndroidViewModel.stateFlowSettings.value.rollIndexTime)

        tabRollAndroidViewModel.settingsRollScore(false)
        assertFalse(tabRollAndroidViewModel.stateFlowSettings.value.rollScore)

        tabRollAndroidViewModel.settingsRollScoreTTS(false)
        assertFalse(tabRollAndroidViewModel.stateFlowSettings.value.rollScoreTTS)

        tabRollAndroidViewModel.settingsDiceTitle(false)
        assertFalse(tabRollAndroidViewModel.stateFlowSettings.value.diceTitle)

        tabRollAndroidViewModel.settingsSideNumber(false)
        assertFalse(tabRollAndroidViewModel.stateFlowSettings.value.sideNumber)

        tabRollAndroidViewModel.settingsSideDescription(false)
        assertFalse(tabRollAndroidViewModel.stateFlowSettings.value.sideDescription)

        tabRollAndroidViewModel.settingsSideSVG(false)
        assertFalse(tabRollAndroidViewModel.stateFlowSettings.value.sideSVG)

        tabRollAndroidViewModel.settingsRollSound(false)
        assertFalse(tabRollAndroidViewModel.stateFlowSettings.value.rollSound)

        tabRollAndroidViewModel.settingsBehaviour(false)
        assertFalse(tabRollAndroidViewModel.stateFlowSettings.value.rollBehaviour)

        tabRollAndroidViewModel.settingsShakeToRoll(false)
        assertFalse(tabRollAndroidViewModel.stateFlowSettings.value.shakeToRoll)

        tabRollAndroidViewModel.settingsUseHaptics(false)
        assertFalse(tabRollAndroidViewModel.stateFlowSettings.value.haptics)
    }

    @Test
    fun undo() = runTest {
        val tabRollAndroidViewModel = tabRollAndroidViewModel()
        tabRollAndroidViewModel._undoEnabled.value = true

        assertEquals(2, tabRollAndroidViewModel.repositoryRoll.fetch().first().size)
        tabRollAndroidViewModel.undo()
        tabRollAndroidViewModel.undo()

        assertEquals(0, tabRollAndroidViewModel.repositoryRoll.fetch().first().size)
    }

    @Test
    fun isContentAvailableToDisplay() = runTest {
        val tabRollAndroidViewModel = tabRollAndroidViewModel()
        val rolls = mutableListOf<Roll>()
        tabRollAndroidViewModel.rollDiceSequence(rolls)
        assertTrue(tabRollAndroidViewModel.isContentAvailableToDisplay(rolls))
    }

    @Test
    fun markGroupAsSelected() = runTest {
        val bagDataTestDouble = BagDataTestDouble()
        val groupDataTestDouble = GroupDataTestDouble(bagDataTestDouble)
        val repositoryGroup = RepositoryFactory().repositoryGroup
        repositoryGroup.store(groupDataTestDouble.groupHistory)

        val tabRollAndroidViewModel = tabRollAndroidViewModel(bagDataTestDouble)

        val group = groupDataTestDouble.groupHistory[0]
        tabRollAndroidViewModel.markGroupAsSelected(group)

        assertTrue(tabRollAndroidViewModel.groupHistory.value.first { it.uuid == group.uuid }.selected)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun rollDiceSequenceWithGroups() = runTest {
        val bagDataTestDouble = BagDataTestDouble()
        // Ensure no dice are selected individually
        bagDataTestDouble.allDice.forEach { it.selected = false }

        val groupDataTestDouble = GroupDataTestDouble(bagDataTestDouble)
        val repositoryGroup = RepositoryFactory().repositoryGroup
        repositoryGroup.store(groupDataTestDouble.groupHistory)

        val tabRollAndroidViewModel = tabRollAndroidViewModel(bagDataTestDouble, selectDefaultDice = false)
        advanceUntilIdle()

        val group = groupDataTestDouble.groupHistory[0] // tg1: dice 0 and 1

        tabRollAndroidViewModel.markGroupAsSelected(group)
        advanceUntilIdle()

        // Mock randomSide to return something predictable
        every { tabRollAndroidViewModel.randomSide(any()) } answers {
            (it.invocation.args[0] as Dice).sides[0]
        }

        val rolls = mutableListOf<Roll>()
        tabRollAndroidViewModel.rollDiceSequence(rolls)

        // tg1 has 2 dice: d2 (mult 1) and d4 (mult 2). Total 3 rolls.
        assertEquals(3, rolls.size)
        assertTrue(rolls.any { it.uuidDice == bagDataTestDouble.allDice[0].uuid })
        assertTrue(rolls.any { it.uuidDice == bagDataTestDouble.allDice[1].uuid })
    }

    @Test
    fun rollDiceSequenceWithExplosionDepthLimit() = runTest {
        val diceBag = BagDataTestDouble()
        diceBag.d6.selected = true
        diceBag.d6.multiplierValue = 1
        diceBag.d6.explode = true
        diceBag.d6.explodeWhen = DiceRollValues.explodeWhenValues[0] // Equals
        diceBag.d6.explodeValue = 6

        val tabRollAndroidViewModel = tabRollAndroidViewModel(diceBag)

        // Force explosion: return side with number 6
        every { tabRollAndroidViewModel.randomSide(any()) } returns diceBag.d6.sides[0]

        val rolls = mutableListOf<Roll>()
        tabRollAndroidViewModel.rollDiceSequence(rolls)

        // Expect 6 rolls due to current logic: Initial + 5 explosions
        assertEquals(6, rolls.size)
    }

    @Test
    fun shuffleRollSequenceMultiplierOrder() = runTest {
        val diceBag = BagDataTestDouble()
        diceBag.d4.selected = true
        diceBag.d6.selected = true
        diceBag.d6.multiplierValue = 3

        val tabRollAndroidViewModel = tabRollAndroidViewModel(diceBag)
        tabRollAndroidViewModel._stateFlowSettingsData.value.shuffle = true

        val rolls = mutableListOf<Roll>()
        // Manually create rolls to ensure they are out of order for multiplierIndex
        rolls.add(Roll(uuidDice = diceBag.d6.uuid, multiplierIndex = 3, side = diceBag.d6.sides[0]))
        rolls.add(Roll(uuidDice = diceBag.d6.uuid, multiplierIndex = 1, side = diceBag.d6.sides[1]))
        rolls.add(Roll(uuidDice = diceBag.d6.uuid, multiplierIndex = 2, side = diceBag.d6.sides[2]))

        tabRollAndroidViewModel.shuffleRollSequence(rolls)

        // Multiplier indices should be 1, 2, 3 in the resulting list (even if the sides were shuffled)
        assertEquals(1, rolls[0].multiplierIndex)
        assertEquals(2, rolls[1].multiplierIndex)
        assertEquals(3, rolls[2].multiplierIndex)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun rollEnabledAfterDeselectingDiceWithGroupSelected() = runTest {
        val bagDataTestDouble = BagDataTestDouble()
        val groupDataTestDouble = GroupDataTestDouble(bagDataTestDouble)
        val group = groupDataTestDouble.groupHistory[0].copy(selected = true)

        val repositoryGroup = RepositoryFactory().repositoryGroup
        repositoryGroup.store(listOf(group))

        val tabRollAndroidViewModel = tabRollAndroidViewModel(bagDataTestDouble)

        // d6 is selected by default in helper. Group is also selected.
        assertTrue(tabRollAndroidViewModel.rollEnabled.value)

        // Deselect d6
        tabRollAndroidViewModel.markDiceAsSelected(bagDataTestDouble.d6, false)

        // This SHOULD be true because the group is still selected.
        // If it's false, then the implementation of markDiceAsSelected is buggy.
        assertTrue("Roll should be enabled because a group is selected", tabRollAndroidViewModel.rollEnabled.value)
    }

    @Test
    fun rollGroupWithDuplicateDice() = runTest {
        val bagDataTestDouble = BagDataTestDouble()

        // Reset all dice selection
        bagDataTestDouble.allDice.forEach { it.selected = false }

        val d2 = bagDataTestDouble.d2
        val d4 = bagDataTestDouble.d4

        d2.selected = true

        val group = Group(
            name = "2d4",
            uuidDice = listOf(d4.uuid, d4.uuid),
            selected = true
        )

        val repositoryGroup = RepositoryFactory().repositoryGroup
        repositoryGroup.store(listOf(group))

        val tabRollAndroidViewModel = tabRollAndroidViewModel(bagDataTestDouble, selectDefaultDice = false)

        coEvery { tabRollAndroidViewModel.playRollSound() } returns Unit

        // Mock randomSide to return sides that won't trigger explosions
        // d2 sides are [2, 1]. d4 sides are [4, 3, 2, 1].
        // d4 explodes on 2. So we'll return side 4 (index 0).
        every { tabRollAndroidViewModel.randomSide(d2) } returns d2.sides[0]
        every { tabRollAndroidViewModel.randomSide(d4) } returns d4.sides[0]

        val newRollSequence = mutableListOf<Roll>()
        tabRollAndroidViewModel.rollDiceSequence(newRollSequence)

        // d2: multiplier 1 -> 1 roll
        // d4: multiplier 2 -> 2 rolls each. 2 d4 -> 4 rolls.
        // Total = 5 rolls (no explosions)

        val d2Rolls = newRollSequence.filter { it.uuidDice == d2.uuid }
        val d4Rolls = newRollSequence.filter { it.uuidDice == d4.uuid }

        assertEquals(1, d2Rolls.size)
        assertEquals(4, d4Rolls.size)
        assertEquals(5, newRollSequence.size)
    }

    @Test
    fun undoAll() = runTest {
        val tabRollAndroidViewModel = tabRollAndroidViewModel()
        tabRollAndroidViewModel._undoEnabled.value = true

        assertEquals(2, tabRollAndroidViewModel.repositoryRoll.fetch().first().size)
        tabRollAndroidViewModel.undoAll()

        assertEquals(0, tabRollAndroidViewModel.repositoryRoll.fetch().first().size)
        assertFalse(tabRollAndroidViewModel.undoEnabled.value)
    }

    private fun tabRollAndroidViewModel(
        bagDataTestDouble: BagDataTestDouble = BagDataTestDouble(),
        selectDefaultDice: Boolean = true
    ): RollsAndroidViewModel {
        val repositorySettings = RepositoryFactory().repositorySettings
        runTest {
            repositorySettings.store(SettingsDataTestDouble())
        }

        val repositoryBag = RepositoryFactory().repositoryBag
        runTest {
            if (selectDefaultDice)
                bagDataTestDouble.d6.selected = true
            repositoryBag.store(bagDataTestDouble.allDice)
        }

        val repositoryRoll = RepositoryFactory().repositoryRoll
        val groupDataTestDouble = GroupDataTestDouble(bagDataTestDouble)
        val rollDataTestDouble = RollHistoryDataTestDouble(bagDataTestDouble, groupDataTestDouble)
        runTest {
            repositoryRoll.store(rollDataTestDouble.rollHistory)
        }

        val repositoryGroup = RepositoryFactory().repositoryGroup

        return spyk<RollsAndroidViewModel>(
            RollsAndroidViewModel(
                application(),
                repositorySettings,
                repositoryBag,
                repositoryRoll,
                repositoryGroup
            )
        )
    }

    private fun deterministicTabRollAndroidViewModel(diceBag: BagDataTestDouble): RollsAndroidViewModel {
        val tabRollAndroidViewModel = tabRollAndroidViewModel(diceBag)

        val diceSlot = slot<Dice>()
        every {
            tabRollAndroidViewModel.randomSide(
                dice = capture(diceSlot)
            )
        } answers {
            // s2
            diceSlot.captured.sides[4]
        }

        return tabRollAndroidViewModel
    }
}
