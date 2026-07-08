package com.github.jameshnsears.chance.ui.dialog.dice

import com.github.jameshnsears.chance.common.utility.UtilityAndroidUnitTestHelper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class DialogDiceCloseEventUnitTest : UtilityAndroidUnitTestHelper() {
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun emitAndCollect() = runTest {
        val collectorJob = launch {
            DialogDiceCloseEvent.sharedFlowDialogBagCloseEvent.collect {
                Assert.assertTrue(true)
            }
        }
        DialogDiceCloseEvent.emit()

        advanceUntilIdle()

        collectorJob.cancel()
    }
}
