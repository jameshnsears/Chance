package com.github.jameshnsears.chance.ui.tab.roll

import com.github.jameshnsears.chance.data.bag.demo.BagDemoData
import com.github.jameshnsears.chance.data.roll.repository.RollRepositoryMock
import com.github.jameshnsears.chance.data.roll.sample.RollSampleData
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertSame
import org.junit.Test

class TabRollViewModelUnitTest {
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun roll() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        try {
            val rollRepository = RollRepositoryMock
            rollRepository.store(RollSampleData.rollHistory_roll1Sequence1)

            assertEquals(1, rollRepository.fetch().size)
            val firstRoll = rollRepository.fetch()[0]

            val tabRollViewModel = TabRollViewModel(rollRepository)

            tabRollViewModel.roll(BagDemoData.dice)
            advanceUntilIdle()

            assertEquals(2, rollRepository.fetch().size)

            assertSame(firstRoll, rollRepository.fetch()[0])
        } finally {
            Dispatchers.resetMain()
        }
    }
}
