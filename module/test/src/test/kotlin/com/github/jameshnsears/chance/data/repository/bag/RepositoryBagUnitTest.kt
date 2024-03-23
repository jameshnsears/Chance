package com.github.jameshnsears.chance.data.repository.bag

import com.github.jameshnsears.chance.data.repository.bag.mock.RepositoryBagTestDouble
import com.github.jameshnsears.chance.data.sample.bag.SampleBag
import com.github.jameshnsears.chance.utility.android.UtilityAndroid
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals

class RepositoryBagUnitTest : UtilityAndroid() {
    @Test
    fun storeAndFetch() = runTest {
        val repositoryBag = RepositoryBagTestDouble.getInstance()

        val diceBag = SampleBag.allDice

        repositoryBag.store(diceBag)

        val fetchedBag = repositoryBag.fetch().first()
        assertEquals(diceBag, fetchedBag)
        assertEquals(diceBag[0].sides.size, fetchedBag[0].sides.size)
        assertEquals(diceBag[6].sides.size, fetchedBag[6].sides.size)

        val fetchedDice = repositoryBag.fetch(diceBag[3].epoch).first()
        assertEquals(diceBag[3], fetchedDice)
    }
}
