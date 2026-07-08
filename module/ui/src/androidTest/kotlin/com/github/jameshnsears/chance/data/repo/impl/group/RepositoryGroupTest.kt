package com.github.jameshnsears.chance.data.repo.impl.group

import androidx.test.platform.app.InstrumentationRegistry
import com.github.jameshnsears.chance.common.utility.UtilityLoggingHelper
import com.github.jameshnsears.chance.data.common.repo.RepositoryFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class RepositoryGroupTest : UtilityLoggingHelper() {
    @Test
    fun storeAndFetch() = runTest {
        val repositoryFactory =
            RepositoryFactory(InstrumentationRegistry.getInstrumentation().targetContext)
        val repositoryGroupImpl = repositoryFactory.repositoryGroup

        val groupHistory = repositoryFactory.groupDataTestDouble

        repositoryGroupImpl.store(groupHistory)

        val fetchedGroupHistory = repositoryGroupImpl.fetch().first()

        Assert.assertEquals(groupHistory.size, fetchedGroupHistory.size)
        groupHistory.forEachIndexed { index, originalGroup ->
            val fetchedGroup = fetchedGroupHistory[index]
            Assert.assertEquals(originalGroup.uuid, fetchedGroup.uuid)
            Assert.assertEquals(originalGroup.name, fetchedGroup.name)
            Assert.assertEquals(originalGroup.uuidDice, fetchedGroup.uuidDice)
            Assert.assertEquals(originalGroup.notes, fetchedGroup.notes)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun importAndExport() = runTest {
        val repositoryFactory =
            RepositoryFactory(InstrumentationRegistry.getInstrumentation().targetContext)
        val repositoryGroupImpl = repositoryFactory.repositoryGroup

        val groupHistory = repositoryFactory.groupDataTestDouble

        repositoryGroupImpl.store(groupHistory)

        val json = repositoryGroupImpl.jsonExport()

        repositoryGroupImpl.clear()

        repositoryGroupImpl.jsonImport(json)

        advanceUntilIdle()

        Assert.assertEquals(json, repositoryGroupImpl.jsonExport())

        advanceUntilIdle()

        val fetchedGroupHistory = repositoryGroupImpl.fetch().first()

        advanceUntilIdle()

        Assert.assertEquals(groupHistory.size, fetchedGroupHistory.size)
        groupHistory.forEachIndexed { index, originalGroup ->
            val fetchedGroup = fetchedGroupHistory[index]
            Assert.assertEquals(originalGroup.uuid, fetchedGroup.uuid)
            Assert.assertEquals(originalGroup.name, fetchedGroup.name)
            Assert.assertEquals(originalGroup.uuidDice, fetchedGroup.uuidDice)
            Assert.assertEquals(originalGroup.notes, fetchedGroup.notes)
        }
    }
}
