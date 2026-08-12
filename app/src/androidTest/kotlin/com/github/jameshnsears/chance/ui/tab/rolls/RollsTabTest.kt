package com.github.jameshnsears.chance.ui.tab.rolls

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.test.espresso.Espresso
import androidx.test.platform.app.InstrumentationRegistry
import com.github.jameshnsears.chance.TestSupport
import com.github.jameshnsears.chance.data.common.repo.RepositoryFactory
import com.github.jameshnsears.chance.ui.dialog.settings.DialogSettingsTestTag
import com.github.jameshnsears.chance.ui.tab.rolls.selection.RollsSelectionTestTag
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class RollsTabTest : TestSupport() {
    @Before
    fun setUp() {
        runBlocking {
            RepositoryFactory(InstrumentationRegistry.getInstrumentation().targetContext).resetStorage()
        }
    }

    @Test
    fun rollFirstDice() {
        androidComposeTestRule.onNodeWithText("Rolls").performClick()

        displayBottomSheet(RollsTestTag.BOTTOM_SHEET)

        androidComposeTestRule.onAllNodes(
            SemanticsMatcher("tagStartsWith") {
                it.config.getOrElse(SemanticsProperties.TestTag) { "" }
                    .startsWith(RollsSelectionTestTag.ROLL_BUTTON)
            }
        ).onFirst().performClick()

        assertClick(RollsTestTag.ROLL_ENABLED)

        assertClick(RollsTestTag.SETTINGS)

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
        androidComposeTestRule.onNodeWithTag(DialogSettingsTestTag.SETTINGS_SCORE)
            .performScrollTo()
            .performClick()

        Espresso.pressBack()

        // Wait for the tab to be visible again after back press
        waitForTag(RollsTestTag.ROLL_ENABLED)
        assertClick(RollsTestTag.ROLL_ENABLED)

        repeat(4) {
            assertClick(RollsTestTag.UNDO)
        }

        androidComposeTestRule.onNodeWithTag(RollsTestTag.UNDO).assertIsNotEnabled()
    }
}
