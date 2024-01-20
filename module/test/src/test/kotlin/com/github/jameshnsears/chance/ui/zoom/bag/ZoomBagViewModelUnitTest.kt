package com.github.jameshnsears.chance.ui.zoom.bag

import com.github.jameshnsears.chance.MainDispatcherRule
import com.github.jameshnsears.chance.data.repository.bag.BagDemoSampleData
import com.github.jameshnsears.chance.data.repository.bag.BagRepositoryTestDouble
import com.github.jameshnsears.chance.data.repository.settings.SettingsRepositoryTestDouble
import kotlinx.coroutines.test.runTest
import org.junit.Assert.fail
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test

class ZoomBagViewModelUnitTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Ignore
    @Test
    fun todo() = runTest {

        val settingsRepository = SettingsRepositoryTestDouble.getInstance()

        val bagRepository = BagRepositoryTestDouble.getInstance()
        bagRepository.store(
            listOf(
                BagDemoSampleData.diceHeadsTails
            )
        )

        val zoomBagRollModel = ZoomBagViewModel(settingsRepository, bagRepository)

        zoomBagRollModel.zoom()

        fail("todo")
    }
}
