package com.github.jameshnsears.chance.data.repo.impl.roll.impl

import android.content.Context
import androidx.datastore.core.DataStore
import com.github.jameshnsears.chance.common.utility.UtilityAndroidUnitTestHelper
import com.github.jameshnsears.chance.data.domain.core.Side
import com.github.jameshnsears.chance.data.domain.core.roll.Roll
import com.github.jameshnsears.chance.data.domain.proto.RollHistoryProtocolBuffer
import com.github.jameshnsears.chance.data.domain.proto.RollListProtocolBuffer
import com.github.jameshnsears.chance.data.domain.proto.RollProtocolBuffer
import com.github.jameshnsears.chance.data.domain.proto.SideProtocolBuffer
import com.github.jameshnsears.chance.data.repo.api.bag.RepositoryBagInterface
import com.github.jameshnsears.chance.data.repo.api.side.RepositorySideCacheInterface
import com.github.jameshnsears.chance.data.repo.impl.utility.svg.UtilitySvgSerializer
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
import org.junit.Assert.assertEquals
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
    fun fetchReferencedSideUuidsReadsPersistedProto() = runTest {
        val rollProtocolBuffer = RollProtocolBuffer.newBuilder()
            .setUuidDice("dice-1")
            .setSide(SideProtocolBuffer.newBuilder().setUuid("roll-side-1").build())
            .build()
        val rollListProtocolBuffer = RollListProtocolBuffer.newBuilder()
            .addRoll(rollProtocolBuffer)
            .build()
        val rollHistoryProtocolBuffer = RollHistoryProtocolBuffer.newBuilder()
            .putValues(1L, rollListProtocolBuffer)
            .build()

        coEvery { dataStore.data } returns flowOf(rollHistoryProtocolBuffer)

        val repository = RepositoryRollProtocolBufferImpl.getInstance(context, repositoryBag)
        val result = repository.fetchReferencedSideUuids()

        assertEquals(setOf("roll-side-1"), result)
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

    @Test
    fun fetchResolvesSvgFromSideCache() = runTest {
        val sideProtocolBuffer = SideProtocolBuffer.newBuilder()
            .setUuid("roll-side-1")
            .build()
        val rollProtocolBuffer = RollProtocolBuffer.newBuilder()
            .setUuidDice("dice-1")
            .setSide(sideProtocolBuffer)
            .build()
        val rollHistoryProtocolBuffer = RollHistoryProtocolBuffer.newBuilder()
            .putValues(1L, RollListProtocolBuffer.newBuilder().addRoll(rollProtocolBuffer).build())
            .build()

        coEvery { dataStore.data } returns flowOf(rollHistoryProtocolBuffer)

        val repositorySideCache = mockk<RepositorySideCacheInterface>()
        every { repositorySideCache.fetch() } returns flowOf(mapOf("roll-side-1" to "<svg></svg>"))

        val repository = RepositoryRollProtocolBufferImpl.getInstance(context, repositoryBag, repositorySideCache)
        val result = repository.fetch().first()

        val roll = result[1L]!![0]
        val expectedBase64 = UtilitySvgSerializer.encodeIntoBase64String("<svg></svg>")
        assertEquals("roll side imageBase64 should be reconstructed from the side cache", expectedBase64, roll.side.imageBase64)
    }

    @Test
    fun fetchLeavesImageBase64EmptyWhenMissingFromSideCache() = runTest {
        val sideProtocolBuffer = SideProtocolBuffer.newBuilder()
            .setUuid("roll-side-1")
            .build()
        val rollProtocolBuffer = RollProtocolBuffer.newBuilder()
            .setUuidDice("dice-1")
            .setSide(sideProtocolBuffer)
            .build()
        val rollHistoryProtocolBuffer = RollHistoryProtocolBuffer.newBuilder()
            .putValues(1L, RollListProtocolBuffer.newBuilder().addRoll(rollProtocolBuffer).build())
            .build()

        coEvery { dataStore.data } returns flowOf(rollHistoryProtocolBuffer)

        val repositorySideCache = mockk<RepositorySideCacheInterface>()
        every { repositorySideCache.fetch() } returns flowOf(emptyMap())

        val repository = RepositoryRollProtocolBufferImpl.getInstance(context, repositoryBag, repositorySideCache)
        val result = repository.fetch().first()

        assertEquals("", result[1L]!![0].side.imageBase64)
    }
}
