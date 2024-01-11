package com.github.jameshnsears.chance.ui.zoom.roll

import com.github.jameshnsears.chance.data.repository.roll.RollRepositoryTestDouble
import com.github.jameshnsears.chance.data.repository.roll.RollSampleData
import com.github.jameshnsears.chance.data.repository.settings.SettingsRepositoryTestDouble
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.fail
import org.junit.Test

class ZoomRollViewModelUnitTest {
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun todo() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        try {
            val settingsRepository = SettingsRepositoryTestDouble.getInstance()

            val rollRepositoryTestDouble = RollRepositoryTestDouble.getInstance()
            rollRepositoryTestDouble.store(
                RollSampleData.rollHistory_roll1Sequence1
            )

            val zoomRollModel = ZoomRollViewModel(settingsRepository, rollRepositoryTestDouble)

            zoomRollModel.zoom()

            fail("todo")
        } finally {
            Dispatchers.resetMain()
        }
    }
}
