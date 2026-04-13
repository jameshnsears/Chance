package com.github.jameshnsears.chance.data.repo.impl.bag

import androidx.test.platform.app.InstrumentationRegistry
import com.github.jameshnsears.chance.common.utility.UtilityLoggingHelper
import com.github.jameshnsears.chance.data.common.repository.RepositoryFactory
import com.github.jameshnsears.chance.data.domain.core.bag.testdouble.BagDataTestDouble
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class RepositoryBagTest : UtilityLoggingHelper() {
    @Test
    fun storeAndFetch() = runTest {
        val repositoryFactory =
            RepositoryFactory(InstrumentationRegistry.getInstrumentation().targetContext)
        val repositoryBagImpl = repositoryFactory.repositoryBag

        val bagDataTestDouble = BagDataTestDouble()

        val originalDiceBag = bagDataTestDouble.allDice

        repositoryBagImpl.store(originalDiceBag)

        val fetchedDiceBag = repositoryBagImpl.fetch().first()

        originalDiceBag.forEachIndexed { indexDice, originalDice ->
            val fetchedDice = fetchedDiceBag[indexDice]

            Assert.assertEquals(originalDice.epoch, fetchedDice.epoch)

            originalDice.sides.forEachIndexed { indexSide, originalSide ->
                Assert.assertEquals(originalSide.number, fetchedDice.sides[indexSide].number)
                Assert.assertEquals(originalSide.numberColour, fetchedDice.sides[indexSide].numberColour)
                Assert.assertEquals(originalSide.imageBase64, fetchedDice.sides[indexSide].imageBase64)
                Assert.assertEquals(
                    originalSide.imageDrawableId,
                    fetchedDice.sides[indexSide].imageDrawableId
                )
                Assert.assertEquals(originalSide.description, fetchedDice.sides[indexSide].description)
                Assert.assertEquals(
                    originalSide.descriptionColour,
                    fetchedDice.sides[indexSide].descriptionColour
                )
            }

            Assert.assertEquals(originalDice.title, fetchedDice.title)
            Assert.assertEquals(originalDice.colour, fetchedDice.colour)
            Assert.assertEquals(originalDice.selected, fetchedDice.selected)

            Assert.assertEquals(originalDice.multiplierValue, fetchedDice.multiplierValue)

            Assert.assertEquals(originalDice.explode, fetchedDice.explode)
            Assert.assertEquals(originalDice.explodeWhen, fetchedDice.explodeWhen)
            Assert.assertEquals(originalDice.explodeValue, fetchedDice.explodeValue)

            Assert.assertEquals(originalDice.modifyScore, fetchedDice.modifyScore)
            Assert.assertEquals(originalDice.modifyScoreValue, fetchedDice.modifyScoreValue)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun importAndExport() = runTest {
        val repositoryFactory =
            RepositoryFactory(InstrumentationRegistry.getInstrumentation().targetContext)
        val repositoryBagImpl = repositoryFactory.repositoryBag

        val bagDataTestDouble = BagDataTestDouble()

        val originalDieBag = bagDataTestDouble.allDice

        repositoryBagImpl.store(originalDieBag)

        val json = repositoryBagImpl.jsonExport()

        repositoryBagImpl.clear()

        repositoryBagImpl.jsonImport(json)

        advanceUntilIdle()

        Assert.assertEquals(json, repositoryBagImpl.jsonExport())

        advanceUntilIdle()

        val fetchedDiceBag = repositoryBagImpl.fetch().first()

        advanceUntilIdle()

        originalDieBag.forEachIndexed { indexDice, originalDice ->
            val fetchedDice = fetchedDiceBag[indexDice]
            Assert.assertTrue(originalDice.epoch == fetchedDice.epoch)
            Assert.assertTrue(originalDice.sides.size == fetchedDice.sides.size)
            Assert.assertTrue(originalDice.title == fetchedDice.title)
            Assert.assertTrue(originalDice.colour == fetchedDice.colour)
            Assert.assertTrue(originalDice.selected == fetchedDice.selected)

            Assert.assertTrue(originalDice.multiplierValue == fetchedDice.multiplierValue)

            Assert.assertTrue(originalDice.explode == fetchedDice.explode)
            Assert.assertTrue(originalDice.explodeWhen == fetchedDice.explodeWhen)
            Assert.assertTrue(originalDice.explodeValue == fetchedDice.explodeValue)

            Assert.assertTrue(originalDice.modifyScore == fetchedDice.modifyScore)
            Assert.assertTrue(originalDice.modifyScoreValue == fetchedDice.modifyScoreValue)
        }
    }
}
