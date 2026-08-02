package com.github.jameshnsears.chance.ui.zoom.setup.dice

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.jameshnsears.chance.common.utility.UtilityAndroidUnitTestHelper
import com.github.jameshnsears.chance.data.common.repo.RepositoryFactory
import com.github.jameshnsears.chance.data.domain.core.bag.testdouble.BagDataTestDouble
import com.github.jameshnsears.chance.data.domain.core.group.testdouble.GroupDataTestDouble
import com.github.jameshnsears.chance.data.domain.core.roll.testdouble.RollHistoryDataTestDouble
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test


class ZoomDiceAndroidViewModelUnitTest : UtilityAndroidUnitTestHelper() {
    @Before
    fun before() = runTest {
        RepositoryFactory().resetStorage()
    }

    @Test
    fun resizeView() = runTest {
        val zoomBagAndroidViewModel = zoomBagAndroidViewModel()
        val repositorySettings = RepositoryFactory().repositorySettings
        val settings = repositorySettings.fetch().first()

        settings.resizeZoom = 1f
        repositorySettings.store(settings)

        assertEquals(65.0.dp, zoomBagAndroidViewModel.stateFlowZoom.value.resizeViewDp)
    }

    @Test
    fun sideNumberFontSizeSp() = runTest {
        assertEquals(
            17.sp,
            zoomBagAndroidViewModel().sideNumberFontSizeSp()
        )
    }

    @Test
    fun sideSvgImageRequestAsync() = runTest {
        val zoomBagAndroidViewModel = zoomBagAndroidViewModel()
        val side = BagDataTestDouble().allDice()[0].sides[0]
        side.imageBase64 = "PHN2ZyB2aWV3Qm94PSIwIDAgMTAgMTAiPjxjaXJjbGUgY3g9IjUiIGN5PSI1IiByPSI0Ii8+PC9zdmc+"

        val imageRequest = zoomBagAndroidViewModel.sideSvgImageRequestAsync(side)
        assertNotNull(imageRequest)
        assertEquals(imageRequest, zoomBagAndroidViewModel.sideSvgImageRequestAsync(side))
    }

    private suspend fun zoomBagAndroidViewModel(
        bagDataTestDouble: BagDataTestDouble = BagDataTestDouble(),
    ): ZoomDiceAndroidViewModel {
        val repositoryBag = RepositoryFactory().repositoryBag
        repositoryBag.store(bagDataTestDouble.allDice())

        val repositoryRoll = RepositoryFactory().repositoryRoll
        repositoryRoll.store(
            RollHistoryDataTestDouble(
                bagDataTestDouble,
                GroupDataTestDouble(bagDataTestDouble)
            ).rollHistory
        )

        return ZoomDiceAndroidViewModel(
            application(),
            RepositoryFactory().repositorySettings,
            repositoryBag,
            repositoryRoll
        )
    }
}
