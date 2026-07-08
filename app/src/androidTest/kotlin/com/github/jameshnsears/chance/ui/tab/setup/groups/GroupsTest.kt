package com.github.jameshnsears.chance.ui.tab.setup.groups

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.platform.app.InstrumentationRegistry
import com.github.jameshnsears.chance.TestSupport
import com.github.jameshnsears.chance.common.R
import com.github.jameshnsears.chance.data.common.repo.RepositoryFactory
import com.github.jameshnsears.chance.ui.dialog.dice.ButtonFeatureTestTag
import com.github.jameshnsears.chance.ui.dialog.dice.card.dice.CardDiceTestTag
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GroupsTest : TestSupport() {
    @Before
    fun setUp() {
        runBlocking {
            RepositoryFactory(InstrumentationRegistry.getInstrumentation().targetContext).resetStorage()
        }
    }

    @Test
    fun createGroup() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext

        // 0. Ensure Setup tab is selected (Primary Tab)
        androidComposeTestRule.onNodeWithText(context.getString(R.string.tab_setup)).performClick()

        // 1. Click on the groups tab (Secondary Tab)
        androidComposeTestRule.waitUntil(timeoutMillis = 10000) {
            androidComposeTestRule
                .onAllNodesWithText(context.getString(R.string.tab_setup_groups))
                .fetchSemanticsNodes().isNotEmpty()
        }
        androidComposeTestRule.onNodeWithText(context.getString(R.string.tab_setup_groups)).performClick()

        // Verify we see existing groups from test double (tg1, tg2)
        // Wait for them to appear as the collection might be async
        androidComposeTestRule.waitUntil(timeoutMillis = 10000) {
            androidComposeTestRule
                .onAllNodesWithText("tg1")
                .fetchSemanticsNodes().isNotEmpty()
        }

        // 2. Click on the fab
        androidComposeTestRule.onNodeWithContentDescription("+").performClick()

        // 3. Create a new group with the name g1
        androidComposeTestRule.onNodeWithTag(CardDiceTestTag.DICE_TITLE).performTextInput("g1")

        // 4. Select the first dice once
        androidComposeTestRule.onAllNodesWithContentDescription("Increase").onFirst().performClick()

        // 5. Click save
        androidComposeTestRule.onNodeWithTag(ButtonFeatureTestTag.BUTTON_FEATURE_SAVE)
            .assertIsEnabled()
            .performClick()

        // 6. Test the groups list contains the g1 group
        androidComposeTestRule.waitUntil(timeoutMillis = 20000) {
            androidComposeTestRule
                .onAllNodesWithText("g1")
                .fetchSemanticsNodes().isNotEmpty()
        }

        androidComposeTestRule.onNodeWithText("g1").assertIsDisplayed()
    }
}
