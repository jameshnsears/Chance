package com.github.jameshnsears.chance.data.repo.impl.roll.impl

import android.content.Context
import androidx.datastore.core.DataStore
import com.github.jameshnsears.chance.common.utility.UtilityAndroidUnitTestHelper
import com.github.jameshnsears.chance.data.domain.core.Side
import com.github.jameshnsears.chance.data.domain.core.roll.Roll
import com.github.jameshnsears.chance.data.domain.proto.RollHistoryProtocolBuffer
import com.github.jameshnsears.chance.data.domain.proto.RollListProtocolBuffer
import com.github.jameshnsears.chance.data.domain.proto.RollProtocolBuffer
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

    @Before
    fun setUp() {
        // Reset singleton instance
        val instanceField = RepositoryRollProtocolBufferImpl::class.java.getDeclaredField("instance")
        instanceField.isAccessible = true
        instanceField.set(null, null)

        mockkStatic("com.github.jameshnsears.chance.data.repo.impl.roll.impl.RepositoryRollProtocolBufferImplKt")
        every { context.rollDataStore } returns dataStore
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

        val repository = RepositoryRollProtocolBufferImpl.getInstance(context, LinkedHashMap())
        val result = repository.fetch().first()

        assertEquals(1, result.size)
        assertTrue(result.containsKey(123456789L))
        assertEquals(6, result[123456789L]!![0].score)
    }

    @Test
    fun store() = runTest {
        coEvery { dataStore.updateData(any()) } returns RollHistoryProtocolBuffer.getDefaultInstance()

        val repository = RepositoryRollProtocolBufferImpl.getInstance(context, LinkedHashMap())
        val rollHistory = LinkedHashMap<Long, List<Roll>>()
        rollHistory[123456789L] = listOf(Roll(uuidDice = "uuidDice", side = Side(number = 20), score = 20))
        repository.store(rollHistory)

        coVerify { dataStore.updateData(any()) }
    }

    @Test
    fun clear() = runTest {
        coEvery { dataStore.updateData(any()) } returns RollHistoryProtocolBuffer.getDefaultInstance()
        coEvery { dataStore.data } returns flowOf(RollHistoryProtocolBuffer.getDefaultInstance())

        val repository = RepositoryRollProtocolBufferImpl.getInstance(context, LinkedHashMap())
        repository.clear()

        coVerify { dataStore.updateData(any()) }
    }

    @Test
    fun jsonExport() = runTest {
        val rollProtocolBuffer = RollProtocolBuffer.newBuilder()
            .setUuidDice("uuidDice")
            .setScore(10)
            .build()
        val rollListProtocolBuffer = RollListProtocolBuffer.newBuilder()
            .addRoll(rollProtocolBuffer)
            .build()
        val rollHistoryProtocolBuffer = RollHistoryProtocolBuffer.newBuilder()
            .putValues(123456789L, rollListProtocolBuffer)
            .build()

        coEvery { dataStore.data } returns flowOf(rollHistoryProtocolBuffer)

        val repository = RepositoryRollProtocolBufferImpl.getInstance(context, LinkedHashMap())
        val json = repository.jsonExport()

        assertTrue(json.contains("\"score\": 10"))
    }

    @Test
    fun jsonImport() = runTest {
        coEvery { dataStore.updateData(any()) } returns RollHistoryProtocolBuffer.getDefaultInstance()

        val repository = RepositoryRollProtocolBufferImpl.getInstance(context, LinkedHashMap())
        val json = """
            {
              "values": {
                "123456789": {
                  "roll": [
                    {
                      "uuidDice": "uuidDice",
                      "score": 12
                    }
                  ]
                }
              }
            }
        """.trimIndent()
        repository.jsonImport(json)

        coVerify { dataStore.updateData(any()) }
    }
}
