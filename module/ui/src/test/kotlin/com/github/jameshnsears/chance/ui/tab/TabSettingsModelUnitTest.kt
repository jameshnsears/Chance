package com.github.jameshnsears.chance.ui.tab

import com.github.jameshnsears.chance.data.domain.core.settings.testdouble.SettingsDataTestDouble
import com.github.jameshnsears.chance.data.repository.RepositoryFactory
import com.github.jameshnsears.chance.utility.android.UtilityAndroidHelper
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class TabSettingsModelUnitTest : UtilityAndroidHelper() {
    @Test
    fun resize() = runTest {
        val repositorySettings = RepositoryFactory().repositorySettings

        assertEquals(
            SettingsDataTestDouble().resize,
            TabSettingsModel.resize(repositorySettings)
        )

        val tabSettingsModel = TabSettingsModel

        val newResize = 100
        tabSettingsModel.resize(repositorySettings, newResize)

        assertEquals(
            newResize,
            tabSettingsModel.resize(repositorySettings)
        )

        tabSettingsModel.resize(repositorySettings, SettingsDataTestDouble().resize)
    }
}