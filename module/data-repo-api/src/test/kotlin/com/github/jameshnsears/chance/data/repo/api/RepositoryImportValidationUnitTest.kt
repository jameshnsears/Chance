package com.github.jameshnsears.chance.data.repo.api

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.jameshnsears.chance.common.utility.UtilityAndroidUnitTestHelper
import kotlinx.coroutines.test.runTest
import org.junit.Test

class RepositoryImportValidationUnitTest : UtilityAndroidUnitTestHelper() {
    @Test
    fun validate() = runTest {
        val dataImpl = resourceAsString("/data/json/Chance-dataImpl.json")
        RepositoryImportValidation.validate(jacksonObjectMapper().readTree(dataImpl))

        val testDouble = resourceAsString("/data/json/Chance-testDouble.json")
        RepositoryImportValidation.validate(jacksonObjectMapper().readTree(testDouble))
    }
}
