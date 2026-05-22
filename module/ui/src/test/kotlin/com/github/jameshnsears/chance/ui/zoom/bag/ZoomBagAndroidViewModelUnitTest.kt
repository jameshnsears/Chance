package com.github.jameshnsears.chance.ui.zoom.bag

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.jameshnsears.chance.common.utility.UtilityAndroidUnitTestHelper
import com.github.jameshnsears.chance.data.common.repository.RepositoryFactory
import com.github.jameshnsears.chance.data.domain.core.bag.testdouble.BagDataTestDouble
import com.github.jameshnsears.chance.data.domain.core.roll.testdouble.RollHistoryDataTestDouble
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test


class ZoomBagAndroidViewModelUnitTest : UtilityAndroidUnitTestHelper() {
    @Before
    fun before() = runTest {
        RepositoryFactory().resetStorage()
    }

    @Test
    fun refreshAfterImport() = runTest {
        val zoomBagAndroidViewModel = zoomBagAndroidViewModel()
        zoomBagAndroidViewModel.refreshAfterImport()
    }

    @Test
    fun resizeView() = runTest {
        val zoomBagAndroidViewModel = zoomBagAndroidViewModel()

        zoomBagAndroidViewModel.setResizeView(1)
        assertEquals(70.0.dp, zoomBagAndroidViewModel.stateFlowZoom.value.resizeViewDp)

        zoomBagAndroidViewModel.setResizeView(2)
        assertEquals(87.5.dp, zoomBagAndroidViewModel.stateFlowZoom.value.resizeViewDp)
    }

    @Test
    fun sideNumberFontSizeSp() = runTest {
        assertEquals(
            17.sp,
            zoomBagAndroidViewModel().sideNumberFontSizeSp()
        )
    }

    private suspend fun zoomBagAndroidViewModel(
        bagDataTestDouble: BagDataTestDouble = BagDataTestDouble(),
    ): ZoomBagAndroidViewModel {
        val repositoryBag = RepositoryFactory().repositoryBag
        repositoryBag.store(bagDataTestDouble.allDice)

        val repositoryRoll = RepositoryFactory().repositoryRoll
        repositoryRoll.store(RollHistoryDataTestDouble(bagDataTestDouble).rollHistory)

        return ZoomBagAndroidViewModel(
            application(),
            RepositoryFactory().repositorySettings,
            repositoryBag,
            repositoryRoll
        )
    }
}
