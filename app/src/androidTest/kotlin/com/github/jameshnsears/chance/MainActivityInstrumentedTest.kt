package com.github.jameshnsears.chance

import com.github.jameshnsears.chance.utility.logging.UtilityLoggingInstrumentedHelper
import org.junit.Assert.fail
import org.junit.Ignore
import org.junit.Test

open class MainActivityInstrumentedTest : UtilityLoggingInstrumentedHelper() {
    @Ignore
    @Test
    fun exportThenDeleteSomeDiceThenImport() {
        // TODO ensure we're using Startup samples!

        // TODO ensure that bag dialog delete and clone can be called via expresso
        fail("todo - when specific file imported then ui must match file contents, focus on settings: changing of tab, etc")
    }
}
