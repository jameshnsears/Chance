package com.github.jameshnsears.chance.ui.dialog.bag.card.dice

import com.github.jameshnsears.chance.data.sample.bag.SampleBagTestData
import com.github.jameshnsears.chance.ui.dialog.bag.DialogBagUnitTestHelper
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CardDiceViewModelUnitTest : DialogBagUnitTestHelper() {
    @Test
    fun diceCardModify() = runTest {
        val diceInDialogBag = SampleBagTestData().d4

        val dialogBagAndroidViewModel = getDialogBagAndroidViewModel(
            diceInDialogBag, diceInDialogBag.sides[0]
        )

        val bagCardDiceAndroidViewModel = dialogBagAndroidViewModel.cardDiceViewModel
        var stateFlowDice = bagCardDiceAndroidViewModel.stateFlowCardDice.value

        assertTrue(stateFlowDice.diceTitle == diceInDialogBag.title)
        assertTrue(stateFlowDice.diceColour == diceInDialogBag.colour)

        val newTitle = "d4Title"
        val newColour = "00000000"
        bagCardDiceAndroidViewModel.diceTitle(newTitle)
        bagCardDiceAndroidViewModel.diceColour(newColour)

        stateFlowDice = bagCardDiceAndroidViewModel.stateFlowCardDice.value
        assertEquals(newTitle, stateFlowDice.diceTitle)
        assertEquals(newColour, stateFlowDice.diceColour)
    }

    @Test
    fun diceCardTitleIsUnique() = runTest {
        val dialogBagAndroidViewModel = getDialogBagAndroidViewModel()

        val bagCardDiceAndroidViewModel = dialogBagAndroidViewModel.cardDiceViewModel
        assertTrue(bagCardDiceAndroidViewModel.stateFlowCardDice.value.diceCanBeSaved)

        bagCardDiceAndroidViewModel.diceTitle("newTitle")
        assertTrue(bagCardDiceAndroidViewModel.stateFlowCardDice.value.diceCanBeSaved)
    }

    @Test
    fun diceCardTitleIsNotUnique() = runTest {
        val dialogBagAndroidViewModel = getDialogBagAndroidViewModel()

        val bagCardDiceAndroidViewModel = dialogBagAndroidViewModel.cardDiceViewModel

        bagCardDiceAndroidViewModel.diceTitle(SampleBagTestData().d8.title)
        assertFalse(bagCardDiceAndroidViewModel.stateFlowCardDice.value.diceCanBeCloned)

        bagCardDiceAndroidViewModel.diceTitle("")
        assertFalse(bagCardDiceAndroidViewModel.stateFlowCardDice.value.diceCanBeSaved)
        assertFalse(bagCardDiceAndroidViewModel.stateFlowCardDice.value.diceCanBeCloned)

        bagCardDiceAndroidViewModel.diceTitle(SampleBagTestData().d10.title)
        assertFalse(bagCardDiceAndroidViewModel.stateFlowCardDice.value.diceCanBeSaved)
        assertFalse(bagCardDiceAndroidViewModel.stateFlowCardDice.value.diceCanBeCloned)
    }

    @Test
    fun diceCardModifySidesMoreAndLess() = runTest {
        val dialogBagAndroidViewModel = getDialogBagAndroidViewModel()

        val bagCardDiceAndroidViewModel = dialogBagAndroidViewModel.cardDiceViewModel
        var stateFlowDice = bagCardDiceAndroidViewModel.stateFlowCardDice.value
        assertEquals(6, stateFlowDice.diceSidesSize)
        assertEquals(2.0f, stateFlowDice.diceSidesPosition)

        bagCardDiceAndroidViewModel.diceSidesSize("20")

        stateFlowDice = bagCardDiceAndroidViewModel.stateFlowCardDice.value
        assertEquals(20, stateFlowDice.diceSidesSize)
        assertEquals(6.0f, stateFlowDice.diceSidesPosition)

        bagCardDiceAndroidViewModel.diceSidesSize("2")

        stateFlowDice = bagCardDiceAndroidViewModel.stateFlowCardDice.value
        assertEquals(2, stateFlowDice.diceSidesSize)
        assertEquals(0.0f, stateFlowDice.diceSidesPosition)
    }

    @Test
    fun diceCardDeleteNotPossible() = runTest {
        val dialogBagAndroidViewModel = getDialogBagAndroidViewModel(
            SampleBagTestData().d2, SampleBagTestData().d2.sides[0]
        )

        dialogBagAndroidViewModel.repositoryBag.store(
            mutableListOf(
                SampleBagTestData().d2,
            ),
        )

        assertFalse(dialogBagAndroidViewModel.cardDiceViewModel.diceCanBeDeleted())
    }

    @Test
    fun diceCardNumberOfSidesPositionInit() {
        val dialogBagAndroidViewModel = getDialogBagAndroidViewModel()

        val bagCardDiceAndroidViewModel = dialogBagAndroidViewModel.cardDiceViewModel

        assertEquals(3.0f, bagCardDiceAndroidViewModel.diceSidesPosition(8))
        assertEquals(4.0f, bagCardDiceAndroidViewModel.diceSidesPosition(10))
        assertEquals(5.0f, bagCardDiceAndroidViewModel.diceSidesPosition(12))
    }
}
