package com.github.jameshnsears.chance.data.repository

import com.github.jameshnsears.chance.utility.android.UtilityAndroidHelper
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class RepositoryFactoryUnitTest : UtilityAndroidHelper() {
    @Test
    fun debug() = runTest {
        val repositoryFactory = RepositoryFactory()

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
            repositoryFactory.rollHistoryDataTestDouble
        )
    }
}
