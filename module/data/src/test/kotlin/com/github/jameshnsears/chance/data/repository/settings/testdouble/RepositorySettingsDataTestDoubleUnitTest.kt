package com.github.jameshnsears.chance.data.repository.settings.testdouble

import com.github.jameshnsears.chance.data.domain.core.settings.testdouble.SettingsDataTestDouble
import com.github.jameshnsears.chance.utility.android.UtilityAndroidHelper
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class RepositorySettingsDataTestDoubleUnitTest : UtilityAndroidHelper() {
    @Test
    fun storeAndFetch() = runTest {
        val repositorySettings = RepositorySettingsTestDouble.getInstance()

        val fetchedSettings = repositorySettings.fetch().first()

        val settingsData = SettingsDataTestDouble()

        assertEquals(settingsData.resize, fetchedSettings.resize)

        assertEquals(settingsData.rollIndexTime, fetchedSettings.rollIndexTime)
        assertEquals(settingsData.rollScore, fetchedSettings.rollScore)

        assertEquals(settingsData.diceTitle, fetchedSettings.diceTitle)
        assertEquals(settingsData.sideNumber, fetchedSettings.sideNumber)
        assertEquals(settingsData.behaviour, fetchedSettings.behaviour)
        assertEquals(settingsData.sideDescription, fetchedSettings.sideDescription)
        assertEquals(settingsData.sideSVG, fetchedSettings.sideSVG)

        assertEquals(settingsData.rollSound, fetchedSettings.rollSound)
    }
}
