package com.github.jameshnsears.chance.ui.tab.bag

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.jameshnsears.chance.data.repository.bag.BagDemoSampleData
import com.github.jameshnsears.chance.data.repository.bag.DiceBagRepositoryTestDouble
import com.github.jameshnsears.chance.data.repository.roll.RollRepositoryTestDouble
import com.github.jameshnsears.chance.data.repository.roll.RollSampleData
import com.github.jameshnsears.chance.data.repository.settings.SettingsRepositoryTestDouble
import com.github.jameshnsears.chance.data.repository.settings.SettingsSampleData
import com.github.jameshnsears.chance.utils.rule.RuleMainDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test


class TabBagViewModelUnitTest {
    @get:Rule
    val ruleMainDispatcher = RuleMainDispatcher()

    @Test
    fun exportSettingsRepository() = runTest {
        assertEquals(
            """
            {
              "tabRowChance": 0,
              "bagDemoBag": true,
              "rollSequentially": false,
              "rollHistory": false,
              "rollScore": false,
              "rollDiceTitle": false,
              "rollSideNumber": false,
              "rollSound": false
            }
            """.trimIndent(),
            tabBagViewModel().settingsRepository.export()
        )
    }

    @Test
    fun exportBagRepository() = runTest {
        val jacksonObjectMapper = jacksonObjectMapper()
        val rootNode = jacksonObjectMapper.readTree(tabBagViewModel().bagRepository.export())
        assertTrue(rootNode.get("dice").size() == 1)
        assertTrue(rootNode.get("dice")[0].get("side").size() == 2)
    }

    @Test
    fun exportRollRepository() = runTest {
        val jacksonObjectMapper = jacksonObjectMapper()
        val rootNode = jacksonObjectMapper.readTree(tabBagViewModel().rollRepository.export())
        assertTrue(rootNode.get("values").size() == 4)

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

    private suspend fun tabBagViewModel(): TabBagViewModel {
        val settingsRepository = SettingsRepositoryTestDouble.getInstance()
        settingsRepository.store(
            SettingsSampleData.settings,
        )

        val bagRepository = DiceBagRepositoryTestDouble.getInstance()
        bagRepository.store(
            listOf(
                BagDemoSampleData.diceHeadsTails,
            ),
        )

        val rollRepository = RollRepositoryTestDouble.getInstance()
        rollRepository.store(RollSampleData.rollHistory)

        return TabBagViewModel(
            settingsRepository,
            bagRepository,
            rollRepository,
        )
    }
}
