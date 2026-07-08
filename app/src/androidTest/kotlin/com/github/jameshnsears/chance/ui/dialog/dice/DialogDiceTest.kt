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
        androidComposeTestRule.waitUntil(timeoutMillis = 10000) {
            androidComposeTestRule
                .onAllNodesWithTag("${ZoomDiceTestTag.ZOOM_SIDE_IMAGE_SHAPE}-d2-1")
                .fetchSemanticsNodes().size == 1
        }

        // Open DialogDice by clicking on a Dice side in TabBagDiceLayout
        androidComposeTestRule
            .onNodeWithTag("${ZoomDiceTestTag.ZOOM_SIDE_IMAGE_SHAPE}-d2-1")
            .performClick()

        // --- CardDice Tab ---
        androidComposeTestRule.waitUntil(timeoutMillis = 10000) {
            androidComposeTestRule
                .onAllNodesWithTag(DialogDiceTabTestTag.TAB_DICE)
                .fetchSemanticsNodes().isNotEmpty()
        }

        androidComposeTestRule
            .onNodeWithTag(DialogDiceTabTestTag.TAB_DICE)
            .assertIsDisplayed()
            .performClick()

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
        androidComposeTestRule
            .onNodeWithTag(DialogDiceTabTestTag.TAB_SIDE)
            .performClick()

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
        androidComposeTestRule
            .onNodeWithTag(DialogDiceTabTestTag.TAB_ROLL)
            .performClick()

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
        androidComposeTestRule.waitUntil(timeoutMillis = 10000) {
            androidComposeTestRule
                .onAllNodesWithTag("${ZoomDiceTestTag.ZOOM_DICE_TITLE}-d2-updated")
                .fetchSemanticsNodes().size == 1
        }

        androidComposeTestRule
            .onNodeWithTag("${ZoomDiceTestTag.ZOOM_DICE_TITLE}-d2-updated")
            .assertIsDisplayed()
    }

    @Test
    fun dialogDiceCloneDelete() {
        androidComposeTestRule.waitUntil(timeoutMillis = 10000) {
            androidComposeTestRule
                .onAllNodesWithTag("${ZoomDiceTestTag.ZOOM_SIDE_IMAGE_SHAPE}-d2-1")
                .fetchSemanticsNodes().size == 1
        }

        // Open DialogDice
        androidComposeTestRule
            .onNodeWithTag("${ZoomDiceTestTag.ZOOM_SIDE_IMAGE_SHAPE}-d2-1")
            .performClick()

        androidComposeTestRule.waitUntil(timeoutMillis = 10000) {
            androidComposeTestRule
                .onAllNodesWithTag(CardDiceTestTag.DICE_TITLE)
                .fetchSemanticsNodes().isNotEmpty()
        }

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
        androidComposeTestRule.waitUntil(timeoutMillis = 10000) {
            androidComposeTestRule
                .onAllNodesWithTag("${ZoomDiceTestTag.ZOOM_DICE_TITLE}-d2-cloned")
                .fetchSemanticsNodes().size == 1
        }

        androidComposeTestRule
            .onNodeWithTag("${ZoomDiceTestTag.ZOOM_DICE_TITLE}-d2-cloned")
            .assertIsDisplayed()

        // Delete the clone
        androidComposeTestRule.waitUntil(timeoutMillis = 10000) {
            androidComposeTestRule
                .onAllNodesWithTag("${ZoomDiceTestTag.ZOOM_SIDE_IMAGE_SHAPE}-d2-cloned-1")
                .fetchSemanticsNodes().size == 1
        }

        androidComposeTestRule
            .onNodeWithTag("${ZoomDiceTestTag.ZOOM_SIDE_IMAGE_SHAPE}-d2-cloned-1")
            .performClick()

        androidComposeTestRule.waitUntil(timeoutMillis = 10000) {
            androidComposeTestRule
                .onAllNodesWithTag(ButtonFeatureTestTag.BUTTON_FEATURE_DELETE)
                .fetchSemanticsNodes().isNotEmpty()
        }

        androidComposeTestRule
            .onNodeWithTag(ButtonFeatureTestTag.BUTTON_FEATURE_DELETE)
            .assertIsEnabled()
            .performClick()

        // Confirm delete in DialogConfirm
        androidComposeTestRule.waitUntil(timeoutMillis = 10000) {
            androidComposeTestRule
                .onAllNodesWithTag(DialogConfirmTestTag.OK)
                .fetchSemanticsNodes().isNotEmpty()
        }

        androidComposeTestRule
            .onNodeWithTag(DialogConfirmTestTag.OK)
            .performClick()

        // Verify clone is gone
        androidComposeTestRule.waitUntil(timeoutMillis = 10000) {
            androidComposeTestRule
                .onAllNodesWithTag("${ZoomDiceTestTag.ZOOM_DICE_TITLE}-d2-cloned")
                .fetchSemanticsNodes().isEmpty()
        }

        androidComposeTestRule
            .onNodeWithTag("${ZoomDiceTestTag.ZOOM_DICE_TITLE}-d2-cloned")
            .assertDoesNotExist()
    }
}
