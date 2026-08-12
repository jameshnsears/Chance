package com.github.jameshnsears.chance

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import com.github.jameshnsears.chance.ui.tab.TabRowTestTag
import org.junit.Test

class StartupTest : TestSupport() {
    @Test
    fun appStarts() {
        waitForTag(TabRowTestTag.TAB_ROW)
        androidComposeTestRule
            .onNodeWithTag(TabRowTestTag.TAB_ROW)
            .assertIsDisplayed()
    }
}
