package com.github.jameshnsears.chance.data.repo.impl.roll.testdouble

import com.github.jameshnsears.chance.data.domain.core.Side
import com.github.jameshnsears.chance.data.domain.core.roll.Roll
import com.github.jameshnsears.chance.data.repo.api.bag.RepositoryBagInterface
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class RepositoryRollProtocolBufferTestDoubleUnitTest {
    private val repositoryBag = mockk<RepositoryBagInterface>()

    @Before
    fun setUp() {
        // Reset singleton instance
        val instanceField = RepositoryRollProtocolBufferTestDouble::class.java.getDeclaredField("instance")
        instanceField.isAccessible = true
        instanceField.set(null, null)

        every { repositoryBag.fetch() } returns flowOf(mutableListOf())
    }

    @Test
    fun storeIncremental() = runTest {
        val repository = RepositoryRollProtocolBufferTestDouble.getInstance(LinkedHashMap(), repositoryBag)
        val rolls = listOf(Roll(uuidDice = "d1", side = Side()))
        repository.store(1L, rolls)
        val result = repository.fetch().first()
        assertEquals(1, result.size)
    }

    @Test
    fun removeLatest() = runTest {
        val repository = RepositoryRollProtocolBufferTestDouble.getInstance(LinkedHashMap(), repositoryBag)
        repository.store(1L, listOf(Roll(uuidDice = "d1", side = Side())))
        repository.removeLatest()
        val result = repository.fetch().first()
        assertTrue(result.isEmpty())
    }

    @Test
    fun jsonExport() = runTest {
        val repository = RepositoryRollProtocolBufferTestDouble.getInstance(LinkedHashMap(), repositoryBag)
        val json = repository.jsonExport()
        assertTrue(json.isNotBlank())
    }

    @Test
    fun jsonImport() = runTest {
        val repository = RepositoryRollProtocolBufferTestDouble.getInstance(LinkedHashMap(), repositoryBag)
        repository.jsonImport("{}")
        val result = repository.fetch().first()
        assertTrue(result.isEmpty())
    }
}
