package com.github.jameshnsears.chance.data.model

import com.github.jameshnsears.chance.data.domain.Dice
import com.github.jameshnsears.chance.data.domain.Side
import com.github.jameshnsears.chance.data.repository.dice.DiceRepositoryMock
import com.github.jameshnsears.chance.data.repository.dice.sample.DiceSampleData
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Order

class DiceModelUnitTest {
    private var d4 = Dice(
        sides = listOf(
            Side(defaultValue = 4),
            Side(defaultValue = 3),
            Side(defaultValue = 2),
            Side(defaultValue = 1)
        ),
        description = "d4",
    )

    @Order(1)
    @Test
    fun confirmDiceRepositoryWorks() {
        val diceModel = DiceModel(getDiceRepository())

        assertEquals(2, diceModel.fetchDice().size)
        assertEquals(6, diceModel.fetchSides(1).size)
        assertEquals(3, diceModel.fetchSide(1, 3).defaultValue)

        diceModel.store(listOf(d4))

        assertEquals(1, diceModel.fetchDice().size)
        assertEquals(4, diceModel.fetchDice(0).sides.size)
    }

    @Test
    fun indexBehaviour() {
        val diceModel = DiceModel(getDiceRepository())

        assertThrows(DiceIndexException::class.java) {
            diceModel.fetchDice(3)
        }

        assertThrows(SideIndexException::class.java) {
            diceModel.fetchSide(1, 7)
        }
    }

    private fun getDiceRepository(): DiceRepositoryMock {
        val diceRepository = DiceRepositoryMock
        diceRepository.store(DiceSampleData.twoDice)
        return diceRepository
    }

    @Test
    fun cloneDice() {
        val diceModel = DiceModel(getDiceRepository())

        d4.diceIndex = 3
        diceModel.cloneDice(d4)

        assertEquals(3, diceModel.fetchDice().size)
        assertEquals(4, diceModel.fetchDice(2).sides.size)
    }

    @Test
    fun canBeDeleted() {
        val diceModel = DiceModel(getDiceRepository())

        assertTrue(diceModel.canBeDeleted())

        diceModel.store(listOf(d4))
        assertFalse(diceModel.canBeDeleted())
    }
}