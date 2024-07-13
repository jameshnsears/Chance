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

        val newResize = 100f
        TabSettingsModel.resize(repositorySettings, newResize)

        assertEquals(
            newResize,
            TabSettingsModel.resize(repositorySettings)
        )

        TabSettingsModel.resize(repositorySettings, Settings().resize)
    }

    @Test
    fun tabRowChance() = runTest {
        val repositorySettings = UtilityDataHelper().repositorySettings

        assertEquals(
            0,
            TabSettingsModel.markTabAsCurrentInSettings(repositorySettings)
        )

        TabSettingsModel.markTabAsCurrentInSettings(repositorySettings, SettingsTab.TAB_ROLLS)

        assertEquals(
            1,
            TabSettingsModel.markTabAsCurrentInSettings(repositorySettings)
        )

        TabSettingsModel.markTabAsCurrentInSettings(repositorySettings, SettingsTab.TAB_DICE)
    }
}