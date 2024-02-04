package com.github.jameshnsears.chance.data.repository.bag

import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals

class BagRepositoryUnitTest {
    @Test
    fun storeAndFetchFromRepository() = runTest {
        val bagRepository = DiceBagRepositoryTestDouble.getInstance()

        val diceBag = BagSampleData.allDice

        bagRepository.store(diceBag)

        val fetchedBag = bagRepository.fetch()
        assertEquals(diceBag, fetchedBag)
        assertEquals(diceBag[0].sides.size, fetchedBag[0].sides.size)
        assertEquals(diceBag[6].sides.size, fetchedBag[6].sides.size)

        val fetchedDice = bagRepository.fetch(diceBag[3].epoch)
        assertEquals(diceBag[3], fetchedDice)
    }
}
