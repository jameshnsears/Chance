package com.github.jameshnsears.chance.data.common.repo

import com.github.jameshnsears.chance.common.utility.UtilityAndroidUnitTestHelper
import com.github.jameshnsears.chance.common.utility.feature.UtilityFeature
import com.github.jameshnsears.chance.data.domain.core.settings.testdouble.SettingsDataTestDouble
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
            repositoryFactory.bagDataTestDouble.allDice()
        )

        assertEquals(
            repositoryFactory.repositoryGroup.fetch().first(),
            repositoryFactory.groupDataTestDouble
        )

        assertEquals(
            repositoryFactory.repositoryRoll.fetch().first(),
            repositoryFactory.rollHistoryTestDouble
        )
    }

    @Test
    fun confirmInitializeWithEmptyAtStartupWorks() = runTest {
        UtilityFeature.enabled = setOf(
            UtilityFeature.Flag.REPO_PROTOCOL_BUFFER_TEST_DOUBLE,
            UtilityFeature.Flag.REPO_PROTOCOL_BUFFER_EMPTY_AT_STARTUP
        )

        val repositoryFactory = RepositoryFactory()

        // Put some data in
        repositoryFactory.repositorySettings.store(SettingsDataTestDouble(resizeZoom = 5.0f))

        repositoryFactory.initialize()

        // It should be cleared then re-populated with defaults (2.0f)
        assertEquals(
            2.0f,
            repositoryFactory.repositorySettings.fetch().first().resizeZoom
        )
    }

    @Test
    fun confirmInitializePopulatesDefaultData() = runTest {
        UtilityFeature.enabled = setOf(
            UtilityFeature.Flag.REPO_PROTOCOL_BUFFER_TEST_DOUBLE
        )

        val repositoryFactory = RepositoryFactory()
        // Ensure it's empty
        repositoryFactory.repositorySettings.clear()
        repositoryFactory.repositoryBag.clear()
        repositoryFactory.repositoryGroup.clear()
        repositoryFactory.repositoryRoll.clear()

        repositoryFactory.initialize()

        assertEquals(
            repositoryFactory.settingsImpl.resizeZoom,
            repositoryFactory.repositorySettings.fetch().first().resizeZoom
        )
        assertEquals(
            repositoryFactory.bagDataImpl.allDice().size,
            repositoryFactory.repositoryBag.fetch().first().size
        )
    }

    @Test
    fun confirmInitializeDoesNotOverwriteExistingData() = runTest {
        UtilityFeature.enabled = setOf(
            UtilityFeature.Flag.REPO_PROTOCOL_BUFFER_TEST_DOUBLE
        )

        val repositoryFactory = RepositoryFactory()
        // Put some non-default data in
        repositoryFactory.repositorySettings.store(SettingsDataTestDouble(resizeZoom = 5.0f))

        val customDice = repositoryFactory.bagDataImpl.allDice().take(1).toMutableList()
        repositoryFactory.repositoryBag.store(customDice)

        repositoryFactory.initialize()

        // Should STILL be 5.0f
        assertEquals(
            5.0f,
            repositoryFactory.repositorySettings.fetch().first().resizeZoom
        )

        // Should STILL be just 1 dice
        assertEquals(
            1,
            repositoryFactory.repositoryBag.fetch().first().size
        )
    }
}
