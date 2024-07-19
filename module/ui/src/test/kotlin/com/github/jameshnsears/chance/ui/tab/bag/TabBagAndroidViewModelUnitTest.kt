package com.github.jameshnsears.chance.ui.tab.bag

import android.app.Application
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.jameshnsears.chance.data.domain.core.settings.Settings
import com.github.jameshnsears.chance.data.repository.RepositoryImportStatus
import com.github.jameshnsears.chance.data.utility.UtilityDataHelper
import com.github.jameshnsears.chance.utility.android.UtilityAndroidHelper
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test


class TabBagAndroidViewModelUnitTest : UtilityAndroidHelper() {
    @Test
    fun exportSettingsRepository() = runTest {
        val tabBagViewModel = tabBagViewModel()

        assertEquals(
            """           
            {
              "resize": ${Settings().resize},
              "rollIndexTime": ${Settings().rollIndexTime},
              "rollScore": ${Settings().rollScore},
              "diceTitle": ${Settings().diceTitle},
              "sideNumber": ${Settings().sideNumber},
              "behaviour": ${Settings().behaviour},
              "sideDescription": ${Settings().sideDescription},
              "sideSVG": ${Settings().sideSVG},
              "rollSound": ${Settings().rollSound}
            }
            """.trimIndent(),
            tabBagViewModel.repositorySettings.jsonExport()
        )
    }

    @Test

    fun exportBagRepository() = runTest {
        val tabBagViewModel = tabBagViewModel()

        val json = tabBagViewModel.repositoryBag.jsonExport()

        val jacksonObjectMapper = jacksonObjectMapper()
        val rootNode =
            jacksonObjectMapper.readTree(json)
        assertTrue(rootNode.get("dice").size() == 8)
        assertTrue(rootNode.get("dice")[0].get("side").size() == 2)
    }

    @Test
    fun exportRollRepository() = runTest {
        val jacksonObjectMapper = jacksonObjectMapper()
        val exportJson = tabBagViewModel().repositoryRoll.jsonExport()
        val rootNode = jacksonObjectMapper.readTree(exportJson)
        assertTrue(rootNode.get("values").size() == 2)

        rootNode.get("values").forEach { rollSequence ->
            rollSequence.get("roll").forEach { roll ->
                assertTrue(roll.get("side").get("number").asInt() != 0)
            }
        }
    }

    @Test
    fun exportAndImport() = runTest {
        val tabBagViewModel = tabBagViewModel()
        tabBagViewModel.resetExportImportStatus()

        assertEquals(
            ExportImportStatus.NONE,
            tabBagViewModel.stateFlowTabBagImport.value.importStatus
        )
        assertEquals(
            ExportImportStatus.NONE,
            tabBagViewModel.stateFlowTabBagExport.value.exportStatus
        )

        tabBagViewModel.import(getResourceAsString("/data/json/import/Valid-BagDataImpl.json"))

        val exportedJson = tabBagViewModel.exportRepositoriesAsJson()

        tabBagViewModel.import(exportedJson)
        assertEquals(
            ExportImportStatus.SUCCESS,
            tabBagViewModel.stateFlowTabBagImport.value.importStatus
        )

        assertEquals(exportedJson, tabBagViewModel.exportRepositoriesAsJson())
    }

    @Test
    fun exportAndImportSamplesStartup() = runTest {
        val tabBagViewModel = tabBagViewModel()

        val exportedJson = tabBagViewModel.exportRepositoriesAsJson()

        tabBagViewModel.import(exportedJson)
        assertEquals(
            ExportImportStatus.SUCCESS,
            tabBagViewModel.stateFlowTabBagImport.value.importStatus
        )

        assertEquals(exportedJson, tabBagViewModel.exportRepositoriesAsJson())
    }

    @Test
    fun importValidSampleData() = runTest {
        val tabBagViewModel = tabBagViewModel()
        assertEquals(
            ExportImportStatus.NONE,
            tabBagViewModel.stateFlowTabBagImport.value.importStatus
        )

        tabBagViewModel.import(getResourceAsString("/data/json/import/Valid-BagDataImpl.json"))
        assertEquals(
            ExportImportStatus.SUCCESS,
            tabBagViewModel.stateFlowTabBagImport.value.importStatus
        )
        assertEquals(
            RepositoryImportStatus.NONE,
            tabBagViewModel.stateFlowTabBagImport.value.importDetail
        )
    }

    @Test
    fun importEmpty() = runTest {
        val tabBagViewModel = tabBagViewModel()

        tabBagViewModel.import("")
        assertEquals(
            ExportImportStatus.FAILURE,
            tabBagViewModel.stateFlowTabBagImport.value.importStatus
        )
        assertEquals(
            RepositoryImportStatus.ERROR_IMPORT_EMPTY,
            tabBagViewModel.stateFlowTabBagImport.value.importDetail
        )
    }

