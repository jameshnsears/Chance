package com.github.jameshnsears.chance.data.repository.settings

import androidx.test.platform.app.InstrumentationRegistry
import com.github.jameshnsears.chance.data.repository.settings.impl.RepositorySettings
import com.github.jameshnsears.chance.data.repository.settings.impl.settingsDataStore
import com.github.jameshnsears.chance.data.sample.settings.SampleSettings
import com.github.jameshnsears.chance.ui.dialog.bag.DialogBagInstrumentedHelper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class RepositorySettingsInstrumentedTest : DialogBagInstrumentedHelper() {
    @Before
    fun emptyDataStore() = runTest {
        RepositorySettings.getInstance(
            InstrumentationRegistry.getInstrumentation().targetContext
        ).empty()
    }

    @Test
    fun demoProtocolBufferUsage() = runTest {
        val context = InstrumentationRegistry.getInstrumentation().targetContext

        val before: Flow<Int> = context.settingsDataStore.data
            .map { settingsProtocolBuffer ->
                settingsProtocolBuffer.tabRowChance
            }

        assertEquals(0, before.first())

        context.settingsDataStore.updateData { currentSettingsProtocolBuffer ->
            currentSettingsProtocolBuffer.toBuilder()
                .setTabRowChance(1)
                .build()
        }

        val after: Flow<Int> = context.settingsDataStore.data
            .map { currentSettingsProtocolBuffer ->
                currentSettingsProtocolBuffer.tabRowChance
            }

        assertEquals(1, after.first())
    }

    @Test
    fun storeAndFetch() = runTest {
        val repositorySettings = RepositorySettings.getInstance(
            InstrumentationRegistry.getInstrumentation().targetContext
        )

        val settings = SampleSettings.settings

        repositorySettings.store(settings)

        assertEquals(
            settings.tabRowChance,
            repositorySettings.fetch().first().tabRowChance
        )
    }

    @Test
    fun importAndExport() = runTest {
        val repositorySettings = RepositorySettings.getInstance(
            InstrumentationRegistry.getInstrumentation().targetContext
        )

        val settings = SampleSettings.settings

        repositorySettings.store(settings)

        val json = repositorySettings.exportJson()

        repositorySettings.empty()

        repositorySettings.importJson(json)

        assertEquals(json, repositorySettings.exportJson())

        assertEquals(settings, repositorySettings.fetch().first())
    }
}
