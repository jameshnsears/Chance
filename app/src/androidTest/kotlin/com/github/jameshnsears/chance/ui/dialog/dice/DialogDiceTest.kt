package com.github.jameshnsears.chance.ui.dialog.dice

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.test.platform.app.InstrumentationRegistry
import com.github.jameshnsears.chance.TestSupport
import com.github.jameshnsears.chance.data.common.repo.RepositoryFactory
import com.github.jameshnsears.chance.ui.dialog.confirm.DialogConfirmTestTag
import com.github.jameshnsears.chance.ui.dialog.dice.card.dice.CardDiceTestTag
import com.github.jameshnsears.chance.ui.dialog.dice.card.face.CardFaceTestTag
import com.github.jameshnsears.chance.ui.dialog.dice.card.roll.CardRollTestTag
import com.github.jameshnsears.chance.ui.zoom.setup.dice.ZoomDiceTestTag
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class DialogDiceTest : TestSupport() {
    @Before
    fun setUp() {
        runBlocking {
            RepositoryFactory(InstrumentationRegistry.getInstrumentation().targetContext).resetStorage()
        }
    }

    @Test
    fun dialogDice() {
        val diceSideTag = "${ZoomDiceTestTag.ZOOM_SIDE_IMAGE_SHAPE}-d2-1"
        waitForTag(diceSideTag)

        // Open DialogDice by clicking on a Dice side in TabBagDiceLayout
        assertClick(diceSideTag)

        // --- CardDice Tab ---
        waitForTag(DialogDiceTabTestTag.TAB_DICE)
        assertClick(DialogDiceTabTestTag.TAB_DICE)

        androidComposeTestRule
            .onNodeWithTag(CardDiceTestTag.DICE_TITLE)
            .assertTextContains("d2")
            .performTextClearance()

        androidComposeTestRule
            .onNodeWithTag(CardDiceTestTag.DICE_TITLE)
            .performTextInput("d2-updated")

        androidComposeTestRule
            .onNodeWithTag(CardDiceTestTag.DICE_SIDES)
            .performScrollTo()
            .assertIsDisplayed()

        androidComposeTestRule
            .onNodeWithTag(CardDiceTestTag.DICE_COLOUR)
            .performScrollTo()
            .assertIsDisplayed()

        // --- CardFace Tab ---
        assertClick(DialogDiceTabTestTag.TAB_SIDE)

        androidComposeTestRule
            .onNodeWithTag(CardFaceTestTag.SIDE_NUMBER)
            .assertIsDisplayed()

        androidComposeTestRule
            .onNodeWithTag(CardFaceTestTag.SIDE_COLOUR)
            .performScrollTo()
            .assertIsDisplayed()

        androidComposeTestRule
            .onNodeWithTag(CardFaceTestTag.SIDE_COLOUR_APPLY_ALL)
            .performScrollTo()
            .assertIsDisplayed()

        androidComposeTestRule
            .onNodeWithTag(CardFaceTestTag.SIDE_IMAGE_SVG)
            .performScrollTo()
            .assertIsDisplayed()

        androidComposeTestRule
            .onNodeWithTag(CardFaceTestTag.SIDE_IMAGE_SVG_DROP)
            .performScrollTo()
            .assertIsDisplayed()

        androidComposeTestRule
            .onNodeWithTag(CardFaceTestTag.SIDE_IMAGE_APPLY_ALL)
            .performScrollTo()
            .assertIsDisplayed()

        androidComposeTestRule
            .onNodeWithTag(CardFaceTestTag.SIDE_DESCRIPTION)
            .performScrollTo()
            .performTextInput("desc")

        androidComposeTestRule
            .onNodeWithTag(CardFaceTestTag.SIDE_DESCRIPTION_COLOUR)
            .performScrollTo()
            .assertIsDisplayed()

        androidComposeTestRule
            .onNodeWithTag(CardFaceTestTag.SIDE_DESCRIPTION_APPLY_ALL)
            .performScrollTo()
            .assertIsDisplayed()

        // --- CardRoll Tab ---
        assertClick(DialogDiceTabTestTag.TAB_ROLL)

        androidComposeTestRule
            .onNodeWithTag(CardRollTestTag.ROLL_MULTIPLIER_VALUE)
            .performScrollTo()
            .assertIsDisplayed()

        androidComposeTestRule
            .onNodeWithTag(CardRollTestTag.ROLL_EXPLODE_CHECKBOX)
            .performScrollTo()
            .assertIsOff()
            .performClick()
            .assertIsOn()

        androidComposeTestRule
            .onNodeWithTag(CardRollTestTag.ROLL_EXPLODE_WHEN)
            .performScrollTo()
            .assertIsDisplayed()

        androidComposeTestRule
            .onNodeWithTag(CardRollTestTag.ROLL_EXPLODE_VALUE)
            .performScrollTo()
            .assertIsDisplayed()

        androidComposeTestRule
            .onNodeWithTag(CardRollTestTag.ROLL_MODIFY_SCORE_CHECKBOX)
            .performScrollTo()
            .assertIsOff()
            .performClick()
            .assertIsOn()

        androidComposeTestRule
            .onNodeWithTag(CardRollTestTag.ROLL_MODIFY_SCORE_VALUE)
            .performScrollTo()
            .assertIsDisplayed()

        // --- Actions ---
        androidComposeTestRule
            .onNodeWithTag(ButtonFeatureTestTag.BUTTON_FEATURE_SAVE)
            .assertIsEnabled()
            .performClick()

        // Dialog should be closed now.
        // The title in ZoomBag should have changed
        val updatedDiceTitleTag = "${ZoomDiceTestTag.ZOOM_DICE_TITLE}-d2-updated"
        waitForTag(updatedDiceTitleTag)
        androidComposeTestRule
            .onNodeWithTag(updatedDiceTitleTag)
            .assertIsDisplayed()
    }

    @Test
    fun dialogDiceCloneDelete() {
        val diceSideTag = "${ZoomDiceTestTag.ZOOM_SIDE_IMAGE_SHAPE}-d2-1"
        waitForTag(diceSideTag)

        // Open DialogDice
        assertClick(diceSideTag)

        waitForTag(CardDiceTestTag.DICE_TITLE)

        // Clone requires a unique title that is NOT the same as the original
        androidComposeTestRule
            .onNodeWithTag(CardDiceTestTag.DICE_TITLE)
            .performTextClearance()

        androidComposeTestRule
            .onNodeWithTag(CardDiceTestTag.DICE_TITLE)
            .performTextInput("d2-cloned")

        androidComposeTestRule
            .onNodeWithTag(ButtonFeatureTestTag.BUTTON_FEATURE_CLONE)
            .assertIsEnabled()
            .performClick()

        // Verify clone exists
        val clonedDiceTitleTag = "${ZoomDiceTestTag.ZOOM_DICE_TITLE}-d2-cloned"
        waitForTag(clonedDiceTitleTag)
        androidComposeTestRule
            .onNodeWithTag(clonedDiceTitleTag)
            .assertIsDisplayed()

        // Delete the clone
        val clonedDiceSideTag = "${ZoomDiceTestTag.ZOOM_SIDE_IMAGE_SHAPE}-d2-cloned-1"
        waitForTag(clonedDiceSideTag)
        assertClick(clonedDiceSideTag)

        waitForTag(ButtonFeatureTestTag.BUTTON_FEATURE_DELETE)
        assertClick(ButtonFeatureTestTag.BUTTON_FEATURE_DELETE)

        // Confirm delete in DialogConfirm
        waitForTag(DialogConfirmTestTag.OK)
        assertClick(DialogConfirmTestTag.OK)

        // Verify clone is gone
        androidComposeTestRule.waitUntil(timeoutMillis = defaultTimeout) {
            androidComposeTestRule
                .onAllNodesWithTag(clonedDiceTitleTag)
                .fetchSemanticsNodes().isEmpty()
        }
        androidComposeTestRule
            .onNodeWithTag(clonedDiceTitleTag)
            .assertDoesNotExist()
    }
}
