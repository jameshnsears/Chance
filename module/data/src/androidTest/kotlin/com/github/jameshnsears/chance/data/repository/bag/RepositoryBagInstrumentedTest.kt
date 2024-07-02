package com.github.jameshnsears.chance.data.repository.bag

import androidx.test.platform.app.InstrumentationRegistry
import com.github.jameshnsears.chance.data.repository.bag.impl.RepositoryBag
import com.github.jameshnsears.chance.data.sample.bag.SampleBagTestData
import com.github.jameshnsears.chance.utility.logging.UtilityLoggingInstrumentedHelper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class RepositoryBagInstrumentedTest : UtilityLoggingInstrumentedHelper() {
    @Before
    fun emptyDataStore() = runTest {
        RepositoryBag.getInstance(
            InstrumentationRegistry.getInstrumentation().targetContext
        ).clear()
    }

    @Test
    fun storeAndFetch() = runTest {
        val repositoryBag = RepositoryBag.getInstance(
            InstrumentationRegistry.getInstrumentation().targetContext
        )

        val sampleBagTestData = SampleBagTestData()

        val originalDiceBag = sampleBagTestData.allDice

        repositoryBag.store(originalDiceBag)

        val fetchedDiceBag = repositoryBag.fetch().first()

        originalDiceBag.forEachIndexed { indexDice, originalDice ->
            val fetchedDice = fetchedDiceBag[indexDice]

            assertEquals(originalDice.epoch, fetchedDice.epoch)

            originalDice.sides.forEachIndexed { indexSide, originalSide ->
                assertEquals(originalSide.number, fetchedDice.sides[indexSide].number)
                assertEquals(originalSide.numberColour, fetchedDice.sides[indexSide].numberColour)
                assertEquals(originalSide.imageBase64, fetchedDice.sides[indexSide].imageBase64)
                assertEquals(
                    originalSide.imageDrawableId,
                    fetchedDice.sides[indexSide].imageDrawableId
                )
                assertEquals(originalSide.description, fetchedDice.sides[indexSide].description)
                assertEquals(
                    originalSide.descriptionColour,
                    fetchedDice.sides[indexSide].descriptionColour
                )
            }

            assertEquals(originalDice.title, fetchedDice.title)
            assertEquals(originalDice.colour, fetchedDice.colour)
            assertEquals(originalDice.selected, fetchedDice.selected)

            assertEquals(originalDice.multiplierValue, fetchedDice.multiplierValue)

            assertEquals(originalDice.explode, fetchedDice.explode)
            assertEquals(originalDice.explodeWhen, fetchedDice.explodeWhen)
            assertEquals(originalDice.explodeValue, fetchedDice.explodeValue)

            assertEquals(originalDice.modifyScore, fetchedDice.modifyScore)
            assertEquals(originalDice.modifyScoreValue, fetchedDice.modifyScoreValue)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun importAndExport() = runTest {
        val repositoryBag = RepositoryBag.getInstance(
            InstrumentationRegistry.getInstrumentation().targetContext
        )

        val sampleBagTestData = SampleBagTestData()

        val originalDieBag = sampleBagTestData.allDice

        repositoryBag.store(originalDieBag)

        val json = repositoryBag.exportJson()

        repositoryBag.clear()

        repositoryBag.importJson(json)

        advanceUntilIdle()

        assertEquals(json, repositoryBag.exportJson())

        advanceUntilIdle()

        val fetchedDiceBag = repositoryBag.fetch().first()

        advanceUntilIdle()

        originalDieBag.forEachIndexed { indexDice, originalDice ->
            val fetchedDice = fetchedDiceBag[indexDice]
            assertTrue(originalDice.epoch == fetchedDice.epoch)
            assertTrue(originalDice.sides.size == fetchedDice.sides.size)
            assertTrue(originalDice.title == fetchedDice.title)
            assertTrue(originalDice.colour == fetchedDice.colour)
            assertTrue(originalDice.selected == fetchedDice.selected)

            assertTrue(originalDice.multiplierValue == fetchedDice.multiplierValue)

            assertTrue(originalDice.explode == fetchedDice.explode)
            assertTrue(originalDice.explodeWhen == fetchedDice.explodeWhen)
            assertTrue(originalDice.explodeValue == fetchedDice.explodeValue)

            assertTrue(originalDice.modifyScore == fetchedDice.modifyScore)
            assertTrue(originalDice.modifyScoreValue == fetchedDice.modifyScoreValue)
        }
    }
}
