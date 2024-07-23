package com.github.jameshnsears.chance.ui.tab.roll

import com.github.jameshnsears.chance.data.domain.core.Dice
import com.github.jameshnsears.chance.data.domain.core.DiceRollValues
import com.github.jameshnsears.chance.data.domain.core.bag.testdouble.BagDataTestDouble
import com.github.jameshnsears.chance.data.domain.core.roll.Roll
import com.github.jameshnsears.chance.data.domain.core.roll.testdouble.RollHistoryDataTestDouble
import com.github.jameshnsears.chance.data.domain.core.settings.testdouble.SettingsDataTestDouble
import com.github.jameshnsears.chance.data.repository.RepositoryFactory
import com.github.jameshnsears.chance.utility.android.UtilityAndroidHelper
import io.mockk.every
import io.mockk.slot
import io.mockk.spyk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test


class TabRollAndroidViewModelUnitTest : UtilityAndroidHelper() {
    @Test
    fun rollDiceSequenceNoExplosionNoScore() = runTest {
        val tabRollViewModel = tabRollViewModel()

        val rollHistory = tabRollViewModel.repositoryRoll.fetch().first()
        assertEquals(2, rollHistory.size)

        val rolls = mutableListOf<Roll>()

        tabRollViewModel.rollDiceSequence(rolls)

        val diceSelected = mutableListOf<Long>()
        tabRollViewModel.diceBag.value.forEach {
            if (it.selected)
                diceSelected.add(it.epoch)
        }

        assertEquals(1, diceSelected.size)

        assertEquals(3, rolls.size)

        rolls.forEach {
            assertEquals(diceSelected[0], it.diceEpoch)
        }

        tabRollViewModel.diceSequenceStore(rolls)
        assertEquals(3, tabRollViewModel.repositoryRoll.fetch().first().size)

        tabRollViewModel.rollDiceSequence()
        assertEquals(4, tabRollViewModel.repositoryRoll.fetch().first().size)
    }

    @Test
    fun rollDiceSequenceWithExplosionEquals() = runTest {
        val diceBag = BagDataTestDouble()

        // d6
        diceBag.allDice.forEach {
            if (it.selected)
                it.explode = true
            it.explodeWhen = DiceRollValues.explodeWhenValues[0]
            it.explodeValue = 2
        }

        // s2
        val tabRollViewModel = deterministicTabRollViewModel(diceBag)

        val rolls = mutableListOf<Roll>()

        tabRollViewModel.rollDiceSequence(rolls)

        assertEquals(18, rolls.size)

        val rollSequence: MutableMap.MutableEntry<Long, List<Roll>> =
            mutableMapOf(1L to rolls.toList()).entries.first()

        assertEquals(
            "36", tabRollViewModel.diceSequenceScore(
                rollSequence
            )
        )
    }

    @Test
    fun rollDiceSequenceWithExplosionLessThan() = runTest {
        val diceBag = BagDataTestDouble()

        // d6
        diceBag.allDice.forEach {
            if (it.selected)
                it.explode = true
            it.explodeWhen = DiceRollValues.explodeWhenValues[1]
            it.explodeValue = 3
        }

        // s2
        val tabRollViewModel = deterministicTabRollViewModel(diceBag)

        val rolls = mutableListOf<Roll>()

        tabRollViewModel.rollDiceSequence(rolls)

        assertEquals(18, rolls.size)
    }

    @Test
    fun rollDiceSequenceWithExplosionGreaterThan() = runTest {
        val diceBag = BagDataTestDouble()

        // d6
        diceBag.allDice.forEach {
            if (it.selected)
                it.explode = true
            it.explodeWhen = DiceRollValues.explodeWhenValues[2]
            it.explodeValue = 1
        }

        // s2
        val tabRollViewModel = deterministicTabRollViewModel(diceBag)

        val rolls = mutableListOf<Roll>()

        tabRollViewModel.rollDiceSequence(rolls)

        assertEquals(18, rolls.size)
    }

    @Test
    fun rollDiceSequenceWithScore() = runTest {
        val diceBag = BagDataTestDouble()

        // d6 only
        diceBag.allDice.forEach {
            if (it.selected)
                it.modifyScore = true
            it.modifyScoreValue = DiceRollValues.modifyScoreValues[0].toInt()
        }

        // s2
        val tabRollViewModel = deterministicTabRollViewModel(diceBag)

        val rolls = mutableListOf<Roll>()

        tabRollViewModel.rollDiceSequence(rolls)

        val rollSequence: MutableMap.MutableEntry<Long, List<Roll>> =
            mutableMapOf(1L to rolls.toList()).entries.first()

        assertEquals(
            "1", tabRollViewModel.diceSequenceScore(
                rollSequence
            )
        )
    }

