package com.github.jameshnsears.chance.ui.tab.bag

import android.app.Application
import com.github.jameshnsears.chance.common.utility.UtilityAndroidUnitTestHelper
import com.github.jameshnsears.chance.data.common.repository.RepositoryFactory
import com.github.jameshnsears.chance.data.domain.core.bag.testdouble.BagDataTestDouble
import com.github.jameshnsears.chance.data.domain.core.roll.Roll
import com.github.jameshnsears.chance.data.domain.core.roll.testdouble.RollHistoryDataTestDouble
import com.github.jameshnsears.chance.data.domain.core.settings.testdouble.SettingsDataTestDouble
import com.github.jameshnsears.chance.data.repo.api.RepositoryImportStatus
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class TabBagAndroidViewModelUnitTest : UtilityAndroidUnitTestHelper() {
    @Test
    fun exportAndImport() = runTest {
        val tabBagViewModel = tabBagViewModel()
        tabBagViewModel.resetExportImportStatus()

        Assert.assertEquals(
            ExportImportStatus.NONE,
            tabBagViewModel.stateFlowTabBagImport.value.importStatus
        )
        Assert.assertEquals(
            ExportImportStatus.NONE,
            tabBagViewModel.stateFlowTabBagExport.value.exportStatus
        )

        tabBagViewModel.import(resourceAsString("/data/json/import/Valid-BagDataImpl.json"))

        val exportedJson = tabBagViewModel.exportRepositoriesAsJson()

        tabBagViewModel.import(exportedJson)
        Assert.assertEquals(
            ExportImportStatus.SUCCESS,
            tabBagViewModel.stateFlowTabBagImport.value.importStatus
        )

        Assert.assertEquals(exportedJson, tabBagViewModel.exportRepositoriesAsJson())
    }

    @Test
    fun exportAndImportSamplesStartup() = runTest {
        val tabBagViewModel = tabBagViewModel()

        val exportedJson = tabBagViewModel.exportRepositoriesAsJson()

        tabBagViewModel.import(exportedJson)
        Assert.assertEquals(
            ExportImportStatus.SUCCESS,
            tabBagViewModel.stateFlowTabBagImport.value.importStatus
        )

        Assert.assertEquals(exportedJson, tabBagViewModel.exportRepositoriesAsJson())
    }

    @Test
    fun importValidSampleData() = runTest {
        val tabBagViewModel = tabBagViewModel()
        Assert.assertEquals(
            ExportImportStatus.NONE,
            tabBagViewModel.stateFlowTabBagImport.value.importStatus
        )

        tabBagViewModel.import(resourceAsString("/data/json/import/Valid-BagDataImpl.json"))
        Assert.assertEquals(
            ExportImportStatus.SUCCESS,
            tabBagViewModel.stateFlowTabBagImport.value.importStatus
        )
        Assert.assertEquals(
            RepositoryImportStatus.NONE,
            tabBagViewModel.stateFlowTabBagImport.value.importDetail
        )
    }

    @Test
    fun importEmpty() = runTest {
        val tabBagViewModel = tabBagViewModel()

        tabBagViewModel.import("")
        Assert.assertEquals(
            ExportImportStatus.FAILURE,
            tabBagViewModel.stateFlowTabBagImport.value.importStatus
        )
        Assert.assertEquals(
            RepositoryImportStatus.ERROR_IMPORT_EMPTY,
            tabBagViewModel.stateFlowTabBagImport.value.importDetail
        )
    }

    @Test
    fun importInvalidDiceMissing() = runTest {
        val tabBagViewModel = tabBagViewModel()
        tabBagViewModel.import(resourceAsString("/data/json/import/Invalid-DiceMissing.json"))

        Assert.assertEquals(
            ExportImportStatus.FAILURE,
            tabBagViewModel.stateFlowTabBagImport.value.importStatus
        )
        Assert.assertEquals(
            RepositoryImportStatus.ERROR_DICE_MISSING,
            tabBagViewModel.stateFlowTabBagImport.value.importDetail
        )
    }

    @Test
    fun importInvalidSchemaSettings() = runTest {
        val tabBagViewModel = tabBagViewModel()
        tabBagViewModel.import(resourceAsString("/data/json/import/Invalid-SchemaSettings.json"))

        Assert.assertEquals(
            ExportImportStatus.FAILURE,
            tabBagViewModel.stateFlowTabBagImport.value.importStatus
        )
        Assert.assertEquals(
            RepositoryImportStatus.ERROR_SCHEMA_SETTINGS,
            tabBagViewModel.stateFlowTabBagImport.value.importDetail
        )
    }

    @Test
    fun importInvalidSchemaDice() = runTest {
        val tabBagViewModel = tabBagViewModel()
        tabBagViewModel.import(resourceAsString("/data/json/import/Invalid-SchemaDice.json"))

        Assert.assertEquals(
            ExportImportStatus.FAILURE,
            tabBagViewModel.stateFlowTabBagImport.value.importStatus
        )
        Assert.assertEquals(
            RepositoryImportStatus.ERROR_SCHEMA_DICE,
            tabBagViewModel.stateFlowTabBagImport.value.importDetail
        )
    }

    @Test
    fun importInvalidSchemaSide() = runTest {
        val tabBagViewModel = tabBagViewModel()
        tabBagViewModel.import(resourceAsString("/data/json/import/Invalid-SchemaSide.json"))

        Assert.assertEquals(
            ExportImportStatus.FAILURE,
            tabBagViewModel.stateFlowTabBagImport.value.importStatus
        )
        Assert.assertEquals(
            RepositoryImportStatus.ERROR_SCHEMA_SIDE,
            tabBagViewModel.stateFlowTabBagImport.value.importDetail
        )
    }

    @Test
    fun importInvalidDiceTitleNotUnique() = runTest {
        val tabBagViewModel = tabBagViewModel()
        tabBagViewModel.import(resourceAsString("/data/json/import/Invalid-DiceTitleNotUnique.json"))
        Assert.assertEquals(
            ExportImportStatus.FAILURE,
            tabBagViewModel.stateFlowTabBagImport.value.importStatus
        )
        Assert.assertEquals(
            RepositoryImportStatus.ERROR_DICE_TITLE,
            tabBagViewModel.stateFlowTabBagImport.value.importDetail
        )
    }

    @Test
    fun importInvalidSettingsMissing() = runTest {
        val tabBagViewModel = tabBagViewModel()
        tabBagViewModel.import(resourceAsString("/data/json/import/Invalid-SettingsMissing.json"))
        Assert.assertEquals(
            ExportImportStatus.FAILURE,
            tabBagViewModel.stateFlowTabBagImport.value.importStatus
        )
        Assert.assertEquals(
            RepositoryImportStatus.ERROR_SECTION_MISSING,
            tabBagViewModel.stateFlowTabBagImport.value.importDetail
        )
    }

    @Test
    fun importInvalidEpochData() = runTest {
        val tabBagViewModel = tabBagViewModel()
        tabBagViewModel.import(resourceAsString("/data/json/import/Invalid-UnknownDiceInRoll.json"))
        Assert.assertEquals(
            ExportImportStatus.FAILURE,
            tabBagViewModel.stateFlowTabBagImport.value.importStatus
        )
        Assert.assertEquals(
            RepositoryImportStatus.ERROR_DICE_UNKNOWN,
            tabBagViewModel.stateFlowTabBagImport.value.importDetail
        )
    }

    @Test
    fun resizeSettings() = runTest {
        val tabBagViewModel = tabBagViewModel()
        Assert.assertEquals(3, tabBagViewModel.stateFlowResize.value)

        tabBagViewModel.resizeSettings(5)
        Assert.assertEquals(5, tabBagViewModel.stateFlowResize.value)
    }

    @Test
    fun resetStorage() = runTest {
        val tabBagViewModel = tabBagViewModel()

        // modify the default data
        tabBagViewModel.repositorySettings.store(
            SettingsDataTestDouble(
                resize = 1,
                rollIndexTime = false, rollScore = false,
                diceTitle = false, sideNumber = false, rollBehaviour = false, sideDescription = false, sideSVG = false,
                rollSound = false, shuffle = true
            )
        )

        val modifiedDiceBag = mutableListOf(BagDataTestDouble().d6)
        tabBagViewModel.repositoryBag.store(modifiedDiceBag)

        tabBagViewModel.repositoryRoll.store(
            linkedMapOf(
                1L to listOf(
                    Roll(
                        modifiedDiceBag[0].epoch,
                        modifiedDiceBag[0].sides[0]
                    ),
                )
            )
        )

        /////////////////////

        tabBagViewModel.resetStorage()

        /////////////////////

        val initialSettings = SettingsDataTestDouble()
        val fetchedSettings = tabBagViewModel.repositorySettings.fetch().first()
        Assert.assertEquals(initialSettings, fetchedSettings)

        val initialBag = BagDataTestDouble()
        val fetchedBag = tabBagViewModel.repositoryBag.fetch().first()
        Assert.assertEquals(initialBag.allDice.size, fetchedBag.size)

        val initialRollHistory = RollHistoryDataTestDouble(initialBag).rollHistory
        val fetchedRollHistory = tabBagViewModel.repositoryRoll.fetch().first()
        Assert.assertEquals(initialRollHistory.size, fetchedRollHistory.size)
        Assert.assertEquals(initialRollHistory[0]?.size, fetchedRollHistory[0]?.size)
        Assert.assertEquals(initialRollHistory[1]?.size, fetchedRollHistory[1]?.size)
    }

    private fun tabBagViewModel(
        applicationContext: Application = mockk<Application>()
    ): TabBagAndroidViewModel {
        val repositorySettings = RepositoryFactory().repositorySettings

        val repositoryBag = RepositoryFactory().repositoryBag

        val repositoryRoll = RepositoryFactory().repositoryRoll

        return TabBagAndroidViewModel(
            applicationContext,
            repositorySettings,
            repositoryBag,
            repositoryRoll,
            3
        )
    }
}
