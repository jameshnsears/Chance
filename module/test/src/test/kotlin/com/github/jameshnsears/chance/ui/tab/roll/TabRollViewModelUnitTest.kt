package com.github.jameshnsears.chance.ui.tab.roll

import com.github.jameshnsears.chance.data.bag.sample.BagSampleData
import com.github.jameshnsears.chance.data.roll.repository.RollRepositoryMock
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
    fun exerciseViewModel() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        try {
            val rollRepository = RollRepositoryMock

            assertEquals(1, rollRepository.rollHistory.size)
            val firstRoll = rollRepository.rollHistory[0]

            val viewModel = TabRollViewModel(rollRepository)

            viewModel.rollDice(BagSampleData.oneDice)
            advanceUntilIdle()

            assertEquals(2, rollRepository.rollHistory.size)

            assertSame(firstRoll, rollRepository.rollHistory[0])
        } finally {
            Dispatchers.resetMain()
        }
    }
}
