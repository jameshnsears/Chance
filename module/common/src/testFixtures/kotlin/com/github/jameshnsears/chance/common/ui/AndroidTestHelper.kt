package com.github.jameshnsears.chance.common.ui

import androidx.compose.ui.test.junit4.v2.createComposeRule
import com.github.jameshnsears.chance.common.utility.UtilityLoggingHelper
import org.junit.Rule

open class AndroidTestHelper : UtilityLoggingHelper() {
    @get:Rule
    val composeRule = createComposeRule()
}
