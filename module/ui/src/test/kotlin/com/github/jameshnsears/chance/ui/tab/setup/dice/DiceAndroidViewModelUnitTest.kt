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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import kotlin.time.Duration.Companion.milliseconds

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
            ExportImportStatus.READY,
            tabBagViewModel.stateFlowTabBagImport.value.importStatus
        )
        Assert.assertEquals(
            ExportImportStatus.READY,
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
            ExportImportStatus.READY,
            tabBagViewModel.stateFlowTabBagImport.value.importStatus
        )
        Assert.assertEquals(
            ExportImportStatus.READY,
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
    fun importValidSampleData25n() = runTest {
        val tabBagViewModel = tabBagViewModel()
        Assert.assertEquals(
            ExportImportStatus.READY,
            tabBagViewModel.stateFlowTabBagImport.value.importStatus
        )

        tabBagViewModel.import(resourceAsString("/data/json/import/2.5.n-valid.json"))
        waitForImportStatus(tabBagViewModel)
        Assert.assertEquals(
            ExportImportStatus.SUCCESS,
            tabBagViewModel.stateFlowTabBagImport.value.importStatus
        )
        Assert.assertEquals(
            RepositoryImportStatus.SUCCESS,
            tabBagViewModel.stateFlowTabBagImport.value.importDetail
        )
    }

    @Test
    fun importValidSampleData260() = runTest {
        val tabBagViewModel = tabBagViewModel()
        Assert.assertEquals(
            ExportImportStatus.READY,
            tabBagViewModel.stateFlowTabBagImport.value.importStatus
        )

        tabBagViewModel.import(resourceAsString("/data/json/import/2.6.0-valid.json"))
        waitForImportStatus(tabBagViewModel)
        Assert.assertEquals(
            ExportImportStatus.SUCCESS,
            tabBagViewModel.stateFlowTabBagImport.value.importStatus
        )
        Assert.assertEquals(
            RepositoryImportStatus.SUCCESS,
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
            RepositoryImportStatus.JSON_FILE_EMPTY,
            tabBagViewModel.stateFlowTabBagImport.value.importDetail
        )
    }

    @Test
    fun importInvalidDiceMissing() = runTest {
        val tabBagViewModel = tabBagViewModel()
        tabBagViewModel.import(resourceAsString("/data/json/import/2.4.0-invalid-JSON_DICE_MISSING.json"))
        waitForImportStatus(tabBagViewModel, ExportImportStatus.FAILURE)

        Assert.assertEquals(
            ExportImportStatus.FAILURE,
            tabBagViewModel.stateFlowTabBagImport.value.importStatus
        )
        Assert.assertEquals(
            RepositoryImportStatus.JSON_DICE_MISSING,
            tabBagViewModel.stateFlowTabBagImport.value.importDetail
        )
    }

    @Test
    fun importInvalidSchemaSettings() = runTest {
        val tabBagViewModel = tabBagViewModel()
        tabBagViewModel.import(resourceAsString("/data/json/import/2.4.0-invalid-JSON_SCHEMA_SETTINGS.json"))
        waitForImportStatus(tabBagViewModel, ExportImportStatus.FAILURE)

        Assert.assertEquals(
            ExportImportStatus.FAILURE,
            tabBagViewModel.stateFlowTabBagImport.value.importStatus
        )
        Assert.assertEquals(
            RepositoryImportStatus.JSON_SCHEMA_SETTINGS,
            tabBagViewModel.stateFlowTabBagImport.value.importDetail
        )
    }

    @Test
    fun importInvalidSchemaDice() = runTest {
        val tabBagViewModel = tabBagViewModel()
        tabBagViewModel.import(resourceAsString("/data/json/import/2.4.0-invalid-JSON_SCHEMA_DICE.json"))
        waitForImportStatus(tabBagViewModel, ExportImportStatus.FAILURE)

        Assert.assertEquals(
            ExportImportStatus.FAILURE,
            tabBagViewModel.stateFlowTabBagImport.value.importStatus
        )
        Assert.assertEquals(
            RepositoryImportStatus.JSON_SCHEMA_DICE,
            tabBagViewModel.stateFlowTabBagImport.value.importDetail
        )
    }

    @Test
    fun importInvalidSchemaSide() = runTest {
        val tabBagViewModel = tabBagViewModel()
        tabBagViewModel.import(resourceAsString("/data/json/import/2.4.0-invalid-JSON_SCHEMA_SIDE.json"))

        waitForImportStatus(tabBagViewModel, ExportImportStatus.FAILURE)

        Assert.assertEquals(
            ExportImportStatus.FAILURE,
            tabBagViewModel.stateFlowTabBagImport.value.importStatus
        )
        Assert.assertEquals(
            RepositoryImportStatus.JSON_SCHEMA_SIDE,
            tabBagViewModel.stateFlowTabBagImport.value.importDetail
        )
    }

    @Test
    fun priorVersionImport() = runTest {
        val tabBagViewModel = tabBagViewModel()
        tabBagViewModel.import(resourceAsString("/data/json/import/2.4.0-valid.json"))

        waitForImportStatus(tabBagViewModel, ExportImportStatus.SUCCESS)

        Assert.assertEquals(
            ExportImportStatus.SUCCESS,
            tabBagViewModel.stateFlowTabBagImport.value.importStatus
        )
    }

    @Test
    fun importInvalidSettingsMissing() = runTest {
        val tabBagViewModel = tabBagViewModel()
        tabBagViewModel.import(resourceAsString("/data/json/import/2.4.0-invalid-JSON_SCHEMA_SETTINGS.json"))
        waitForImportStatus(tabBagViewModel, ExportImportStatus.FAILURE)
        Assert.assertEquals(
            ExportImportStatus.FAILURE,
            tabBagViewModel.stateFlowTabBagImport.value.importStatus
        )
        Assert.assertEquals(
            RepositoryImportStatus.JSON_SCHEMA_SETTINGS,
            tabBagViewModel.stateFlowTabBagImport.value.importDetail
        )
    }

    @Test
    fun importInvalidEpochData() = runTest {
        val tabBagViewModel = tabBagViewModel()
        tabBagViewModel.import(resourceAsString("/data/json/import/2.4.0-invalid-JSON_DICE_UUID.json"))
        waitForImportStatus(tabBagViewModel, ExportImportStatus.FAILURE)
        Assert.assertEquals(
            ExportImportStatus.FAILURE,
            tabBagViewModel.stateFlowTabBagImport.value.importStatus
        )
        Assert.assertEquals(
            RepositoryImportStatus.JSON_DICE_UUID,
            tabBagViewModel.stateFlowTabBagImport.value.importDetail
        )
    }

    @Test
    fun resizeSettings() = runTest {
        val tabBagViewModel = tabBagViewModel()

        tabBagViewModel.resizeSettings(5f)
        waitForResizeValue(tabBagViewModel, 5f)
        Assert.assertEquals(5f, tabBagViewModel.stateFlowResize.value)

        tabBagViewModel.resizeSettings(18f)
        waitForResizeValue(tabBagViewModel, 18f)
        Assert.assertEquals(18f, tabBagViewModel.stateFlowResize.value)
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
        Assert.assertEquals(initialBag.allDice().size, fetchedBag.size)

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

    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun waitForResizeValue(
        viewModel: DiceAndroidViewModel,
        expectedValue: Float
    ) {
        try {
            withContext(Dispatchers.Default.limitedParallelism(1)) {
                withTimeout(5_000.milliseconds) {
                    viewModel.stateFlowResize
                        .filter {
                            it == expectedValue
                        }
                        .first()
                }
            }
        } catch (e: Exception) {
            throw e
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun waitForImportStatus(
        viewModel: DiceAndroidViewModel,
        expectedStatus: ExportImportStatus = ExportImportStatus.SUCCESS
    ) {
        try {
            withContext(Dispatchers.Default.limitedParallelism(1)) {
                withTimeout(5_000.milliseconds) {
                    viewModel.stateFlowTabBagImport
                        .filter {
                            println(it.importStatus)
                            it.importStatus == expectedStatus
                        }
                        .first()
                }
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
            2f
        )
    }
}
