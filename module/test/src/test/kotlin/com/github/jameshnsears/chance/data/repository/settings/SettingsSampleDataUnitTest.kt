package com.github.jameshnsears.chance.data.repository.settings

import com.github.jameshnsears.chance.data.repository.SampleDataValidationUnitTestBase
import org.junit.Test

class SettingsSampleDataUnitTest : SampleDataValidationUnitTestBase() {
    @Test
    fun validateSettingsSampleData() {
        validateSettings(SettingsSampleData.settings)
    }
}
