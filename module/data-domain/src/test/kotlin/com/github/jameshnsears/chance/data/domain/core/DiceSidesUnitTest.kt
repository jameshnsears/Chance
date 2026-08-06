package com.github.jameshnsears.chance.data.domain.core

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class DiceSidesUnitTest {
    @Test
    fun diceSidesWithinRange() {
        val dice = Dice(sides = (1..DiceRollValues.SIDES_MIN).map { Side(number = it) })
        assertEquals(DiceRollValues.SIDES_MIN, dice.sides.size)

        val diceMax = Dice(sides = (1..DiceRollValues.SIDES_MAX).map { Side(number = it) })
        assertEquals(DiceRollValues.SIDES_MAX, diceMax.sides.size)
    }

    @Test
    fun diceSidesBoundsConstants() {
        assertEquals(2, DiceRollValues.SIDES_MIN)
        assertEquals(1000, DiceRollValues.SIDES_MAX)
    }

    @Test
    fun diceSidesValidation() {
        // This test documents the expected behavior that is currently enforced in the Repository layer
        // and UI layer, but could be moved to the domain model in the future.
        val validRange = DiceRollValues.SIDES_MIN..DiceRollValues.SIDES_MAX

        assertTrue(2 in validRange)
        assertTrue(1000 in validRange)
        assertTrue(1 !in validRange)
        assertTrue(1001 !in validRange)
    }
}
