package com.github.jameshnsears.chance.data.repo.api.bag

import com.github.jameshnsears.chance.common.utility.UtilityAndroidUnitTestHelper
import com.github.jameshnsears.chance.data.domain.core.Dice
import com.github.jameshnsears.chance.data.domain.core.Side
import com.github.jameshnsears.chance.data.domain.core.bag.DiceBag
import io.mockk.coVerify
import io.mockk.every
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

        every { mockRepository.fetch() } returns flowOf(diceBag)

        val result = mockRepository.fetch().first()
        assertEquals(1, result.size)
        assertEquals("d6", result[0].title)
    }

    @Test
    fun fetchDiceByUuid() = runTest {
        val mockRepository = mockk<RepositoryBagInterface>()
        val uuid = "dice-uuid"
        val dice = Dice(uuid = uuid, title = "d20")

        every { mockRepository.fetch(uuid) } returns flowOf(dice)

        val result = mockRepository.fetch(uuid).first()
        assertEquals(uuid, result.uuid)
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
            override fun fetch() = flowOf(mutableListOf<Dice>())
            override fun fetch(uuid: String) = flowOf(Dice())
            override suspend fun store(newDiceBag: DiceBag) {}
            override suspend fun jsonExport() = ""
            override suspend fun jsonImport(json: String) {}
            override suspend fun clear() {}
        }

        val side1 = Side(uuid = "side-uuid-1")
        val side2 = Side(uuid = "side-uuid-2")
        val dice = Dice(uuid = "dice-uuid", sides = listOf(side1, side2))
        val diceBag: DiceBag = mutableListOf(dice)

        // Verifies no crash when calling default implementation
        repository.traceUuid(diceBag)
    }
}
