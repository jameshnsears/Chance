package com.github.jameshnsears.chance.utils.logging

import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.BeforeClass
import org.junit.Rule
import timber.log.Timber

open class LoggingLineNumberTreeInstrumentedFeature {
    @get:Rule
    val composeTestRule = createComposeRule()

    companion object {
        @BeforeClass
        @JvmStatic
        fun beforeClass() {
            if (Timber.treeCount == 0) {
                Timber.plant(LoggingLineNumberTree())
            }
        }
    }
}
