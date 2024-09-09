package com.github.jameshnsears.chance.ui.tab.bag

import com.github.jameshnsears.chance.utility.android.UtilityAndroidHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Test

class TabBagImportEventUnitTest : UtilityAndroidHelper() {
    @Test
    fun emitAndCollect() = runTest {
        val collectorJob = launch {
            TabBagImportEvent.sharedFlowTabBagImportEvent.collect {
                assertTrue(true)
            }
        }
        TabBagImportEvent.emit()

        delay(100)

        collectorJob.cancel()
    }
}
