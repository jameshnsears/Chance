package com.github.jameshnsears.chance.data.repo.api.bag

import com.github.jameshnsears.chance.common.utility.UtilityAndroidUnitTestHelper
import com.github.jameshnsears.chance.data.domain.core.Dice
import com.github.jameshnsears.chance.data.domain.core.Side
import com.github.jameshnsears.chance.data.domain.core.bag.DiceBag
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class RepositoryBagUnitTest : UtilityAndroidUnitTestHelper() {
    @Test
    fun fetchDiceBag() = runTest {
        val mockRepository = mockk<RepositoryBagInterface>()
        val diceBag: DiceBag = mutableListOf(Dice(title = "d6"))

        coEvery { mockRepository.fetch() } returns flowOf(diceBag)

        val result = mockRepository.fetch().first()
        assertEquals(1, result.size)
        assertEquals("d6", result[0].title)
    }

    @Test
    fun fetchDiceByEpoch() = runTest {
        val mockRepository = mockk<RepositoryBagInterface>()
        val epoch = 123456789L
        val dice = Dice(epoch = epoch, title = "d20")

        coEvery { mockRepository.fetch(epoch) } returns flowOf(dice)

        val result = mockRepository.fetch(epoch).first()
        assertEquals(epoch, result.epoch)
        assertEquals("d20", result.title)
    }

    @Test
    fun storeDiceBag() = runTest {
        val mockRepository = mockk<RepositoryBagInterface>(relaxed = true)
        val diceBag: DiceBag = mutableListOf(Dice(title = "d10"))

        mockRepository.store(diceBag)

        coVerify { mockRepository.store(diceBag) }
    }

    @Test
    fun traceUuid() {
        val repository = object : RepositoryBagInterface {
            override suspend fun fetch() = flowOf(mutableListOf<Dice>())
            override suspend fun fetch(epoch: Long) = flowOf(Dice())
            override suspend fun store(newDiceBag: DiceBag) {}
            override suspend fun jsonExport() = ""
            override suspend fun jsonImport(json: String) {}
            override suspend fun clear() {}
        }

        val side = Side(uuid = "side-uuid")
        val dice = Dice(uuid = "dice-uuid", sides = listOf(side))
        val diceBag: DiceBag = mutableListOf(dice)

        // Verifies no crash when calling default implementation
        repository.traceUuid(diceBag)
    }
}
