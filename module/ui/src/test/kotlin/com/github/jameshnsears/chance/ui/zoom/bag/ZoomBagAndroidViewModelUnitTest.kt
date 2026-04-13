package com.github.jameshnsears.chance.ui.zoom.bag

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.jameshnsears.chance.common.utility.UtilityAndroidUnitTestHelper
import com.github.jameshnsears.chance.data.common.repository.RepositoryFactory
import com.github.jameshnsears.chance.data.domain.R
import com.github.jameshnsears.chance.data.domain.core.bag.testdouble.BagDataTestDouble
import com.github.jameshnsears.chance.data.domain.core.roll.testdouble.RollHistoryDataTestDouble
import io.mockk.spyk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test


class ZoomBagAndroidViewModelUnitTest : UtilityAndroidUnitTestHelper() {
    @Test
    fun refreshAfterImport() = runTest {
        val zoomBagAndroidViewModel = this@ZoomBagAndroidViewModelUnitTest.zoomBagAndroidViewModel()
        zoomBagAndroidViewModel.refreshAfterImport()
    }

    @Test
    fun resizeView() = runTest {
        val zoomBagAndroidViewModel = this@ZoomBagAndroidViewModelUnitTest.zoomBagAndroidViewModel()

        zoomBagAndroidViewModel.setResizeView(1)
        assertEquals(80.0.dp, zoomBagAndroidViewModel.stateFlowZoom.value.resizeViewDp)

        zoomBagAndroidViewModel.setResizeView(2)
        assertEquals(100.0.dp, zoomBagAndroidViewModel.stateFlowZoom.value.resizeViewDp)

        zoomBagAndroidViewModel.setResizeView(3)
        assertEquals(120.0.dp, zoomBagAndroidViewModel.stateFlowZoom.value.resizeViewDp)

        zoomBagAndroidViewModel.setResizeView(4)
        assertEquals(140.0.dp, zoomBagAndroidViewModel.stateFlowZoom.value.resizeViewDp)

        zoomBagAndroidViewModel.setResizeView(5)
        assertEquals(160.0.dp, zoomBagAndroidViewModel.stateFlowZoom.value.resizeViewDp)
    }

    @Test
    fun sideNumberFontSizeSp() = runTest {
        assertEquals(
            17.0.sp,
            zoomBagAndroidViewModel().sideNumberFontSizeSp()
        )
    }

    @Test
    fun hasSideWithDescription() = runTest {
        assertFalse(
            zoomBagAndroidViewModel()
                .hasSideWithDescription(BagDataTestDouble().d2)
        )

        assertTrue(
            zoomBagAndroidViewModel()
                .hasSideWithDescription(BagDataTestDouble().diceStory)
        )
    }

    @Test
    fun drawableForDiceSides() = runTest {
        assertEquals(
            R.drawable.d2,
            zoomBagAndroidViewModel().drawableForDiceSides(BagDataTestDouble().d2)
        )

        assertEquals(
            R.drawable.d6,
            zoomBagAndroidViewModel().drawableForDiceSides(BagDataTestDouble().d6)
        )

        assertEquals(
            R.drawable.d10,
            zoomBagAndroidViewModel().drawableForDiceSides(BagDataTestDouble().d10)
        )

        assertEquals(
            R.drawable.d12,
            zoomBagAndroidViewModel().drawableForDiceSides(BagDataTestDouble().d12)
        )

        assertEquals(
            R.drawable.d4_d8_d20,
            zoomBagAndroidViewModel().drawableForDiceSides(BagDataTestDouble().d4)
        )
    }

    private fun zoomBagAndroidViewModel(
        bagDataTestDouble: BagDataTestDouble = BagDataTestDouble(),
    ): ZoomBagAndroidViewModel {
        val repositoryBag = RepositoryFactory().repositoryBag
        runTest {
            // 8 dice = .allDice
            repositoryBag.store(bagDataTestDouble.allDice)
        }

        val repositoryRoll = RepositoryFactory().repositoryRoll
        runTest {
            // 2 rolls:
            //      1st roll:
            //              d2
            //              d4
            //              d8
            //              d10
            //              d12
            //              d20
            //      2nd roll:
            //              d4
            //              d4
            //              d4
            repositoryRoll.store(RollHistoryDataTestDouble(bagDataTestDouble).rollHistory)
        }

        return spyk<ZoomBagAndroidViewModel>(
            ZoomBagAndroidViewModel(
                application(),
                RepositoryFactory().repositorySettings,
                repositoryBag,
                repositoryRoll
            )
        )
    }
}
