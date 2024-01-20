package com.github.jameshnsears.chance.data.repository.bag

import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Ignore
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.fail

class BagModelUnitTest {
    private val bagRepository = BagRepositoryTestDouble.getInstance()

    @Ignore
    @Test
    fun todo() = runTest {
        bagRepository.store(listOf(BagDemoSampleData.diceHeadsTails))
        assertEquals(1, bagRepository.fetch().size)

        val bagModel = BagModel(bagRepository)
        runBlocking {
            bagModel.diceUpdate(BagDemoSampleData.diceStory)
        }

        fail("todo")

        assertEquals(2, bagRepository.fetch().size)
    }

    @Test
    fun diceCanBeDeleted() = runTest {
        bagRepository.store(BagSampleData.allDice)

        val bagModel = BagModel(bagRepository)
        runBlocking {
            assertTrue(bagModel.diceCanBeDeleted())
        }

        bagRepository.store(
            listOf(
                BagDemoSampleData.diceHeadsTails
            )
        )
        runBlocking {
            assertFalse(bagModel.diceCanBeDeleted())
        }
    }
}