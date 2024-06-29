package com.github.jameshnsears.chance.ui.tab.roll

import com.github.jameshnsears.chance.utility.android.UtilityAndroidHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Test

class TabRollEventUnitTest : UtilityAndroidHelper() {
    @Test
    fun emitAndCollect() = runTest {
        val collectorJob = launch {
            TabRollEvent.sharedFlowTabRollEvent.collect {
                assertTrue(true)
            }
        }
        TabRollEvent.emit()

        delay(100)

        collectorJob.cancel()
    }
}
