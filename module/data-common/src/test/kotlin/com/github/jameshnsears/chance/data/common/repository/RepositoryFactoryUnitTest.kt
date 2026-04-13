package com.github.jameshnsears.chance.data.common.repository

import com.github.jameshnsears.chance.common.utility.UtilityAndroidUnitTestHelper
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class RepositoryFactoryUnitTest : UtilityAndroidUnitTestHelper() {
    @Test
    fun confirmResetStorageWorks() = runTest {
        val repositoryFactory = RepositoryFactory()
        repositoryFactory.resetStorage()

        assertEquals(
            repositoryFactory.repositorySettings.fetch().first(),
            repositoryFactory.settingsTestDouble
        )

        assertEquals(
            repositoryFactory.repositoryBag.fetch().first(),
            repositoryFactory.bagDataTestDouble.allDice
        )

        assertEquals(
            repositoryFactory.repositoryRoll.fetch().first(),
            repositoryFactory.rollHistoryTestDouble
        )
    }
}
