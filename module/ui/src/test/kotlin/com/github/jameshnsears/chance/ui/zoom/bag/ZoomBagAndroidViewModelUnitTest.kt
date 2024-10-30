package com.github.jameshnsears.chance.ui.zoom.bag

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.jameshnsears.chance.data.R
import com.github.jameshnsears.chance.data.domain.core.bag.testdouble.BagDataTestDouble
import com.github.jameshnsears.chance.data.domain.core.roll.testdouble.RollHistoryDataTestDouble
import com.github.jameshnsears.chance.data.repository.RepositoryFactory
import com.github.jameshnsears.chance.utility.android.UtilityAndroidHelper
import io.mockk.spyk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test


class ZoomBagAndroidViewModelUnitTest : UtilityAndroidHelper() {
    @Test
    fun refreshAfterImport() = runTest {
        val zoomBagAndroidViewModel = this@ZoomBagAndroidViewModelUnitTest.zoomBagAndroidViewModel()
        zoomBagAndroidViewModel.refreshAfterImport()
    }

    @Test
    fun resizeView() = runTest {
        val zoomBagAndroidViewModel = this@ZoomBagAndroidViewModelUnitTest.zoomBagAndroidViewModel()

        zoomBagAndroidViewModel.resizeView(1)
        assertEquals(60.0.dp, zoomBagAndroidViewModel.stateFlowZoom.value.resizeView)

        zoomBagAndroidViewModel.resizeView(2)
        assertEquals(80.0.dp, zoomBagAndroidViewModel.stateFlowZoom.value.resizeView)

        zoomBagAndroidViewModel.resizeView(3)
        assertEquals(100.0.dp, zoomBagAndroidViewModel.stateFlowZoom.value.resizeView)

        zoomBagAndroidViewModel.resizeView(4)
        assertEquals(120.0.dp, zoomBagAndroidViewModel.stateFlowZoom.value.resizeView)

        zoomBagAndroidViewModel.resizeView(5)
        assertEquals(140.0.dp, zoomBagAndroidViewModel.stateFlowZoom.value.resizeView)
    }

    @Test
    fun sideImageShapeNumberFontSize() = runTest {
        assertEquals(
            17.0.sp,
            zoomBagAndroidViewModel().sideImageShapeNumberFontSize()
        )
    }

    @Test
    fun diceContainsAtLeastOneSideWithDescription() = runTest {
        assertFalse(
            zoomBagAndroidViewModel()
                .diceContainsAtLeastOneSideWithDescription(BagDataTestDouble().d2)
        )

        assertTrue(
            zoomBagAndroidViewModel()
                .diceContainsAtLeastOneSideWithDescription(BagDataTestDouble().diceStory)
        )
    }

    @Test
    fun sideImageShapeNumberShape() = runTest {
        assertEquals(
            R.drawable.d2,
            zoomBagAndroidViewModel().sideImageShapeNumberShape(BagDataTestDouble().d2)
        )

        assertEquals(
            R.drawable.d6,
            zoomBagAndroidViewModel().sideImageShapeNumberShape(BagDataTestDouble().d6)
        )

        assertEquals(
            R.drawable.d10,
            zoomBagAndroidViewModel().sideImageShapeNumberShape(BagDataTestDouble().d10)
        )

        assertEquals(
            R.drawable.d12,
            zoomBagAndroidViewModel().sideImageShapeNumberShape(BagDataTestDouble().d12)
        )

        assertEquals(
            R.drawable.d4_d8_d20,
            zoomBagAndroidViewModel().sideImageShapeNumberShape(BagDataTestDouble().d4)
        )
    }

    private fun zoomBagAndroidViewModel(
        bagDataTestDouble: BagDataTestDouble = BagDataTestDouble(),
    ): ZoomBagAndroidViewModel {
        val repositoryBag = RepositoryFactory().repositoryBag
        runBlocking(Dispatchers.Main) {
            // 8 dice = .allDice
            repositoryBag.store(bagDataTestDouble.allDice)
        }

        val repositoryRoll = RepositoryFactory().repositoryRoll
        runBlocking(Dispatchers.Main) {
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
                getApplication(),
                RepositoryFactory().repositorySettings,
                repositoryBag,
                repositoryRoll
            )
        )
    }
}
