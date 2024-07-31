package com.github.jameshnsears.chance.ui.zoom

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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


class ZoomAndroidViewModelUnitTest : UtilityAndroidHelper() {
    @Test
    fun refreshAfterImport() = runTest {
        val zoomAndroidViewModel = zoomAndroidViewModel()
        zoomAndroidViewModel.refreshAfterImport()
    }

    @Test
    fun resizeView() = runTest {
        val zoomAndroidViewModel = zoomAndroidViewModel()

        zoomAndroidViewModel.resizeView(1)
        assertEquals(60.0.dp, zoomAndroidViewModel.stateFlowZoom.value.resizeView)

        zoomAndroidViewModel.resizeView(2)
        assertEquals(64.0.dp, zoomAndroidViewModel.stateFlowZoom.value.resizeView)

        zoomAndroidViewModel.resizeView(3)
        assertEquals(72.0.dp, zoomAndroidViewModel.stateFlowZoom.value.resizeView)

        zoomAndroidViewModel.resizeView(4)
        assertEquals(80.0.dp, zoomAndroidViewModel.stateFlowZoom.value.resizeView)

        zoomAndroidViewModel.resizeView(5)
        assertEquals(88.0.dp, zoomAndroidViewModel.stateFlowZoom.value.resizeView)

        zoomAndroidViewModel.resizeView(6)
        assertEquals(96.0.dp, zoomAndroidViewModel.stateFlowZoom.value.resizeView)

        zoomAndroidViewModel.resizeView(7)
        assertEquals(100.0.dp, zoomAndroidViewModel.stateFlowZoom.value.resizeView)
    }

    @Test
    fun sideImageShapeNumberFontSize() = runTest {
        assertEquals(
            17.0.sp,
            zoomAndroidViewModel().sideImageShapeNumberFontSize()
        )
    }

    @Test
    fun diceContainsAtLeastOneSideWithDescription() = runTest {
        assertFalse(
            zoomAndroidViewModel()
                .diceContainsAtLeastOneSideWithDescription(BagDataTestDouble().d2)
        )

        assertTrue(
            zoomAndroidViewModel()
                .diceContainsAtLeastOneSideWithDescription(BagDataTestDouble().diceStory)
        )
    }

    @Test
    fun sideImageShapeNumberShape() = runTest {
        assertEquals(
            com.github.jameshnsears.chance.data.R.drawable.d2,
            zoomAndroidViewModel().sideImageShapeNumberShape(BagDataTestDouble().d2)
        )

        assertEquals(
            com.github.jameshnsears.chance.data.R.drawable.d6,
            zoomAndroidViewModel().sideImageShapeNumberShape(BagDataTestDouble().d6)
        )

        assertEquals(
            com.github.jameshnsears.chance.data.R.drawable.d10,
            zoomAndroidViewModel().sideImageShapeNumberShape(BagDataTestDouble().d10)
        )

        assertEquals(
            com.github.jameshnsears.chance.data.R.drawable.d12,
            zoomAndroidViewModel().sideImageShapeNumberShape(BagDataTestDouble().d12)
        )

        assertEquals(
            com.github.jameshnsears.chance.data.R.drawable.d4_d8_d20,
            zoomAndroidViewModel().sideImageShapeNumberShape(BagDataTestDouble().d4)
        )
    }

    private fun zoomAndroidViewModel(
        bagDataTestDouble: BagDataTestDouble = BagDataTestDouble(),
    ): ZoomAndroidViewModel {
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

        return spyk<ZoomAndroidViewModel>(
            ZoomAndroidViewModel(
                getApplication(),
                RepositoryFactory().repositorySettings,
                repositoryBag,
                repositoryRoll
            )
        )
    }
}
