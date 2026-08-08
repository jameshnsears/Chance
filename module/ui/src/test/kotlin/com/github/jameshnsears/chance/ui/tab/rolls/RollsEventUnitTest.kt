package com.github.jameshnsears.chance.ui.tab.rolls

import com.github.jameshnsears.chance.common.utility.UtilityAndroidUnitTestHelper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class RollsEventUnitTest : UtilityAndroidUnitTestHelper() {
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun emitAndCollect() = runTest {
        var emittedValue = 0L
        val collectorJob = launch {
            RollsEvent.sharedFlowTabRollEvent.collect {
                emittedValue = it
            }
        }
        RollsEvent.emit()

        advanceUntilIdle()

        Assert.assertTrue(emittedValue > 0)
        collectorJob.cancel()
    }
}
