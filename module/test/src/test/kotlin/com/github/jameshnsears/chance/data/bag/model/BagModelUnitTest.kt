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
            Side(number = 4),
            Side(number = 3),
            Side(number = 2),
            Side(number = 1)
        ),
        title = "d4",
    )

    @Order(1)
    @Test
    fun confirmDiceRepositoryWorks() {
        val bagModel = BagModel(getDiceRepository())

        assertEquals(2, bagModel.dice().size)
        assertEquals(2, bagModel.sides(1).size)

        bagModel.store(listOf(d4))

        assertEquals(1, bagModel.dice().size)
        assertEquals(4, bagModel.dice(0).sides.size)
    }

    @Test
    fun indexBehaviour() {
        val bagModel = BagModel(getDiceRepository())

        assertThrows(BagModelException::class.java) {
            bagModel.dice(3)
        }

        assertThrows(BagModelException::class.java) {
            bagModel.side(1, 7)
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

        d4.epoch = 3
        bagModel.diceClone(d4)

        assertEquals(3, bagModel.dice().size)
        assertEquals(4, bagModel.dice(2).sides.size)
    }

    @Test
    fun canBeDeleted() {
        val bagModel = BagModel(getDiceRepository())

        assertTrue(bagModel.diceCanBeDeleted())

        bagModel.store(listOf(d4))
        assertFalse(bagModel.diceCanBeDeleted())
    }
}