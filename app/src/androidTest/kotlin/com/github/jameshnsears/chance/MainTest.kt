package com.github.jameshnsears.chance

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.github.jameshnsears.chance.common.utility.UtilityLoggingHelper
import com.github.jameshnsears.chance.ui.R
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test


class MainTest : UtilityLoggingHelper() {
    @get:Rule
    val androidComposeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun startAppForFirstTime() = runTest {
        androidComposeTestRule
            .onNodeWithText(getString(R.string.tab_roll))
            .performClick()
    }
}
