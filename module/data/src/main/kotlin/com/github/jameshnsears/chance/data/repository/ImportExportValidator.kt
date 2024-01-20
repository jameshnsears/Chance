package com.github.jameshnsears.chance.data.repository

class ImportExportValidator {
    fun validate(json: String) {
        if (json == "") throw ImportExportException()

        // TODO incorporate SampleDataValidationUnitTestBase rules
    }

}
