package com.github.jameshnsears.chance.data.repository.bag.testdouble

import com.github.jameshnsears.chance.data.domain.core.bag.testdouble.BagDataTestDouble
import com.github.jameshnsears.chance.utility.android.UtilityAndroidHelper
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals

class RepositoryBagTestDoubleUnitTest : UtilityAndroidHelper() {
    @Test
    fun storeAndFetch() = runTest {
        val diceBag = BagDataTestDouble().allDice

        val repositoryBag = RepositoryBagTestDouble.getInstance(diceBag)

        val fetchedBag = repositoryBag.fetch().first()
        assertEquals(diceBag, fetchedBag)
        assertEquals(diceBag[0].sides.size, fetchedBag[0].sides.size)
        assertEquals(diceBag[6].sides.size, fetchedBag[6].sides.size)

        val fetchedDice = repositoryBag.fetch(diceBag[3].epoch).first()
        assertEquals(diceBag[3], fetchedDice)
    }
}
