package com.github.jameshnsears.chance.ui.dialog.settings

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.jameshnsears.chance.common.ui.AndroidTestHelper
import com.github.jameshnsears.chance.common.ui.theme.ChanceTheme
import com.github.jameshnsears.chance.common.utility.feature.UtilityFeature
import com.github.jameshnsears.chance.data.common.repo.RepositoryFactory
import com.github.jameshnsears.chance.ui.tab.rolls.RollsAndroidViewModel
import com.github.jameshnsears.chance.ui.tab.rolls.RollsAndroidViewModelFactory
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class DialogSettingsTest : AndroidTestHelper() {
    @Before
    fun setup() = runTest {
        UtilityFeature.enabled = setOf(
            UtilityFeature.Flag.REPO_PROTOCOL_BUFFER_TEST_DOUBLE,
        )

        RepositoryFactory().resetStorage()
    }

    @Test
    fun settingsInteraction() = runTest {
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

            ChanceTheme {
                DialogSettingsLayout(
                    remember { mutableStateOf(true) },
                    rollsAndroidViewModel
                )
            }
        }

        composeRule.onNodeWithTag(DialogSettingsTestTag.SETTINGS_TIME).assertIsDisplayed()

        // Toggle Time: initially OFF
        composeRule.onNodeWithTag(DialogSettingsTestTag.SETTINGS_TIME).assertIsOff()
        composeRule.onNodeWithTag(DialogSettingsTestTag.SETTINGS_TIME).performClick()
        composeRule.onNodeWithTag(DialogSettingsTestTag.SETTINGS_TIME).assertIsOn()

        // Toggle Score: initially ON
        composeRule.onNodeWithTag(DialogSettingsTestTag.SETTINGS_SCORE).assertIsOn()
        composeRule.onNodeWithTag(DialogSettingsTestTag.SETTINGS_SCORE).performClick()
        composeRule.onNodeWithTag(DialogSettingsTestTag.SETTINGS_SCORE).assertIsOff()

        // Verify other settings are displayed (scrolling if necessary)
        composeRule.onNodeWithTag(DialogSettingsTestTag.SETTINGS_GROUP_TITLE).performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithTag(DialogSettingsTestTag.SETTINGS_DICE_TITLE).performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithTag(DialogSettingsTestTag.SETTINGS_BEHAVIOUR).performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithTag(DialogSettingsTestTag.SETTINGS_SIDE_NUMBER).performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithTag(DialogSettingsTestTag.SETTINGS_SIDE_SVG).performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithTag(DialogSettingsTestTag.SETTINGS_SIDE_DESCRIPTION).performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithTag(DialogSettingsTestTag.SETTINGS_ROLL_SHUFFLE).performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithTag(DialogSettingsTestTag.SETTINGS_ROLL_HAPTICS).performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithTag(DialogSettingsTestTag.SETTINGS_ROLL_SOUND).performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithTag(DialogSettingsTestTag.SETTINGS_SCORE_TTS).performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithTag(DialogSettingsTestTag.SETTINGS_SHAKE_TO_ROLL).performScrollTo().assertIsDisplayed()

        composeRule.onNodeWithTag(DialogSettingsTestTag.SETTINGS_UNDO_ALL).performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithTag(DialogSettingsTestTag.SETTINGS_UNDO_ALL).assertIsEnabled()

        // Test Undo All
        composeRule.onNodeWithTag(DialogSettingsTestTag.SETTINGS_UNDO_ALL).performClick()
        composeRule.onNodeWithTag(DialogSettingsTestTag.SETTINGS_UNDO_ALL).assertIsNotEnabled()
    }
}
