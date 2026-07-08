package com.github.jameshnsears.chance.ui.dialog.confirm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.github.jameshnsears.chance.common.ui.AndroidTestHelper
import com.github.jameshnsears.chance.common.ui.theme.ChanceTheme
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class DialogConfirmTest : AndroidTestHelper() {

    @Test
    fun dialogConfirmInteraction() {
        var openDialog by mutableStateOf(value = true)
        var confirmed = false
        var dismissed = false

        composeRule.setContent {
            ChanceTheme {
                DialogConfirm(
                    openDialog = openDialog,
                    onDismissRequest = {
                        openDialog = false
                        dismissed = true
                    },
                    onConfirmation = {
                        openDialog = false
                        confirmed = true
                    },
                    title = "Confirm Title",
                    text = "Confirm Text",
                )
            }
        }

        // Verify title and text
        composeRule.onNodeWithText("Confirm Title").assertIsDisplayed()
        composeRule.onNodeWithText("Confirm Text").assertIsDisplayed()

        // Test OK
        composeRule.onNodeWithTag(DialogConfirmTestTag.OK).performClick()
        assertTrue(confirmed)
        assertFalse(dismissed)
        assertFalse(openDialog)

        // Reset and test Cancel
        openDialog = true
        confirmed = false
        dismissed = false

        composeRule.onNodeWithTag(DialogConfirmTestTag.CANCEL).performClick()
        assertFalse(confirmed)
        assertTrue(dismissed)
        assertFalse(openDialog)
    }

    @Test
    fun dialogConfirmHidden() {
        composeRule.setContent {
            ChanceTheme {
                DialogConfirm(
                    openDialog = false,
                    onDismissRequest = {},
                    onConfirmation = {},
                    title = "Confirm Title",
                    text = "Confirm Text"
                )
            }
        }

        composeRule.onNodeWithText("Confirm Title").assertDoesNotExist()
    }
}