    @Test
    fun markDiceAsSelected() = runTest {
        val tabRollViewModel = tabRollViewModel()

        assertFalse(tabRollViewModel.diceBag.value[0].selected)
        assertFalse(tabRollViewModel.diceBag.value[1].selected)
        assertTrue(tabRollViewModel.diceBag.value[2].selected)
        assertFalse(tabRollViewModel.diceBag.value[3].selected)
        assertFalse(tabRollViewModel.diceBag.value[4].selected)
        assertFalse(tabRollViewModel.diceBag.value[5].selected)
        assertFalse(tabRollViewModel.diceBag.value[6].selected)
        assertFalse(tabRollViewModel.diceBag.value[7].selected)

        tabRollViewModel.diceBag.value.forEach {
            tabRollViewModel.markDiceAsSelected(it, true)
        }

        tabRollViewModel.diceBag.value.forEach {
            assertTrue(it.selected)
        }
    }

    @Test
    fun dialogSettings() = runTest {
        val tabRollViewModel = tabRollViewModel()

        tabRollViewModel.settingsIndexTime(false)
        assertFalse(tabRollViewModel.stateFlowSettings.value.rollIndexTime)

        tabRollViewModel.settingsRollScore(false)
        assertFalse(tabRollViewModel.stateFlowSettings.value.rollScore)

        tabRollViewModel.settingsDiceTitle(false)
        assertFalse(tabRollViewModel.stateFlowSettings.value.diceTitle)

        tabRollViewModel.settingsSideNumber(false)
        assertFalse(tabRollViewModel.stateFlowSettings.value.sideNumber)

        tabRollViewModel.settingsSideDescription(false)
        assertFalse(tabRollViewModel.stateFlowSettings.value.sideDescription)

        tabRollViewModel.settingsSideSVG(false)
        assertFalse(tabRollViewModel.stateFlowSettings.value.sideSVG)

        tabRollViewModel.settingsRollSound(false)
        assertFalse(tabRollViewModel.stateFlowSettings.value.rollSound)

        tabRollViewModel.settingsBehaviour(false)
        assertFalse(tabRollViewModel.stateFlowSettings.value.behaviour)
    }

    @Test
    fun undo() = runTest {
        val tabRollViewModel = tabRollViewModel()

        assertEquals(2, tabRollViewModel.repositoryRoll.fetch().first().size)
        tabRollViewModel.undo()
        tabRollViewModel.undo()

        assertEquals(0, tabRollViewModel.repositoryRoll.fetch().first().size)
    }

    @Test
    fun isContentAvailableToDisplay() = runTest {
        val tabRollViewModel = tabRollViewModel()
        val rolls = mutableListOf<Roll>()
        tabRollViewModel.rollDiceSequence(rolls)
        assertTrue(tabRollViewModel.isContentAvailableToDisplay(rolls))
    }

    private fun tabRollViewModel(
        bagDataTestDouble: BagDataTestDouble = BagDataTestDouble(),
    ): TabRollAndroidViewModel {
        val repositorySettings = RepositoryFactory().repositorySettings
        runBlocking(Dispatchers.Main) {
            repositorySettings.store(SettingsDataTestDouble())
        }

        val repositoryBag = RepositoryFactory().repositoryBag
        runBlocking(Dispatchers.Main) {
            repositoryBag.store(bagDataTestDouble.allDice)
        }

        val repositoryRoll = RepositoryFactory().repositoryRoll
        val rollDataTestDouble = RollHistoryDataTestDouble(bagDataTestDouble)
        runBlocking(Dispatchers.Main) {
            repositoryRoll.store(rollDataTestDouble.rollHistory)
        }

        return spyk<TabRollAndroidViewModel>(
            TabRollAndroidViewModel(
                getApplication(),
                repositorySettings,
                repositoryBag,
                repositoryRoll
            )
        )
    }

    private fun deterministicTabRollViewModel(diceBag: BagDataTestDouble): TabRollAndroidViewModel {
        val tabRollViewModel = tabRollViewModel(diceBag)

        val diceSlot = slot<Dice>()
        every {
            tabRollViewModel.randomSide(
                dice = capture(diceSlot)
            )
        } answers {
            // s2
            diceSlot.captured.sides[4]
        }

        return tabRollViewModel
    }
}
