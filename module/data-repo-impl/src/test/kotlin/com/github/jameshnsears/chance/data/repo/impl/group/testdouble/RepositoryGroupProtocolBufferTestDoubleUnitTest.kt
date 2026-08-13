package com.github.jameshnsears.chance.data.repo.impl.group.testdouble

import com.github.jameshnsears.chance.data.domain.core.group.Group
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class RepositoryGroupProtocolBufferTestDoubleUnitTest {
    @Before
    fun setUp() {
        val instanceField = RepositoryGroupProtocolBufferTestDouble::class.java.getDeclaredField("instance")
        instanceField.isAccessible = true
        instanceField.set(null, null)
    }

    @Test
    fun jsonExport() = runTest {
        val groupHistory = listOf(
            Group(
                uuid = "uuid",
                name = "Group 1",
                displayIndex = 0,
                selected = false
            )
        )
        val repository = RepositoryGroupProtocolBufferTestDouble.getInstance(groupHistory)
        val json = repository.jsonExport()

        assertTrue(json.contains("\"name\": \"Group 1\""))
        assertTrue(json.contains("\"displayIndex\": 0"))
        assertTrue(json.contains("\"selected\": false"))
    }

    @Test
    fun storeTargeted() = runTest {
        val groupUuid = "group-uuid"
        val groupHistory = listOf(
            Group(
                uuid = groupUuid,
                selected = false
            )
        )
        val repository = RepositoryGroupProtocolBufferTestDouble.getInstance(groupHistory)

        repository.store(groupUuid, true)
        val fetchedHistory = repository.fetch().first()
        assertTrue(fetchedHistory.first().selected)

        repository.store(groupUuid, false)
        val fetchedHistory2 = repository.fetch().first()
        assertTrue(!fetchedHistory2.first().selected)
    }

    @Test
    fun fetchSelected() = runTest {
        val groupUuid = "group-uuid"
        val groupHistory = listOf(
            Group(
                uuid = groupUuid,
                selected = true
            ),
            Group(
                uuid = "other-uuid",
                selected = false
            )
        )
        val repository = RepositoryGroupProtocolBufferTestDouble.getInstance(groupHistory)

        val selectedUuids = repository.fetchSelected().first()
        assertTrue(selectedUuids.size == 1)
        assertTrue(selectedUuids.contains(groupUuid))

        repository.store(groupUuid, false)
        val selectedUuids2 = repository.fetchSelected().first()
        assertTrue(selectedUuids2.isEmpty())
    }
}
