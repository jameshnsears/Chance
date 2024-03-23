package com.github.jameshnsears.chance.data.repository.bag

import androidx.test.platform.app.InstrumentationRegistry
import com.github.jameshnsears.chance.data.repository.bag.impl.RepositoryBag
import com.github.jameshnsears.chance.data.sample.bag.SampleBag
import com.github.jameshnsears.chance.utility.logging.UtilityLoggingInstrumentedHelper
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class RepositoryBagInstrumentedTest : UtilityLoggingInstrumentedHelper() {
    @Before
    fun emptyDataStore() = runTest {
        RepositoryBag.getInstance(
            InstrumentationRegistry.getInstrumentation().targetContext
        ).empty()
    }

    @Test
    fun storeAndFetch() = runTest {
        val repositoryBag = RepositoryBag.getInstance(
            InstrumentationRegistry.getInstrumentation().targetContext
        )

        val originalDiceBag = SampleBag.allDice

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
                    originalSide.descriptionStringsId,
                    fetchedDice.sides[indexSide].descriptionStringsId
                )
                assertEquals(
                    originalSide.descriptionColour,
                    fetchedDice.sides[indexSide].descriptionColour
                )
            }

            assertEquals(originalDice.title, fetchedDice.title)
            assertEquals(originalDice.titleStringsId, fetchedDice.titleStringsId)
            assertEquals(originalDice.colour, fetchedDice.colour)
            assertEquals(originalDice.selected, fetchedDice.selected)

            assertEquals(originalDice.multiplier, fetchedDice.multiplier)
            assertEquals(originalDice.multiplierValue, fetchedDice.multiplierValue)

            assertEquals(originalDice.explode, fetchedDice.explode)
            assertEquals(originalDice.explodeWhen, fetchedDice.explodeWhen)
            assertEquals(originalDice.explodeValue, fetchedDice.explodeValue)

            assertEquals(originalDice.modifyScore, fetchedDice.modifyScore)
            assertEquals(originalDice.modifyScoreValue, fetchedDice.modifyScoreValue)
        }
    }

    @Test
    fun importAndExport() = runTest {
        val repositoryBag = RepositoryBag.getInstance(
            InstrumentationRegistry.getInstrumentation().targetContext
        )

        val diceBag = SampleBag.allDice

        repositoryBag.store(diceBag)

        val json = repositoryBag.exportJson()

        repositoryBag.empty()

        repositoryBag.importJson(json)

        assertEquals(json, repositoryBag.exportJson())

        assertEquals(diceBag, repositoryBag.fetch().first())
    }
}
