package com.github.jameshnsears.chance

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import com.github.jameshnsears.chance.ui.tab.TabRowTestTag
import org.junit.Test

class StartupTest : TestSupport() {
    @Test
    fun appStarts() {
        /*
        if (BuildConfig.DEBUG) {
            getSharedPreferences("UtilitySharedPreferencesHelper", MODE_PRIVATE)
            .edit()
            .clear()
            .apply()
        }
         */

        androidComposeTestRule.waitForIdle()
        androidComposeTestRule
            .onNodeWithTag(TabRowTestTag.TAB_ROW)
            .assertIsDisplayed()
    }
}
