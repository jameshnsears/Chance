package com.github.jameshnsears.chance.ui.tab.bag

import android.app.Application
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.jameshnsears.chance.data.repository.bag.mock.RepositoryBagTestDouble
import com.github.jameshnsears.chance.data.repository.roll.mock.RepositoryRollTestDouble
import com.github.jameshnsears.chance.data.repository.settings.mock.RepositorySettingsTestDouble
import com.github.jameshnsears.chance.data.sample.bag.SampleBag
import com.github.jameshnsears.chance.data.sample.roll.SampleRollSampleBagStartup
import com.github.jameshnsears.chance.data.sample.settings.SampleSettings
import com.github.jameshnsears.chance.utility.android.UtilityAndroid
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test


class TabBagAndroidViewModelUnitTest : UtilityAndroid() {
    @Test
    fun exportSettingsRepository() = runTest {
        assertEquals(
            """
            {
              "tabRowChance": 0,
              "resize": 0.0,
              "rollSequentially": false,
              "rollTime": false,
              "rollScore": false,
              "rollDiceTitle": false,
              "rollSideNumber": false,
              "rollSound": false
            }
            """.trimIndent(),
            tabBagViewModel().repositorySettings.exportJson()
        )
    }

    @Test
    fun exportBagRepository() = runTest {
        val jacksonObjectMapper = jacksonObjectMapper()
        val rootNode =
            jacksonObjectMapper.readTree(tabBagViewModel().repositoryBag.exportJson())
        assertTrue(rootNode.get("dice").size() == 7)
        assertTrue(rootNode.get("dice")[0].get("side").size() == 2)
    }

    @Test
    fun exportRollRepository() = runTest {
        val jacksonObjectMapper = jacksonObjectMapper()
        val exportJson = tabBagViewModel().repositoryRoll.exportJson()
        val rootNode = jacksonObjectMapper.readTree(exportJson)
        assertTrue(rootNode.get("values").size() == 3)

        rootNode.get("values").forEach { rollSequence ->
            rollSequence.get("roll").forEach { roll ->
                assertTrue(roll.get("side").get("number").asInt() != 0)
            }
        }
    }

    @Test
    fun importAndExport() = runTest {
        val tabBagViewModel = tabBagViewModel()
        assertFalse(tabBagViewModel.importFailed.value)

        val exportedJson = tabBagViewModel.export()

        tabBagViewModel.import(exportedJson)
        assertFalse(tabBagViewModel.importFailed.value)

        assertEquals(exportedJson, tabBagViewModel.export())
    }

    @Test
    fun importTrash() = runTest {
        val tabBagViewModel = tabBagViewModel()
        assertFalse(tabBagViewModel.importFailed.value)

        tabBagViewModel.import("")
        assertTrue(tabBagViewModel.importFailed.value)

        fail("todo - more trash imports")
    }

    private suspend fun tabBagViewModel(): TabBagAndroidViewModel {
        val repositorySettings = RepositorySettingsTestDouble.getInstance()
        repositorySettings.store(
            SampleSettings.settings,
        )

        val repositoryBag = RepositoryBagTestDouble.getInstance()
        repositoryBag.store(
            SampleBag.allDice,
        )

        val repositoryRoll = RepositoryRollTestDouble.getInstance()
        repositoryRoll.store(SampleRollSampleBagStartup.rollHistory)

        return TabBagAndroidViewModel(
            mockk<Application>(),
            repositorySettings,
            repositoryBag,
            repositoryRoll,
        )
    }
}
