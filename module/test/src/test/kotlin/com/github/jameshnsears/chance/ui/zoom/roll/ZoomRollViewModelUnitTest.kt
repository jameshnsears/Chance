package com.github.jameshnsears.chance.ui.zoom.roll

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.fail
import org.junit.Test

class ZoomRollViewModelUnitTest {
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun todo() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        try {
            fail("todo")
        } finally {
            Dispatchers.resetMain()
        }
    }
}
