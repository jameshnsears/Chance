package com.github.jameshnsears.chance.ui.tab.bag

import com.github.jameshnsears.chance.MainDispatcherRule
import com.github.jameshnsears.chance.data.repository.bag.BagDemoSampleData
import com.github.jameshnsears.chance.data.repository.bag.BagRepositoryTestDouble
import com.github.jameshnsears.chance.data.repository.roll.RollRepositoryTestDouble
import com.github.jameshnsears.chance.data.repository.roll.RollSampleData
import com.github.jameshnsears.chance.data.repository.settings.SettingsRepositoryTestDouble
import com.github.jameshnsears.chance.data.repository.settings.SettingsSampleData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Rule
import org.junit.Test

class TabBagViewModelUnitTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `import and export`() = runTest {
        val settingsRepository = SettingsRepositoryTestDouble.getInstance()
        settingsRepository.store(
            SettingsSampleData.settings
        )

        val bagRepository = BagRepositoryTestDouble.getInstance()
        bagRepository.store(
            listOf(
                BagDemoSampleData.diceHeadsTails
            )
        )

        val rollRepository = RollRepositoryTestDouble.getInstance()
        rollRepository.store(RollSampleData.rollHistory)

        val tabBagViewModel = TabBagViewModel(
            settingsRepository,
            bagRepository
        )

        val exportedJson = tabBagViewModel.export()

        tabBagViewModel.import(exportedJson)
        advanceUntilIdle()  // new

        assertEquals(exportedJson, tabBagViewModel.export())
    }

    @Test
    fun `check colour import values good`() = runTest {
        // json color values must be like FFFFFFFF
        fail("todo")
    }
}
