package com.github.jameshnsears.chance.data.repo.impl.settings.impl

import android.content.Context
import androidx.datastore.core.DataStore
import com.github.jameshnsears.chance.common.utility.UtilityAndroidUnitTestHelper
import com.github.jameshnsears.chance.data.domain.core.settings.impl.SettingsDataImpl
import com.github.jameshnsears.chance.data.domain.proto.SettingsProtocolBuffer
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

class RepositorySettingsProtocolBufferUnitTest : UtilityAndroidUnitTestHelper() {
    private val context = mockk<Context>()
    private val dataStore = mockk<DataStore<SettingsProtocolBuffer>>()

    @Before
    fun setUp() {
        // Reset singleton instance
        val instanceField = RepositorySettingsProtocolBufferImpl::class.java.getDeclaredField("instance")
        instanceField.isAccessible = true
        instanceField.set(null, null)

        mockkStatic("com.github.jameshnsears.chance.data.repo.impl.settings.impl.RepositorySettingsProtocolBufferImplKt")
        every { context.settingsDataStore } returns dataStore
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun fetch() = runTest {
        val settingsProtocolBuffer = SettingsProtocolBuffer.newBuilder()
            .setResizeZoom(3.0f)
            .build()

        coEvery { dataStore.data } returns flowOf(settingsProtocolBuffer)

        val repository = RepositorySettingsProtocolBufferImpl.getInstance(context)
        val result = repository.fetch().first()

        assertEquals(3.0f, result.resizeZoom)
    }

    @Test
    fun store() = runTest {
        coEvery { dataStore.updateData(any()) } returns SettingsProtocolBuffer.getDefaultInstance()

        val repository = RepositorySettingsProtocolBufferImpl.getInstance(context)
        val settingsData = SettingsDataImpl(resizeZoom = 4.0f)
        repository.store(settingsData)

        coVerify { dataStore.updateData(any()) }
    }

    @Test
    fun clear() = runTest {
        coEvery { dataStore.updateData(any()) } returns SettingsProtocolBuffer.getDefaultInstance()
        coEvery { dataStore.data } returns flowOf(SettingsProtocolBuffer.getDefaultInstance())

        val repository = RepositorySettingsProtocolBufferImpl.getInstance(context)
        repository.clear()

        coVerify { dataStore.updateData(any()) }
    }

    @Test
    fun jsonExport() = runTest {
        val settingsProtocolBuffer = SettingsProtocolBuffer.newBuilder()
            .setResizeZoom(5.0f)
            .build()

        coEvery { dataStore.data } returns flowOf(settingsProtocolBuffer)

        val repository = RepositorySettingsProtocolBufferImpl.getInstance(context)
        val json = repository.jsonExport()

        assertTrue(json.contains("\"resizeZoom\": 5"))
    }

    @Test
    fun jsonImport() = runTest {
        coEvery { dataStore.updateData(any()) } returns SettingsProtocolBuffer.getDefaultInstance()

        val repository = RepositorySettingsProtocolBufferImpl.getInstance(context)
        val json = "{\"resizeZoom\": 6.0}"
        repository.jsonImport(json)

        coVerify { dataStore.updateData(any()) }
    }
}
