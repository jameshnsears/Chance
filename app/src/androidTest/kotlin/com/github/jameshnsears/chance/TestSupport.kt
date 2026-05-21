package com.github.jameshnsears.chance

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipe
import androidx.test.platform.app.InstrumentationRegistry
import com.github.jameshnsears.chance.common.utility.UtilityLoggingHelper
import org.junit.Rule

open class TestSupport : UtilityLoggingHelper() {
    @get:Rule
    val androidComposeTestRule = createAndroidComposeRule<MainActivity>()

    fun displayBottomSheet(testTag: String) {
        androidComposeTestRule
            .onNodeWithTag(testTag)
            .performTouchInput {
                swipe(
                    start = topCenter,
                    end = Offset(centerX, -1000f),
                    durationMillis = 500
                )
            }

        // androidComposeTestRule.onRoot().printToLog("DEBUG_TAGS")
    }

    fun assertClick(testTag: String) {
        androidComposeTestRule
            .onNodeWithTag(testTag)
            .assertIsDisplayed()
            .performClick()
    }

    fun waitForGitHubCI(testTag: String) {
        // Give CI time to reflect the state change where Undo All becomes enabled
        androidComposeTestRule.waitUntil(timeoutMillis = 20000) {
            androidComposeTestRule
                .onAllNodesWithTag(testTag)
                .fetchSemanticsNodes()
                .any {
                    // If the Disabled property is missing, getOrElse returns false.
                    // We want the result to be true (enabled) when it is NOT disabled.
                    !it.config.contains(SemanticsProperties.Disabled)
                }
        }
    }

    val isCI: Boolean
        get() = InstrumentationRegistry.getArguments().getString("IN_CI") == "1"
}
