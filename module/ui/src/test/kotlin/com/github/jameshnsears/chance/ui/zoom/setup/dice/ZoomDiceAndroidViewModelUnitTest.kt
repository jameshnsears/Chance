package com.github.jameshnsears.chance.ui.zoom.setup.dice

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.jameshnsears.chance.common.utility.UtilityAndroidUnitTestHelper
import com.github.jameshnsears.chance.data.common.repo.RepositoryFactory
import com.github.jameshnsears.chance.data.domain.core.bag.testdouble.BagDataTestDouble
import com.github.jameshnsears.chance.data.domain.core.group.testdouble.GroupDataTestDouble
import com.github.jameshnsears.chance.data.domain.core.roll.testdouble.RollHistoryDataTestDouble
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test


class ZoomDiceAndroidViewModelUnitTest : UtilityAndroidUnitTestHelper() {
    private val createdViewModels = mutableListOf<ViewModel>()

    @Before
    fun before() = runTest {
        RepositoryFactory().resetStorage()
    }

    @After
    fun tearDown() {
        // ViewModels created in a unit test never receive onCleared(), so their
        // viewModelScope coroutines keep running and can pollute subsequent tests.
        createdViewModels.forEach { it.viewModelScope.cancel() }
        createdViewModels.clear()
    }

    private fun track(viewModel: ZoomDiceAndroidViewModel): ZoomDiceAndroidViewModel {
        createdViewModels.add(viewModel)
        return viewModel
    }

    @Test
    fun resizeView() = runTest {
        val zoomBagAndroidViewModel = zoomBagAndroidViewModel()
        val repositorySettings = RepositoryFactory().repositorySettings
        val settings = repositorySettings.fetch().first()

        settings.resizeZoom = 1f
        repositorySettings.store(settings)

        val state = zoomBagAndroidViewModel.stateFlowZoom.first { it.resizeViewDp == 65.0.dp }
        assertEquals(65.0.dp, state.resizeViewDp)
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

    @Test
    fun sideSvgImageRequestAsyncServesFromSideCache() = runTest {
        val repositoryFactory = RepositoryFactory()
        val bagDataTestDouble = BagDataTestDouble()
        val side = bagDataTestDouble.allDice()[0].sides[0]
        side.imageBase64 = "PHN2ZyB2aWV3Qm94PSIwIDAgMTAgMTAiPjxjaXJjbGUgY3g9IjUiIGN5PSI1IiByPSI0Ii8+PC9zdmc+"
        repositoryFactory.repositoryBag.store(bagDataTestDouble.allDice())

        val viewModel = track(
            ZoomDiceAndroidViewModel(
                application(),
                repositoryFactory.repositorySettings,
                repositoryFactory.repositoryBag,
                repositoryFactory.repositoryRoll,
                repositoryFactory.repositoryLocked,
                repositoryFactory.repositorySideCache,
            )
        )

        val imageRequest = viewModel.sideSvgImageRequestAsync(side)
        assertNotNull("a request should be built from the side cache SVG", imageRequest)
        val data = imageRequest!!.data
        assertTrue(
            "the request data should be the decoded SVG bytes",
            data is ByteArray && data.isNotEmpty()
        )
    }

    @Test
    fun sideSvgImageRequestAsyncReturnsNullWhenNoImage() = runTest {
        val repositoryFactory = RepositoryFactory()
        val viewModel = track(
            ZoomDiceAndroidViewModel(
                application(),
                repositoryFactory.repositorySettings,
                repositoryFactory.repositoryBag,
                repositoryFactory.repositoryRoll,
                repositoryFactory.repositoryLocked,
                repositoryFactory.repositorySideCache,
            )
        )

        val side = BagDataTestDouble().allDice()[0].sides[0]
        side.imageBase64 = ""
        side.imageDrawableId = 0

        assertNull(viewModel.sideSvgImageRequestAsync(side))
    }

    @Test
    fun stateFlowZoomKeepsImageBase64WhenSvgIsCached() = runTest {
        val repositoryFactory = RepositoryFactory()
        val bagDataTestDouble = BagDataTestDouble()
        val side = bagDataTestDouble.allDice()[0].sides[0]
        side.imageBase64 = "PHN2ZyB2aWV3Qm94PSIwIDAgMTAgMTAiPjxjaXJjbGUgY3g9IjUiIGN5PSI1IiByPSI0Ii8+PC9zdmc+"
        repositoryFactory.repositoryBag.store(bagDataTestDouble.allDice())

        val viewModel = track(
            ZoomDiceAndroidViewModel(
                application(),
                repositoryFactory.repositorySettings,
                repositoryFactory.repositoryBag,
                repositoryFactory.repositoryRoll,
                repositoryFactory.repositoryLocked,
                repositoryFactory.repositorySideCache,
            )
        )

        val state = viewModel.stateFlowZoom.first { it.diceBag.isNotEmpty() }
        assertEquals(
            "imageBase64 must survive into the zoom state when the SVG is in the side cache",
            side.imageBase64,
            state.diceBag[0].sides[0].imageBase64
        )
    }

    private suspend fun zoomBagAndroidViewModel(
        bagDataTestDouble: BagDataTestDouble = BagDataTestDouble(),
    ): ZoomDiceAndroidViewModel {
        val repositoryFactory = RepositoryFactory()
        val repositoryBag = repositoryFactory.repositoryBag
        repositoryBag.store(bagDataTestDouble.allDice())

        val repositoryRoll = repositoryFactory.repositoryRoll
        repositoryRoll.store(
            RollHistoryDataTestDouble(
                bagDataTestDouble,
                GroupDataTestDouble(bagDataTestDouble)
            ).rollHistory
        )

        return track(
            ZoomDiceAndroidViewModel(
                application(),
                repositoryFactory.repositorySettings,
                repositoryBag,
                repositoryRoll,
                repositoryFactory.repositoryLocked
            )
        )
    }
}
