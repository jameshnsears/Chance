package com.github.jameshnsears.chance.ui.tab.rolls

import android.app.Application
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.jameshnsears.chance.common.ui.AndroidTestHelper
import com.github.jameshnsears.chance.common.ui.theme.ChanceTheme
import com.github.jameshnsears.chance.common.utility.feature.UtilityFeature
import com.github.jameshnsears.chance.data.common.repo.RepositoryFactory
import com.github.jameshnsears.chance.ui.zoom.rolls.ZoomRollsAndroidViewModel
import com.github.jameshnsears.chance.ui.zoom.rolls.ZoomRollsAndroidViewModelFactory
import com.github.jameshnsears.chance.ui.zoom.rolls.ZoomRollsTestTag
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class RollsLockTest : AndroidTestHelper() {
    @Before
    fun setup() = runTest {
        UtilityFeature.enabled = setOf(
            UtilityFeature.Flag.REPO_PROTOCOL_BUFFER_TEST_DOUBLE,
        )

        RepositoryFactory().resetStorage()

        val repositoryBag = RepositoryFactory().repositoryBag
        val diceBag = repositoryBag.fetch().first()
        // select at least one dice to enable roll
        if (diceBag.isNotEmpty()) {
            diceBag[0].selected = true
            repositoryBag.store(diceBag)
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Test
    fun rollWithLock() = runTest {
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

        // 1. Initial Roll
        composeRule.onNodeWithTag(RollsTestTag.ROLL_ENABLED).performClick()
        composeRule.waitForIdle()

        // 2. Click a dice side to lock it
        // Note: ZoomRollFaceImageShape uses Modifier.clickable if onToggleLock is provided
        // We find the first one in the first sequence
        composeRule.onNodeWithTag(ZoomRollsTestTag.LAZY_COLUMN_NOT_EMPTY).performClick()
        composeRule.waitForIdle()

        // 3. Verify lock icon is displayed
        composeRule.onNodeWithTag(ZoomRollsTestTag.ROLL_LOCK).assertIsDisplayed()

        // 4. Perform second roll (this should be a RE-ROLL updating the same entry)
        composeRule.onNodeWithTag(RollsTestTag.ROLL_ENABLED).performClick()
        composeRule.waitForIdle()

        // 5. Verify that the lock icon STILL EXISTS (locks persist during re-roll)
        composeRule.onNodeWithTag(ZoomRollsTestTag.ROLL_LOCK).assertIsDisplayed()

        // 6. Click the lock to unlock it
        composeRule.onNodeWithTag(ZoomRollsTestTag.ROLL_LOCK).performClick()
        composeRule.waitForIdle()

        // 7. Perform third roll (this should be a NEW roll)
        composeRule.onNodeWithTag(RollsTestTag.ROLL_ENABLED).performClick()
        composeRule.waitForIdle()

        // 8. Verify lock is gone
        composeRule.onNodeWithTag(ZoomRollsTestTag.ROLL_LOCK).assertDoesNotExist()
    }
}
