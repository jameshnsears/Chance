package com.github.jameshnsears.chance.common.utility

import androidx.test.platform.app.InstrumentationRegistry
import com.github.jameshnsears.chance.common.utility.feature.UtilityFeature
import org.junit.BeforeClass
import timber.log.Timber

open class UtilityLoggingHelper {
    init {
        UtilityFeature.enabled = setOf(
            UtilityFeature.Flag.REPO_PROTOCOL_BUFFER_TEST_DOUBLE,
        )
    }

    val isCI: Boolean
        get() = InstrumentationRegistry.getArguments().getString("IN_CI") == "1"

    val defaultTimeout: Long
        get() = if (isCI) 20_000L else 5_000L

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
