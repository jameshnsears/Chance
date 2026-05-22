package com.github.jameshnsears.chance

import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performSemanticsAction
import androidx.compose.ui.test.performTextReplacement
import com.github.jameshnsears.chance.common.R
import com.github.jameshnsears.chance.ui.dialog.bag.ButtonFeatureTestTag
import com.github.jameshnsears.chance.ui.dialog.bag.TabTestTag
import com.github.jameshnsears.chance.ui.dialog.bag.card.dice.DiceTestTag
import com.github.jameshnsears.chance.ui.dialog.bag.card.roll.RollTestTag
import com.github.jameshnsears.chance.ui.dialog.bag.card.side.SideTestTag
import com.github.jameshnsears.chance.ui.dialog.colour.DialogColourTestTag
import com.github.jameshnsears.chance.ui.dialog.confirm.ConfirmTestTag
import com.github.jameshnsears.chance.ui.tab.bag.BagTestTag
import com.github.jameshnsears.chance.ui.zoom.bag.ZoomBagTestTag
import kotlinx.coroutines.test.runTest
import org.junit.Assume.assumeTrue
import org.junit.Test

class BagDialogTest : TestSupport() {
    @Test
    fun reset() = runTest {
        displayBottomSheet(BagTestTag.BOTTOM_SHEET)

        androidComposeTestRule
            .onNodeWithTag(BagTestTag.RESET)
            .performClick()

        androidComposeTestRule
            .onNodeWithText(getString(R.string.tab_bag_reset_storage_confirm))
            .assertIsDisplayed()
    }

    @Test
    fun dialogSide() = runTest {
        // click on dice
        androidComposeTestRule
            .onNodeWithTag("${ZoomBagTestTag.ZOOM_SIDE_IMAGE_SHAPE}-d4-2")
            .performClick()

        // change Side # colour
        androidComposeTestRule
            .onNodeWithTag(SideTestTag.SIDE_COLOUR)
            .performClick()

        // CLick OK in the colour dialog
        androidComposeTestRule
            .onNodeWithTag(DialogColourTestTag.DIALOG_COLOUR_OK)
            .performClick()

        androidComposeTestRule
            .onNodeWithTag(SideTestTag.SIDE_COLOUR_APPLY_ALL)
            .performClick()

        // Drop SVG + apply to all sides
        androidComposeTestRule
            .onNodeWithTag(SideTestTag.SIDE_IMAGE_SVG_DROP)
            .performClick()

        androidComposeTestRule
            .onNodeWithTag(SideTestTag.SIDE_IMAGE_APPLY_ALL)
            .performClick()

        // Change Description + Colour (cancel button) + apply to all sides
        androidComposeTestRule
            .onNodeWithTag(SideTestTag.SIDE_DESCRIPTION)
            .performTextReplacement("New Side Description")

        androidComposeTestRule
            .onNodeWithTag(SideTestTag.SIDE_DESCRIPTION_COLOUR)
            .performScrollTo()
            .performClick()

        androidComposeTestRule
            .onNodeWithTag(DialogColourTestTag.DIALOG_COLOUR_CANCEL)
            .performClick()

        androidComposeTestRule
            .onNodeWithTag(SideTestTag.SIDE_DESCRIPTION_APPLY_ALL)
            .performClick()

        // Click Save
        androidComposeTestRule
            .onNodeWithTag(ButtonFeatureTestTag.BUTTON_FEATURE_SAVE)
            .performClick()
    }

    @Test
    fun diceDelete() = runTest {
        androidComposeTestRule
            .onNodeWithTag("${ZoomBagTestTag.ZOOM_SIDE_IMAGE_SHAPE}-d2-2")
            .performClick()

        // Click Dice Tab
        androidComposeTestRule
            .onNodeWithTag(TabTestTag.TAB_DICE)
            .performClick()

        // Click Delete
        androidComposeTestRule
            .onNodeWithTag(ButtonFeatureTestTag.BUTTON_FEATURE_DELETE)
            .performClick()

        // Click OK
        androidComposeTestRule
            .onNodeWithTag(ConfirmTestTag.OK)
            .performClick()
    }

