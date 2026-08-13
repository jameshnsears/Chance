package com.github.jameshnsears.chance.data.repo.impl.bag.impl

import android.content.Context
import androidx.datastore.core.DataStore
import com.github.jameshnsears.chance.common.utility.UtilityAndroidUnitTestHelper
import com.github.jameshnsears.chance.data.domain.core.Dice
import com.github.jameshnsears.chance.data.domain.proto.BagProtocolBuffer
import com.github.jameshnsears.chance.data.domain.proto.DiceProtocolBuffer
import com.github.jameshnsears.chance.data.domain.proto.SideProtocolBuffer
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

class RepositoryBagProtocolBufferUnitTest : UtilityAndroidUnitTestHelper() {
    private val context = mockk<Context>()
    private val dataStore = mockk<DataStore<BagProtocolBuffer>>()

    @Before
    fun setUp() {
        // Reset singleton instance
        val instanceField = RepositoryBagProtocolBufferImpl::class.java.getDeclaredField("instance")
        instanceField.isAccessible = true
        instanceField.set(null, null)

        mockkStatic("com.github.jameshnsears.chance.data.repo.impl.bag.impl.RepositoryBagProtocolBufferImplKt")
        every { context.diceBagDataStore } returns dataStore
        coEvery { dataStore.data } returns flowOf(BagProtocolBuffer.getDefaultInstance())
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun fetch() = runTest {
        val diceProtocolBuffer = DiceProtocolBuffer.newBuilder()
            .setUuid("uuid")
            .setTitle("d6")
            .build()
        val bagProtocolBuffer = BagProtocolBuffer.newBuilder()
            .addDice(diceProtocolBuffer)
            .build()

        coEvery { dataStore.data } returns flowOf(bagProtocolBuffer)

        val repository = RepositoryBagProtocolBufferImpl.getInstance(context)
        val result = repository.fetch().first()

        assertEquals(1, result.size)
        assertEquals("d6", result[0].title)
    }

    @Test
    fun fetchByUuid() = runTest {
        val diceProtocolBuffer = DiceProtocolBuffer.newBuilder()
            .setUuid("uuid")
            .setTitle("d6")
            .build()
        val bagProtocolBuffer = BagProtocolBuffer.newBuilder()
            .addDice(diceProtocolBuffer)
            .build()

        coEvery { dataStore.data } returns flowOf(bagProtocolBuffer)

        val repository = RepositoryBagProtocolBufferImpl.getInstance(context)
        val result = repository.fetch("uuid").first()

        assertEquals("d6", result.title)
        assertEquals("uuid", result.uuid)
    }

    @Test
    fun store() = runTest {
        coEvery { dataStore.updateData(any()) } returns BagProtocolBuffer.getDefaultInstance()

        val repository = RepositoryBagProtocolBufferImpl.getInstance(context)
        val diceBag = mutableListOf(Dice(title = "d10"))
        repository.store(diceBag)

        coVerify { dataStore.updateData(any()) }
    }

    @Test
    fun clear() = runTest {
        coEvery { dataStore.updateData(any()) } returns BagProtocolBuffer.getDefaultInstance()
        coEvery { dataStore.data } returns flowOf(BagProtocolBuffer.getDefaultInstance())

        val repository = RepositoryBagProtocolBufferImpl.getInstance(context)
        repository.clear()

        coVerify { dataStore.updateData(any()) }
    }

    @Test
    fun jsonExport() = runTest {
        val diceProtocolBuffer = DiceProtocolBuffer.newBuilder()
            .setUuid("uuid")
            .setTitle("d6")
            .setSelected(false)
            .setExplode(false)
            .setDisplayIndex(0)
            .setModifyScore(false)
            .addSide(
                SideProtocolBuffer.newBuilder()
                    .setImageBase64("")
                    .setDescription("")
                    .build()
            )
            .build()
        val bagProtocolBuffer = BagProtocolBuffer.newBuilder()
            .addDice(diceProtocolBuffer)
            .build()

        coEvery { dataStore.data } returns flowOf(bagProtocolBuffer)

        val repository = RepositoryBagProtocolBufferImpl.getInstance(context)
        val json = repository.jsonExport()

        assertTrue(json.contains("\"title\": \"d6\""))
        assertTrue(json.contains("\"selected\": false"))
        assertTrue(json.contains("\"explode\": false"))
        assertTrue(json.contains("\"displayIndex\": 0"))
        assertTrue(json.contains("\"modifyScore\": false"))
        assertTrue(json.contains("\"imageDrawableId\": 0"))
        assertTrue(json.contains("\"imageBase64\": \"\""))
        assertTrue(json.contains("\"description\": \"\""))
    }

    @Test
    fun jsonExportWithDeduplication() = runTest {
        val image64 = "someImage"
        val diceProtocolBuffer = DiceProtocolBuffer.newBuilder()
            .setUuid("uuid")
            .setTitle("d6")
            .setSelected(false)
            .setExplode(false)
            .setDisplayIndex(0)
            .setModifyScore(false)
            .addSide(
                SideProtocolBuffer.newBuilder()
                    .setUuid("s1")
                    .setImageBase64(image64)
                    .setDescription("")
                    .build()
            )
            .addSide(
                SideProtocolBuffer.newBuilder()
                    .setUuid("s2")
                    .setImageBase64(image64)
                    .setDescription("")
                    .build()
            )
            .build()
        val bagProtocolBuffer = BagProtocolBuffer.newBuilder()
            .addDice(diceProtocolBuffer)
            .build()

        coEvery { dataStore.data } returns flowOf(bagProtocolBuffer)

        val repository = RepositoryBagProtocolBufferImpl.getInstance(context)
        val json = repository.jsonExport()

        // The exported JSON should contain the imageBase64Uuid for deduplication
        assertTrue(json.contains("\"imageBase64Uuid\": \"s1\""))
    }

    @Test
    fun jsonImport() = runTest {
        coEvery { dataStore.updateData(any()) } returns BagProtocolBuffer.getDefaultInstance()

        val repository = RepositoryBagProtocolBufferImpl.getInstance(context)
        val json = """
            {
              "dice": [
                {
                  "uuid": "uuid",
                  "title": "d6"
                }
              ]
            }
        """.trimIndent()
        repository.jsonImport(json)

        coVerify { dataStore.updateData(any()) }
    }
}
