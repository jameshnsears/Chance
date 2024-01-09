package com.github.jameshnsears.chance.ui.zoom.bag

import com.github.jameshnsears.chance.data.bag.demo.BagDemoData
import com.github.jameshnsears.chance.data.bag.repository.BagRepositoryInterface
import com.github.jameshnsears.chance.data.bag.repository.BagRepositoryTestDouble
import com.github.jameshnsears.chance.data.settings.repository.SettingsRepositoryInterface
import com.github.jameshnsears.chance.data.settings.repository.SettingsRepositoryTestDouble
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.fail
import org.junit.Test

class ZoomBagViewModelUnitTest {
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun todo() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        try {
            val settingsRepository = SettingsRepositoryTestDouble

            val bagRepository = BagRepositoryTestDouble
            bagRepository.store(
                listOf(
                    BagDemoData.diceHeadsTails
                )
            )

            val zoomBagRollModel = ZoomBagViewModel(settingsRepository, bagRepository)

            zoomBagRollModel.zoom()

            fail("todo")
        } finally {
            Dispatchers.resetMain()
        }
    }
}
