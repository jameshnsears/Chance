package com.github.jameshnsears.chance.ui.tab.rolls.selection

import android.app.Application
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.jameshnsears.chance.common.ui.AndroidTestHelper
import com.github.jameshnsears.chance.common.ui.theme.ChanceTheme
import com.github.jameshnsears.chance.common.utility.feature.UtilityFeature
import com.github.jameshnsears.chance.data.common.repo.RepositoryFactory
import com.github.jameshnsears.chance.data.domain.core.Dice
import com.github.jameshnsears.chance.data.domain.core.group.Group
import com.github.jameshnsears.chance.ui.tab.rolls.RollsAndroidViewModel
import com.github.jameshnsears.chance.ui.tab.rolls.RollsAndroidViewModelFactory
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
    fun rollSelectionFilterChip() = runTest {
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
                )
            )

            ChanceTheme {
                RollSelectionFilterChip(rollsAndroidViewModel, dice)
            }
        }

        composeRule
            .onNodeWithTag(RollsSelectionTestTag.ROLL_BUTTON + diceTitle)
            .performClick()
            .assertIsSelected()
    }

    @Test
    fun rollSelectionGroupFilterChip() = runTest {
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
                )
            )

            ChanceTheme {
                RollSelectionGroupFilterChip(rollsAndroidViewModel, group)
            }
        }

        composeRule
            .onNodeWithTag(RollsSelectionTestTag.ROLL_BUTTON + groupName)
            .performClick()
            .assertIsSelected()
    }
}
