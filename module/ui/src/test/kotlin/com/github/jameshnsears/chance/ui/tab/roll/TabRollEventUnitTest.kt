package com.github.jameshnsears.chance.ui.tab.roll

import com.github.jameshnsears.chance.common.utility.UtilityAndroidUnitTestHelper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class TabRollEventUnitTest : UtilityAndroidUnitTestHelper() {
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun emitAndCollect() = runTest {
        val collectorJob = launch {
            RollEvent.sharedFlowTabRollEvent.collect {
                Assert.assertTrue(true)
            }
        }
        RollEvent.emit()

        advanceUntilIdle()

        collectorJob.cancel()
    }
}
