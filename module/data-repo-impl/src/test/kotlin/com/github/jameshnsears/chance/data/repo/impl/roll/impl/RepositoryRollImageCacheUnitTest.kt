package com.github.jameshnsears.chance.data.repo.impl.roll.impl

import android.content.Context
import androidx.datastore.core.DataStore
import com.github.jameshnsears.chance.common.utility.UtilityAndroidUnitTestHelper
import com.github.jameshnsears.chance.data.domain.core.Side
import com.github.jameshnsears.chance.data.domain.core.roll.Roll
import com.github.jameshnsears.chance.data.domain.proto.RollHistoryProtocolBuffer
import com.github.jameshnsears.chance.data.repo.impl.RepositoryProtocolBufferImageCache
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.slot
import io.mockk.unmockkAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class RepositoryRollImageCacheUnitTest : UtilityAndroidUnitTestHelper() {
    private val context = mockk<Context>()
    private val dataStore = mockk<DataStore<RollHistoryProtocolBuffer>>()
    private val IMAGE_BASE64 =
        "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mP8z8BQDwAEhQGAhKmMIQAAAABJRU5ErkJggg"

    @Before
    fun setUp() {
        val instanceField = RepositoryRollProtocolBufferImpl::class.java.getDeclaredField("instance")
        instanceField.isAccessible = true
        instanceField.set(null, null)

        mockkStatic("com.github.jameshnsears.chance.data.repo.impl.roll.impl.RepositoryRollProtocolBufferImplKt")
        every { context.rollDataStore } returns dataStore
        coEvery { dataStore.data } returns flowOf(RollHistoryProtocolBuffer.getDefaultInstance())
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun sideCaching() = runTest {
        val updateDataSlot = slot<suspend (RollHistoryProtocolBuffer) -> RollHistoryProtocolBuffer>()
        coEvery { dataStore.updateData(capture(updateDataSlot)) } coAnswers {
            updateDataSlot.captured.invoke(RollHistoryProtocolBuffer.getDefaultInstance())
        }

        val repository = RepositoryRollProtocolBufferImpl.getInstance(context)

        val side1 = Side(uuid = "s1", number = 6, imageBase64 = IMAGE_BASE64)
        val roll1 = Roll(uuidDice = "d1", side = side1)
        val roll2 = Roll(uuidDice = "d2", side = side1)

        repository.store(1L, listOf(roll1, roll2))

        val result = updateDataSlot.captured.invoke(RollHistoryProtocolBuffer.getDefaultInstance())

        // Check cache exists at -1L
        assertTrue(result.containsValues(RepositoryProtocolBufferImageCache.EPOCH_IMAGE_CACHE))
        val cache = result.getValuesOrThrow(RepositoryProtocolBufferImageCache.EPOCH_IMAGE_CACHE)
        assertEquals(1, cache.rollCount)
        assertEquals("s1", cache.getRoll(0).side.uuid)
        assertEquals(IMAGE_BASE64, cache.getRoll(0).side.imageBase64)
        assertEquals(6, cache.getRoll(0).side.number)

        // Check real rolls use minimal Side (only UUID)
        val rolls = result.getValuesOrThrow(1L)
        assertEquals(2, rolls.rollCount)
        assertEquals("s1", rolls.getRoll(0).side.uuid)
        assertEquals("", rolls.getRoll(0).side.imageBase64) // Should be empty in main history
        assertEquals(0, rolls.getRoll(0).side.number)      // Should be default in main history
    }

    @Test
    fun sideHydration() = runTest {
        val proto = RollHistoryProtocolBuffer.newBuilder()
            .putValues(
                RepositoryProtocolBufferImageCache.EPOCH_IMAGE_CACHE,
                com.github.jameshnsears.chance.data.domain.proto.RollListProtocolBuffer.newBuilder()
                    .addRoll(
                        com.github.jameshnsears.chance.data.domain.proto.RollProtocolBuffer.newBuilder()
                            .setSide(
                                com.github.jameshnsears.chance.data.domain.proto.SideProtocolBuffer.newBuilder()
                                    .setUuid("s1")
                                    .setNumber(6)
                                    .setImageBase64(IMAGE_BASE64)
                                    .build()
                            )
                            .build()
                    )
                    .build()
            )
            .putValues(
                1L, com.github.jameshnsears.chance.data.domain.proto.RollListProtocolBuffer.newBuilder()
                    .addRoll(
                        com.github.jameshnsears.chance.data.domain.proto.RollProtocolBuffer.newBuilder()
                            .setUuidDice("d1")
                            .setSide(
                                com.github.jameshnsears.chance.data.domain.proto.SideProtocolBuffer.newBuilder()
                                    .setUuid("s1")
                                    .build()
                            )
                            .build()
                    )
                    .build()
            )
            .build()

        coEvery { dataStore.data } returns flowOf(proto)

        val repository = RepositoryRollProtocolBufferImpl.getInstance(context)

        val result = repository.fetch().first()
        assertEquals(1, result.size)
        val hydratedRoll = result[1L]!![0]
        assertEquals("s1", hydratedRoll.side.uuid)
        assertEquals(6, hydratedRoll.side.number)
        assertEquals(IMAGE_BASE64, hydratedRoll.side.imageBase64)
        assertFalse(result.containsKey(RepositoryProtocolBufferImageCache.EPOCH_IMAGE_CACHE))
    }

    @Test
    fun cachePruning() = runTest {
        val repository = RepositoryRollProtocolBufferImpl.getInstance(context)
        val initialProto = RollHistoryProtocolBuffer.newBuilder()
            .putValues(
                RepositoryProtocolBufferImageCache.EPOCH_IMAGE_CACHE,
                com.github.jameshnsears.chance.data.domain.proto.RollListProtocolBuffer.newBuilder()
                    .addRoll(
                        com.github.jameshnsears.chance.data.domain.proto.RollProtocolBuffer.newBuilder()
                            .setSide(
                                com.github.jameshnsears.chance.data.domain.proto.SideProtocolBuffer.newBuilder()
                                    .setUuid("s1")
                                    .setImageBase64(IMAGE_BASE64)
                                    .build()
                            )
                            .build()
                    )
                    .build()
            )
            .putValues(
                1L, com.github.jameshnsears.chance.data.domain.proto.RollListProtocolBuffer.newBuilder()
                    .addRoll(
                        com.github.jameshnsears.chance.data.domain.proto.RollProtocolBuffer.newBuilder()
                            .setSide(
                                com.github.jameshnsears.chance.data.domain.proto.SideProtocolBuffer.newBuilder()
                                    .setUuid("s1")
                                    .build()
                            )
                            .build()
                    )
                    .build()
            )
            .build()

        val updateDataSlot = slot<suspend (RollHistoryProtocolBuffer) -> RollHistoryProtocolBuffer>()
        coEvery { dataStore.updateData(capture(updateDataSlot)) } coAnswers {
            updateDataSlot.captured.invoke(initialProto)
        }

        repository.removeLatest()

        val result = updateDataSlot.captured.invoke(initialProto)
        assertFalse(result.containsValues(1L))
        // Cache should be pruned because no more rolls reference "s1"
        assertFalse(result.containsValues(RepositoryProtocolBufferImageCache.EPOCH_IMAGE_CACHE))
    }
}
