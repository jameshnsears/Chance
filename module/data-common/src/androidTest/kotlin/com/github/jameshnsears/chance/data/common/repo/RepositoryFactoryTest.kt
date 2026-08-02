package com.github.jameshnsears.chance.data.common.repo

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import com.github.jameshnsears.chance.common.utility.UtilityLoggingHelper
import com.github.jameshnsears.chance.common.utility.feature.UtilityFeature
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class RepositoryFactoryTest : UtilityLoggingHelper() {
    private val applicationContext: Application = ApplicationProvider.getApplicationContext()

    @Test
    fun debugWithTestDoubleFlag() = runTest {
        UtilityFeature.enabled = setOf(
            UtilityFeature.Flag.REPO_PROTOCOL_BUFFER_TEST_DOUBLE,
        )

        val repositoryFactory = RepositoryFactory(applicationContext)
        repositoryFactory.resetStorage()

        assertEquals(
            repositoryFactory.settingsTestDouble.resizeZoom,
            repositoryFactory.repositorySettings.fetch().first().resizeZoom
        )

        assertEquals(
            repositoryFactory.bagDataTestDouble.allDice().size,
            repositoryFactory.repositoryBag.fetch().first().size
        )

        assertEquals(
            repositoryFactory.rollHistoryTestDouble.size,
            repositoryFactory.repositoryRoll.fetch().first().size
        )
    }

    @Test
    fun debugWithProdFlag() = runTest {
        UtilityFeature.enabled = setOf(
            UtilityFeature.Flag.REPO_PROTOCOL_BUFFER_PROD,
        )

        val repositoryFactory = RepositoryFactory(applicationContext)
        repositoryFactory.resetStorage()

        assertEquals(
            repositoryFactory.settingsImpl,
            repositoryFactory.repositorySettings.fetch().first()
        )

        assertEquals(
            repositoryFactory.bagDataImpl.allDice().size,
            repositoryFactory.repositoryBag.fetch().first().size
        )
    }
}
