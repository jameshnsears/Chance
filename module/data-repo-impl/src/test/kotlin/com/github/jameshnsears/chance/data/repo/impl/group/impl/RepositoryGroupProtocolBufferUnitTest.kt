package com.github.jameshnsears.chance.data.repo.impl.group.impl

import android.content.Context
import androidx.datastore.core.DataStore
import com.github.jameshnsears.chance.common.utility.UtilityAndroidUnitTestHelper
import com.github.jameshnsears.chance.data.domain.core.group.Group
import com.github.jameshnsears.chance.data.domain.proto.GroupHistoryProtocolBuffer
import com.github.jameshnsears.chance.data.domain.proto.GroupProtocolBuffer
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

class RepositoryGroupProtocolBufferUnitTest : UtilityAndroidUnitTestHelper() {
    private val context = mockk<Context>()
    private val dataStore = mockk<DataStore<GroupHistoryProtocolBuffer>>()

    @Before
    fun setUp() {
        // Reset singleton instance
        val instanceField = RepositoryGroupProtocolBufferImpl::class.java.getDeclaredField("instance")
        instanceField.isAccessible = true
        instanceField.set(null, null)

        mockkStatic("com.github.jameshnsears.chance.data.repo.impl.group.impl.RepositoryGroupProtocolBufferImplKt")
        every { context.groupHistoryDataStore } returns dataStore
        coEvery { dataStore.data } returns flowOf(GroupHistoryProtocolBuffer.getDefaultInstance())
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun fetch() = runTest {
        val groupProtocolBuffer = GroupProtocolBuffer.newBuilder()
            .setUuid("uuid")
            .setName("Group 1")
            .build()
        val groupHistoryProtocolBuffer = GroupHistoryProtocolBuffer.newBuilder()
            .addGroup(groupProtocolBuffer)
            .build()

        coEvery { dataStore.data } returns flowOf(groupHistoryProtocolBuffer)

        val repository = RepositoryGroupProtocolBufferImpl.getInstance(context)
        val result = repository.fetch().first()

        assertEquals(1, result.size)
        assertEquals("Group 1", result[0].name)
    }

    @Test
    fun store() = runTest {
        coEvery { dataStore.updateData(any()) } returns GroupHistoryProtocolBuffer.getDefaultInstance()

        val repository = RepositoryGroupProtocolBufferImpl.getInstance(context)
        val groupHistory = listOf(Group(name = "Group 2"))
        repository.store(groupHistory)

        coVerify { dataStore.updateData(any()) }
    }

    @Test
    fun clear() = runTest {
        coEvery { dataStore.updateData(any()) } returns GroupHistoryProtocolBuffer.getDefaultInstance()
        coEvery { dataStore.data } returns flowOf(GroupHistoryProtocolBuffer.getDefaultInstance())

        val repository = RepositoryGroupProtocolBufferImpl.getInstance(context)
        repository.clear()

        coVerify { dataStore.updateData(any()) }
    }

    @Test
    fun jsonExport() = runTest {
        val groupProtocolBuffer = GroupProtocolBuffer.newBuilder()
            .setUuid("uuid")
            .setName("Group 3")
            .setDisplayIndex(0)
            .setSelected(false)
            .build()
        val groupHistoryProtocolBuffer = GroupHistoryProtocolBuffer.newBuilder()
            .addGroup(groupProtocolBuffer)
            .build()

        coEvery { dataStore.data } returns flowOf(groupHistoryProtocolBuffer)

        val repository = RepositoryGroupProtocolBufferImpl.getInstance(context)
        val json = repository.jsonExport()

        assertTrue(json.contains("\"name\": \"Group 3\""))
        assertTrue(json.contains("\"displayIndex\": 0"))
        assertTrue(json.contains("\"selected\": false"))
    }

    @Test
    fun jsonImport() = runTest {
        coEvery { dataStore.updateData(any()) } returns GroupHistoryProtocolBuffer.getDefaultInstance()

        val repository = RepositoryGroupProtocolBufferImpl.getInstance(context)
        val json = """
            {
              "group": [
                {
                  "uuid": "uuid",
                  "name": "Group 4"
                }
              ]
            }
        """.trimIndent()
        repository.jsonImport(json)

        coVerify { dataStore.updateData(any()) }
    }
}
