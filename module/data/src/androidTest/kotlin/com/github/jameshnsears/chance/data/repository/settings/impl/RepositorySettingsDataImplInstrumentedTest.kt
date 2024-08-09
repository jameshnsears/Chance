package com.github.jameshnsears.chance.data.repository.settings.impl

import androidx.test.core.app.ApplicationProvider
import com.github.jameshnsears.chance.data.domain.core.settings.impl.SettingsDataImpl
import com.github.jameshnsears.chance.data.repository.RepositoryFactory
import com.github.jameshnsears.chance.data.repository.RepositoryInstrumentedHelper
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class RepositorySettingsDataImplInstrumentedTest : RepositoryInstrumentedHelper() {
    @Test
    fun storeAndFetch() = runTest {
        val repositoryFactory = RepositoryFactory(ApplicationProvider.getApplicationContext())

        val originalSettings = SettingsDataImpl()
        originalSettings.resize = 2
        originalSettings.rollIndexTime = false
        originalSettings.rollScore = false
        originalSettings.diceTitle = true
        originalSettings.sideNumber = false
        originalSettings.rollBehaviour = true
        originalSettings.sideDescription = true
        originalSettings.sideSVG = false
        originalSettings.rollSound = true

        repositoryFactory.repositorySettings.store(originalSettings)

        val fetchedSettings = repositoryFactory.repositorySettings.fetch().first()

        assertEquals(originalSettings.resize, fetchedSettings.resize)

        assertEquals(originalSettings.rollIndexTime, fetchedSettings.rollIndexTime)
        assertEquals(originalSettings.rollScore, fetchedSettings.rollScore)

        assertEquals(originalSettings.diceTitle, fetchedSettings.diceTitle)
        assertEquals(originalSettings.sideNumber, fetchedSettings.sideNumber)
        assertEquals(originalSettings.rollBehaviour, fetchedSettings.rollBehaviour)
        assertEquals(originalSettings.sideDescription, fetchedSettings.sideDescription)
        assertEquals(originalSettings.sideSVG, fetchedSettings.sideSVG)

        assertEquals(originalSettings.rollSound, fetchedSettings.rollSound)
    }

    @Test
    fun importAndExport() = runTest {
        val repositoryFactory = RepositoryFactory(ApplicationProvider.getApplicationContext())

        val originalSettings = SettingsDataImpl()

        repositoryFactory.repositorySettings.store(originalSettings)

        val json = repositoryFactory.repositorySettings.jsonExport()

        repositoryFactory.repositorySettings.clear()

        repositoryFactory.repositorySettings.jsonImport(json)

        assertEquals(json, repositoryFactory.repositorySettings.jsonExport())

        val fetchedSettings = repositoryFactory.repositorySettings.fetch().first()

        assertEquals(originalSettings.resize, fetchedSettings.resize)

        assertEquals(originalSettings.rollIndexTime, fetchedSettings.rollIndexTime)
        assertEquals(originalSettings.rollScore, fetchedSettings.rollScore)

        assertEquals(originalSettings.diceTitle, fetchedSettings.diceTitle)
        assertEquals(originalSettings.sideNumber, fetchedSettings.sideNumber)
        assertEquals(originalSettings.rollBehaviour, fetchedSettings.rollBehaviour)
        assertEquals(originalSettings.sideDescription, fetchedSettings.sideDescription)
        assertEquals(originalSettings.sideSVG, fetchedSettings.sideSVG)

        assertEquals(originalSettings.rollSound, fetchedSettings.rollSound)
    }
}
