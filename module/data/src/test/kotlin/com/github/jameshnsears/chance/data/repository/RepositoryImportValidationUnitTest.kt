package com.github.jameshnsears.chance.data.repository

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.jameshnsears.chance.utility.android.UtilityAndroidHelper
import kotlinx.coroutines.test.runTest
import org.junit.Test

class RepositoryImportValidationUnitTest : UtilityAndroidHelper() {
    @Test
    fun validate() = runTest {
        val dataImpl = getResourceAsString("/data/json/Chance-dataImpl.json")
        RepositoryImportValidation.validate(jacksonObjectMapper().readTree(dataImpl))

        val testDouble = getResourceAsString("/data/json/Chance-testDouble.json")
        RepositoryImportValidation.validate(jacksonObjectMapper().readTree(testDouble))
    }
}
