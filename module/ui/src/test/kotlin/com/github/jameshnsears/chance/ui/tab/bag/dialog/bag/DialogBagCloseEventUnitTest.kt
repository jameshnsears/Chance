package com.github.jameshnsears.chance.ui.tab.bag.dialog.bag

import com.github.jameshnsears.chance.common.utility.UtilityAndroidUnitTestHelper
import com.github.jameshnsears.chance.ui.dialog.bag.DialogBagCloseEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class DialogBagCloseEventUnitTest : UtilityAndroidUnitTestHelper() {
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun emitAndCollect() = runTest {
        val collectorJob = launch {
            DialogBagCloseEvent.sharedFlowDialogBagCloseEvent.collect {
                Assert.assertTrue(true)
            }
        }
        DialogBagCloseEvent.emit()

        advanceUntilIdle()

        collectorJob.cancel()
    }
}
