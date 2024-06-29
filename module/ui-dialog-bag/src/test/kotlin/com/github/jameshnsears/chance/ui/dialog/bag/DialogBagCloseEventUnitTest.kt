package com.github.jameshnsears.chance.ui.dialog.bag

import com.github.jameshnsears.chance.utility.android.UtilityAndroidHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Test

class DialogBagCloseEventUnitTest : UtilityAndroidHelper() {
    @Test
    fun emitAndCollect() = runTest {
        val collectorJob = launch {
            DialogBagCloseEvent.sharedFlowDialogBagCloseEvent.collect {
                assertTrue(true)
            }
        }
        DialogBagCloseEvent.emit()

        delay(100)

        collectorJob.cancel()
    }
}
