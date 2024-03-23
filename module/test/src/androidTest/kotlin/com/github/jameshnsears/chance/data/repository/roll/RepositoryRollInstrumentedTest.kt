package com.github.jameshnsears.chance.data.repository.roll

import androidx.test.platform.app.InstrumentationRegistry
import com.github.jameshnsears.chance.data.repository.roll.impl.RepositoryRoll
import com.github.jameshnsears.chance.data.sample.roll.SampleRollSampleBagStartup
import com.github.jameshnsears.chance.utility.logging.UtilityLoggingInstrumentedHelper
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class RepositoryRollInstrumentedTest : UtilityLoggingInstrumentedHelper() {
    @Before
    fun emptyDataStore() = runTest {
        RepositoryRoll.getInstance(
            InstrumentationRegistry.getInstrumentation().targetContext
        ).empty()
    }

    @Test
    fun storeAndFetch() = runTest {
        val repositoryRoll = RepositoryRoll.getInstance(
            InstrumentationRegistry.getInstrumentation().targetContext
        )

        val rollHistory = SampleRollSampleBagStartup.rollHistory

        repositoryRoll.store(rollHistory)

        assertEquals(
            rollHistory,
            repositoryRoll.fetch().first()
        )
    }

    @Test
    fun importAndExport() = runTest {
        val repositoryRoll = RepositoryRoll.getInstance(
            InstrumentationRegistry.getInstrumentation().targetContext
        )

        val rollHistory = SampleRollSampleBagStartup.rollHistory

        repositoryRoll.store(rollHistory)

        val json = repositoryRoll.exportJson()

        repositoryRoll.empty()

        repositoryRoll.importJson(json)

        assertEquals(json, repositoryRoll.exportJson())

        assertEquals(rollHistory, repositoryRoll.fetch().first())
    }
}
