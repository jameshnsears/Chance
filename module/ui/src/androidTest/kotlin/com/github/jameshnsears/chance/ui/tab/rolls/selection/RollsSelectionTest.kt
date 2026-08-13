package com.github.jameshnsears.chance.ui.tab.rolls.selection

import android.app.Application
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.jameshnsears.chance.common.ui.AndroidTestHelper
import com.github.jameshnsears.chance.common.ui.theme.ChanceTheme
import com.github.jameshnsears.chance.common.utility.feature.UtilityFeature
import com.github.jameshnsears.chance.data.common.repo.RepositoryFactory
import com.github.jameshnsears.chance.data.domain.core.Dice
import com.github.jameshnsears.chance.data.domain.core.Side
import com.github.jameshnsears.chance.data.domain.core.group.Group
import com.github.jameshnsears.chance.data.domain.core.roll.Roll
import com.github.jameshnsears.chance.ui.tab.rolls.RollsAndroidViewModel
import com.github.jameshnsears.chance.ui.tab.rolls.RollsAndroidViewModelFactory
import com.github.jameshnsears.chance.ui.zoom.rolls.ZoomRollsAndroidViewModel
import com.github.jameshnsears.chance.ui.zoom.rolls.ZoomRollsAndroidViewModelFactory
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class RollsSelectionTest : AndroidTestHelper() {
    @Before
    fun setup() = runTest {
        UtilityFeature.enabled = setOf(
            UtilityFeature.Flag.REPO_PROTOCOL_BUFFER_TEST_DOUBLE,
        )

        RepositoryFactory().resetStorage()
    }

    @Test
    fun rollSelectionFilterChip() {
        val diceTitle = "D6"
        val dice = Dice(title = diceTitle)

        composeRule.setContent {
            val application = LocalContext.current.applicationContext as Application
            val repositoryFactory = RepositoryFactory()

            val rollsAndroidViewModel: RollsAndroidViewModel = viewModel(
                factory = RollsAndroidViewModelFactory(
                    application = application,
                    repositoryFactory.repositorySettings,
                    repositoryFactory.repositoryBag,
                    repositoryFactory.repositoryRoll,
                    repositoryFactory.repositoryGroup,
                    repositoryFactory.repositoryLocked
                )
            )

            ChanceTheme {
                RollSelectionFilterChip(rollsAndroidViewModel, dice, true, true)
            }
        }

        val chipTag = RollsSelectionTestTag.ROLL_BUTTON + diceTitle
        waitForTag(chipTag)
        composeRule.onNodeWithTag(chipTag).performClick()
        waitForSelected(chipTag)
    }

    @Test
    fun rollSelectionGroupFilterChip() {
        val groupName = "Group 1"
        val group = Group(name = groupName)

        composeRule.setContent {
            val application = LocalContext.current.applicationContext as Application
            val repositoryFactory = RepositoryFactory()

            val rollsAndroidViewModel: RollsAndroidViewModel = viewModel(
                factory = RollsAndroidViewModelFactory(
                    application = application,
                    repositoryFactory.repositorySettings,
                    repositoryFactory.repositoryBag,
                    repositoryFactory.repositoryRoll,
                    repositoryFactory.repositoryGroup,
                    repositoryFactory.repositoryLocked
                )
            )

            ChanceTheme {
                RollSelectionGroupFilterChip(rollsAndroidViewModel, group, true, true)
            }
        }

        val chipTag = RollsSelectionTestTag.ROLL_BUTTON + groupName
        waitForTag(chipTag)
        composeRule.onNodeWithTag(chipTag).performClick()
        waitForSelected(chipTag)
    }

    @Test
    fun rollSelectionRowDisabledWhenLocked() {
        composeRule.setContent {
            val application = LocalContext.current.applicationContext as Application
            val repositoryFactory = RepositoryFactory()

            val rollsAndroidViewModel: RollsAndroidViewModel = viewModel(
                factory = RollsAndroidViewModelFactory(
                    application = application,
                    repositoryFactory.repositorySettings,
                    repositoryFactory.repositoryBag,
                    repositoryFactory.repositoryRoll,
                    repositoryFactory.repositoryGroup,
                    repositoryFactory.repositoryLocked
                )
            )

            val zoomRollsAndroidViewModel: ZoomRollsAndroidViewModel = viewModel(
                factory = ZoomRollsAndroidViewModelFactory(
                    application,
                    repositoryFactory.repositorySettings,
                    repositoryFactory.repositoryBag,
                    repositoryFactory.repositoryRoll,
                    repositoryFactory.repositoryLocked,
                    repositoryFactory.repositorySideCache,
                    repositoryFactory.repositoryGroup
                )
            )

            LaunchedEffect(Unit) {
                zoomRollsAndroidViewModel.toggleLock(Roll(uuidDice = "dummy", side = Side()))
            }

            ChanceTheme {
                RollSelectionRow(rollsAndroidViewModel, zoomRollsAndroidViewModel)
            }
        }

        val chipTag = RollsSelectionTestTag.ROLL_BUTTON + "d6"
        waitForTag(chipTag)
        composeRule
            .onNodeWithTag(chipTag)
            .assertIsNotEnabled()
    }
}
