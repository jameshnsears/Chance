package com.github.jameshnsears.chance.ui.tab

import com.github.jameshnsears.chance.data.repository.settings.mock.RepositorySettingsTestDouble
import com.github.jameshnsears.chance.data.sample.settings.SampleSettingsStartup
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class TabSettingsModelUnitTest {
    @Test
    fun resize() = runTest {
        val repositorySettings = repositorySettingsTestDouble()

        assertEquals(
            SampleSettingsStartup().settings.resize,
            TabSettingsModel.resize(repositorySettings)
        )

        val newResize = 100f
        TabSettingsModel.resize(repositorySettings, newResize)

        assertEquals(
            newResize,
            TabSettingsModel.resize(repositorySettings)
        )

        TabSettingsModel.resize(repositorySettings, SampleSettingsStartup().settings.resize)
    }

    @Test
    fun tabRowChance() = runTest {
        val repositorySettings = repositorySettingsTestDouble()

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

    private suspend fun repositorySettingsTestDouble(): RepositorySettingsTestDouble {
        val repositorySettings = RepositorySettingsTestDouble.getInstance()
        repositorySettings.store(
            SampleSettingsStartup().settings,
        )
        return repositorySettings
    }
}