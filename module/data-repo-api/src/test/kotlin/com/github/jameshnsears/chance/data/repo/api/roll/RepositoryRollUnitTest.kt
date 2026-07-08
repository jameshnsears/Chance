package com.github.jameshnsears.chance.data.repo.api.roll

import com.github.jameshnsears.chance.common.utility.UtilityAndroidUnitTestHelper
import com.github.jameshnsears.chance.data.domain.core.Side
import com.github.jameshnsears.chance.data.domain.core.roll.Roll
import com.github.jameshnsears.chance.data.domain.core.roll.RollHistory
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class RepositoryRollUnitTest : UtilityAndroidUnitTestHelper() {
    @Test
    fun fetch() = runTest {
        val mockRepository = mockk<RepositoryRollInterface>()
        val rollHistory: RollHistory = linkedMapOf(
            1L to listOf(Roll(uuidDice = "10", side = Side(number = 1)))
        )

        coEvery { mockRepository.fetch() } returns flowOf(rollHistory)

        val result = mockRepository.fetch().first()
        assertEquals(1, result.size)
        assertEquals("10", result[1L]!![0].uuidDice)
    }

    @Test
    fun store() = runTest {
        val mockRepository = mockk<RepositoryRollInterface>(relaxed = true)
        val rollHistory: RollHistory = linkedMapOf(
            1L to listOf(Roll(uuidDice = "10", side = Side(number = 1)))
        )

        mockRepository.store(rollHistory)

        coVerify { mockRepository.store(rollHistory) }
    }

    @Test
    fun traceUuid() {
        val repository = object : RepositoryRollInterface {
            override suspend fun fetch() = flowOf(linkedMapOf<Long, List<Roll>>())
            override suspend fun store(newRollHistory: RollHistory) {}
            override suspend fun jsonExport() = ""
            override suspend fun jsonImport(json: String) {}
            override suspend fun clear() {}
        }

        val rollHistory: RollHistory = linkedMapOf(
            1L to listOf(Roll(uuidDice = "10", side = Side(uuid = "side-uuid", number = 1)))
        )

        // Verifies no crash when calling default implementation
        repository.traceUuid(rollHistory)
    }
}
