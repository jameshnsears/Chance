package com.github.jameshnsears.chance.data.repository.settings.impl

import androidx.test.platform.app.InstrumentationRegistry
import com.github.jameshnsears.chance.data.domain.core.bag.testdouble.BagDataTestDouble
import com.github.jameshnsears.chance.data.domain.core.settings.Settings
import com.github.jameshnsears.chance.data.repository.RepositoryInstrumentedHelper
import com.github.jameshnsears.chance.data.utility.UtilityDataHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class RepositorySettingsImplInstrumentedTest : RepositoryInstrumentedHelper() {
    private val repositoryBag = UtilityDataHelper().repositoryBag

    init {
        val bagDataTestDouble = BagDataTestDouble()

        runBlocking(Dispatchers.Main) {
            repositoryBag.store(
                mutableListOf(
                    bagDataTestDouble.d2,
                ),
            )
        }
    }

    @Before
    fun emptyDataStore() = runTest {
        RepositorySettingsImpl.getInstance(
            InstrumentationRegistry.getInstrumentation().targetContext
        ).clear()
    }

    @Test
    fun demoProtocolBufferUsage() = runTest {
        val context = InstrumentationRegistry.getInstrumentation().targetContext

        val before: Flow<Int> = context.settingsDataStore.data
            .map { settingsProtocolBuffer ->
                settingsProtocolBuffer.resize
            }

        assertEquals(0, before.first())

        context.settingsDataStore.updateData { currentSettingsProtocolBuffer ->
            currentSettingsProtocolBuffer.toBuilder()
                .setResize(7)
                .build()
        }

        val after: Flow<Int> = context.settingsDataStore.data
            .map { currentSettingsProtocolBuffer ->
                currentSettingsProtocolBuffer.resize
            }

        assertEquals(7, after.first())
    }

    @Test
    fun storeAndFetch() = runTest {
        val repositorySettingsImpl = RepositorySettingsImpl.getInstance(
            InstrumentationRegistry.getInstrumentation().targetContext
        )

        val originalSettings = Settings()
        originalSettings.resize = 2
        originalSettings.rollIndexTime = false
        originalSettings.rollScore = false
        originalSettings.diceTitle = true
        originalSettings.sideNumber = false
        originalSettings.behaviour = true
        originalSettings.sideDescription = true
        originalSettings.sideSVG = false
        originalSettings.rollSound = true

        repositorySettingsImpl.store(originalSettings)

        val fetchedSettings = repositorySettingsImpl.fetch().first()

        assertEquals(originalSettings.resize, fetchedSettings.resize)

        assertEquals(originalSettings.rollIndexTime, fetchedSettings.rollIndexTime)
        assertEquals(originalSettings.rollScore, fetchedSettings.rollScore)

        assertEquals(originalSettings.diceTitle, fetchedSettings.diceTitle)
        assertEquals(originalSettings.sideNumber, fetchedSettings.sideNumber)
        assertEquals(originalSettings.behaviour, fetchedSettings.behaviour)
        assertEquals(originalSettings.sideDescription, fetchedSettings.sideDescription)
        assertEquals(originalSettings.sideSVG, fetchedSettings.sideSVG)

        assertEquals(originalSettings.rollSound, fetchedSettings.rollSound)
    }

    @Test
    fun importAndExport() = runTest {
        val repositorySettingsImpl = RepositorySettingsImpl.getInstance(
            InstrumentationRegistry.getInstrumentation().targetContext
        )

        val originalSettings = Settings()

        repositorySettingsImpl.store(originalSettings)

        val json = repositorySettingsImpl.exportJson()

        repositorySettingsImpl.clear()

        repositorySettingsImpl.importJson(json)

        assertEquals(json, repositorySettingsImpl.exportJson())

        assertEquals(originalSettings, repositorySettingsImpl.fetch().first())
    }
}
