package com.github.jameshnsears.chance.ui.tab.setup.dice

import com.github.jameshnsears.chance.common.utility.UtilityAndroidUnitTestHelper
import com.github.jameshnsears.chance.ui.tab.SetupImportEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test


class DiceImportEventUnitTest : UtilityAndroidUnitTestHelper() {
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun emitAndCollect() = runTest {
        val collectorJob = launch {
            SetupImportEvent.sharedFlowTabBagImportEvent.collect {
                Assert.assertTrue(true)
            }
        }
        SetupImportEvent.emit()

        advanceUntilIdle()

        collectorJob.cancel()
    }
}
