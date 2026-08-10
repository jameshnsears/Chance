package com.github.jameshnsears.chance.data.repo.impl.locked.impl

import android.content.Context
import androidx.datastore.core.DataStore
import com.github.jameshnsears.chance.common.utility.UtilityAndroidUnitTestHelper
import com.github.jameshnsears.chance.data.domain.proto.LockedProtocolBuffer
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

class RepositoryLockedProtocolBufferUnitTest : UtilityAndroidUnitTestHelper() {
    private val context = mockk<Context>()
    private val dataStore = mockk<DataStore<LockedProtocolBuffer>>()

    @Before
    fun setUp() {
        // Reset singleton instance
        val instanceField = RepositoryLockedProtocolBufferImpl::class.java.getDeclaredField("instance")
        instanceField.isAccessible = true
        instanceField.set(null, null)

        mockkStatic("com.github.jameshnsears.chance.data.repo.impl.locked.impl.RepositoryLockedProtocolBufferImplKt")
        every { context.lockedDataStore } returns dataStore
        coEvery { dataStore.data } returns flowOf(LockedProtocolBuffer.getDefaultInstance())
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun fetch() = runTest {
        val lockedProtocolBuffer = LockedProtocolBuffer.newBuilder()
            .addLockedRollIndices("key1")
            .addLockedRollIndices("key2")
            .build()

        coEvery { dataStore.data } returns flowOf(lockedProtocolBuffer)

        val repository = RepositoryLockedProtocolBufferImpl.getInstance(context)
        val result = repository.fetch().first()

        assertEquals(2, result.size)
        assertTrue(result.contains("key1"))
        assertTrue(result.contains("key2"))
    }

    @Test
    fun store() = runTest {
        coEvery { dataStore.updateData(any()) } returns LockedProtocolBuffer.getDefaultInstance()

        val repository = RepositoryLockedProtocolBufferImpl.getInstance(context)
        repository.store(setOf("key1", "key2"))

        coVerify { dataStore.updateData(any()) }
    }

    @Test
    fun clear() = runTest {
        coEvery { dataStore.updateData(any()) } returns LockedProtocolBuffer.getDefaultInstance()

        val repository = RepositoryLockedProtocolBufferImpl.getInstance(context)
        repository.clear()

        coVerify { dataStore.updateData(any()) }
    }
}
