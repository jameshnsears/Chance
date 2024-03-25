package com.github.jameshnsears.chance.ui.dialog.bag.card.dice

import com.github.jameshnsears.chance.utility.android.UtilityAndroid
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class CardDiceSideSizeEventUnitTest : UtilityAndroid() {
    @Test
    fun emitAndCollect() = runTest {
        val collectorJob = launch {
            CardDiceSideSizeEvent.sharedFlowDiceSidesSize.collect {
                assertEquals(2, it)
            }
        }
        CardDiceSideSizeEvent.emitSideSize(2)

        delay(100)

        collectorJob.cancel()
    }
}
