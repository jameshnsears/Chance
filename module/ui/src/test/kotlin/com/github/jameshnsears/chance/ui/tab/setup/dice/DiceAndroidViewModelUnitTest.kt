package com.github.jameshnsears.chance.ui.tab.setup.dice

import android.app.Application
import com.github.jameshnsears.chance.common.utility.UtilityAndroidUnitTestHelper
import com.github.jameshnsears.chance.data.common.repo.RepositoryFactory
import com.github.jameshnsears.chance.data.domain.core.bag.testdouble.BagDataTestDouble
import com.github.jameshnsears.chance.data.domain.core.group.testdouble.GroupDataTestDouble
import com.github.jameshnsears.chance.data.domain.core.roll.Roll
import com.github.jameshnsears.chance.data.domain.core.roll.testdouble.RollHistoryDataTestDouble
import com.github.jameshnsears.chance.data.domain.core.settings.testdouble.SettingsDataTestDouble
import com.github.jameshnsears.chance.data.repo.api.RepositoryImportStatus
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withTimeout
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class DiceAndroidViewModelUnitTest : UtilityAndroidUnitTestHelper() {
    @Before
    fun setUp() = runTest {
        RepositoryFactory().resetStorage()
    }

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

        tabBagViewModel.import(tabBagViewModel.exportRepositoriesAsJson())
        waitForImportStatus(tabBagViewModel)

        val exportedJson = tabBagViewModel.exportRepositoriesAsJson()

        tabBagViewModel.import(exportedJson)
        waitForImportStatus(tabBagViewModel)
        Assert.assertEquals(
            ExportImportStatus.SUCCESS,
            tabBagViewModel.stateFlowTabBagImport.value.importStatus
        )

        Assert.assertEquals(exportedJson, tabBagViewModel.exportRepositoriesAsJson())
    }

    @Test
    fun exportAndImportSamplesStartup() = runTest {
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

        val exportedJson = tabBagViewModel.exportRepositoriesAsJson()

        tabBagViewModel.import(exportedJson)
        waitForImportStatus(tabBagViewModel)
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

        tabBagViewModel.import(resourceAsString("/data/json/import/Valid-BagDataTestDouble.json"))
        waitForImportStatus(tabBagViewModel)
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
        waitForImportStatus(tabBagViewModel, ExportImportStatus.FAILURE)
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
        waitForImportStatus(tabBagViewModel, ExportImportStatus.FAILURE)

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
        waitForImportStatus(tabBagViewModel, ExportImportStatus.FAILURE)

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
        waitForImportStatus(tabBagViewModel, ExportImportStatus.FAILURE)

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

        waitForImportStatus(tabBagViewModel, ExportImportStatus.FAILURE)

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
    fun priorVersionImport() = runTest {
        val tabBagViewModel = tabBagViewModel()
        tabBagViewModel.import(resourceAsString("/data/json/import/2.3.0.json"))

        waitForImportStatus(tabBagViewModel, ExportImportStatus.SUCCESS)

        Assert.assertEquals(
            ExportImportStatus.SUCCESS,
            tabBagViewModel.stateFlowTabBagImport.value.importStatus
        )
    }

    @Test
    fun importInvalidDiceTitleNotUnique() = runTest {
        val tabBagViewModel = tabBagViewModel()
        tabBagViewModel.import(resourceAsString("/data/json/import/Invalid-DiceTitleNotUnique.json"))
        waitForImportStatus(tabBagViewModel, ExportImportStatus.FAILURE)
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
        waitForImportStatus(tabBagViewModel, ExportImportStatus.FAILURE)
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
    fun importInvalidEpochData() = runTest {
        val tabBagViewModel = tabBagViewModel()
        tabBagViewModel.import(resourceAsString("/data/json/import/Invalid-UnknownDiceInRoll.json"))
        waitForImportStatus(tabBagViewModel, ExportImportStatus.FAILURE)
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
        Assert.assertEquals(3f, tabBagViewModel.stateFlowResize.value)

        tabBagViewModel.resizeSettings(5f)
        Assert.assertEquals(5f, tabBagViewModel.stateFlowResize.value)
    }

    @Test
    fun resetStorage() = runTest {
        val tabBagViewModel = tabBagViewModel()

        // modify the default data
        tabBagViewModel.repositorySettings.store(
            SettingsDataTestDouble(
                resizeZoom = 1f,
                rollIndexTime = false, rollScore = false,
                diceTitle = false, sideNumber = false, rollBehaviour = false, sideDescription = false, sideSVG = false,
                haptics = false, rollSound = false, shuffle = true
            )
        )

        val modifiedDiceBag = mutableListOf(BagDataTestDouble().d6)
        tabBagViewModel.repositoryBag.store(modifiedDiceBag)

        tabBagViewModel.repositoryRoll.store(
            linkedMapOf(
                1L to listOf(
                    Roll(
                        modifiedDiceBag[0].uuid,
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

        val initialRollHistory =
            RollHistoryDataTestDouble(initialBag, GroupDataTestDouble(initialBag)).rollHistory
        val fetchedRollHistory = tabBagViewModel.repositoryRoll.fetch().first()
        Assert.assertEquals(initialRollHistory.size, fetchedRollHistory.size)
        Assert.assertEquals(initialRollHistory[0]?.size, fetchedRollHistory[0]?.size)
        Assert.assertEquals(initialRollHistory[1]?.size, fetchedRollHistory[1]?.size)

        val initialGroupHistory = GroupDataTestDouble(initialBag).groupHistory
        val fetchedGroupHistory = tabBagViewModel.repositoryGroup.fetch().first()
        Assert.assertEquals(initialGroupHistory.size, fetchedGroupHistory.size)
    }

    private suspend fun waitForImportStatus(
        viewModel: DiceAndroidViewModel,
        expectedStatus: ExportImportStatus = ExportImportStatus.SUCCESS
    ) {
        try {
            withTimeout(5_000) {
                viewModel.stateFlowTabBagImport
                    .filter {
                        println(it.importStatus)
                        it.importStatus == expectedStatus
                    }
                    .first()
            }
        } catch (e: Exception) {
            throw e
        }
    }

    private fun tabBagViewModel(
        applicationContext: Application = mockk<Application>()
    ): DiceAndroidViewModel {
        every { applicationContext.getString(any()) } returns "Mock String"

        val repositorySettings = RepositoryFactory().repositorySettings

        val repositoryBag = RepositoryFactory().repositoryBag

        val repositoryRoll = RepositoryFactory().repositoryRoll

        val repositoryGroup = RepositoryFactory().repositoryGroup

        return DiceAndroidViewModel(
            applicationContext,
            repositorySettings,
            repositoryBag,
            repositoryRoll,
            repositoryGroup,
            3f
        )
    }
}
