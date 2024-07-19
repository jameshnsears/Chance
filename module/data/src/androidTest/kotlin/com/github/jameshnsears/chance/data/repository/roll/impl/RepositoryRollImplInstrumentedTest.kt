package com.github.jameshnsears.chance.data.repository.roll.impl

import androidx.test.platform.app.InstrumentationRegistry
import com.github.jameshnsears.chance.data.domain.core.bag.testdouble.BagDataTestDouble
import com.github.jameshnsears.chance.data.domain.core.roll.testdouble.RollHistoryDataTestDouble
import com.github.jameshnsears.chance.data.repository.RepositoryInstrumentedHelper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class RepositoryRollImplInstrumentedTest : RepositoryInstrumentedHelper() {
    @Before
    fun emptyDataStore() = runTest {
        RepositoryRollImpl.getInstance(
            InstrumentationRegistry.getInstrumentation().targetContext
        ).clear()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun storeAndFetch() = runTest {
        val repositoryRollImpl = RepositoryRollImpl.getInstance(
            InstrumentationRegistry.getInstrumentation().targetContext
        )

        val bagDataTestDouble = BagDataTestDouble()
        val rollDataTestDouble = RollHistoryDataTestDouble(bagDataTestDouble)

        val originalRollHistory = rollDataTestDouble.rollHistory

        repositoryRollImpl.store(originalRollHistory)

        advanceUntilIdle()

        val fetchedRollHistory = repositoryRollImpl.fetch().first()

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
        val repositoryRollImpl = RepositoryRollImpl.getInstance(
            InstrumentationRegistry.getInstrumentation().targetContext
        )

        val bagDataTestDouble = BagDataTestDouble()
        val rollDataTestDouble = RollHistoryDataTestDouble(bagDataTestDouble)

        val originalRollHistory = rollDataTestDouble.rollHistory

        repositoryRollImpl.store(originalRollHistory)

        val originalRollHistoryJson = repositoryRollImpl.jsonExport()

        repositoryRollImpl.clear()

        repositoryRollImpl.jsonImport(originalRollHistoryJson)

        advanceUntilIdle()

        val fetchedRollHistoryJson = repositoryRollImpl.jsonExport()

        assertEquals(originalRollHistoryJson, fetchedRollHistoryJson)
    }
}
