package com.github.jameshnsears.chance.ui.tab.rolls

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import com.github.jameshnsears.chance.TestSupport
import com.github.jameshnsears.chance.ui.dialog.settings.DialogSettingsTestTag
import com.github.jameshnsears.chance.ui.tab.rolls.selection.RollsSelectionTestTag
import org.junit.Test

class RollsTabTest : TestSupport() {
    @Test
    fun rollFirstDice() {
        androidComposeTestRule.onNodeWithText("Rolls").performClick()
        androidComposeTestRule.waitForIdle()

        displayBottomSheet(RollsTestTag.BOTTOM_SHEET)
        androidComposeTestRule.waitForIdle()

        androidComposeTestRule.onAllNodes(
            SemanticsMatcher("tagStartsWith") {
                it.config.getOrElse(SemanticsProperties.TestTag) { "" }
                    .startsWith(RollsSelectionTestTag.ROLL_BUTTON)
            }
        ).onFirst().performClick()
        androidComposeTestRule.waitForIdle()

        assertClick(RollsTestTag.ROLL_ENABLED)
        androidComposeTestRule.waitForIdle()

        androidComposeTestRule.onNodeWithTag(RollsTestTag.SETTINGS).performClick()
        androidComposeTestRule.waitForIdle()

        val settingsTags = listOf(
            DialogSettingsTestTag.SETTINGS_TIME,
            DialogSettingsTestTag.SETTINGS_SCORE,
            DialogSettingsTestTag.SETTINGS_SCORE_TTS,
            DialogSettingsTestTag.SETTINGS_DICE_TITLE,
            DialogSettingsTestTag.SETTINGS_SIDE_NUMBER,
            DialogSettingsTestTag.SETTINGS_SIDE_DESCRIPTION,
            DialogSettingsTestTag.SETTINGS_SIDE_SVG,
            DialogSettingsTestTag.SETTINGS_BEHAVIOUR,
            DialogSettingsTestTag.SETTINGS_GROUP_TITLE,
            DialogSettingsTestTag.SETTINGS_ROLL_SHUFFLE,
            DialogSettingsTestTag.SETTINGS_ROLL_HAPTICS,
            DialogSettingsTestTag.SETTINGS_SHAKE_TO_ROLL,
            DialogSettingsTestTag.SETTINGS_ROLL_SOUND,
        )

        settingsTags.forEach { tag ->
            androidComposeTestRule.onNodeWithTag(tag)
                .performScrollTo()
                .performClick()
        }

        // Ensure at least one setting that enables Roll button is ON
        // SETTINGS_SCORE is a good candidate. If it was ON and we clicked it, it's now OFF.
        // We'll click it again to be sure it's ON.
        androidComposeTestRule.onNodeWithTag(DialogSettingsTestTag.SETTINGS_SCORE)
            .performScrollTo()
            .performClick()

        androidComposeTestRule.onNodeWithTag(DialogSettingsTestTag.SETTINGS_CLOSE).performClick()
        androidComposeTestRule.waitForIdle()

        assertClick(RollsTestTag.ROLL_ENABLED)
        androidComposeTestRule.waitForIdle()

        repeat(3) {
            androidComposeTestRule.onNodeWithTag(RollsTestTag.UNDO).performClick()
            androidComposeTestRule.waitForIdle()
        }

        androidComposeTestRule.onNodeWithTag(RollsTestTag.UNDO).assertIsNotEnabled()
    }
}
