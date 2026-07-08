package com.github.jameshnsears.chance.data.common.repo

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import com.github.jameshnsears.chance.common.utility.UtilityLoggingHelper
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class RepositoryFactoryTest : UtilityLoggingHelper() {
    private val applicationContext: Application = ApplicationProvider.getApplicationContext()

    @Test
    fun debugWithFlag() = runTest {
        val repositoryFactory = RepositoryFactory(applicationContext)

        assertEquals(
            repositoryFactory.repositorySettings.fetch().first().resizeZoom,
            repositoryFactory.settingsImpl.resizeZoom
        )

        assertEquals(
            repositoryFactory.repositoryBag.fetch().first().size,
            repositoryFactory.bagDataTestDouble.allDice.size
        )

        assertEquals(
            repositoryFactory.repositoryRoll.fetch().first().size,
            repositoryFactory.rollHistoryTestDouble.size
        )
    }
}
