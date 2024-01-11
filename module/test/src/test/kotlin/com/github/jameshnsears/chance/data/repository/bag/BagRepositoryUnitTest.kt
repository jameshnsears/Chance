package com.github.jameshnsears.chance.data.repository.bag

import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals

class BagRepositoryUnitTest {
    @Test
    fun `store and fetch`() = runTest {
        val bagRepository = BagRepositoryTestDouble.getInstance()

        val diceBag = BagSampleData.allDice

        bagRepository.store(diceBag)

        val fetchedBag = bagRepository.fetch()

        assertEquals(diceBag, fetchedBag)
        assertEquals(diceBag[0].sides.size, fetchedBag[0].sides.size)
        assertEquals(diceBag[6].sides.size, fetchedBag[6].sides.size)
    }
}
