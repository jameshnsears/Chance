package com.github.jameshnsears.chance.ui.tab.bag

import com.github.jameshnsears.chance.common.utility.UtilityAndroidUnitTestHelper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test


class TabBagImportEventUnitTest : UtilityAndroidUnitTestHelper() {
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun emitAndCollect() = runTest {
        val collectorJob = launch {
            BagImportEvent.sharedFlowTabBagImportEvent.collect {
                Assert.assertTrue(true)
            }
        }
        BagImportEvent.emit()

        advanceUntilIdle()

        collectorJob.cancel()
    }
}
