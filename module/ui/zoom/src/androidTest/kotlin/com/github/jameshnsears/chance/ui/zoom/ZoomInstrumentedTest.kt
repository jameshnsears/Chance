package com.github.jameshnsears.chance.ui.zoom

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ZoomInstrumentedTest {
    @Test
    fun zoomInstrumentedTest() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.github.jameshnsears.choice.ui.zoom.test", appContext.packageName)
    }
}