    @Test
    fun diceClone() = runTest {
        // click on dice
        androidComposeTestRule
            .onNodeWithTag("${ZoomBagTestTag.ZOOM_SIDE_IMAGE_SHAPE}-d2-2")
            .performClick()

        // Click Dice Tab
        androidComposeTestRule
            .onNodeWithTag(TabTestTag.TAB_DICE)
            .performClick()

        androidComposeTestRule
            .onNodeWithTag(ButtonFeatureTestTag.BUTTON_FEATURE_CLONE)
            .assertIsNotEnabled()

        // Change Title, so unique
        androidComposeTestRule
            .onNodeWithTag(DiceTestTag.DICE_TITLE)
            .performTextReplacement("d2clone")

        // Ensure Clone is enabled + click on it
        androidComposeTestRule
            .onNodeWithTag(ButtonFeatureTestTag.BUTTON_FEATURE_CLONE)
            .assertIsEnabled()
            .performClick()
    }

    @Test
    fun diceChangeSides() = runTest {
        // click on dice
        androidComposeTestRule
            .onNodeWithTag("${ZoomBagTestTag.ZOOM_SIDE_IMAGE_SHAPE}-d6-6")
            .performClick()

        // Click Dice Tab
        androidComposeTestRule
            .onNodeWithTag(TabTestTag.TAB_DICE)
            .performClick()

        // change from 6 to 4
        androidComposeTestRule
            .onNodeWithTag(DiceTestTag.DICE_SIDES)
            .performSemanticsAction(SemanticsActions.SetProgress) { action -> action(1f) }

        // change Title colour
        androidComposeTestRule
            .onNodeWithTag(DiceTestTag.DICE_COLOUR)
            .performClick()

        // CLick OK in the colour dialog
        androidComposeTestRule
            .onNodeWithTag(DialogColourTestTag.DIALOG_COLOUR_OK)
            .performClick()

        // Click Save
        androidComposeTestRule
            .onNodeWithTag(ButtonFeatureTestTag.BUTTON_FEATURE_SAVE)
            .performClick()
    }

    @Test
    fun bagDialogBehaviour() = runTest {
        assumeTrue("Skipping bagDialogBehaviour in CI", !isCI)

        // click on dice
        androidComposeTestRule
            .onNodeWithTag("${ZoomBagTestTag.ZOOM_SIDE_IMAGE_SHAPE}-Story-6")
            .performClick()

        // Click behaviour tab
        waitForGitHubCI(TabTestTag.TAB_ROLL)

        androidComposeTestRule
            .onNodeWithTag(TabTestTag.TAB_ROLL)
            .performClick()

        // Change Roll multiplier to 1
        androidComposeTestRule
            .onNodeWithTag(RollTestTag.ROLL_MULTIPLIER_VALUE)
            .performClick()

        androidComposeTestRule
            .onNodeWithTag(RollTestTag.ROLL_MULTIPLIER_VALUE + "-1")
            .performClick()

        // Tick explosion
        androidComposeTestRule
            .onNodeWithTag(RollTestTag.ROLL_EXPLODE)
            .assertIsDisplayed()

        androidComposeTestRule
            .onNodeWithTag(RollTestTag.ROLL_EXPLODE_CHECKBOX)
            .performClick()

        // Test explode when/value tags
        androidComposeTestRule
            .onNodeWithTag(RollTestTag.ROLL_EXPLODE_WHEN)
            .performClick()

        androidComposeTestRule
            .onNodeWithTag(RollTestTag.ROLL_EXPLODE_WHEN + "-=")
            .performClick()

        androidComposeTestRule
            .onNodeWithTag(RollTestTag.ROLL_EXPLODE_VALUE)
            .performClick()

        androidComposeTestRule
            .onNodeWithTag(RollTestTag.ROLL_EXPLODE_VALUE + "-6")
            .performClick()

        // Tick Score adjustment
        androidComposeTestRule
            .onNodeWithTag(RollTestTag.ROLL_MODIFY_SCORE)
            .assertIsDisplayed()

        androidComposeTestRule
            .onNodeWithTag(RollTestTag.ROLL_MODIFY_SCORE_CHECKBOX)
            .performClick()

        // Change Score adjustment to 1
        androidComposeTestRule
            .onNodeWithTag(RollTestTag.ROLL_MODIFY_SCORE_VALUE)
            .performClick()

        androidComposeTestRule
            .onNodeWithTag(RollTestTag.ROLL_MODIFY_SCORE_VALUE + "-1")
            .performClick()

        // Click Save
        androidComposeTestRule
            .onNodeWithTag(ButtonFeatureTestTag.BUTTON_FEATURE_SAVE)
            .performClick()
    }
}
