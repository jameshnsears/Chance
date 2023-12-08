package com.github.jameshnsears.chance.data.bag.model

import com.github.jameshnsears.chance.data.bag.repository.BagRepositoryMock
import com.github.jameshnsears.chance.data.bag.sample.BagSampleData
import com.github.jameshnsears.chance.data.domain.Dice
import com.github.jameshnsears.chance.data.domain.Side
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Order

class BagModelUnitTest {
    private var d4 = Dice(
        sides = listOf(
            Side(sideIndex = 4),
            Side(sideIndex = 3),
            Side(sideIndex = 2),
            Side(sideIndex = 1)
        ),
        description = "d4",
    )

    @Order(1)
    @Test
    fun confirmDiceRepositoryWorks() {
        val bagModel = BagModel(getDiceRepository())

        assertEquals(2, bagModel.fetchDice().size)
        assertEquals(6, bagModel.fetchSides(1).size)
        assertEquals(3, bagModel.fetchSide(1, 3).sideIndex)

        bagModel.store(listOf(d4))

        assertEquals(1, bagModel.fetchDice().size)
        assertEquals(4, bagModel.fetchDice(0).sides.size)
    }

    @Test
    fun indexBehaviour() {
        val bagModel = BagModel(getDiceRepository())

        assertThrows(BagModelIndexException::class.java) {
            bagModel.fetchDice(3)
        }

        assertThrows(BagModelIndexException::class.java) {
            bagModel.fetchSide(1, 7)
        }
    }

    private fun getDiceRepository(): BagRepositoryMock {
        val bagRepository = BagRepositoryMock
        bagRepository.store(BagSampleData.twoDice)
        return bagRepository
    }

    @Test
    fun cloneDice() {
        val bagModel = BagModel(getDiceRepository())

        d4.diceIndex = 3
        bagModel.cloneDice(d4)

        assertEquals(3, bagModel.fetchDice().size)
        assertEquals(4, bagModel.fetchDice(2).sides.size)
    }

    @Test
    fun canBeDeleted() {
        val bagModel = BagModel(getDiceRepository())

        assertTrue(bagModel.canBeDeleted())

        bagModel.store(listOf(d4))
        assertFalse(bagModel.canBeDeleted())
    }
}