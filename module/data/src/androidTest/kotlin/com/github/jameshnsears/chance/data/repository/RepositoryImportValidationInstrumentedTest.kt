package com.github.jameshnsears.chance.data.repository

import android.content.Context
import android.net.Uri
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import com.github.jameshnsears.chance.data.repository.bag.impl.RepositoryBagImpl
import com.github.jameshnsears.chance.data.repository.roll.impl.RepositoryRollImpl
import com.github.jameshnsears.chance.data.repository.settings.impl.RepositorySettingsImpl
import com.github.jameshnsears.chance.ui.tab.bag.ExportImportStatus
import com.github.jameshnsears.chance.ui.tab.bag.TabBagAndroidViewModel
import com.github.jameshnsears.chance.utility.logging.UtilityLoggingInstrumentedHelper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.io.File
import java.io.FileOutputStream

class RepositoryImportValidationInstrumentedTest : UtilityLoggingInstrumentedHelper() {
    private val context = InstrumentationRegistry.getInstrumentation().context

    private lateinit var tabBagAndroidViewModel: TabBagAndroidViewModel

    @Before
    fun setUp() {
        tabBagAndroidViewModel = TabBagAndroidViewModel(
            ApplicationProvider.getApplicationContext(),
            RepositorySettingsImpl.getInstance(context),
            RepositoryBagImpl.getInstance(context),
            RepositoryRollImpl.getInstance(context),
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun importInvalidEmptyJsonFile() = runTest {
        tabBagAndroidViewModel.importFileJson(Uri.fromFile(
            createTmpFileFromAssetFile(
                context,
                "data/json/import/Invalid-Empty.json"
            )
        ))

        Thread.sleep(1000)

        val stateFlowTabBagImport = tabBagAndroidViewModel.stateFlowTabBagImport.value
        assertEquals(ExportImportStatus.FAILURE, stateFlowTabBagImport.importStatus)
        assertEquals(RepositoryImportStatus.ERROR_IMPORT_EMPTY, stateFlowTabBagImport.importDetail)
    }

    @Test
    fun importValidSampleDataJsonFile() = runTest {
        tabBagAndroidViewModel.importFileJson(Uri.fromFile(
            createTmpFileFromAssetFile(
                context,
                "data/json/import/Valid-SampleBagStartup.json"
            )
        ))

        Thread.sleep(5000)

        val stateFlowTabBagImport = tabBagAndroidViewModel.stateFlowTabBagImport.value
        assertEquals(ExportImportStatus.SUCCESS, stateFlowTabBagImport.importStatus)
        assertEquals(RepositoryImportStatus.NONE, stateFlowTabBagImport.importDetail)
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
        val context = InstrumentationRegistry.getInstrumentation().context

        return context.assets.open(filePath).bufferedReader().use {
            it.readText()
        }
    }
}
