package com.github.jameshnsears.chance.ui.tab.rolls

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.github.jameshnsears.chance.common.utility.UtilityAndroidUnitTestHelper
import com.github.jameshnsears.chance.data.common.repo.RepositoryFactory
import com.github.jameshnsears.chance.data.domain.core.Dice
import com.github.jameshnsears.chance.data.domain.core.DiceRollValues
import com.github.jameshnsears.chance.data.domain.core.bag.testdouble.BagDataTestDouble
import com.github.jameshnsears.chance.data.domain.core.group.GroupHistory
import com.github.jameshnsears.chance.data.domain.core.group.testdouble.GroupDataTestDouble
import com.github.jameshnsears.chance.data.domain.core.roll.Roll
import com.github.jameshnsears.chance.data.domain.core.roll.testdouble.RollHistoryDataTestDouble
import com.github.jameshnsears.chance.data.domain.core.settings.testdouble.SettingsDataTestDouble
import io.mockk.every
import io.mockk.slot
import io.mockk.spyk
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class RollsAndroidViewModelUnitTest : UtilityAndroidUnitTestHelper() {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @OptIn(DelicateCoroutinesApi::class)
    @Test
    fun rollDiceSequenceNoExplosionNoScore() = runTest {
        val tabRollAndroidViewModel = tabRollAndroidViewModel()

        val rollHistory = tabRollAndroidViewModel.repositoryRoll.fetch().first()
        assertEquals(2, rollHistory.size)

        val rolls = mutableListOf<Roll>()

        tabRollAndroidViewModel.rollsCoreHelper.generateRollDiceSequence(
            tabRollAndroidViewModel.diceBag.value,
            tabRollAndroidViewModel.groupHistory.value,
            rolls,
        )

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

        tabRollAndroidViewModel.rollsCoreHelper.saveNewRollSequence(rolls)
        assertEquals(3, tabRollAndroidViewModel.repositoryRoll.fetch().first().size)

        val job = launch {
            tabRollAndroidViewModel.rollDiceSequence()
        }
        job.join()

        assertEquals(4, tabRollAndroidViewModel.repositoryRoll.fetch().first().size)
    }


    @OptIn(DelicateCoroutinesApi::class, ExperimentalCoroutinesApi::class)
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

        val rollsAndroidViewModel = tabRollAndroidViewModel(
            bagDataTestDouble = bagDataTestDouble,
            selectDefaultDice = false
        )

        ////////////////

        val rolls = mutableListOf<Roll>()

        rollsAndroidViewModel.rollsCoreHelper.generateRollDiceSequence(
            rollsAndroidViewModel.diceBag.value,
            rollsAndroidViewModel.groupHistory.value,
            rolls
        )
        assertEquals(8, rolls.size)

        rollsAndroidViewModel.settingsShuffle(checked = true)
        advanceUntilIdle()
        rollsAndroidViewModel.rollsCoreHelper.shuffleRollSequence(rolls, true)

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
        diceBag.allDice().forEach {
            if (it.selected)
                it.explode = true
            it.explodeWhen = DiceRollValues.EXPLODE_WHEN_VALUES[0]
            it.explodeValue = 2
        }

        // s2
        val tabRollAndroidViewModel = deterministicTabRollAndroidViewModel(diceBag)

        val rolls = mutableListOf<Roll>()

        tabRollAndroidViewModel.rollsCoreHelper.generateRollDiceSequence(
            tabRollAndroidViewModel.diceBag.value,
            tabRollAndroidViewModel.groupHistory.value,
            rolls,
        )

        assertEquals(18, rolls.size)

        val rollSequence: MutableMap.MutableEntry<Long, List<Roll>> =
            mutableMapOf(1L to rolls.toList()).entries.first()

        assertEquals(
            "36", tabRollAndroidViewModel.rollsCoreHelper.rollSequenceScore(
                rollSequence
            )
        )
    }

    @Test
    fun rollDiceSequenceWithExplosionLessThan() = runTest {
        val diceBag = BagDataTestDouble()
        diceBag.d6.selected = true

        // d6
        diceBag.allDice().forEach {
            if (it.selected)
                it.explode = true
            it.explodeWhen = DiceRollValues.EXPLODE_WHEN_VALUES[1]
            it.explodeValue = 3
        }

        // s2
        val tabRollAndroidViewModel = deterministicTabRollAndroidViewModel(diceBag)

        val rolls = mutableListOf<Roll>()

        tabRollAndroidViewModel.rollsCoreHelper.generateRollDiceSequence(
            tabRollAndroidViewModel.diceBag.value,
            tabRollAndroidViewModel.groupHistory.value,
            rolls,
        )

        assertEquals(18, rolls.size)
    }

    @Test
    fun rollDiceSequenceWithExplosionGreaterThan() = runTest {
        val diceBag = BagDataTestDouble()
        diceBag.d6.selected = true

        // d6
        diceBag.allDice().forEach {
            if (it.selected)
                it.explode = true
            it.explodeWhen = DiceRollValues.EXPLODE_WHEN_VALUES[2]
            it.explodeValue = 1
        }

        // s2
        val tabRollAndroidViewModel = deterministicTabRollAndroidViewModel(diceBag)

        val rolls = mutableListOf<Roll>()

        tabRollAndroidViewModel.rollsCoreHelper.generateRollDiceSequence(
            tabRollAndroidViewModel.diceBag.value,
            tabRollAndroidViewModel.groupHistory.value,
            rolls,
        )

        assertEquals(18, rolls.size)
    }

    @Test
    fun rollDiceSequenceWithScore() = runTest {
        val diceBag = BagDataTestDouble()
        diceBag.d6.selected = true

        // d6 only
        diceBag.allDice().forEach {
            if (it.selected)
                it.modifyScore = true
            it.modifyScoreValue = DiceRollValues.MODIFY_SCORE_VALUES[0].toInt()
        }

        // s2
        val tabRollAndroidViewModel = deterministicTabRollAndroidViewModel(diceBag)

        val rolls = mutableListOf<Roll>()

        tabRollAndroidViewModel.rollsCoreHelper.generateRollDiceSequence(
            tabRollAndroidViewModel.diceBag.value,
            tabRollAndroidViewModel.groupHistory.value,
            rolls,
        )

        val rollSequence: MutableMap.MutableEntry<Long, List<Roll>> =
            mutableMapOf(1L to rolls.toList()).entries.first()

        assertEquals(
            "1", tabRollAndroidViewModel.rollsCoreHelper.rollSequenceScore(
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

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun undo() = runTest {
        val tabRollAndroidViewModel = tabRollAndroidViewModel()
        advanceUntilIdle()

        assertEquals(2, tabRollAndroidViewModel.repositoryRoll.fetch().first().size)
        assertTrue(tabRollAndroidViewModel.undoEnabled.value)
        tabRollAndroidViewModel.undo()
        tabRollAndroidViewModel.undo()
        advanceUntilIdle()

        assertEquals(0, tabRollAndroidViewModel.repositoryRoll.fetch().first().size)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun markGroupAsSelected() = runTest {
        val bagDataTestDouble = BagDataTestDouble()
        val groupDataTestDouble = GroupDataTestDouble(bagDataTestDouble)

        val tabRollAndroidViewModel = tabRollAndroidViewModel(
            bagDataTestDouble,
            groupHistory = groupDataTestDouble.groupHistory
        )
        advanceUntilIdle()

        val group = groupDataTestDouble.groupHistory[0]
        tabRollAndroidViewModel.markGroupAsSelected(group)
        advanceUntilIdle()

        assertTrue(tabRollAndroidViewModel.groupHistory.value.first { it.uuid == group.uuid }.selected)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun rollDiceSequenceWithGroups() = runTest {
        val bagDataTestDouble = BagDataTestDouble()
        // Ensure no dice are selected individually
        bagDataTestDouble.allDice().forEach { it.selected = false }

        val groupDataTestDouble = GroupDataTestDouble(bagDataTestDouble)
        val tabRollAndroidViewModel = tabRollAndroidViewModel(
            bagDataTestDouble,
            groupHistory = groupDataTestDouble.groupHistory,
            selectDefaultDice = false
        )
        advanceUntilIdle()

        val group = groupDataTestDouble.groupHistory[0] // tg1: dice 0 and 1

        tabRollAndroidViewModel.markGroupAsSelected(group)
        advanceUntilIdle()

        // Mock randomSide to return something predictable
        every { tabRollAndroidViewModel.rollsCoreHelper.randomSide(any()) } answers {
            (it.invocation.args[0] as Dice).sides[0]
        }

        val rolls = mutableListOf<Roll>()
        tabRollAndroidViewModel.rollsCoreHelper.generateRollDiceSequence(
            tabRollAndroidViewModel.diceBag.value,
            tabRollAndroidViewModel.groupHistory.value,
            rolls,
        )

        // tg1 has 2 dice: d2 (mult 1) and d4 (mult 2). Total 3 rolls.
        assertEquals(3, rolls.size)
        val allDice = bagDataTestDouble.allDice()
        assertTrue(rolls.any { it.uuidDice == allDice[0].uuid })
        assertTrue(rolls.any { it.uuidDice == allDice[1].uuid })
    }

    @Test
    fun rollDiceSequenceWithExplosionDepthLimit() = runTest {
        val diceBag = BagDataTestDouble()
        diceBag.d6.selected = true
        diceBag.d6.multiplierValue = 1
        diceBag.d6.explode = true
        diceBag.d6.explodeWhen = DiceRollValues.EXPLODE_WHEN_VALUES[0] // Equals
        diceBag.d6.explodeValue = 6

        val tabRollAndroidViewModel = tabRollAndroidViewModel(diceBag)

        // Force explosion: return side with number 6
        every { tabRollAndroidViewModel.rollsCoreHelper.randomSide(any()) } returns diceBag.d6.sides[0]

        val rolls = mutableListOf<Roll>()
        tabRollAndroidViewModel.rollsCoreHelper.generateRollDiceSequence(
            tabRollAndroidViewModel.diceBag.value,
            tabRollAndroidViewModel.groupHistory.value,
            rolls,
        )

        // Expect 6 rolls due to current logic: Initial + 5 explosions
        assertEquals(6, rolls.size)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun shuffleRollSequenceMultiplierOrder() = runTest {
        val diceBag = BagDataTestDouble()
        diceBag.d4.selected = true
        diceBag.d6.selected = true
        diceBag.d6.multiplierValue = 3

        val tabRollAndroidViewModel = tabRollAndroidViewModel(diceBag)
        tabRollAndroidViewModel.settingsShuffle(checked = true)
        advanceUntilIdle()

        val rolls = mutableListOf<Roll>()
        // Manually create rolls to ensure they are out of order for multiplierIndex
        rolls.add(Roll(uuidDice = diceBag.d6.uuid, multiplierIndex = 3, side = diceBag.d6.sides[0]))
        rolls.add(Roll(uuidDice = diceBag.d6.uuid, multiplierIndex = 1, side = diceBag.d6.sides[1]))
        rolls.add(Roll(uuidDice = diceBag.d6.uuid, multiplierIndex = 2, side = diceBag.d6.sides[2]))

        tabRollAndroidViewModel.rollsCoreHelper.shuffleRollSequence(rolls, true)

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
        val groupHistory = groupDataTestDouble.groupHistory.toMutableList()
        groupHistory[0] = groupHistory[0].copy(selected = true)

        val tabRollAndroidViewModel = tabRollAndroidViewModel(
            bagDataTestDouble,
            groupHistory = groupHistory
        )

        advanceUntilIdle()

        // d6 is selected by default in helper. Group is also selected.
        assertTrue(tabRollAndroidViewModel.rollEnabled.value)

        // Deselect d6
        tabRollAndroidViewModel.markDiceAsSelected(bagDataTestDouble.d6, false)
        advanceUntilIdle()

        // This SHOULD be true because the group is still selected.
        // If it's false, then the implementation of markDiceAsSelected is buggy.
        assertTrue("Roll should be enabled because a group is selected", tabRollAndroidViewModel.rollEnabled.value)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun rollGroupWithDuplicateDice() = runTest {
        val bagDataTestDouble = BagDataTestDouble()

        // Reset all dice selection
        bagDataTestDouble.allDice().forEach { it.selected = false }

        val d2 = bagDataTestDouble.d2
        val d4 = bagDataTestDouble.d4

        d2.selected = true

        val groupDataTestDouble = GroupDataTestDouble(bagDataTestDouble)
        val groupHistory = groupDataTestDouble.groupHistory.toMutableList()
        groupHistory[0] = groupHistory[0].copy(
            name = "2d4",
            uuidDice = listOf(d4.uuid, d4.uuid),
            selected = true
        )

        val tabRollAndroidViewModel = tabRollAndroidViewModel(
            bagDataTestDouble,
            groupHistory = groupHistory,
            selectDefaultDice = false
        )
        advanceUntilIdle()

        // Mock randomSide to return sides that won't trigger explosions
        // d2 sides are [2, 1]. d4 sides are [4, 3, 2, 1].
        // d4 explodes on 2. So we'll return side 4 (index 0).
        every { tabRollAndroidViewModel.rollsCoreHelper.randomSide(d2) } returns d2.sides[0]
        every { tabRollAndroidViewModel.rollsCoreHelper.randomSide(d4) } returns d4.sides[0]

        val newRollSequence = mutableListOf<Roll>()
        tabRollAndroidViewModel.rollsCoreHelper.generateRollDiceSequence(
            tabRollAndroidViewModel.diceBag.value,
            tabRollAndroidViewModel.groupHistory.value,
            newRollSequence
        )

        // d2: multiplier 1 -> 1 roll
        // d4: multiplier 2 -> 2 rolls each. 2 d4 -> 4 rolls.
        // Total = 5 rolls (no explosions)

        val d2Rolls = newRollSequence.filter { it.uuidDice == d2.uuid }
        val d4Rolls = newRollSequence.filter { it.uuidDice == d4.uuid }

        assertEquals(1, d2Rolls.size)
        assertEquals(4, d4Rolls.size)
        assertEquals(5, newRollSequence.size)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun undoAll() = runTest {
        val tabRollAndroidViewModel = tabRollAndroidViewModel()
        advanceUntilIdle()

        assertEquals(2, tabRollAndroidViewModel.repositoryRoll.fetch().first().size)
        assertTrue(tabRollAndroidViewModel.undoEnabled.value)
        tabRollAndroidViewModel.undoAll()
        advanceUntilIdle()

        assertEquals(0, tabRollAndroidViewModel.repositoryRoll.fetch().first().size)
        assertFalse(tabRollAndroidViewModel.undoEnabled.value)
    }

    private fun tabRollAndroidViewModel(
        bagDataTestDouble: BagDataTestDouble = BagDataTestDouble(),
        groupHistory: GroupHistory? = null,
        selectDefaultDice: Boolean = true
    ): RollsAndroidViewModel {
        val repositorySettings = RepositoryFactory().repositorySettings
        runBlocking {
            repositorySettings.store(SettingsDataTestDouble())
        }

        val repositoryBag = RepositoryFactory().repositoryBag
        runBlocking {
            if (selectDefaultDice)
                bagDataTestDouble.d6.selected = true
            repositoryBag.store(bagDataTestDouble.allDice())
        }

        val repositoryGroup = RepositoryFactory().repositoryGroup
        val finalGroupHistory = groupHistory ?: GroupDataTestDouble(bagDataTestDouble).groupHistory
        runBlocking {
            repositoryGroup.store(finalGroupHistory)
        }

        val repositoryRoll = RepositoryFactory().repositoryRoll
        val groupData = object : com.github.jameshnsears.chance.data.domain.core.group.GroupDataInterface {
            override val groupHistory = finalGroupHistory
        }
        val rollDataTestDouble = RollHistoryDataTestDouble(bagDataTestDouble, groupData)
        runBlocking {
            repositoryRoll.store(rollDataTestDouble.rollHistory)
        }

        val repositoryRollSpy = spyk(repositoryRoll)
        val rollsCoreHelper = spyk(RollsCoreHelper(repositoryRollSpy))
        val rollsSelectionHelper = RollsSelectionHelper(repositoryBag, repositoryGroup)

        return RollsAndroidViewModel(
            application(),
            repositorySettings,
            repositoryBag,
            repositoryRollSpy,
            repositoryGroup,
            rollsSelectionHelper,
            rollsCoreHelper
        )
    }

    private fun deterministicTabRollAndroidViewModel(diceBag: BagDataTestDouble): RollsAndroidViewModel {
        val tabRollAndroidViewModel = tabRollAndroidViewModel(diceBag)

        val diceSlot = slot<Dice>()
        every {
            tabRollAndroidViewModel.rollsCoreHelper.randomSide(
                dice = capture(diceSlot)
            )
        } answers {
            // s2
            diceSlot.captured.sides[4]
        }

        return tabRollAndroidViewModel
    }
}
