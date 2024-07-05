package com.github.jameshnsears.chance.ui.zoom

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.jameshnsears.chance.data.domain.state.Settings
import com.github.jameshnsears.chance.data.repository.bag.testdouble.RepositoryBagTestDouble
import com.github.jameshnsears.chance.data.repository.roll.testdouble.RepositoryRollTestDouble
import com.github.jameshnsears.chance.data.repository.settings.testdouble.RepositorySettingsTestDouble
import com.github.jameshnsears.chance.data.sample.bag.SampleBagTestData
import com.github.jameshnsears.chance.data.sample.roll.SampleRollTestData
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
    fun sideImageNumberFontSize() = runTest {
        assertEquals(
            17.0.sp,
            zoomAndroidViewModel().sideImageNumberFontSize()
        )
    }

    @Test
    fun removeRollSequenceWithDiceThatBeenDeleted() = runTest {
        val zoomAndroidViewModel = zoomAndroidViewModel()

        assertEquals(2, zoomAndroidViewModel.repositoryRoll.fetch().first().size)

        val diceBag = zoomAndroidViewModel.repositoryBag.fetch().first()
        assertEquals(8, diceBag.size)

        diceBag.removeAt(0)
        zoomAndroidViewModel.repositoryBag.store(diceBag)

        zoomAndroidViewModel.removeRollSequenceWithDiceThatBeenDeleted()

        assertEquals(7, zoomAndroidViewModel.repositoryBag.fetch().first().size)

        assertEquals(1, zoomAndroidViewModel.repositoryRoll.fetch().first().size)
    }

    @Test
    fun diceContainsAtLeastOneSideWithDescription() = runTest {
        assertFalse(
            zoomAndroidViewModel()
                .diceContainsAtLeastOneSideWithDescription(SampleBagTestData().d2)
        )

        assertTrue(
            zoomAndroidViewModel()
                .diceContainsAtLeastOneSideWithDescription(SampleBagTestData().diceStory)
        )
    }

    @Test
    fun sideImageNumberShape() = runTest {
        assertEquals(
            com.github.jameshnsears.chance.data.R.drawable.d2,
            zoomAndroidViewModel().sideImageNumberShape(SampleBagTestData().d2)
        )

        assertEquals(
            com.github.jameshnsears.chance.data.R.drawable.d6,
            zoomAndroidViewModel().sideImageNumberShape(SampleBagTestData().d6)
        )

        assertEquals(
            com.github.jameshnsears.chance.data.R.drawable.d10,
            zoomAndroidViewModel().sideImageNumberShape(SampleBagTestData().d10)
        )

        assertEquals(
            com.github.jameshnsears.chance.data.R.drawable.d12,
            zoomAndroidViewModel().sideImageNumberShape(SampleBagTestData().d12)
        )

        assertEquals(
            com.github.jameshnsears.chance.data.R.drawable.d4_d8_d20,
            zoomAndroidViewModel().sideImageNumberShape(SampleBagTestData().d4)
        )
    }

    private fun zoomAndroidViewModel(
        sampleBagTestData: SampleBagTestData = SampleBagTestData(),
    ): ZoomAndroidViewModel {

        val repositorySettings = RepositorySettingsTestDouble.getInstance()
        runBlocking(Dispatchers.Main) {
            repositorySettings.store(Settings())
        }

        val repositoryBag = RepositoryBagTestDouble.getInstance()
        runBlocking(Dispatchers.Main) {
            repositoryBag.store(sampleBagTestData.allDice)
        }

        val sampleRollTestData = SampleRollTestData(sampleBagTestData)

        val repositoryRoll = RepositoryRollTestDouble.getInstance()
        runBlocking(Dispatchers.Main) {
            repositoryRoll.store(sampleRollTestData.rollHistory)
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
