package com.github.jameshnsears.chance.ui.tab.rolls

import android.app.Application
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipe
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.jameshnsears.chance.common.ui.AndroidTestHelper
import com.github.jameshnsears.chance.common.ui.theme.ChanceTheme
import com.github.jameshnsears.chance.common.utility.feature.UtilityFeature
import com.github.jameshnsears.chance.data.common.repo.RepositoryFactory
import com.github.jameshnsears.chance.ui.zoom.rolls.ZoomRollsAndroidViewModel
import com.github.jameshnsears.chance.ui.zoom.rolls.ZoomRollsAndroidViewModelFactory
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class RollsTest : AndroidTestHelper() {
    @Before
    fun setup() = runTest {
        UtilityFeature.enabled = setOf(
            UtilityFeature.Flag.REPO_PROTOCOL_BUFFER_TEST_DOUBLE,
        )

        RepositoryFactory().resetStorage()
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Test
    fun rollDice() = runTest {

        val repositorySettings = RepositoryFactory().repositorySettings
        val repositoryBag = RepositoryFactory().repositoryBag
        val repositoryRoll = RepositoryFactory().repositoryRoll
        val repositoryGroup = RepositoryFactory().repositoryGroup

        composeRule.setContent {
            val application = LocalContext.current.applicationContext as Application

            val rollsAndroidViewModel: RollsAndroidViewModel = viewModel(
                factory = RollsAndroidViewModelFactory(
                    application = application,
                    repositorySettings,
                    repositoryBag,
                    repositoryRoll,
                    repositoryGroup,
                )
            )

            val zoomRollsAndroidViewModel: ZoomRollsAndroidViewModel = viewModel(
                factory = ZoomRollsAndroidViewModelFactory(
                    application = application,
                    repositorySettings,
                    repositoryBag,
                    repositoryRoll,
                    repositoryGroup,
                )
            )

            ChanceTheme {
                TabRollLayout(
                    rollsAndroidViewModel,
                    zoomRollsAndroidViewModel
                )
            }
        }

        composeRule.waitForIdle()

        composeRule.mainClock.autoAdvance = false

        composeRule
            .onNodeWithTag(RollsTestTag.BOTTOM_SHEET)
            .performTouchInput {
                swipe(
                    start = topCenter,
                    end = Offset(centerX, -1000f),
                    durationMillis = 500
                )
            }

        repeat(10) {
            composeRule.mainClock.advanceTimeBy(100)
            Thread.sleep(100)
        }
    }
}