    @Test
    fun importInvalidDiceMissing() = runTest {
        val tabBagViewModel = tabBagViewModel()
        tabBagViewModel.import(getResourceAsString("/data/json/import/Invalid-DiceMissing.json"))

        assertEquals(
            ExportImportStatus.FAILURE,
            tabBagViewModel.stateFlowTabBagImport.value.importStatus
        )
        assertEquals(
            RepositoryImportStatus.ERROR_DICE_MISSING,
            tabBagViewModel.stateFlowTabBagImport.value.importDetail
        )
    }

    @Test
    fun importInvalidSchemaSettings() = runTest {
        val tabBagViewModel = tabBagViewModel()
        tabBagViewModel.import(getResourceAsString("/data/json/import/Invalid-SchemaSettings.json"))

        assertEquals(
            ExportImportStatus.FAILURE,
            tabBagViewModel.stateFlowTabBagImport.value.importStatus
        )
        assertEquals(
            RepositoryImportStatus.ERROR_SCHEMA_SETTINGS,
            tabBagViewModel.stateFlowTabBagImport.value.importDetail
        )
    }

    @Test
    fun importInvalidSchemaDice() = runTest {
        val tabBagViewModel = tabBagViewModel()
        tabBagViewModel.import(getResourceAsString("/data/json/import/Invalid-SchemaDice.json"))

        assertEquals(
            ExportImportStatus.FAILURE,
            tabBagViewModel.stateFlowTabBagImport.value.importStatus
        )
        assertEquals(
            RepositoryImportStatus.ERROR_SCHEMA_DICE,
            tabBagViewModel.stateFlowTabBagImport.value.importDetail
        )
    }

    @Test
    fun importInvalidSchemaSide() = runTest {
        val tabBagViewModel = tabBagViewModel()
        tabBagViewModel.import(getResourceAsString("/data/json/import/Invalid-SchemaSide.json"))

        assertEquals(
            ExportImportStatus.FAILURE,
            tabBagViewModel.stateFlowTabBagImport.value.importStatus
        )
        assertEquals(
            RepositoryImportStatus.ERROR_SCHEMA_SIDE,
            tabBagViewModel.stateFlowTabBagImport.value.importDetail
        )
    }

    @Test
    fun importInvalidDiceTitleNotUnique() = runTest {
        val tabBagViewModel = tabBagViewModel()
        tabBagViewModel.import(getResourceAsString("/data/json/import/Invalid-DiceTitleNotUnique.json"))
        assertEquals(
            ExportImportStatus.FAILURE,
            tabBagViewModel.stateFlowTabBagImport.value.importStatus
        )
        assertEquals(
            RepositoryImportStatus.ERROR_DICE_TITLE,
            tabBagViewModel.stateFlowTabBagImport.value.importDetail
        )
    }

    @Test
    fun importInvalidSettingsMissing() = runTest {
        val tabBagViewModel = tabBagViewModel()
        tabBagViewModel.import(getResourceAsString("/data/json/import/Invalid-SettingsMissing.json"))
        assertEquals(
            ExportImportStatus.FAILURE,
            tabBagViewModel.stateFlowTabBagImport.value.importStatus
        )
        assertEquals(
            RepositoryImportStatus.ERROR_SECTION_MISSING,
            tabBagViewModel.stateFlowTabBagImport.value.importDetail
        )
    }

    @Test
    fun importInvalidEpochData() = runTest {
        val tabBagViewModel = tabBagViewModel()
        tabBagViewModel.import(getResourceAsString("/data/json/import/Invalid-UnknownDiceInRoll.json"))
        assertEquals(
            ExportImportStatus.FAILURE,
            tabBagViewModel.stateFlowTabBagImport.value.importStatus
        )
        assertEquals(
            RepositoryImportStatus.ERROR_DICE_UNKNOWN,
            tabBagViewModel.stateFlowTabBagImport.value.importDetail
        )
    }

    @Test
    fun resize() = runTest {
        val tabBagViewModel = tabBagViewModel()
        assertEquals(4, tabBagViewModel.stateFlowTabBag.value.resize)

        tabBagViewModel.resize(5)
        assertEquals(5, tabBagViewModel.stateFlowTabBag.value.resize)
    }

    private fun tabBagViewModel(
    ): TabBagAndroidViewModel {
        val repositorySettings = UtilityDataHelper().repositorySettings

        val repositoryBag = UtilityDataHelper().repositoryBag

        val repositoryRoll = UtilityDataHelper().repositoryRoll

        return TabBagAndroidViewModel(
            mockk<Application>(),
            repositorySettings,
            repositoryBag,
            repositoryRoll,
        )
    }
}
