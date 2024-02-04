package com.github.jameshnsears.chance.data.repository.settings

import androidx.test.platform.app.InstrumentationRegistry
import com.github.jameshnsears.chance.utils.logging.LoggingLineNumberTreeInstrumentedFeature
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class SettingsRepositoryInstrumentedTest : LoggingLineNumberTreeInstrumentedFeature() {
    @Before
    fun emptyDataStore() = runTest {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val settingsRepository = SettingsRepository.getInstance(context)
        settingsRepository.empty()
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
    fun store() = runTest {
        val context = InstrumentationRegistry.getInstrumentation().targetContext

        val settingsRepository = SettingsRepository.getInstance(context)

        val settings = SettingsSampleData.settings

        settingsRepository.store(settings)

        val actualSettings = settingsRepository.fetch().first()
        assertEquals(settings.tabRowChance, actualSettings.tabRowChance)
    }
}
