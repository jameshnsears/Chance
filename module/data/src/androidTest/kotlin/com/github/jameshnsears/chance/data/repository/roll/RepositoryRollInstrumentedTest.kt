package com.github.jameshnsears.chance.data.repository.roll

import androidx.test.platform.app.InstrumentationRegistry
import com.github.jameshnsears.chance.data.repository.roll.impl.RepositoryRoll
import com.github.jameshnsears.chance.data.sample.bag.SampleBagTestData
import com.github.jameshnsears.chance.data.sample.roll.SampleRollTestData
import com.github.jameshnsears.chance.utility.logging.UtilityLoggingInstrumentedHelper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class RepositoryRollInstrumentedTest : UtilityLoggingInstrumentedHelper() {
    @Before
    fun emptyDataStore() = runTest {
        RepositoryRoll.getInstance(
            InstrumentationRegistry.getInstrumentation().targetContext
        ).clear()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun storeAndFetch() = runTest {
        val repositoryRoll = RepositoryRoll.getInstance(
            InstrumentationRegistry.getInstrumentation().targetContext
        )

        val sampleBagTestData = SampleBagTestData()
        val sampleRollTestData = SampleRollTestData(sampleBagTestData)

        val originalRollHistory = sampleRollTestData.rollHistory

        repositoryRoll.store(originalRollHistory)

        advanceUntilIdle()

        val fetchedRollHistory = repositoryRoll.fetch().first()

        advanceUntilIdle()

        assertTrue(originalRollHistory.size == fetchedRollHistory.size)

        originalRollHistory.forEach {
            val originalRollSequence = it.value
            val fetchedRollSequence = fetchedRollHistory[it.key]!!

            originalRollSequence.forEachIndexed { index, roll ->
                val fetchedRoll = fetchedRollSequence[index]

                assertEquals(roll.diceEpoch, fetchedRoll.diceEpoch)

                val originalSide = roll.side
                val fetchedSide = fetchedRoll.side
                assertEquals(originalSide.number, fetchedSide.number)
                assertEquals(originalSide.numberColour, fetchedSide.numberColour)
                assertEquals(originalSide.imageDrawableId, fetchedSide.imageDrawableId)
                assertEquals(originalSide.imageBase64, fetchedSide.imageBase64)
                assertEquals(originalSide.description, fetchedSide.description)
                assertEquals(originalSide.descriptionColour, fetchedSide.descriptionColour)

                assertEquals(roll.multiplierIndex, fetchedRoll.multiplierIndex)
                assertEquals(roll.explodeIndex, fetchedRoll.explodeIndex)
                assertEquals(roll.scoreAdjustment, fetchedRoll.scoreAdjustment)
                assertEquals(roll.score, fetchedRoll.score)
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun importAndExport() = runTest {
        val repositoryRoll = RepositoryRoll.getInstance(
            InstrumentationRegistry.getInstrumentation().targetContext
        )

        val sampleBagTestData = SampleBagTestData()
        val sampleRollTestData = SampleRollTestData(sampleBagTestData)

        val originalRollHistory = sampleRollTestData.rollHistory

        repositoryRoll.store(originalRollHistory)

        val originalRollHistoryJson = repositoryRoll.exportJson()

        repositoryRoll.clear()

        repositoryRoll.importJson(originalRollHistoryJson)

        advanceUntilIdle()

        val fetchedRollHistoryJson = repositoryRoll.exportJson()

        assertEquals(originalRollHistoryJson, fetchedRollHistoryJson)
    }
}
