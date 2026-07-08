package com.github.jameshnsears.chance.ui.dialog.group

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextInput
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.jameshnsears.chance.common.ui.AndroidTestHelper
import com.github.jameshnsears.chance.common.ui.theme.ChanceTheme
import com.github.jameshnsears.chance.common.utility.feature.UtilityFeature
import com.github.jameshnsears.chance.data.common.repo.RepositoryFactory
import com.github.jameshnsears.chance.data.domain.core.Dice
import com.github.jameshnsears.chance.ui.dialog.dice.ButtonFeatureTestTag
import com.github.jameshnsears.chance.ui.dialog.dice.card.dice.CardDiceTestTag
import com.github.jameshnsears.chance.ui.group.GroupTestTag
import com.github.jameshnsears.chance.ui.tab.setup.groups.GroupsAndroidViewModel
import com.github.jameshnsears.chance.ui.tab.setup.groups.GroupsAndroidViewModelFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class DialogGroupTest : AndroidTestHelper() {
    @Before
    fun setup() = runTest {
        UtilityFeature.enabled = setOf(
            UtilityFeature.Flag.REPO_PROTOCOL_BUFFER_TEST_DOUBLE,
        )

        val repositoryFactory = RepositoryFactory()
        repositoryFactory.resetStorage()
        repositoryFactory.repositoryGroup.clear()

        // Seed with some dice so they appear in the selection list
        repositoryFactory.repositoryBag.store(
            mutableListOf(
                Dice(title = "D6", uuid = "d6-uuid"),
                Dice(title = "D20", uuid = "d20-uuid")
            )
        )
    }

    @Test
    fun dialogGroupInteraction() = runTest {
        val repositoryFactory = RepositoryFactory()
        val repositoryBag = repositoryFactory.repositoryBag
        val repositoryRoll = repositoryFactory.repositoryRoll
        val repositoryGroup = repositoryFactory.repositoryGroup

        val showDialog = mutableStateOf(true)

        composeRule.setContent {
            val application = LocalContext.current.applicationContext as Application

            val groupsAndroidViewModel: GroupsAndroidViewModel = viewModel(
                factory = GroupsAndroidViewModelFactory(
                    application = application,
                    repositoryBag,
                    repositoryGroup,
                    repositoryRoll,
                )
            )

            ChanceTheme {
                DialogGroup(groupsAndroidViewModel, showDialog)
            }
        }

        // 1. Verify initial state
        composeRule.onNodeWithTag(CardDiceTestTag.DICE_TITLE).assertIsDisplayed()
        composeRule.onNodeWithTag(ButtonFeatureTestTag.BUTTON_FEATURE_SAVE).assertIsNotEnabled()

        // 2. Set Name
        composeRule.onNodeWithTag(CardDiceTestTag.DICE_TITLE).performTextInput("New Group")
        composeRule.onNodeWithTag(ButtonFeatureTestTag.BUTTON_FEATURE_SAVE).assertIsNotEnabled() // Still needs dice

        // 3. Select Dice
        composeRule.onNodeWithText("D6").assertIsDisplayed()

        // Click "Increase" on the first item (D6)
        composeRule.onAllNodesWithContentDescription("Increase")[0].performClick()

        // 4. Verify Save is now enabled
        composeRule.onNodeWithTag(ButtonFeatureTestTag.BUTTON_FEATURE_SAVE).assertIsEnabled()

        // 5. Add Notes
        composeRule.onNodeWithTag(GroupTestTag.NOTES).performScrollTo().performTextInput("Some notes")

        // 6. Save
        composeRule.onNodeWithTag(ButtonFeatureTestTag.BUTTON_FEATURE_SAVE).performClick()
        composeRule.waitForIdle()

        // Verify dialog is closed
        assert(!showDialog.value)

        // 7. Verify it's saved in the repository
        val groups = repositoryGroup.fetch().first()
        assert(groups.size == 1)
        assert(groups[0].name == "New Group")
        assert(groups[0].notes == "Some notes")
        assert(groups[0].uuidDice.contains("d6-uuid"))
    }

    @Test
    fun dialogGroupClose() = runTest {
        val repositoryFactory = RepositoryFactory()
        val showDialog = mutableStateOf(true)

        composeRule.setContent {
            val application = LocalContext.current.applicationContext as Application
            val groupsAndroidViewModel: GroupsAndroidViewModel = viewModel(
                factory = GroupsAndroidViewModelFactory(
                    application = application,
                    repositoryFactory.repositoryBag,
                    repositoryFactory.repositoryGroup,
                    repositoryFactory.repositoryRoll,
                )
            )

            ChanceTheme {
                DialogGroup(groupsAndroidViewModel, showDialog)
            }
        }

        composeRule.onNodeWithContentDescription("Close").performClick()
        assert(!showDialog.value)
    }
}
