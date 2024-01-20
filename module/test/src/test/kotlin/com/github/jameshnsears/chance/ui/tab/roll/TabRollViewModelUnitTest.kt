package com.github.jameshnsears.chance.ui.tab.roll

import com.github.jameshnsears.chance.MainDispatcherRule
import com.github.jameshnsears.chance.data.repository.bag.BagDemoSampleData
import com.github.jameshnsears.chance.data.repository.bag.BagRepositoryTestDouble
import com.github.jameshnsears.chance.data.repository.bag.BagSampleData
import com.github.jameshnsears.chance.data.repository.roll.RollRepositoryTestDouble
import com.github.jameshnsears.chance.data.repository.roll.RollSampleData
import com.github.jameshnsears.chance.data.repository.settings.SettingsRepositoryTestDouble
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertSame
import org.junit.Rule
import org.junit.Test

class TabRollViewModelUnitTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun roll() = runTest {
        val settingsRepository = SettingsRepositoryTestDouble.getInstance()

        val bagRepository = BagRepositoryTestDouble.getInstance()
        bagRepository.store(BagSampleData.allDice)

        val rollRepository = RollRepositoryTestDouble.getInstance()
        rollRepository.store(RollSampleData.rollHistory)

        assertEquals(1, rollRepository.fetch().size)
        val firstRoll = rollRepository.fetch()[0]

        val tabRollViewModel = TabRollViewModel(
            settingsRepository,
            bagRepository,
            rollRepository
        )

        tabRollViewModel.roll(BagDemoSampleData.allDice)
        advanceUntilIdle()

        assertEquals(2, rollRepository.fetch().size)

        assertSame(firstRoll, rollRepository.fetch()[0])
    }
}
