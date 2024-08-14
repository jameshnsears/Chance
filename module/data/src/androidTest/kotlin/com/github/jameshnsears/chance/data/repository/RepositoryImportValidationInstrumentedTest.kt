package com.github.jameshnsears.chance.data.repository

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.test.core.app.ApplicationProvider
import androidx.test.filters.SdkSuppress
import androidx.test.platform.app.InstrumentationRegistry
import com.github.jameshnsears.chance.ui.tab.bag.ExportImportStatus
import com.github.jameshnsears.chance.ui.tab.bag.TabBagAndroidViewModel
import com.github.jameshnsears.chance.ui.tab.bag.TabBagImportEvent
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream

class RepositoryImportValidationInstrumentedTest : RepositoryInstrumentedHelper() {
    private val context = InstrumentationRegistry.getInstrumentation().context

    private val applicationContext: Application = ApplicationProvider.getApplicationContext()

    private lateinit var tabBagAndroidViewModel: TabBagAndroidViewModel

    private lateinit var repositoryFactory: RepositoryFactory

    @Before
    fun setUp() {
        repositoryFactory = RepositoryFactory(context)

        tabBagAndroidViewModel = TabBagAndroidViewModel(
            applicationContext,
            repositoryFactory.repositorySettings,
            repositoryFactory.repositoryBag,
            repositoryFactory.repositoryRoll,
            3
        )
    }

    @SdkSuppress(minSdkVersion = 30)
    @Test
    fun importInvalidEmptyJsonFile() = runTest {
        val tempFile = createTmpFileFromAssetFile(
            context,
            "data/json/import/Invalid-Empty.json"
        )

        tabBagAndroidViewModel.importFileJson(
            Uri.fromFile(
                tempFile
            )
        )

        waitForCI()

        val collectorJob = launch {
            TabBagImportEvent.sharedFlowTabBagImportEvent.collect {
                val stateFlowTabBagImport = tabBagAndroidViewModel.stateFlowTabBagImport.value
                assertEquals(ExportImportStatus.FAILURE, stateFlowTabBagImport.importStatus)
                assertEquals(
                    RepositoryImportStatus.ERROR_IMPORT_EMPTY,
                    stateFlowTabBagImport.importDetail
                )
            }
        }

        collectorJob.cancel()
    }

    private fun waitForCI() {
        repeat(60) {
            if (tabBagAndroidViewModel.stateFlowTabBagImport.value.importStatus == ExportImportStatus.IMPORT_STARTED) {
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

        tabBagAndroidViewModel.importFileJson(
            Uri.fromFile(
                tempFile
            )
        )

        waitForCI()

        val collectorJob = launch {
            TabBagImportEvent.sharedFlowTabBagImportEvent.collect {
                val stateFlowTabBagImport = tabBagAndroidViewModel.stateFlowTabBagImport.value
                assertEquals(ExportImportStatus.SUCCESS, stateFlowTabBagImport.importStatus)
                assertEquals(RepositoryImportStatus.NONE, stateFlowTabBagImport.importDetail)
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
