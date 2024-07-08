package com.github.jameshnsears.chance.data.repository.settings.testdouble

import com.github.jameshnsears.chance.data.sample.settings.SampleSettingsStartup
import com.github.jameshnsears.chance.utility.android.UtilityAndroidHelper
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class RepositorySettingsTestDoubleUnitTest : UtilityAndroidHelper() {
    @Test
    fun storeAndFetch() = runTest {
        val repositorySettingsTestDouble = RepositorySettingsTestDouble.getInstance()

        val settings = SampleSettingsStartup().settings

        repositorySettingsTestDouble.store(settings)

        val fetchedSettings = repositorySettingsTestDouble.fetch().first()

        assertEquals(settings, fetchedSettings)

        assertEquals(settings.tabRowChance, fetchedSettings.tabRowChance)

        assertEquals(settings.resize, fetchedSettings.resize)

        assertEquals(settings.rollIndexTime, fetchedSettings.rollIndexTime)
        assertEquals(settings.rollScore, fetchedSettings.rollScore)

        assertEquals(settings.diceTitle, fetchedSettings.diceTitle)
        assertEquals(settings.sideNumber, fetchedSettings.sideNumber)
        assertEquals(settings.behaviour, fetchedSettings.behaviour)
        assertEquals(settings.sideDescription, fetchedSettings.sideDescription)
        assertEquals(settings.sideSVG, fetchedSettings.sideSVG)

        assertEquals(settings.rollSound, fetchedSettings.rollSound)
    }
}
