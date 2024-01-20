package com.github.jameshnsears.chance.data.repository.bag

import com.github.jameshnsears.chance.data.repository.SampleDataValidationUnitTestBase
import org.junit.Test

class BagDemoSampleDataUnitTest : SampleDataValidationUnitTestBase() {
    @Test
    fun validateBagDemoSampleData() {
        validateDiceList(BagDemoSampleData.allDice)
    }
}
