package com.github.jameshnsears.chance.data.repository

import com.github.jameshnsears.chance.data.domain.Dice
import com.github.jameshnsears.chance.data.domain.Settings
import com.github.jameshnsears.chance.data.domain.Side
import junit.framework.TestCase.assertTrue
import junit.framework.TestCase.fail

open class SampleDataValidationUnitTestBase {
    fun validateDiceList(diceList: List<Dice>) {
        diceEpochMustBeUnique(diceList)

        diceColourMustBeEightCharactersLong(diceList)

        diceMustHaveValidNumberOfSides(diceList)

        diceTitleMustBeUniqueAndNotEmpty(diceList)

        sideColourMustBeEightCharactersLong(diceList)

        sideDescriptionColourMustBeEightCharactersLong(diceList)
    }

    fun validateDiceHasSide(dice: Dice, side: Side) {
        assertTrue(dice.sides.contains(side))
    }

    fun validateSettings(settings: Settings) {
        if (settings.tabRowChance !in setOf(0, 1)) fail()
    }

    private fun diceEpochMustBeUnique(diceList: List<Dice>) {
        val epochs = mutableListOf<Long>()

        for (dice: Dice in diceList) {
            if (dice.epoch in epochs)
                fail()
            else
                epochs.add(dice.epoch)
        }
    }

    private fun diceColourMustBeEightCharactersLong(diceList: List<Dice>) {
        for (dice: Dice in diceList) {
            if (dice.colour.length != 8) fail()
        }
    }

    private fun diceMustHaveValidNumberOfSides(diceList: List<Dice>) {
        for (dice: Dice in diceList) {
            if (dice.sides.size !in setOf(2, 4, 6, 8, 10, 12, 20)) fail()
        }
    }

    private fun diceTitleMustBeUniqueAndNotEmpty(diceList: List<Dice>) {
        val titles = mutableListOf<String>()

        val titleStringsIds = mutableListOf<Int>()

        for (dice: Dice in diceList) {
            if (dice.title == "" && dice.titleStringsId == 0) fail()

            if (dice.title != "" && dice.titleStringsId == 0)
                if (dice.title in titles)
                    fail()
                else
                    titles.add(dice.title)

            if (dice.title == "" && dice.titleStringsId != 0)
                if (dice.titleStringsId in titleStringsIds)
                    fail()
                else
                    titleStringsIds.add(dice.titleStringsId)
        }
    }

    private fun sideColourMustBeEightCharactersLong(diceList: List<Dice>) {
        for (dice: Dice in diceList) {
            for (side: Side in dice.sides) {
                validateSideColour(side)
            }
        }
    }

    fun validateSideColour(side: Side) {
        if (side.colour.length != 8) fail()
    }

    private fun sideDescriptionColourMustBeEightCharactersLong(diceList: List<Dice>) {
        for (dice: Dice in diceList) {
            for (side: Side in dice.sides) {
                validateSideDescriptionColour(side)
            }
        }
    }

    fun validateSideDescriptionColour(side: Side) {
        if (side.descriptionColour.length != 8) fail()
    }
}
