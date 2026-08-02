package com.github.jameshnsears.chance.data.repo.impl.group.testdouble

import com.github.jameshnsears.chance.data.domain.core.group.Group
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
}
