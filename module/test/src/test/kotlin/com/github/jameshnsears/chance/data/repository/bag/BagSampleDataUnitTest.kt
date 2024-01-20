package com.github.jameshnsears.chance.data.repository.bag

import com.github.jameshnsears.chance.data.repository.SampleDataValidationUnitTestBase
import org.junit.Test

class BagSampleDataUnitTest : SampleDataValidationUnitTestBase() {
    @Test
    fun validateBagSampleData() {
        validateDiceList(BagSampleData.allDice)
    }
}
