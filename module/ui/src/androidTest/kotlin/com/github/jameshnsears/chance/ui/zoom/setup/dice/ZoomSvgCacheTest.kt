package com.github.jameshnsears.chance.ui.zoom.setup.dice

import android.graphics.Bitmap
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.core.app.ApplicationProvider
import com.github.jameshnsears.chance.common.ui.AndroidTestHelper
import com.github.jameshnsears.chance.common.utility.feature.UtilityFeature
import com.github.jameshnsears.chance.data.common.repo.RepositoryFactory
import com.github.jameshnsears.chance.data.domain.core.Dice
import com.github.jameshnsears.chance.data.domain.core.Side
import com.github.jameshnsears.chance.data.domain.core.bag.testdouble.BagDataTestDouble
import com.github.jameshnsears.chance.ui.navigation.ChanceNavKey
import com.github.jameshnsears.chance.ui.navigation.LocalNavigator
import com.github.jameshnsears.chance.ui.navigation.Navigator
import com.github.jameshnsears.chance.ui.navigation.rememberNavigationState
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ZoomSvgCacheTest : AndroidTestHelper() {
    private val svgBase64 = "PHN2ZyB2aWV3Qm94PSIwIDAgMTAgMTAiPjxjaXJjbGUgY3g9IjUiIGN5PSI1IiByPSI0Ii8+PC9zdmc+"

    @After
    fun tearDown() {
        UtilityFeature.enabled = setOf(
            UtilityFeature.Flag.REPO_PROTOCOL_BUFFER_TEST_DOUBLE,
        )
    }

    @Test
    fun svgFaceDisplaysFromSideCacheProdImpl() = runTest {
        UtilityFeature.enabled = setOf(
            UtilityFeature.Flag.REPO_PROTOCOL_BUFFER_PROD,
        )

        val repositoryFactory = RepositoryFactory(ApplicationProvider.getApplicationContext())
        repositoryFactory.resetStorage()

        val dice = Dice(
            sides = mutableListOf(Side(number = 6, imageBase64 = svgBase64)),
            title = "d6"
        )
        repositoryFactory.repositoryBag.store(mutableListOf(dice))

        val viewModel = ZoomDiceAndroidViewModel(
            ApplicationProvider.getApplicationContext(),
            repositoryFactory.repositorySettings,
            repositoryFactory.repositoryBag,
            repositoryFactory.repositoryRoll,
            repositoryFactory.repositoryLocked,
            repositoryFactory.repositorySideCache,
        )

        composeRule.setContent {
            val navigationState = rememberNavigationState(
                startRoute = ChanceNavKey.SetupDice,
                topLevelRoutes = setOf(ChanceNavKey.SetupDice)
            )
            val navigator = remember(navigationState) { Navigator(navigationState) }

            CompositionLocalProvider(LocalNavigator provides navigator) {
                ZoomBag(viewModel)
            }
        }

        // the SVG was moved into the side cache on store, so the face must come from the cache
        val svgTag = "${ZoomDiceTestTag.ZOOM_SIDE_IMAGE_SVG}-d6-6"
        waitForTag(svgTag)
        composeRule.onNodeWithTag(svgTag).assertIsDisplayed()

        val bitmap: Bitmap = composeRule.onNodeWithTag(svgTag).captureToImage().asAndroidBitmap()
        assertNotEquals(
            "the captured svg face should not be a single flat colour",
            countDistinctPixels(bitmap),
            1
        )
    }

    @Test
    fun initializeKeepsCacheEntriesReferencedByBag() = runTest {
        UtilityFeature.enabled = setOf(
            UtilityFeature.Flag.REPO_PROTOCOL_BUFFER_PROD,
        )

        val repositoryFactory = RepositoryFactory(ApplicationProvider.getApplicationContext())

        val dice = Dice(
            sides = mutableListOf(Side(number = 6, imageBase64 = svgBase64)),
            title = "d6"
        )
        repositoryFactory.repositoryBag.store(mutableListOf(dice))

        // mimic the RepositoryFactory.initialize() prune step, reading referenced
        // uuids straight from persisted data (must be current immediately after store)
        val referencedUuids = repositoryFactory.repositoryBag.fetchReferencedSideUuids() +
            repositoryFactory.repositoryRoll.fetchReferencedSideUuids()
        repositoryFactory.repositorySideCache.prune(referencedUuids)

        val cache = repositoryFactory.repositorySideCache.fetch().first()
        assertTrue(
            "cache must retain entries referenced by the bag, cache size=${cache.size}",
            cache.isNotEmpty()
        )
    }

    @Test
    fun svgFaceDisplaysFromSideCache() = runTest {
        val repositoryFactory = RepositoryFactory(ApplicationProvider.getApplicationContext())
        val bagDataTestDouble = BagDataTestDouble()

        val dice = bagDataTestDouble.d6
        val side = dice.sides[0]
        side.imageBase64 = ""
        side.imageDrawableId = 0

        // simulate the store optimization: the SVG lives ONLY in the side cache
        repositoryFactory.repositorySideCache.store(side.uuid, svgBase64)
        repositoryFactory.repositoryBag.store(mutableListOf(dice))

        val viewModel = ZoomDiceAndroidViewModel(
            ApplicationProvider.getApplicationContext(),
            repositoryFactory.repositorySettings,
            repositoryFactory.repositoryBag,
            repositoryFactory.repositoryRoll,
            repositoryFactory.repositoryLocked,
            repositoryFactory.repositorySideCache,
        )

        composeRule.setContent {
            val navigationState = rememberNavigationState(
                startRoute = ChanceNavKey.SetupDice,
                topLevelRoutes = setOf(ChanceNavKey.SetupDice)
            )
            val navigator = remember(navigationState) { Navigator(navigationState) }

            CompositionLocalProvider(LocalNavigator provides navigator) {
                ZoomBag(viewModel)
            }
        }

        val svgTag = "${ZoomDiceTestTag.ZOOM_SIDE_IMAGE_SVG}-${dice.title}-${side.number}"
        waitForTag(svgTag)

        composeRule.onNodeWithTag(svgTag).assertIsDisplayed()

        val bitmap: Bitmap = composeRule.onNodeWithTag(svgTag).captureToImage().asAndroidBitmap()
        assertTrue("svg node should have a non-zero size", bitmap.width > 0 && bitmap.height > 0)
        assertNotEquals(
            "the captured svg face should not be a single flat colour",
            countDistinctPixels(bitmap),
            1
        )
    }

    private fun countDistinctPixels(bitmap: Bitmap): Int {
        val pixels = IntArray(bitmap.width * bitmap.height)
        bitmap.getPixels(pixels, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
        return pixels.toSet().size
    }
}
