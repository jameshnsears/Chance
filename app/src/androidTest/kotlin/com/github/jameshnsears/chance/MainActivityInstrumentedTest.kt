package com.github.jameshnsears.chance

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.github.jameshnsears.chance.ui.tab.compose.TabRowTestTag
import com.github.jameshnsears.chance.utility.logging.UtilityLoggingInstrumentedHelper
import kotlinx.coroutines.test.runTest
import org.junit.Assert.fail
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test

class MainActivityInstrumentedTest : UtilityLoggingInstrumentedHelper() {
    @get:Rule
    val androidComposeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun displayAppForFirstTime() = runTest {
        androidComposeTestRule.onNodeWithTag(TabRowTestTag.TAB_ROW).assertExists()
    }

    @Ignore
    @Test
    fun importDiceBagThatInvalid() = runTest {
        fail("todo - check that appropriate toast is displayed")

        fail("todo - confirm correct # of dice in bag & that roll is empty")
    }

    @Ignore
    @Test
    fun importDiceBagThatValid() = runTest {
        fail("todo - check that appropriate toast is displayed")
    }
}
