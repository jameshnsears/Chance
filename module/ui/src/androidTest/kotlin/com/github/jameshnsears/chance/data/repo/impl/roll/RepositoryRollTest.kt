package com.github.jameshnsears.chance.data.repo.impl.roll

import androidx.test.platform.app.InstrumentationRegistry
import com.github.jameshnsears.chance.common.utility.UtilityLoggingHelper
import com.github.jameshnsears.chance.data.common.repository.RepositoryFactory
import com.github.jameshnsears.chance.data.domain.core.bag.testdouble.BagDataTestDouble
import com.github.jameshnsears.chance.data.domain.core.roll.testdouble.RollHistoryDataTestDouble
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class RepositoryRollTest : UtilityLoggingHelper() {
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun storeAndFetch() = runTest {
        val repositoryFactory =
            RepositoryFactory(InstrumentationRegistry.getInstrumentation().targetContext)
        val repositoryRollImpl = repositoryFactory.repositoryRoll

        val bagDataTestDouble = BagDataTestDouble()
        val rollDataTestDouble = RollHistoryDataTestDouble(bagDataTestDouble)

        val originalRollHistory = rollDataTestDouble.rollHistory

        repositoryRollImpl.store(originalRollHistory)

        advanceUntilIdle()

        val fetchedRollHistory = repositoryRollImpl.fetch().first()

        advanceUntilIdle()

        Assert.assertTrue(originalRollHistory.size == fetchedRollHistory.size)

        originalRollHistory.forEach {
            val originalRollSequence = it.value
            val fetchedRollSequence = fetchedRollHistory[it.key]!!

            originalRollSequence.forEachIndexed { index, roll ->
                val fetchedRoll = fetchedRollSequence[index]

                Assert.assertEquals(roll.diceEpoch, fetchedRoll.diceEpoch)

                val originalSide = roll.side
                val fetchedSide = fetchedRoll.side
                Assert.assertEquals(originalSide.number, fetchedSide.number)
                Assert.assertEquals(originalSide.numberColour, fetchedSide.numberColour)
                Assert.assertEquals(originalSide.imageDrawableId, fetchedSide.imageDrawableId)
                Assert.assertEquals(originalSide.imageBase64, fetchedSide.imageBase64)
                Assert.assertEquals(originalSide.description, fetchedSide.description)
                Assert.assertEquals(originalSide.descriptionColour, fetchedSide.descriptionColour)

                Assert.assertEquals(roll.multiplierIndex, fetchedRoll.multiplierIndex)
                Assert.assertEquals(roll.explodeIndex, fetchedRoll.explodeIndex)
                Assert.assertEquals(roll.scoreAdjustment, fetchedRoll.scoreAdjustment)
                Assert.assertEquals(roll.score, fetchedRoll.score)
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun importAndExport() = runTest {
        val repositoryFactory =
            RepositoryFactory(InstrumentationRegistry.getInstrumentation().targetContext)
        val repositoryRollImpl = repositoryFactory.repositoryRoll

        val bagDataTestDouble = BagDataTestDouble()
        val rollDataTestDouble = RollHistoryDataTestDouble(bagDataTestDouble)

        val originalRollHistory = rollDataTestDouble.rollHistory

        repositoryRollImpl.store(originalRollHistory)

        val originalRollHistoryJson = repositoryRollImpl.jsonExport()

        repositoryRollImpl.clear()

        repositoryRollImpl.jsonImport(originalRollHistoryJson)

        advanceUntilIdle()

        val fetchedRollHistoryJson = repositoryRollImpl.jsonExport()

        Assert.assertEquals(originalRollHistoryJson, fetchedRollHistoryJson)
    }
}
