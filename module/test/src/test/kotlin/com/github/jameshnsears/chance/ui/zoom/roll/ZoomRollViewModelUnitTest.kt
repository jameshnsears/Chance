package com.github.jameshnsears.chance.ui.zoom.roll

import com.github.jameshnsears.chance.MainDispatcherRule
import com.github.jameshnsears.chance.data.repository.roll.RollRepositoryTestDouble
import com.github.jameshnsears.chance.data.repository.roll.RollSampleData
import com.github.jameshnsears.chance.data.repository.settings.SettingsRepositoryTestDouble
import kotlinx.coroutines.test.runTest
import org.junit.Assert.fail
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test

class ZoomRollViewModelUnitTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Ignore
    @Test
    fun todo() = runTest {
        val settingsRepository = SettingsRepositoryTestDouble.getInstance()

        val rollRepositoryTestDouble = RollRepositoryTestDouble.getInstance()
        rollRepositoryTestDouble.store(
            RollSampleData.rollHistory
        )

        val zoomRollModel = ZoomRollViewModel(settingsRepository, rollRepositoryTestDouble)

        zoomRollModel.zoom()

        fail("todo")
    }
}
