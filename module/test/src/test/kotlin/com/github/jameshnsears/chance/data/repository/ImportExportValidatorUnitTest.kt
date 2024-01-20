package com.github.jameshnsears.chance.data.repository

import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertThrows
import org.junit.Test

class ImportExportValidatorUnitTest {
    @Test
    fun importEmpty() = runTest {
        val jsonToImport = ""

        val importExportValidator = ImportExportValidator()

        assertThrows(ImportExportException::class.java) {
            importExportValidator.validate(jsonToImport)
        }
    }
}
