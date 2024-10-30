package com.github.jameshnsears.chance.utility.logging

import androidx.test.platform.app.InstrumentationRegistry
import com.github.jameshnsears.chance.utility.feature.UtilityFeature
import org.junit.BeforeClass
import timber.log.Timber

open class UtilityLoggingInstrumentedHelper {
    init {
        UtilityFeature.enabled = setOf(
            UtilityFeature.Flag.REPO_PROTOCOL_BUFFER_TEST_DOUBLE,
        )
    }

    companion object {
        @BeforeClass
        @JvmStatic
        fun beforeClass() {
            if (Timber.treeCount == 0) {
                Timber.plant(UtilityLoggingLineNumberTree())
            }
        }

        fun getString(id: Int) =
            InstrumentationRegistry.getInstrumentation().targetContext.getString(id)
    }
}
