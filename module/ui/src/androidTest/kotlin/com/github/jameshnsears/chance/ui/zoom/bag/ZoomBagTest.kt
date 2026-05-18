package com.github.jameshnsears.chance.ui.zoom.bag

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.test.core.app.ApplicationProvider
import com.github.jameshnsears.chance.common.ui.AndroidTestHelper
import com.github.jameshnsears.chance.common.utility.feature.UtilityFeature
import com.github.jameshnsears.chance.data.common.repository.RepositoryFactory
import com.github.jameshnsears.chance.data.domain.core.bag.testdouble.BagDataTestDouble
import com.github.jameshnsears.chance.ui.dialog.bag.TabTestTag
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Test

class ZoomBagTest : AndroidTestHelper() {
    @After
    fun tearDown() {
        UtilityFeature.enabled = setOf(
            UtilityFeature.Flag.REPO_PROTOCOL_BUFFER_TEST_DOUBLE,
        )
    }

    @Test
    fun zoomBagSideDescription() = runTest {
        val bagDataTestDouble = BagDataTestDouble()
        val repositoryFactory = RepositoryFactory(ApplicationProvider.getApplicationContext())
        val repositoryBag = repositoryFactory.repositoryBag

        val diceStory = bagDataTestDouble.diceStory
        repositoryBag.store(mutableListOf(diceStory))

        val viewModel = ZoomBagAndroidViewModel(
            ApplicationProvider.getApplicationContext(),
            repositoryFactory.repositorySettings,
            repositoryBag,
            repositoryFactory.repositoryRoll
        )

        composeRule.setContent {
            ZoomBag(viewModel)
        }

        // multiple sides have same description tag in this Composable
        composeRule.onAllNodesWithTag("${ZoomBagTestTag.ZOOM_SIDE_DESCRIPTION}-${diceStory.title}")[0].assertIsDisplayed()
    }

    @Test
    fun zoomBagShowEpochUuid() = runTest {
        UtilityFeature.enabled = setOf(
            UtilityFeature.Flag.REPO_PROTOCOL_BUFFER_TEST_DOUBLE,
            UtilityFeature.Flag.UI_SHOW_EPOCH_UUID
        )

        val bagDataTestDouble = BagDataTestDouble()
        val repositoryFactory = RepositoryFactory(ApplicationProvider.getApplicationContext())
        val repositoryBag = repositoryFactory.repositoryBag
        val firstDice = bagDataTestDouble.d6
        repositoryBag.store(mutableListOf(firstDice))

        val viewModel = ZoomBagAndroidViewModel(
            ApplicationProvider.getApplicationContext(),
            repositoryFactory.repositorySettings,
            repositoryBag,
            repositoryFactory.repositoryRoll
        )

        composeRule.setContent {
            ZoomBag(viewModel)
        }

        composeRule.onNodeWithText(firstDice.title).assertIsDisplayed()
        composeRule.onNodeWithText("${firstDice.epoch}").assertIsDisplayed()
        composeRule.onNodeWithText(firstDice.uuid).assertIsDisplayed()
    }

    @Test
    fun zoomBagClickSideOpensDialog() = runTest {
        val bagDataTestDouble = BagDataTestDouble()
        val repositoryFactory = RepositoryFactory(ApplicationProvider.getApplicationContext())
        val repositoryBag = repositoryFactory.repositoryBag
        repositoryBag.store(bagDataTestDouble.allDice)

        val viewModel = ZoomBagAndroidViewModel(
            ApplicationProvider.getApplicationContext(),
            repositoryFactory.repositorySettings,
            repositoryBag,
            repositoryFactory.repositoryRoll
        )

        composeRule.setContent {
            ZoomBag(viewModel)
        }

        val firstDice = bagDataTestDouble.allDice[0]
        val firstSide = firstDice.sides[0]

        composeRule.onNodeWithTag("${ZoomBagTestTag.ZOOM_SIDE_IMAGE_SHAPE}-${firstDice.title}-${firstSide.number}")
            .performClick()

        composeRule.onNodeWithTag(TabTestTag.TAB_SIDE).assertIsDisplayed()
    }
}
