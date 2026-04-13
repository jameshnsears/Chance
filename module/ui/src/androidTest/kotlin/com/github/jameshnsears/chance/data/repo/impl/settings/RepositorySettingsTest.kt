package com.github.jameshnsears.chance.data.repo.impl.settings

import androidx.test.core.app.ApplicationProvider
import com.github.jameshnsears.chance.common.utility.UtilityLoggingHelper
import com.github.jameshnsears.chance.data.common.repository.RepositoryFactory
import com.github.jameshnsears.chance.data.domain.core.settings.impl.SettingsDataImpl
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class RepositorySettingsTest : UtilityLoggingHelper() {
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

        Assert.assertEquals(originalSettings.resize, fetchedSettings.resize)

        Assert.assertEquals(originalSettings.rollIndexTime, fetchedSettings.rollIndexTime)
        Assert.assertEquals(originalSettings.rollScore, fetchedSettings.rollScore)

        Assert.assertEquals(originalSettings.diceTitle, fetchedSettings.diceTitle)
        Assert.assertEquals(originalSettings.sideNumber, fetchedSettings.sideNumber)
        Assert.assertEquals(originalSettings.rollBehaviour, fetchedSettings.rollBehaviour)
        Assert.assertEquals(originalSettings.sideDescription, fetchedSettings.sideDescription)
        Assert.assertEquals(originalSettings.sideSVG, fetchedSettings.sideSVG)

        Assert.assertEquals(originalSettings.rollSound, fetchedSettings.rollSound)
    }

    @Test
    fun importAndExport() = runTest {
        val repositoryFactory = RepositoryFactory(ApplicationProvider.getApplicationContext())

        val originalSettings = SettingsDataImpl()

        repositoryFactory.repositorySettings.store(originalSettings)

        val json = repositoryFactory.repositorySettings.jsonExport()

        repositoryFactory.repositorySettings.clear()

        repositoryFactory.repositorySettings.jsonImport(json)

        Assert.assertEquals(json, repositoryFactory.repositorySettings.jsonExport())

        val fetchedSettings = repositoryFactory.repositorySettings.fetch().first()

        Assert.assertEquals(originalSettings.resize, fetchedSettings.resize)

        Assert.assertEquals(originalSettings.rollIndexTime, fetchedSettings.rollIndexTime)
        Assert.assertEquals(originalSettings.rollScore, fetchedSettings.rollScore)

        Assert.assertEquals(originalSettings.diceTitle, fetchedSettings.diceTitle)
        Assert.assertEquals(originalSettings.sideNumber, fetchedSettings.sideNumber)
        Assert.assertEquals(originalSettings.rollBehaviour, fetchedSettings.rollBehaviour)
        Assert.assertEquals(originalSettings.sideDescription, fetchedSettings.sideDescription)
        Assert.assertEquals(originalSettings.sideSVG, fetchedSettings.sideSVG)

        Assert.assertEquals(originalSettings.rollSound, fetchedSettings.rollSound)
    }
}
