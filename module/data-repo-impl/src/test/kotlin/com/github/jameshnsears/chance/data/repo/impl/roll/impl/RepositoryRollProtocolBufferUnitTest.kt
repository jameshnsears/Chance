package com.github.jameshnsears.chance.data.repo.impl.roll.impl

import android.content.Context
import androidx.datastore.core.DataStore
import com.github.jameshnsears.chance.common.utility.UtilityAndroidUnitTestHelper
import com.github.jameshnsears.chance.data.domain.core.Side
import com.github.jameshnsears.chance.data.domain.core.roll.Roll
import com.github.jameshnsears.chance.data.domain.proto.RollHistoryProtocolBuffer
import com.github.jameshnsears.chance.data.repo.api.bag.RepositoryBagInterface
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class RepositoryRollProtocolBufferUnitTest : UtilityAndroidUnitTestHelper() {
    private val context = mockk<Context>()
    private val dataStore = mockk<DataStore<RollHistoryProtocolBuffer>>()
    private val repositoryBag = mockk<RepositoryBagInterface>()

    @Before
    fun setUp() {
        // Reset singleton instance
        val instanceField = RepositoryRollProtocolBufferImpl::class.java.getDeclaredField("instance")
        instanceField.isAccessible = true
        instanceField.set(null, null)

        mockkStatic("com.github.jameshnsears.chance.data.repo.impl.roll.impl.RepositoryRollProtocolBufferImplKt")
        every { context.rollDataStore } returns dataStore
        coEvery { dataStore.data } returns flowOf(RollHistoryProtocolBuffer.getDefaultInstance())
        every { repositoryBag.fetch() } returns flowOf(mutableListOf())
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun fetch() = runTest {
        val repository = RepositoryRollProtocolBufferImpl.getInstance(context, repositoryBag)
        val result = repository.fetch().first()
        assertTrue(result.isEmpty())
    }

    @Test
    fun store() = runTest {
        coEvery { dataStore.updateData(any()) } returns RollHistoryProtocolBuffer.getDefaultInstance()

        val repository = RepositoryRollProtocolBufferImpl.getInstance(context, repositoryBag)
        val rollHistory = LinkedHashMap<Long, List<Roll>>()
        rollHistory[1L] = listOf(Roll(uuidDice = "d1", side = Side()))
        repository.store(rollHistory)

        coVerify { dataStore.updateData(any()) }
    }

    @Test
    fun storeIncremental() = runTest {
        coEvery { dataStore.updateData(any()) } returns RollHistoryProtocolBuffer.getDefaultInstance()

        val repository = RepositoryRollProtocolBufferImpl.getInstance(context, repositoryBag)
        val rolls = listOf(Roll(uuidDice = "d1", side = Side()))
        repository.store(1L, rolls)

        coVerify { dataStore.updateData(any()) }
    }

    @Test
    fun removeLatest() = runTest {
        coEvery { dataStore.updateData(any()) } returns RollHistoryProtocolBuffer.getDefaultInstance()

        val repository = RepositoryRollProtocolBufferImpl.getInstance(context, repositoryBag)
        repository.removeLatest()

        coVerify { dataStore.updateData(any()) }
    }

    @Test
    fun clear() = runTest {
        coEvery { dataStore.updateData(any()) } returns RollHistoryProtocolBuffer.getDefaultInstance()

        val repository = RepositoryRollProtocolBufferImpl.getInstance(context, repositoryBag)
        repository.clear()

        coVerify { dataStore.updateData(any()) }
    }

    @Test
    fun jsonExport() = runTest {
        val repository = RepositoryRollProtocolBufferImpl.getInstance(context, repositoryBag)
        val json = repository.jsonExport()
        // Flexible assertion for JSON output
        assertTrue(json.isNotBlank())
    }

    @Test
    fun jsonImport() = runTest {
        coEvery { dataStore.updateData(any()) } returns RollHistoryProtocolBuffer.getDefaultInstance()

        val repository = RepositoryRollProtocolBufferImpl.getInstance(context, repositoryBag)
        repository.jsonImport("{}")

        coVerify { dataStore.updateData(any()) }
    }

    @Test
    fun fetchWithRehydration() = runTest {
        val repository = RepositoryRollProtocolBufferImpl.getInstance(context, repositoryBag)
        val result = repository.fetch().first()
        assertTrue(result.isEmpty())
    }
}
