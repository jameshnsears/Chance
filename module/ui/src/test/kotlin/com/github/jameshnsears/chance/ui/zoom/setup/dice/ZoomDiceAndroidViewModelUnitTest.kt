package com.github.jameshnsears.chance.ui.zoom.setup.dice

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.jameshnsears.chance.common.utility.UtilityAndroidUnitTestHelper
import com.github.jameshnsears.chance.data.common.repo.RepositoryFactory
import com.github.jameshnsears.chance.data.domain.core.bag.testdouble.BagDataTestDouble
import com.github.jameshnsears.chance.data.domain.core.group.testdouble.GroupDataTestDouble
import com.github.jameshnsears.chance.data.domain.core.roll.testdouble.RollHistoryDataTestDouble
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test


class ZoomDiceAndroidViewModelUnitTest : UtilityAndroidUnitTestHelper() {
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

        zoomBagAndroidViewModel.setResizeView(1f)
        assertEquals(65.0.dp, zoomBagAndroidViewModel.stateFlowZoom.value.resizeViewDp)
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
    ): ZoomDiceAndroidViewModel {
        val repositoryBag = RepositoryFactory().repositoryBag
        repositoryBag.store(bagDataTestDouble.allDice)

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
