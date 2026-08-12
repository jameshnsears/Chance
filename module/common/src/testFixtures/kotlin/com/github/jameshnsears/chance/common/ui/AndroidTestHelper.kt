package com.github.jameshnsears.chance.common.ui

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import com.github.jameshnsears.chance.common.utility.UtilityLoggingHelper
import org.junit.Rule

open class AndroidTestHelper : UtilityLoggingHelper() {
    @get:Rule
    val composeRule = createComposeRule()

    fun waitForTag(testTag: String) {
        composeRule.waitUntil(timeoutMillis = defaultTimeout) {
            composeRule
                .onAllNodesWithTag(testTag)
                .fetchSemanticsNodes()
                .isNotEmpty()
        }
    }

    fun waitForText(text: String) {
        composeRule.waitUntil(timeoutMillis = defaultTimeout) {
            composeRule
                .onAllNodesWithText(text)
                .fetchSemanticsNodes()
                .isNotEmpty()
        }
    }

    fun waitForSelected(testTag: String) {
        composeRule.waitUntil(timeoutMillis = defaultTimeout) {
            composeRule
                .onAllNodesWithTag(testTag)
                .fetchSemanticsNodes()
                .any {
                    it.config.getOrElse(SemanticsProperties.Selected) { false }
                }
        }
    }
}
