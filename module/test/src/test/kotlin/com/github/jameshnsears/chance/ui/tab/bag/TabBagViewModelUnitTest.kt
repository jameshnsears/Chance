package com.github.jameshnsears.chance.ui.tab.bag

import com.github.jameshnsears.chance.data.repository.bag.BagDemoData
import com.github.jameshnsears.chance.data.repository.bag.BagRepositoryTestDouble
import com.github.jameshnsears.chance.data.repository.roll.RollRepositoryTestDouble
import com.github.jameshnsears.chance.data.repository.roll.RollSampleData
import com.github.jameshnsears.chance.data.repository.settings.SettingsRepositoryTestDouble
import com.github.jameshnsears.chance.data.repository.settings.SettingsSampleData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Test

class TabBagViewModelUnitTest {
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `import and export`() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        try {
            val settingsRepository = SettingsRepositoryTestDouble.getInstance()
            settingsRepository.store(
                SettingsSampleData.headsTails
            )

            val bagRepository = BagRepositoryTestDouble.getInstance()
            bagRepository.store(
                listOf(
                    BagDemoData.diceHeadsTails
                )
            )

            val rollRepository = RollRepositoryTestDouble.getInstance()
            rollRepository.store(RollSampleData.rollHistory_roll1Sequence1)

            val tabBagViewModel = TabBagViewModel(
                settingsRepository,
                bagRepository
            )

            runBlocking {
                val exportedJson = tabBagViewModel.export()

                tabBagViewModel.import(exportedJson)

                assertEquals(exportedJson, tabBagViewModel.export())
            }
        } finally {
            Dispatchers.resetMain()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun todo() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        try {
            fail("todo")
        } finally {
            Dispatchers.resetMain()
        }
    }
}
