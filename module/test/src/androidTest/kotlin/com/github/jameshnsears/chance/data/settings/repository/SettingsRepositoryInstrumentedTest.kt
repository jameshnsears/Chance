package com.github.jameshnsears.chance.data.settings.repository

import androidx.test.platform.app.InstrumentationRegistry
import com.github.jameshnsears.chance.data.settings.sample.SettingsSampleData
import com.github.jameshnsears.chance.utils.logging.LoggingLineNumberTreeInstrumentedFeature
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test

class SettingsRepositoryInstrumentedTest : LoggingLineNumberTreeInstrumentedFeature() {
    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun emptyDataStore() = runTest {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val settingsRepository = SettingsRepository

        runBlocking {
            settingsRepository.empty(context)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun demoProtocolBufferUsage() = runTest {
        val context = InstrumentationRegistry.getInstrumentation().targetContext

        val before: Flow<Int> = context.settingsDataStore.data
            .map { settingsProtocolBuffer ->
                settingsProtocolBuffer.bagZoom
            }

        assertEquals(0, before.first())

        runBlocking {
            context.settingsDataStore.updateData { currentSettingsProtocolBuffer ->
                currentSettingsProtocolBuffer.toBuilder()
                    .setBagZoom(5)
                    .build()
            }
        }

        val after: Flow<Int> = context.settingsDataStore.data
            .map { currentSettingsProtocolBuffer ->
                currentSettingsProtocolBuffer.bagZoom
            }

        assertEquals(5, after.first())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun store() = runTest {
        val context = InstrumentationRegistry.getInstrumentation().targetContext

        val settingsRepository = SettingsRepository

        val settings = SettingsSampleData.headsTails

        runBlocking {
            settingsRepository.store(context, settings)
        }

        val actualSettings = settingsRepository.fetch(context).first()
        assertEquals(settings.bagZoom, actualSettings.bagZoom)

        fail("todo = save ALL of Settings into SettingsProtocolBuffer: modify .store & .fetch")
    }
}
