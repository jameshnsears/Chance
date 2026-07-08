package com.github.jameshnsears.chance.data.repo.api

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.test.core.app.ApplicationProvider
import androidx.test.filters.SdkSuppress
import androidx.test.platform.app.InstrumentationRegistry
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.jameshnsears.chance.common.utility.UtilityLoggingHelper
import com.github.jameshnsears.chance.common.utility.feature.UtilityFeature
import com.github.jameshnsears.chance.data.common.repo.RepositoryFactory
import com.github.jameshnsears.chance.ui.tab.SetupImportEvent
import com.github.jameshnsears.chance.ui.tab.setup.dice.DiceAndroidViewModel
import com.github.jameshnsears.chance.ui.tab.setup.dice.ExportImportStatus
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream

class RepositoryImportValidationTest : UtilityLoggingHelper() {
    private val context = InstrumentationRegistry.getInstrumentation().context

    private val applicationContext: Application = ApplicationProvider.getApplicationContext()

    private lateinit var diceAndroidViewModel: DiceAndroidViewModel

    private lateinit var repositoryFactory: RepositoryFactory

    @Before
    fun setUp() {
        repositoryFactory = RepositoryFactory(context)

        diceAndroidViewModel = DiceAndroidViewModel(
            applicationContext,
            repositoryFactory.repositorySettings,
            repositoryFactory.repositoryBag,
            repositoryFactory.repositoryRoll,
            repositoryFactory.repositoryGroup,
            3.0f
        )
    }

    @Test
    fun validateDefaultDataShippedWithApp() = runTest {
        UtilityFeature.enabled = setOf(
            UtilityFeature.Flag.REPO_PROTOCOL_BUFFER_PROD
        )

        val dataImplJson = "[" +
            repositoryFactory.repositorySettings.jsonExport() +
            "," +
            repositoryFactory.repositoryBag.jsonExport() +
            "," +
            repositoryFactory.repositoryRoll.jsonExport() +
            "," +
            repositoryFactory.repositoryGroup.jsonExport() +
            "]"

        try {
            RepositoryImportValidation.validate(jacksonObjectMapper().readTree(dataImplJson))
        } catch (e: Exception) {
            Timber.e(e, e.message)
            throw e
        }
    }

    @SdkSuppress(minSdkVersion = 30)
    @Test
    fun importInvalidEmptyJsonFile() = runTest {
        val tempFile = createTmpFileFromAssetFile(
            context,
            "data/json/import/Invalid-Empty.json"
        )

        diceAndroidViewModel.importFileJson(
            Uri.fromFile(
                tempFile
            )
        )

        waitForCI()

        val collectorJob = launch {
            SetupImportEvent.sharedFlowTabBagImportEvent.collect {
                val stateFlowTabBagImport = diceAndroidViewModel.stateFlowTabBagImport.value
                Assert.assertEquals(ExportImportStatus.FAILURE, stateFlowTabBagImport.importStatus)
                Assert.assertEquals(
                    RepositoryImportStatus.ERROR_IMPORT_EMPTY,
                    stateFlowTabBagImport.importDetail
                )
            }
        }

        collectorJob.cancel()
    }

    private fun waitForCI() {
        repeat(60) {
            if (diceAndroidViewModel.stateFlowTabBagImport.value.importStatus == ExportImportStatus.IMPORT_STARTED) {
                Thread.sleep(1000)
                Timber.d("Waiting for CI")
            }
        }
    }

    @SdkSuppress(minSdkVersion = 30)
    @Test
    fun importValidSampleDataJsonFile() = runTest {
        val tempFile = createTmpFileFromAssetFile(
            context,
            "data/json/import/Valid-BagDataImpl.json"
        )

        diceAndroidViewModel.importFileJson(
            Uri.fromFile(
                tempFile
            )
        )

        waitForCI()

        val collectorJob = launch {
            SetupImportEvent.sharedFlowTabBagImportEvent.collect {
                val stateFlowTabBagImport = diceAndroidViewModel.stateFlowTabBagImport.value
                Assert.assertEquals(ExportImportStatus.SUCCESS, stateFlowTabBagImport.importStatus)
                Assert.assertEquals(RepositoryImportStatus.NONE, stateFlowTabBagImport.importDetail)
            }
        }

        collectorJob.cancel()
    }

    private fun createTmpFileFromAssetFile(
        context: Context,
        filePath: String
    ): File {
        val tempFile = File.createTempFile("temp", null, context.cacheDir)
        tempFile.deleteOnExit()
        FileOutputStream(tempFile).use { output ->
            output.write(
                contentFromAssets(filePath).toByteArray()
            )
        }
        return tempFile
    }

    private fun contentFromAssets(filePath: String): String {
        return context.assets.open(filePath).bufferedReader().use {
            it.readText()
        }
    }
}
