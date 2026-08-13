package com.github.jameshnsears.chance.data.repo.impl.side.impl

import android.content.Context
import androidx.datastore.core.DataStore
import com.github.jameshnsears.chance.common.utility.UtilityAndroidUnitTestHelper
import com.github.jameshnsears.chance.data.domain.proto.SideCacheProtocolBuffer
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
import org.junit.Before
import org.junit.Test
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

class RepositorySideCacheProtocolBufferUnitTest : UtilityAndroidUnitTestHelper() {
    private val context = mockk<Context>()
    private val dataStore = mockk<DataStore<SideCacheProtocolBuffer>>()

    @Before
    fun setUp() {
        // Reset singleton instance
        val instanceField = RepositorySideCacheProtocolBufferImpl::class.java.getDeclaredField("instance")
        instanceField.isAccessible = true
        instanceField.set(null, null)

        mockkStatic("com.github.jameshnsears.chance.data.repo.impl.side.impl.RepositorySideCacheProtocolBufferImplKt")
        every { context.sideCacheDataStore } returns dataStore
        coEvery { dataStore.data } returns flowOf(SideCacheProtocolBuffer.getDefaultInstance())
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun fetch() = runTest {
        val sideCacheProtocolBuffer = SideCacheProtocolBuffer.newBuilder()
            .putEntries("uuid1", "svg1")
            .putEntries("uuid2", "svg2")
            .build()

        coEvery { dataStore.data } returns flowOf(sideCacheProtocolBuffer)

        val repository = RepositorySideCacheProtocolBufferImpl.getInstance(context)
        val result = repository.fetch().first()

        assertEquals(2, result.size)
        assertEquals("svg1", result["uuid1"])
        assertEquals("svg2", result["uuid2"])
    }

    @OptIn(ExperimentalEncodingApi::class)
    @Test
    fun storeBulk() = runTest {
        coEvery { dataStore.updateData(any()) } returns SideCacheProtocolBuffer.getDefaultInstance()
        coEvery { dataStore.data } returns flowOf(SideCacheProtocolBuffer.getDefaultInstance())

        val repository = RepositorySideCacheProtocolBufferImpl.getInstance(context)

        val imageBase64 = Base64.encode("svg_content".toByteArray())
        val entries = mapOf("uuid1" to imageBase64, "uuid2" to imageBase64)

        repository.store(entries)

        coVerify { dataStore.updateData(any()) }
    }

    @OptIn(ExperimentalEncodingApi::class)
    @Test
    fun storeDoesNotStoreDuplicateUuids() = runTest {
        val initialCache = SideCacheProtocolBuffer.newBuilder()
            .putEntries("uuid1", "originalSvg")
            .build()

        var current = initialCache
        coEvery { dataStore.updateData(any()) } coAnswers { call ->
            @Suppress("UNCHECKED_CAST")
            val transform = call.invocation.args[0] as suspend (SideCacheProtocolBuffer) -> SideCacheProtocolBuffer
            current = transform(current)
            current
        }
        coEvery { dataStore.data } answers { flowOf(current) }

        val repository = RepositorySideCacheProtocolBufferImpl.getInstance(context)

        val newImageBase64 = Base64.encode("newSvg".toByteArray())
        repository.store(mapOf("uuid1" to newImageBase64, "uuid2" to newImageBase64))

        val result = repository.fetch().first()
        assertEquals(2, result.size)
        assertEquals("originalSvg", result["uuid1"])
        assertEquals("newSvg", result["uuid2"])
    }

    @Test
    fun prune() = runTest {
        coEvery { dataStore.updateData(any()) } returns SideCacheProtocolBuffer.getDefaultInstance()

        val sideCacheProtocolBuffer = SideCacheProtocolBuffer.newBuilder()
            .putEntries("uuid1", "svg1")
            .putEntries("uuid2", "svg2")
            .build()
        coEvery { dataStore.data } returns flowOf(sideCacheProtocolBuffer)

        val repository = RepositorySideCacheProtocolBufferImpl.getInstance(context)
        repository.prune(setOf("uuid1"))

        coVerify { dataStore.updateData(any()) }
    }

    @Test
    fun clear() = runTest {
        coEvery { dataStore.updateData(any()) } returns SideCacheProtocolBuffer.getDefaultInstance()
        coEvery { dataStore.data } returns flowOf(SideCacheProtocolBuffer.getDefaultInstance())

        val repository = RepositorySideCacheProtocolBufferImpl.getInstance(context)
        repository.clear()

        coVerify { dataStore.updateData(any()) }
    }
}
