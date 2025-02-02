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
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test


class TabRollAndroidViewModelUnitTest : UtilityAndroidHelper() {
    @OptIn(DelicateCoroutinesApi::class)
    @Test
    fun rollDiceSequenceNoExplosionNoScore() = runTest {
        val tabRollAndroidViewModel = tabRollAndroidViewModel()

        every { tabRollAndroidViewModel.mediaPlayerRollSound() } returns Unit

        val rollHistory = tabRollAndroidViewModel.repositoryRoll.fetch().first()
        assertEquals(2, rollHistory.size)

        val rolls = mutableListOf<Roll>()

        tabRollAndroidViewModel.rollDiceSequence(rolls)

        val diceSelected = mutableListOf<Long>()
        tabRollAndroidViewModel.diceBag.value.forEach {
            if (it.selected)
                diceSelected.add(it.epoch)
        }

        assertEquals(1, diceSelected.size)

        assertEquals(3, rolls.size)

        rolls.forEach {
            assertEquals(diceSelected[0], it.diceEpoch)
        }

        tabRollAndroidViewModel.saveNewRollSequence(rolls)
        assertEquals(3, tabRollAndroidViewModel.repositoryRoll.fetch().first().size)

        val job = GlobalScope.launch {
            tabRollAndroidViewModel.rollDiceSequence()
        }
        job.join()

        assertEquals(4, tabRollAndroidViewModel.repositoryRoll.fetch().first().size)
    }


    @OptIn(DelicateCoroutinesApi::class)
    @Test
    fun shuffleRollSequence() = runTest {
        val repositorySettings = RepositoryFactory().repositorySettings
        runBlocking(Dispatchers.Main) {
            repositorySettings.store(SettingsDataTestDouble())
        }

        val bagDataTestDouble = BagDataTestDouble()
        bagDataTestDouble.d4.selected = true
        bagDataTestDouble.d4.multiplierValue = 3
        bagDataTestDouble.d4.explode = false
        bagDataTestDouble.d6.selected = true
        bagDataTestDouble.d6.multiplierValue = 5
        bagDataTestDouble.d6.explode = false

        val repositoryBag = RepositoryFactory().repositoryBag
        runBlocking(Dispatchers.Main) {
            repositoryBag.store(
                mutableListOf(
                    bagDataTestDouble.d4,
                    bagDataTestDouble.d6
                )
            )
        }

        val repositoryRoll = RepositoryFactory().repositoryRoll
        val rollDataTestDouble = RollHistoryDataTestDouble(bagDataTestDouble)
        runBlocking(Dispatchers.Main) {
            repositoryRoll.store(rollDataTestDouble.rollHistory)
        }

        val tabRollAndroidViewModel = spyk<TabRollAndroidViewModel>(
            TabRollAndroidViewModel(
                getApplication(),
                repositorySettings,
                repositoryBag,
                repositoryRoll
            )
        )

        every { tabRollAndroidViewModel.mediaPlayerRollSound() } returns Unit

        ////////////////

        val rolls = mutableListOf<Roll>()

        tabRollAndroidViewModel.rollDiceSequence(rolls)
        assertEquals(8, rolls.size)

        tabRollAndroidViewModel._stateFlowSettingsData.value.shuffle = true
        tabRollAndroidViewModel.shuffleRollSequence(rolls)

        assertEquals(8, rolls.size)

        val diceEpochGroupRolls = rolls.groupBy { it.diceEpoch }
        diceEpochGroupRolls.forEach { (_, diceEpochGroup) ->
            val sortedDiceEpochGroup = diceEpochGroup.sortedBy { it.multiplierIndex }
            for (i in sortedDiceEpochGroup.indices) {
                assertEquals(i + 1, sortedDiceEpochGroup[i].multiplierIndex)
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
            "36", tabRollAndroidViewModel.diceSequenceScore(
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
            "1", tabRollAndroidViewModel.diceSequenceScore(
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
        assertFalse(tabRollAndroidViewModel.diceBag.value[7].selected)

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

    private fun tabRollAndroidViewModel(
        bagDataTestDouble: BagDataTestDouble = BagDataTestDouble(),
    ): TabRollAndroidViewModel {
        val repositorySettings = RepositoryFactory().repositorySettings
        runBlocking(Dispatchers.Main) {
            repositorySettings.store(SettingsDataTestDouble())
        }

        val repositoryBag = RepositoryFactory().repositoryBag
        runBlocking(Dispatchers.Main) {
            bagDataTestDouble.d6.selected = true
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

    private fun deterministicTabRollAndroidViewModel(diceBag: BagDataTestDouble): TabRollAndroidViewModel {
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
