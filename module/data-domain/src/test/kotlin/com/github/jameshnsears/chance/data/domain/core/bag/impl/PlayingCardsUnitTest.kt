package com.github.jameshnsears.chance.data.domain.core.bag.impl

import org.junit.Assert.assertEquals
import org.junit.Test

class PlayingCardsUnitTest {
    @Test
    fun playingCardsHas54Sides() {
        val playingCards = PlayingCards().dice
        assertEquals(54, playingCards.sides.size)
    }

    @Test
    fun playingCardsNumbersAreDescending() {
        val playingCards = PlayingCards().dice
        assertEquals(54, playingCards.sides.first().number)
        assertEquals(1, playingCards.sides.last().number)
    }
}
