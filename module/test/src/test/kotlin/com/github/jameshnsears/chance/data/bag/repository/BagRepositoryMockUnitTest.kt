package com.github.jameshnsears.chance.data.bag.repository

import com.github.jameshnsears.chance.data.bag.sample.BagSampleData
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals

class BagRepositoryMockUnitTest {
    @Test
    fun confirmRepositoryWorks() {
        val bagRepository = BagRepositoryMock
        bagRepository.store(BagSampleData.allDice)

        val bag = bagRepository.fetch()

        assertEquals(7, bag.size)
        assertEquals(2, bag[0].sides.size)
        assertEquals(20, bag[6].sides.size)
    }
}
