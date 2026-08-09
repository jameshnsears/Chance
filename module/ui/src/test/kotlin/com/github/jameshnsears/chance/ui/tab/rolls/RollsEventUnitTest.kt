package com.github.jameshnsears.chance.ui.tab.rolls

import com.github.jameshnsears.chance.common.utility.UtilityAndroidUnitTestHelper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
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
        runCurrent()
        RollsEvent.emit()

        advanceUntilIdle()

        Assert.assertTrue(emittedValue > 0)
        collectorJob.cancel()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun emitAndCollectMultiple() = runTest {
        val emittedValues = mutableListOf<Long>()
        val collectorJob = launch {
            RollsEvent.sharedFlowTabRollEvent.collect {
                emittedValues.add(it)
            }
        }
        runCurrent()

        RollsEvent.emit()
        RollsEvent.emit()
        RollsEvent.emit()

        advanceUntilIdle()

        Assert.assertEquals(3, emittedValues.size)
        collectorJob.cancel()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun emitAndCollectMultipleCollectors() = runTest {
        var emittedValue1 = 0L
        var emittedValue2 = 0L

        val collectorJob1 = launch {
            RollsEvent.sharedFlowTabRollEvent.collect {
                emittedValue1 = it
            }
        }
        val collectorJob2 = launch {
            RollsEvent.sharedFlowTabRollEvent.collect {
                emittedValue2 = it
            }
        }
        runCurrent()

        RollsEvent.emit()

        advanceUntilIdle()

        Assert.assertTrue(emittedValue1 > 0)
        Assert.assertEquals(emittedValue1, emittedValue2)

        collectorJob1.cancel()
        collectorJob2.cancel()
    }
}
