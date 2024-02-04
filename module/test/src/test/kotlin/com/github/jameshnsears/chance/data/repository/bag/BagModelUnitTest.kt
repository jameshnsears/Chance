package com.github.jameshnsears.chance.data.repository.bag

import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertTrue

class BagModelUnitTest {
    private val bagRepository = DiceBagRepositoryTestDouble.getInstance()

    @Test
    fun deleteDiceFromBagHoldingOneDiceUsingBagModel() = runTest {
        bagRepository.store(listOf(BagDemoSampleData.diceHeadsTails))
        assertEquals(1, bagRepository.fetch().size)

        val bagModel = BagModel(bagRepository)
        bagModel.delete(bagRepository.fetch()[0])

        assertEquals(1, bagRepository.fetch().size)
    }

    @Test
    fun deleteDiceFromBagHoldingTwoDiceUsingBagModel() = runTest {
        bagRepository.store(BagDemoSampleData.allDice)
        assertEquals(2, bagRepository.fetch().size)

        val bagModel = BagModel(bagRepository)
        val diceToDelete = bagRepository.fetch()[0]
        bagModel.delete(diceToDelete)

        assertEquals(1, bagRepository.fetch().size)
        assertNotEquals(diceToDelete, bagRepository.fetch()[0])
    }

    @Test
    fun saveDiceUsingBagModel() = runTest {
        val originalDice = BagDemoSampleData.diceHeadsTails
        bagRepository.store(listOf(originalDice))

        val bagModel = BagModel(bagRepository)
        val newColour = "FF000000"
        originalDice.colour = newColour
        bagModel.save(originalDice)

        assertEquals(1, bagRepository.fetch().size)
        assertEquals(newColour, bagRepository.fetch()[0].colour)
    }

    @Test
    fun cloneDiceUsingBagModel() = runTest {
        bagRepository.store(listOf(BagDemoSampleData.diceHeadsTails))

        assertEquals(1, bagRepository.fetch().size)

        val originalDice = bagRepository.fetch()[0]

        val bagModel = BagModel(bagRepository)

        bagModel.clone(originalDice)

        assertEquals(2, bagRepository.fetch().size)

        val clonedDice = bagRepository.fetch()[1]

        assertNotEquals(originalDice, clonedDice)
        assertTrue(clonedDice.epoch > originalDice.epoch)
        assertTrue(clonedDice.title == originalDice.title)
    }

    @Test
    fun canDiceBeDeletedUsingBagModel() = runTest {
        bagRepository.store(BagSampleData.allDice)

        val bagModel = BagModel(bagRepository)

        assertTrue(bagModel.canBeDeleted())

        bagRepository.store(
            listOf(
                BagDemoSampleData.diceHeadsTails,
            ),
        )

        assertFalse(bagModel.canBeDeleted())
    }
}
