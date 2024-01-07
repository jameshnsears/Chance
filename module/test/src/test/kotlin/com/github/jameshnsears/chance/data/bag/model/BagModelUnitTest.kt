package com.github.jameshnsears.chance.data.bag.model

import com.github.jameshnsears.chance.data.bag.demo.BagDemoData
import com.github.jameshnsears.chance.data.bag.repository.BagRepositoryMock
import com.github.jameshnsears.chance.data.bag.sample.BagSampleData
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.fail

class BagModelUnitTest {
    private val bagRepository = BagRepositoryMock

    @Test
    fun diceUpdate() {
        bagRepository.store(listOf(BagDemoData.diceHeadsTails))
        assertEquals(1, bagRepository.fetch().size)

        val bagModel = BagModel(bagRepository)
        bagModel.diceUpdate(BagDemoData.diceStory)

        fail("todo")

        assertEquals(2, bagRepository.fetch().size)
    }

    @Test
    fun diceCanBeDeleted() {
        bagRepository.store(BagSampleData.allDice)

        val bagModel = BagModel(bagRepository)
        assertTrue(bagModel.diceCanBeDeleted())

        bagRepository.store(
            listOf(
                BagDemoData.diceHeadsTails
            )
        )
        assertFalse(bagModel.diceCanBeDeleted())
    }
}