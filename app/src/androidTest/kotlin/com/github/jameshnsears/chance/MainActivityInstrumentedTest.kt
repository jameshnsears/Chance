package com.github.jameshnsears.chance

import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.github.jameshnsears.chance.ui.tab.compose.TabRowTestTag
import com.github.jameshnsears.chance.ui.tab.roll.compose.TabRollTestTag
import com.github.jameshnsears.chance.utility.logging.UtilityLoggingInstrumentedHelper
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test


class MainActivityInstrumentedTest : UtilityLoggingInstrumentedHelper() {
    @get:Rule
    val androidComposeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun startAppForFirstTime() = runTest {
        androidComposeTestRule
            .onNodeWithTag(TabRowTestTag.TAB_ROW)
            .assertExists()

        androidComposeTestRule
            .onNodeWithText(getString(com.github.jameshnsears.chance.ui.R.string.tab_roll))
            .performClick()

        androidComposeTestRule
            .onNodeWithTag(TabRollTestTag.UNDO)
            .assertIsEnabled()

        androidComposeTestRule
            .onNodeWithTag(TabRollTestTag.ROLL)
            .assertIsNotEnabled()
    }
}
