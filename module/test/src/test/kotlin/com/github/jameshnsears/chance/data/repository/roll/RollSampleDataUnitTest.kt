package com.github.jameshnsears.chance.data.repository.roll

import com.github.jameshnsears.chance.data.domain.Roll
import com.github.jameshnsears.chance.data.repository.SampleDataValidationUnitTestBase
import org.junit.Test

class RollSampleDataUnitTest : SampleDataValidationUnitTestBase() {
    @Test
    fun validateRollSampleData() {
        val rollHistory = RollSampleData.rollHistory

        for (epochKey in rollHistory.keys) {
            for (roll: Roll in rollHistory[epochKey]!!) {
                val dice = roll.dice
                validateDiceList(listOf(dice))

                val side = roll.side
                validateSideColour(side)
                validateSideDescriptionColour(side)

                validateDiceHasSide(dice, side)
            }
        }
    }
}
