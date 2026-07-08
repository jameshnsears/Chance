package com.github.jameshnsears.chance.data.repo.api.group

import com.github.jameshnsears.chance.common.utility.UtilityAndroidUnitTestHelper
import com.github.jameshnsears.chance.data.domain.core.group.Group
import com.github.jameshnsears.chance.data.domain.core.group.GroupHistory
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class RepositoryGroupUnitTest : UtilityAndroidUnitTestHelper() {
    @Test
    fun fetchGroupHistory() = runTest {
        val mockRepository = mockk<RepositoryGroupInterface>()
        val groupHistory: GroupHistory = listOf(Group(name = "Group 1"))

        coEvery { mockRepository.fetch() } returns flowOf(groupHistory)

        val result = mockRepository.fetch().first()
        assertEquals(1, result.size)
        assertEquals("Group 1", result[0].name)
    }

    @Test
    fun storeGroupHistory() = runTest {
        val mockRepository = mockk<RepositoryGroupInterface>(relaxed = true)
        val groupHistory: GroupHistory = listOf(Group(name = "Group 2"))

        mockRepository.store(groupHistory)

        coVerify { mockRepository.store(groupHistory) }
    }

    @Test
    fun traceUuid() {
        val repository = object : RepositoryGroupInterface {
            override suspend fun fetch() = flowOf(emptyList<Group>())
            override suspend fun store(newGroupHistory: GroupHistory) {}
            override suspend fun jsonExport() = ""
            override suspend fun jsonImport(json: String) {}
            override suspend fun clear() {}
        }

        val groupHistory: GroupHistory = listOf(Group(uuid = "group-uuid", name = "group-name"))

        // Verifies no crash when calling default implementation
        repository.traceUuid(groupHistory)
    }
}
