package com.github.jameshnsears.chance.ui.group

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import com.github.jameshnsears.chance.common.ui.AndroidTestHelper
import com.github.jameshnsears.chance.common.ui.theme.ChanceTheme
import com.github.jameshnsears.chance.data.domain.core.Dice
import com.github.jameshnsears.chance.data.domain.core.group.Group
import com.github.jameshnsears.chance.ui.dialog.confirm.DialogConfirmTestTag
import com.github.jameshnsears.chance.ui.dialog.dice.ButtonFeatureTestTag
import com.github.jameshnsears.chance.ui.dialog.dice.card.dice.CardDiceTestTag
import org.junit.Test
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicReference

class GroupTest : AndroidTestHelper() {

    @Test
    fun groupExpansion() {
        val group = Group(name = "Test Group")

        composeRule.setContent {
            ChanceTheme {
                Box(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    Group(
                        group = group,
                        initiallyExpanded = false
                    )
                }
            }
        }

        // Initially collapsed
        composeRule.onNodeWithTag(GroupTestTag.NAME, useUnmergedTree = true).assertIsDisplayed()
        composeRule.onNodeWithTag(GroupTestTag.NOTES).assertDoesNotExist()

        // Click to expand
        composeRule.onNodeWithTag(GroupTestTag.EXPAND, useUnmergedTree = true).performClick()

        // Now expanded
        composeRule.onNodeWithTag(GroupTestTag.NOTES).assertIsDisplayed()

        // Click to collapse
        composeRule.onNodeWithTag(GroupTestTag.EXPAND, useUnmergedTree = true).performClick()

        // Now collapsed again
        composeRule.onNodeWithTag(GroupTestTag.NOTES).assertDoesNotExist()
    }

    @Test
    fun groupCallbacks() {
        val group = Group(name = "Initial Name", notes = "Initial Notes", uuidDice = listOf("d6-uuid"))
        val diceBag = listOf(Dice(title = "D6", uuid = "d6-uuid"))

        val nameChanged = AtomicReference<String>()
        val notesChanged = AtomicReference<String>()
        val deleteClicked = AtomicBoolean(false)
        val saveClicked = AtomicBoolean(false)
        val moveUpClicked = AtomicBoolean(false)
        val moveDownClicked = AtomicBoolean(false)
        val diceQuantityChanged = AtomicInteger(-1)

        composeRule.setContent {
            var groupDraftState by remember { mutableStateOf(group) }
            ChanceTheme {
                Box(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    Group(
                        group = group,
                        groupDraft = groupDraftState,
                        diceBag = diceBag,
                        initiallyExpanded = true,
                        onNameChange = {
                            nameChanged.set(it)
                            groupDraftState = groupDraftState.copy(name = it)
                        },
                        onNotesChange = {
                            notesChanged.set(it)
                            groupDraftState = groupDraftState.copy(notes = it)
                        },
                        onDelete = { deleteClicked.set(true) },
                        onSave = { saveClicked.set(true) },
                        onMoveUp = { moveUpClicked.set(true) },
                        onMoveDown = { moveDownClicked.set(true) },
                        onUuidDiceChange = { _, quantity -> diceQuantityChanged.set(quantity) }
                    )
                }
            }
        }

        // Test Name Change
        composeRule.onNodeWithTag(CardDiceTestTag.DICE_TITLE).performTextClearance()
        composeRule.onNodeWithTag(CardDiceTestTag.DICE_TITLE).performTextInput("New Name")
        assert(nameChanged.get() == "New Name")

        // Test Notes Change
        composeRule.onNodeWithTag(GroupTestTag.NOTES).performScrollTo().performTextClearance()
        composeRule.onNodeWithTag(GroupTestTag.NOTES).performTextInput("New Notes")
        assert(notesChanged.get() == "New Notes")

        // Test Dice Quantity Change
        composeRule.onAllNodesWithContentDescription("Increase")[0].performClick()
        assert(diceQuantityChanged.get() == 2)

        // Test Move Up/Down
        composeRule.onNodeWithTag(GroupTestTag.MOVE_UP, useUnmergedTree = true).performClick()
        assert(moveUpClicked.get())

        composeRule.onNodeWithTag(GroupTestTag.MOVE_DOWN, useUnmergedTree = true).performClick()
        assert(moveDownClicked.get())

        // Test Delete with Confirmation
        composeRule.onNodeWithTag(ButtonFeatureTestTag.BUTTON_FEATURE_DELETE).performScrollTo().performClick()

        // Check if confirmation dialog is shown by looking for its OK button
        composeRule.onNodeWithTag(DialogConfirmTestTag.OK).assertIsDisplayed()

        // Confirm delete
        composeRule.onNodeWithTag(DialogConfirmTestTag.OK).performClick()
        assert(deleteClicked.get())

        // Test Save
        composeRule.onNodeWithTag(ButtonFeatureTestTag.BUTTON_FEATURE_SAVE).performScrollTo().performClick()
        assert(saveClicked.get())

        // After save, it should be collapsed (based on Group.kt implementation: expanded = false)
        composeRule.onNodeWithTag(GroupTestTag.NOTES).assertDoesNotExist()
    }

    @Test
    fun groupMoveButtonsVisibilityProvided() {
        val group = Group(name = "Test Group")

        // Move buttons visible when callbacks are provided
        composeRule.setContent {
            ChanceTheme {
                Group(
                    group = group,
                    onMoveUp = {},
                    onMoveDown = {}
                )
            }
        }
        composeRule.onNodeWithTag(GroupTestTag.MOVE_UP, useUnmergedTree = true).assertIsDisplayed()
        composeRule.onNodeWithTag(GroupTestTag.MOVE_DOWN, useUnmergedTree = true).assertIsDisplayed()
    }

    @Test
    fun groupMoveButtonsVisibilityNotProvided() {
        val group = Group(name = "Test Group")

        // No move buttons when callbacks are null
        composeRule.setContent {
            ChanceTheme {
                Group(
                    group = group,
                    onMoveUp = null,
                    onMoveDown = null
                )
            }
        }
        composeRule.onNodeWithTag(GroupTestTag.MOVE_UP).assertDoesNotExist()
        composeRule.onNodeWithTag(GroupTestTag.MOVE_DOWN).assertDoesNotExist()
    }
}
