package com.github.jameshnsears.chance.ui.dialog.colour

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import com.github.jameshnsears.chance.common.ui.AndroidTestHelper
import com.github.jameshnsears.chance.common.ui.theme.ChanceTheme
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertFalse
import org.junit.Test

class DialogColourPickerTest : AndroidTestHelper() {
    @Test
    fun dialogDisplayed() {
        val showDialog = mutableStateOf(true)
        val dialogTitle = "Pick a Colour"
        val currentColour = "FFFF0000"
        val setColour: (String) -> Unit = mockk(relaxed = true)

        composeRule.setContent {
            ChanceTheme {
                DialogColourPicker(
                    showDialog = showDialog,
                    dialogTitle = dialogTitle,
                    currentColour = currentColour,
                    setColour = setColour
                )
            }
        }

        composeRule.onNodeWithText(dialogTitle).assertIsDisplayed()
        composeRule.onNodeWithTag(DialogColourTestTag.DIALOG_COLOUR_CANCEL).assertExists()
        composeRule.onNodeWithTag(DialogColourTestTag.DIALOG_COLOUR_OK).assertExists()
        composeRule.onNodeWithTag(DialogColourTestTag.DIALOG_COLOUR_HEX).assertExists()
    }

    @Test
    fun cancelClicked() {
        val showDialog = mutableStateOf(true)
        val dialogTitle = "Pick a Colour"
        val currentColour = "FFFF0000"
        val setColour: (String) -> Unit = mockk(relaxed = true)

        composeRule.setContent {
            ChanceTheme {
                DialogColourPicker(
                    showDialog = showDialog,
                    dialogTitle = dialogTitle,
                    currentColour = currentColour,
                    setColour = setColour
                )
            }
        }

        composeRule.onNodeWithTag(DialogColourTestTag.DIALOG_COLOUR_CANCEL)
            .performScrollTo()
            .performClick()

        assertFalse(showDialog.value)
        verify(exactly = 0) { setColour(any()) }
    }

    @Test
    fun okClicked() {
        val showDialog = mutableStateOf(true)
        val dialogTitle = "Pick a Colour"
        val currentColour = "FFFF0000"
        val setColour: (String) -> Unit = mockk(relaxed = true)

        composeRule.setContent {
            ChanceTheme {
                DialogColourPicker(
                    showDialog = showDialog,
                    dialogTitle = dialogTitle,
                    currentColour = currentColour,
                    setColour = setColour
                )
            }
        }

        composeRule.onNodeWithTag(DialogColourTestTag.DIALOG_COLOUR_OK)
            .performScrollTo()
            .performClick()

        assertFalse(showDialog.value)
        verify(exactly = 1) { setColour(any()) }
    }
}
