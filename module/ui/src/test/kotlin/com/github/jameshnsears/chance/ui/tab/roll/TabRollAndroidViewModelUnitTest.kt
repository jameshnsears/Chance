package com.github.jameshnsears.chance.ui.tab.roll

import com.github.jameshnsears.chance.data.domain.state.Dice
import com.github.jameshnsears.chance.data.domain.state.DiceRollValues
import com.github.jameshnsears.chance.data.domain.state.Roll
import com.github.jameshnsears.chance.data.domain.state.Settings
import com.github.jameshnsears.chance.data.repository.bag.mock.RepositoryBagTestDouble
import com.github.jameshnsears.chance.data.repository.roll.mock.RepositoryRollTestDouble
import com.github.jameshnsears.chance.data.repository.settings.mock.RepositorySettingsTestDouble
import com.github.jameshnsears.chance.data.sample.bag.SampleBagTestData
import com.github.jameshnsears.chance.data.sample.roll.SampleRollTestData
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
    fun diceSequenceRollNoExplosionNoScore() = runTest {
        val tabRollViewModel = tabRollViewModel()

        val rollHistory = tabRollViewModel.repositoryRoll.fetch().first()
        assertEquals(2, rollHistory.size)

        val rolls = mutableListOf<Roll>()

        tabRollViewModel.diceSequenceRoll(rolls)

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

        tabRollViewModel.diceSequenceRoll()
        assertEquals(4, tabRollViewModel.repositoryRoll.fetch().first().size)
    }

    @Test
    fun diceSequenceRollWithExplosionEquals() = runTest {
        val diceBag = SampleBagTestData()

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

        tabRollViewModel.diceSequenceRoll(rolls)

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
    fun diceSequenceRollWithExplosionLessThan() = runTest {
        val diceBag = SampleBagTestData()

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

        tabRollViewModel.diceSequenceRoll(rolls)

        assertEquals(18, rolls.size)
    }

    @Test
    fun diceSequenceRollWithExplosionGreaterThan() = runTest {
        val diceBag = SampleBagTestData()

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

        tabRollViewModel.diceSequenceRoll(rolls)

        assertEquals(18, rolls.size)
    }

    @Test
    fun diceSequenceRollWithScore() = runTest {
        val diceBag = SampleBagTestData()

        // d6 only
        diceBag.allDice.forEach {
            if (it.selected)
                it.modifyScore = true
            it.modifyScoreValue = DiceRollValues.modifyScoreValues[0].toInt()
        }

        // s2
        val tabRollViewModel = deterministicTabRollViewModel(diceBag)

        val rolls = mutableListOf<Roll>()

        tabRollViewModel.diceSequenceRoll(rolls)

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
    fun markTabAsCurrentInSettings() = runTest {
        val tabRollViewModel = tabRollViewModel()

        tabRollViewModel.markTabAsCurrentInSettings()

        val repositorySettings = tabRollViewModel.repositorySettings.fetch().first()
        assertEquals(
            1, repositorySettings.tabRowChance
        )
    }

    @Test
    fun dialogSettings() = runTest {
        val tabRollViewModel = tabRollViewModel()

        tabRollViewModel.settingsTime(false)
        assertFalse(tabRollViewModel.stateFlowTabRoll.value.rollTime)

        tabRollViewModel.settingsRollScore(false)
        assertFalse(tabRollViewModel.stateFlowTabRoll.value.rollScore)

        tabRollViewModel.settingsDiceTitle(false)
        assertFalse(tabRollViewModel.stateFlowTabRoll.value.diceTitle)

        tabRollViewModel.settingsSideNumber(false)
        assertFalse(tabRollViewModel.stateFlowTabRoll.value.sideNumber)

        tabRollViewModel.settingsSideDescription(false)
        assertFalse(tabRollViewModel.stateFlowTabRoll.value.sideDescription)

        tabRollViewModel.settingsSideSVG(false)
        assertFalse(tabRollViewModel.stateFlowTabRoll.value.sideSVG)

        tabRollViewModel.settingsRollSound(false)
        assertFalse(tabRollViewModel.stateFlowTabRoll.value.rollSound)

        tabRollViewModel.settingsBehaviour(false)
        assertFalse(tabRollViewModel.stateFlowTabRoll.value.behaviour)
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
        tabRollViewModel.diceSequenceRoll(rolls)
        assertTrue(tabRollViewModel.isContentAvailableToDisplay(rolls))
    }

    private fun tabRollViewModel(
        sampleBagTestData: SampleBagTestData = SampleBagTestData(),
    ): TabRollAndroidViewModel {

        val repositorySettings = RepositorySettingsTestDouble.getInstance()
        runBlocking(Dispatchers.Main) {
            repositorySettings.store(Settings())
        }

        val repositoryBag = RepositoryBagTestDouble.getInstance()
        runBlocking(Dispatchers.Main) {
            repositoryBag.store(sampleBagTestData.allDice)
        }

        val sampleRollTestData = SampleRollTestData(sampleBagTestData)

        val repositoryRoll = RepositoryRollTestDouble.getInstance()
        runBlocking(Dispatchers.Main) {
            repositoryRoll.store(sampleRollTestData.rollHistory)
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

    private fun deterministicTabRollViewModel(diceBag: SampleBagTestData): TabRollAndroidViewModel {
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
