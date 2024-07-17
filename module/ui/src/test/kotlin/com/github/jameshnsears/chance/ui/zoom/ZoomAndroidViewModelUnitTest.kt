package com.github.jameshnsears.chance.ui.zoom

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.jameshnsears.chance.data.domain.core.Side
import com.github.jameshnsears.chance.data.domain.core.bag.testdouble.BagDataTestDouble
import com.github.jameshnsears.chance.data.domain.core.roll.testdouble.RollHistoryDataTestDouble
import com.github.jameshnsears.chance.data.domain.utility.epoch.UtilityEpochTimeGenerator
import com.github.jameshnsears.chance.data.utility.UtilityDataHelper
import com.github.jameshnsears.chance.utility.android.UtilityAndroidHelper
import io.mockk.spyk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
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
    fun removeRollSequenceWithDiceThatBeenDeleted() = runTest {
        val zoomAndroidViewModel = zoomAndroidViewModel()

        val diceBag = zoomAndroidViewModel.repositoryBag.fetch().first()
        assertEquals(8, diceBag.size)

        val rollHistory = zoomAndroidViewModel.repositoryRoll.fetch().first()
        assertEquals(2, rollHistory.size)

        diceBag.removeAt(1)     // d4
        zoomAndroidViewModel.repositoryBag.store(diceBag)

        zoomAndroidViewModel.removeRollSequenceWithDiceThatBeenDeleted()

        assertEquals(7, zoomAndroidViewModel.repositoryBag.fetch().first().size)

        assertEquals(0, zoomAndroidViewModel.repositoryRoll.fetch().first().size)
    }

    @Test
    fun removeRollSequenceWithDiceWhereNumberOfSidesChanged() = runTest {
        val zoomAndroidViewModel = zoomAndroidViewModel()

        val diceBag = zoomAndroidViewModel.repositoryBag.fetch().first()
        assertEquals(8, diceBag.size)

        val rollHistory = zoomAndroidViewModel.repositoryRoll.fetch().first()
        assertEquals(2, rollHistory.size)

        // change the number of sides of d4, from 4 to 2
        val newDiceEpoch = UtilityEpochTimeGenerator.now()
        diceBag[1].epoch = newDiceEpoch
        diceBag[1].sides = listOf(
            Side(number = 2),
            Side(number = 1)
        )
        zoomAndroidViewModel.repositoryBag.store(diceBag)

        assertEquals(8, zoomAndroidViewModel.repositoryBag.fetch().first().size)

        assertTrue(zoomAndroidViewModel.diceBagEpochs().contains(newDiceEpoch))

        assertFalse(zoomAndroidViewModel.diceRollEpochs().contains(newDiceEpoch))

        zoomAndroidViewModel.removeRollSequenceWithDiceWhereNumberOfSidesChanged()

        assertEquals(0, zoomAndroidViewModel.repositoryRoll.fetch().first().size)
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
        val repositoryBag = UtilityDataHelper().repositoryBag
        runBlocking(Dispatchers.Main) {
            // 8 dice = .allDice
            repositoryBag.store(bagDataTestDouble.allDice)
        }

        val repositoryRoll = UtilityDataHelper().repositoryRoll
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
                repositoryBag,
                repositoryRoll
            )
        )
    }
}
