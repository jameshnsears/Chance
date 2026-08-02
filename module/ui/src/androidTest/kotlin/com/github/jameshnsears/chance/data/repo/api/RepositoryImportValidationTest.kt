package com.github.jameshnsears.chance.data.repo.api

import android.app.Application
import android.net.Uri
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.jameshnsears.chance.common.utility.UtilityLoggingHelper
import com.github.jameshnsears.chance.common.utility.feature.UtilityFeature
import com.github.jameshnsears.chance.data.common.repo.RepositoryFactory
import com.github.jameshnsears.chance.ui.BuildConfig
import com.github.jameshnsears.chance.ui.tab.setup.dice.DiceAndroidViewModel
import com.github.jameshnsears.chance.ui.tab.setup.dice.ExportImportStatus
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import kotlin.time.Duration.Companion.milliseconds

class RepositoryImportValidationTest : UtilityLoggingHelper() {
    private val context = InstrumentationRegistry.getInstrumentation().context
    private val targetContext = InstrumentationRegistry.getInstrumentation().targetContext

    private val applicationContext: Application = ApplicationProvider.getApplicationContext()

    private lateinit var diceAndroidViewModel: DiceAndroidViewModel

    private lateinit var repositoryFactory: RepositoryFactory

    @Before
    fun setUp() {
        runBlocking {
            repositoryFactory = RepositoryFactory(targetContext)
            repositoryFactory.resetStorage()

            diceAndroidViewModel = DiceAndroidViewModel(
                applicationContext,
                repositoryFactory.repositorySettings,
                repositoryFactory.repositoryBag,
                repositoryFactory.repositoryRoll,
                repositoryFactory.repositoryGroup,
                3.0f
            )
        }
    }

    @Test
    fun validateDefaultDataShippedWithApp() {
        runBlocking {
            UtilityFeature.enabled = setOf(
                UtilityFeature.Flag.REPO_PROTOCOL_BUFFER_PROD
            )

            repositoryFactory.resetStorage()

            val repositoryFactoryProd = RepositoryFactory(targetContext)
            repositoryFactoryProd.initialize()

            val dataImplJson = "[" +
                repositoryFactoryProd.repositorySettings.jsonExport() +
                "," +
                repositoryFactoryProd.repositoryBag.jsonExport() +
                "," +
                repositoryFactoryProd.repositoryRoll.jsonExport() +
                "," +
                repositoryFactoryProd.repositoryGroup.jsonExport() +
                "]"

            try {
                val validator = RepositoryImportValidation(BuildConfig.VERSION)
                validator.validate(jacksonObjectMapper().readTree(dataImplJson))
            } catch (e: Exception) {
                Timber.e(e, e.message)
                throw e
            }
        }
    }

    @Test
    fun importInvalidEmptyJsonFile() {
        runBlocking {
            val tempFile = createTmpFileFromAssetFile(
                "data/json/import/Invalid-Empty.json"
            )

            diceAndroidViewModel.importFileJson(
                Uri.fromFile(
                    tempFile
                )
            )

            waitForImportToComplete()

            val stateFlowTabBagImport = diceAndroidViewModel.stateFlowTabBagImport.value
            Assert.assertEquals(ExportImportStatus.FAILURE, stateFlowTabBagImport.importStatus)
            Assert.assertEquals(
                RepositoryImportStatus.JSON_FILE_EMPTY,
                stateFlowTabBagImport.importDetail
            )
        }
    }

    private suspend fun waitForImportToComplete() {
        withTimeout(10000.milliseconds) {
            diceAndroidViewModel.stateFlowTabBagImport.filter {
                it.importStatus == ExportImportStatus.SUCCESS || it.importStatus == ExportImportStatus.FAILURE
            }.first()
        }
    }

    @Test
    fun importValidSampleDataJsonFile() {
        runBlocking {
            val tempFile = createTmpFileFromAssetFile(
                "data/json/import/Valid-BagDataImpl.json"
            )

            diceAndroidViewModel.importFileJson(
                Uri.fromFile(
                    tempFile
                )
            )

            waitForImportToComplete()

            val stateFlowTabBagImport = diceAndroidViewModel.stateFlowTabBagImport.value
            Assert.assertEquals(ExportImportStatus.SUCCESS, stateFlowTabBagImport.importStatus)
            Assert.assertEquals(RepositoryImportStatus.SUCCESS, stateFlowTabBagImport.importDetail)
        }
    }

    @Test
    fun importValidNewObjectFormat() {
        runBlocking {
            val exportedJson = diceAndroidViewModel.exportRepositoriesAsJson()
            val rootNode = jacksonObjectMapper().readTree(exportedJson)

            val validator = RepositoryImportValidation(BuildConfig.VERSION)
            val importData = validator.validate(rootNode)

            Assert.assertEquals(BuildConfig.VERSION, importData.version)
            Assert.assertNotNull(importData.jsonSettings)
            Assert.assertNotNull(importData.jsonBag)
            Assert.assertNotNull(importData.jsonRolls)
        }
    }

    @Test
    fun importInvalidFutureVersion() {
        runBlocking {
            val exportedJson = diceAndroidViewModel.exportRepositoriesAsJson()
            val mapper = jacksonObjectMapper()
            val rootNode = mapper.readTree(exportedJson) as com.fasterxml.jackson.databind.node.ObjectNode
            rootNode.put("version", "99.9.9")

            try {
                val validator = RepositoryImportValidation(BuildConfig.VERSION)
                validator.validate(rootNode)
                Assert.fail("Should have thrown RepositoryImportException")
            } catch (e: RepositoryImportException) {
                Assert.assertEquals(RepositoryImportStatus.JSON_FILE_UNKNOWN_VERSION, e.detail)
            }
        }
    }

    @Test
    fun importInvalidAdditionalProperty() {
        runBlocking {
            val exportedJson = diceAndroidViewModel.exportRepositoriesAsJson()
            val mapper = jacksonObjectMapper()
            val rootNode = mapper.readTree(exportedJson) as com.fasterxml.jackson.databind.node.ObjectNode
            (rootNode.get("settings") as com.fasterxml.jackson.databind.node.ObjectNode).put("extraProperty", true)

            try {
                val validator = RepositoryImportValidation(BuildConfig.VERSION)
                validator.validate(rootNode)
                Assert.fail("Should have thrown RepositoryImportException")
            } catch (e: RepositoryImportException) {
                Assert.assertEquals(RepositoryImportStatus.JSON_SCHEMA_SETTINGS, e.detail)
            }
        }
    }


    private fun createTmpFileFromAssetFile(
        filePath: String
    ): File {
        val tempFile = File.createTempFile("temp", null, targetContext.cacheDir)
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
