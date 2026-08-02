package com.github.jameshnsears.chance.data.repo.impl.roll.impl

import android.content.Context
import androidx.datastore.core.DataStore
import com.github.jameshnsears.chance.common.utility.UtilityAndroidUnitTestHelper
import com.github.jameshnsears.chance.data.domain.core.Dice
import com.github.jameshnsears.chance.data.domain.core.Side
import com.github.jameshnsears.chance.data.domain.core.roll.Roll
import com.github.jameshnsears.chance.data.domain.proto.RollHistoryProtocolBuffer
import com.github.jameshnsears.chance.data.domain.proto.RollListProtocolBuffer
import com.github.jameshnsears.chance.data.domain.proto.RollProtocolBuffer
import com.github.jameshnsears.chance.data.domain.proto.SideProtocolBuffer
import com.github.jameshnsears.chance.data.repo.api.bag.RepositoryBagInterface
import com.github.jameshnsears.chance.data.repo.impl.RepositoryProtocolBufferImageCache
import io.mockk.coEvery
import io.mockk.coVerify
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
        coEvery { repositoryBag.fetch() } returns flowOf(mutableListOf())
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun fetch() = runTest {
        val rollProtocolBuffer = RollProtocolBuffer.newBuilder()
            .setUuidDice("uuidDice")
            .setScore(6)
            .build()
        val rollListProtocolBuffer = RollListProtocolBuffer.newBuilder()
            .addRoll(rollProtocolBuffer)
            .build()
        val rollHistoryProtocolBuffer = RollHistoryProtocolBuffer.newBuilder()
            .putValues(123456789L, rollListProtocolBuffer)
            .build()

        coEvery { dataStore.data } returns flowOf(rollHistoryProtocolBuffer)

        val repository = RepositoryRollProtocolBufferImpl.getInstance(context, repositoryBag)
        val result = repository.fetch().first()

        assertEquals(1, result.size)
        assertTrue(result.containsKey(123456789L))
        assertEquals(6, result[123456789L]!![0].score)
    }

    @Test
    fun store() = runTest {
        coEvery { dataStore.updateData(any()) } returns RollHistoryProtocolBuffer.getDefaultInstance()

        val repository = RepositoryRollProtocolBufferImpl.getInstance(context, repositoryBag)
        val rollHistory = LinkedHashMap<Long, List<Roll>>()
        rollHistory[123456789L] = listOf(Roll(uuidDice = "uuidDice", side = Side(number = 20), score = 20))
        repository.store(rollHistory)

        coVerify { dataStore.updateData(any()) }
    }

    @Test
    fun storeIncremental() = runTest {
        val initialHistory = RollHistoryProtocolBuffer.newBuilder()
            .putValues(
                1L, RollListProtocolBuffer.newBuilder()
                    .addRoll(RollProtocolBuffer.newBuilder().setUuidDice("d1").build()).build()
            )
            .build()

        val updateDataSlot = slot<suspend (RollHistoryProtocolBuffer) -> RollHistoryProtocolBuffer>()
        coEvery { dataStore.updateData(capture(updateDataSlot)) } coAnswers {
            updateDataSlot.captured.invoke(initialHistory)
        }

        val repository = RepositoryRollProtocolBufferImpl.getInstance(context, repositoryBag)
        repository.store(2L, listOf(Roll(uuidDice = "d2", side = Side(number = 2))))

        val result = updateDataSlot.captured.invoke(initialHistory)
        // 1L, 2L and cache -1L
        assertEquals(3, result.valuesCount)
        assertTrue(result.containsValues(1L))
        assertTrue(result.containsValues(2L))
        assertTrue(result.containsValues(RepositoryProtocolBufferImageCache.EPOCH_IMAGE_CACHE))
    }

    @Test
    fun removeLatest() = runTest {
        val initialHistory = RollHistoryProtocolBuffer.newBuilder()
            .putValues(1L, RollListProtocolBuffer.newBuilder().build())
            .putValues(2L, RollListProtocolBuffer.newBuilder().build())
            .build()

        val updateDataSlot = slot<suspend (RollHistoryProtocolBuffer) -> RollHistoryProtocolBuffer>()
        coEvery { dataStore.updateData(capture(updateDataSlot)) } coAnswers {
            updateDataSlot.captured.invoke(initialHistory)
        }

        val repository = RepositoryRollProtocolBufferImpl.getInstance(context, repositoryBag)
        repository.removeLatest()

        val result = updateDataSlot.captured.invoke(initialHistory)
        assertEquals(1, result.valuesCount)
        assertTrue(result.containsValues(1L))
        assertFalse(result.containsValues(2L))
    }

    @Test
    fun clear() = runTest {
        coEvery { dataStore.updateData(any()) } returns RollHistoryProtocolBuffer.getDefaultInstance()
        coEvery { dataStore.data } returns flowOf(RollHistoryProtocolBuffer.getDefaultInstance())

        val repository = RepositoryRollProtocolBufferImpl.getInstance(context, repositoryBag)
        repository.clear()

        coVerify { dataStore.updateData(any()) }
    }

    @Test
    fun jsonExport() = runTest {
        val sideUuid = "uuidSide"
        val rollProtocolBuffer = RollProtocolBuffer.newBuilder()
            .setUuidDice("uuidDice")
            .setScore(0)
            .setSide(
                SideProtocolBuffer.newBuilder()
                    .setUuid(sideUuid)
                    .setNumber(6)
                    .setImageBase64("imageBase64Data")
                    .setDescription("sideDescription")
                    .build()
            )
            .build()
        val rollListProtocolBuffer = RollListProtocolBuffer.newBuilder()
            .addRoll(rollProtocolBuffer)
            .build()

        val rollHistoryProtocolBuffer = RollHistoryProtocolBuffer.newBuilder()
            .putValues(123456789L, rollListProtocolBuffer)
            .build()

        coEvery { dataStore.data } returns flowOf(rollHistoryProtocolBuffer)

        val repository = RepositoryRollProtocolBufferImpl.getInstance(context, repositoryBag)
        val json = repository.jsonExport()

        // Cache should NOT be present
        assertTrue(!json.contains("\"-1\""))

        // Only UUID should be present in the side
        assertTrue(json.contains("\"uuid\": \"uuidSide\""))
        assertFalse(json.contains("\"number\": 6"))
        assertFalse(json.contains("\"imageBase64\": \"imageBase64Data\""))
        assertFalse(json.contains("\"description\": \"sideDescription\""))

        assertTrue(json.contains("\"123456789\""))
    }

    @Test
    fun jsonImport() = runTest {
        val sideUuid = "uuidSide"
        val bag = mutableListOf(
            Dice(
                sides = listOf(
                    Side(uuid = sideUuid, number = 6, imageBase64 = "imageBase64Data", description = "sideDescription")
                )
            )
        )
        coEvery { repositoryBag.fetch() } returns flowOf(bag)

        val updateDataSlot = slot<suspend (RollHistoryProtocolBuffer) -> RollHistoryProtocolBuffer>()
        coEvery { dataStore.updateData(capture(updateDataSlot)) } coAnswers {
            updateDataSlot.captured.invoke(RollHistoryProtocolBuffer.getDefaultInstance())
        }

        val repository = RepositoryRollProtocolBufferImpl.getInstance(context, repositoryBag)
        val json = """
            {
              "values": {
                "123456789": {
                  "roll": [
                    {
                      "uuidDice": "uuidDice",
                      "side": {
                        "uuid": "${sideUuid}"
                      },
                      "score": 6
                    }
                  ]
                }
              }
            }
        """.trimIndent()
        repository.jsonImport(json)

        val resultProto = updateDataSlot.captured.invoke(RollHistoryProtocolBuffer.getDefaultInstance())
        val result = repository.mapRollHistoryProtocolBufferIntoRollHistory(resultProto, bag)

        assertEquals(1, result.size)
        val rolls = result[123456789L]!!
        assertEquals(1, rolls.size)
        assertEquals(sideUuid, rolls[0].side.uuid)
        assertEquals(6, rolls[0].side.number)
        assertEquals("imageBase64Data", rolls[0].side.imageBase64)
        assertEquals("sideDescription", rolls[0].side.description)
    }

    @Test
    fun fetchWithRehydration() = runTest {
        val sideUuid = "uuidSide"
        val rollProtocolBuffer = RollProtocolBuffer.newBuilder()
            .setUuidDice("uuidDice")
            .setSide(SideProtocolBuffer.newBuilder().setUuid(sideUuid).build())
            .build()
        val rollListProtocolBuffer = RollListProtocolBuffer.newBuilder()
            .addRoll(rollProtocolBuffer)
            .build()
        val rollHistoryProtocolBuffer = RollHistoryProtocolBuffer.newBuilder()
            .putValues(123456789L, rollListProtocolBuffer)
            .build()

        coEvery { dataStore.data } returns flowOf(rollHistoryProtocolBuffer)
        coEvery { repositoryBag.fetch() } returns flowOf(
            mutableListOf(
                Dice(sides = listOf(Side(uuid = sideUuid, number = 6)))
            )
        )

        val repository = RepositoryRollProtocolBufferImpl.getInstance(context, repositoryBag)
        val result = repository.fetch().first()

        val roll = result[123456789L]!![0]
        // This is expected to FAIL currently because rollHistoryFlow doesn't combine with repositoryBag
        assertEquals(6, roll.side.number)
    }
}
