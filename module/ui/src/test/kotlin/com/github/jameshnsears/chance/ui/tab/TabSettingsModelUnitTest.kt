package com.github.jameshnsears.chance.ui.tab

import com.github.jameshnsears.chance.data.domain.core.settings.Settings
import com.github.jameshnsears.chance.data.utility.UtilityDataHelper
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class TabSettingsModelUnitTest {
    @Test
    fun resize() = runTest {
        val repositorySettings = UtilityDataHelper().repositorySettings

        assertEquals(
            Settings().resize,
            TabSettingsModel.resize(repositorySettings)
        )

        val newResize = 100
        TabSettingsModel.resize(repositorySettings, newResize)

        assertEquals(
            newResize,
            TabSettingsModel.resize(repositorySettings)
        )

        TabSettingsModel.resize(repositorySettings, Settings().resize)
    }
}