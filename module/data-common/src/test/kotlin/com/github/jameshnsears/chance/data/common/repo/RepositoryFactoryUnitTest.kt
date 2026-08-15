package com.github.jameshnsears.chance.data.common.repo

import com.github.jameshnsears.chance.common.utility.UtilityAndroidUnitTestHelper
import com.github.jameshnsears.chance.common.utility.feature.UtilityFeature
import com.github.jameshnsears.chance.data.domain.core.settings.testdouble.SettingsDataTestDouble
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class RepositoryFactoryUnitTest : UtilityAndroidUnitTestHelper() {
    private lateinit var repositoryFactory: RepositoryFactory

    @Before
    fun setUp() {
        UtilityFeature.enabled = setOf(
            UtilityFeature.Flag.REPO_SQLDELIGHT_TEST_DOUBLE
        )
        repositoryFactory = RepositoryFactory()
    }

    @After
    fun tearDown() {
        repositoryFactory.close()
    }

    @Test
    fun confirmResetStorageWorks() = runTest {
        repositoryFactory.resetStorage()

        assertEquals(
            repositoryFactory.repositorySettings.fetch().first(),
            repositoryFactory.settingsTestDouble
        )

        assertEquals(
            repositoryFactory.repositoryBag.fetch().first().size,
            repositoryFactory.bagDataTestDouble.allDice().size
        )

        assertEquals(
            repositoryFactory.repositoryGroup.fetch().first().size,
            repositoryFactory.groupDataTestDouble.size
        )

        assertEquals(
            repositoryFactory.repositoryRoll.fetch().first().size,
            repositoryFactory.rollHistoryTestDouble.size
        )
    }

    @Test
    fun confirmInitializeWithEmptyAtStartupWorks() = runTest {
        UtilityFeature.enabled = setOf(
            UtilityFeature.Flag.REPO_SQLDELIGHT_TEST_DOUBLE,
            UtilityFeature.Flag.REPO_SQLDELIGHT_EMPTY_AT_STARTUP
        )

        // Put some data in
        repositoryFactory.repositorySettings.store(SettingsDataTestDouble(resizeZoom = 5.0f))

        repositoryFactory.initialize()

        // It should be cleared then re-populated with defaults (3.0f for test double)
        assertEquals(
            3.0f,
            repositoryFactory.repositorySettings.fetch().first().resizeZoom
        )
    }

    @Test
    fun confirmInitializePopulatesDefaultData() = runTest {
        // Ensure it's empty
        repositoryFactory.repositorySettings.clear()
        repositoryFactory.repositoryBag.clear()
        repositoryFactory.repositoryGroup.clear()
        repositoryFactory.repositoryRoll.clear()

        repositoryFactory.initialize()

        assertEquals(
            repositoryFactory.settingsTestDouble.resizeZoom,
            repositoryFactory.repositorySettings.fetch().first().resizeZoom
        )
        assertEquals(
            repositoryFactory.bagDataTestDouble.allDice().size,
            repositoryFactory.repositoryBag.fetch().first().size
        )
    }

    @Test
    fun confirmInitializeDoesNotOverwriteExistingData() = runTest {
        // Put some non-default data in
        repositoryFactory.repositorySettings.store(SettingsDataTestDouble(resizeZoom = 5.0f))

        // Store the full bag but with a distinguishable change so the group/roll
        // seeding in initialize() has valid dice references to satisfy the FK.
        val customDice = repositoryFactory.bagDataTestDouble.allDice().toMutableList()
        customDice[0] = customDice[0].copy(title = "custom-title")
        repositoryFactory.repositoryBag.store(customDice)

        repositoryFactory.initialize()

        // Should STILL be 5.0f
        assertEquals(
            5.0f,
            repositoryFactory.repositorySettings.fetch().first().resizeZoom
        )

        // Should STILL be the custom dice (not overwritten by defaults)
        val fetchedBag = repositoryFactory.repositoryBag.fetch().first()
        assertEquals(customDice.size, fetchedBag.size)
        assertEquals("custom-title", fetchedBag[0].title)
    }
}
