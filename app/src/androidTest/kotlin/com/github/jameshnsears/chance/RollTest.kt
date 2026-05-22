package com.github.jameshnsears.chance

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import com.github.jameshnsears.chance.common.R
import com.github.jameshnsears.chance.common.utility.feature.UtilityFeature
import com.github.jameshnsears.chance.common.utility.feature.UtilityFeature.Flag
import com.github.jameshnsears.chance.data.common.repository.RepositoryFactory
import com.github.jameshnsears.chance.ui.dialog.settings.DialogSettingsTestTag
import com.github.jameshnsears.chance.ui.tab.roll.RollTestTag
import com.github.jameshnsears.chance.ui.tab.roll.selection.RollSelectionTestTag
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class RollTest : TestSupport() {
    @Before
    fun setup() = runTest {
        RepositoryFactory().resetStorage()

        androidComposeTestRule.waitForIdle()
    }

    @Test
    fun roll() = runTest {
        androidComposeTestRule
            .onNodeWithText(getString(R.string.tab_roll))
            .performClick()

        androidComposeTestRule.waitForIdle()

        androidComposeTestRule
            .onNodeWithText(getString(R.string.tab_roll))
            .performClick()
            .assertIsSelected()

        displayBottomSheet(RollTestTag.BOTTOM_SHEET)

        androidComposeTestRule
            .onNodeWithTag(RollSelectionTestTag.ROLL_BUTTON + "d6")
            .assertIsDisplayed()
            .performClick()

        androidComposeTestRule
            .onNodeWithTag(RollTestTag.ROLL_ENABLED)
            .assertIsDisplayed()
            .performClick()
    }

    @Test
    fun rollUndo() = runTest {
        androidComposeTestRule
            .onNodeWithText(getString(R.string.tab_roll))
            .performClick()

        androidComposeTestRule.waitForIdle()

        displayBottomSheet(RollTestTag.BOTTOM_SHEET)

        androidComposeTestRule
            .onNodeWithTag(RollTestTag.UNDO)
            .assertIsEnabled()
            .performClick()

        androidComposeTestRule.waitForIdle()

        androidComposeTestRule
            .onNodeWithTag(RollTestTag.SETTINGS)
            .performClick()

        androidComposeTestRule.waitForIdle()

        androidComposeTestRule
            .onNodeWithTag(DialogSettingsTestTag.SETTINGS_UNDO_ALL)
            .performScrollTo()

        androidComposeTestRule.waitForIdle()

        androidComposeTestRule
            .onNodeWithTag(DialogSettingsTestTag.SETTINGS_UNDO_ALL)
            .assertIsEnabled()
            .performClick()

        androidComposeTestRule.waitForIdle()

        androidComposeTestRule
            .onNodeWithTag(RollTestTag.UNDO)
            .assertIsNotEnabled()
    }

    @Test
    fun rollSettings() = runTest {
        androidComposeTestRule
            .onNodeWithText(getString(R.string.tab_roll))
            .performClick()

        androidComposeTestRule.waitForIdle()

        displayBottomSheet(RollTestTag.BOTTOM_SHEET)

        androidComposeTestRule
            .onNodeWithTag(RollTestTag.SETTINGS)
            .performClick()

        // run assert & click on all test tags for the dialog
        val dialogSettingsTestTags = DialogSettingsTestTag.Companion::class.java.declaredFields
            .filter { it.type == String::class.java }
            .map {
                it.isAccessible = true
                it.get(DialogSettingsTestTag.Companion) as String
            }

        dialogSettingsTestTags.forEach { tag ->
            assertClick(tag)
        }
    }
}
