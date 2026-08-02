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
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class RepositoryRollStorageRefactorTest : UtilityAndroidUnitTestHelper() {
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
    fun verifyNewStorageBehavior() = runTest {
        val updateDataSlot = slot<suspend (RollHistoryProtocolBuffer) -> RollHistoryProtocolBuffer>()
        coEvery { dataStore.updateData(capture(updateDataSlot)) } coAnswers {
            updateDataSlot.captured.invoke(RollHistoryProtocolBuffer.getDefaultInstance())
        }

        val repository = RepositoryRollProtocolBufferImpl.getInstance(context)

        val side = Side(
            uuid = "side-uuid",
            number = 6,
            numberColour = "FF0000",
            imageBase64 = IMAGE_BASE64,
            imageDrawableId = 123,
            description = "side description",
            descriptionColour = "00FF00"
        )
        val roll = Roll(uuidDice = "dice-uuid", side = side)

        repository.store(1L, listOf(roll))

        val result = updateDataSlot.captured.invoke(RollHistoryProtocolBuffer.getDefaultInstance())

        // Roll itself should be minimal
        val rolls = result.getValuesOrThrow(1L)
        val rollProto = rolls.getRoll(0)
        val sideProto = rollProto.side

        assertEquals("side-uuid", sideProto.uuid)
        assertEquals(0, sideProto.number) // Default/empty
        assertEquals("", sideProto.imageBase64) // Default/empty

        // Details should be in cache
        val cache = result.getValuesOrThrow(RepositoryProtocolBufferImageCache.EPOCH_IMAGE_CACHE)
        val cachedSide = cache.getRoll(0).side

        assertEquals("side-uuid", cachedSide.uuid)
        assertEquals(6, cachedSide.number)
        assertEquals("FF0000", cachedSide.numberColour)
        assertEquals(123, cachedSide.imageDrawableId)
        assertEquals("side description", cachedSide.description)
        assertEquals("00FF00", cachedSide.descriptionColour)
        assertEquals(IMAGE_BASE64, cachedSide.imageBase64)
    }
}
