package com.github.jameshnsears.chance.ui.dialog.bag.card.dice

import com.github.jameshnsears.chance.data.sample.bag.SampleBag
import com.github.jameshnsears.chance.ui.dialog.bag.DialogBagUnitTestHelper
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CardDiceAndroidViewModelUnitTest : DialogBagUnitTestHelper() {
    @Test
    fun diceCardModify() = runTest {
        // leaving sides as is, for other tests
        val diceInDialogBag = SampleBag.d4

        val dialogBagAndroidViewModel = getDialogBagAndroidViewModel(
            diceInDialogBag, diceInDialogBag.sides[0]
        )

        val bagCardDiceAndroidViewModel = dialogBagAndroidViewModel.cardDiceAndroidViewModel
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

        val bagCardDiceAndroidViewModel = dialogBagAndroidViewModel.cardDiceAndroidViewModel
        assertTrue(bagCardDiceAndroidViewModel.stateFlowCardDice.value.diceTitleIsUnique)

        bagCardDiceAndroidViewModel.diceTitle("newTitle")
        assertTrue(bagCardDiceAndroidViewModel.stateFlowCardDice.value.diceTitleIsUnique)
    }

    @Test
    fun diceCardTitleIsNotUnique() = runTest {
        val dialogBagAndroidViewModel = getDialogBagAndroidViewModel()

        val bagCardDiceAndroidViewModel = dialogBagAndroidViewModel.cardDiceAndroidViewModel
        bagCardDiceAndroidViewModel.diceTitle(SampleBag.d2.title)
        assertFalse(bagCardDiceAndroidViewModel.stateFlowCardDice.value.diceTitleIsUnique)
    }

    @Test
    fun diceCardModifySidesMoreAndLess() = runTest {
        val dialogBagAndroidViewModel = getDialogBagAndroidViewModel()

        val bagCardDiceAndroidViewModel = dialogBagAndroidViewModel.cardDiceAndroidViewModel
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
            SampleBag.d2, SampleBag.d2.sides[0]
        )

        dialogBagAndroidViewModel.repositoryBag.store(
            mutableListOf(
                SampleBag.d2,
            ),
        )

        assertFalse(dialogBagAndroidViewModel.cardDiceAndroidViewModel.diceCanBeDeleted())
    }

    @Test
    fun diceCardDeletePossible() = runTest {
        assertTrue(
            getDialogBagAndroidViewModel(
                SampleBag.d2, SampleBag.d2.sides[0]
            ).cardDiceAndroidViewModel.diceCanBeDeleted()
        )
    }

    @Test
    fun diceCardDeletePressed() = runTest {
        val dialogBagAndroidViewModel = getDialogBagAndroidViewModel()

        val bagCardDiceAndroidViewModel = dialogBagAndroidViewModel.cardDiceAndroidViewModel
        var stateFlowDice = bagCardDiceAndroidViewModel.stateFlowCardDice.value
        assertFalse(stateFlowDice.diceDelete)

        bagCardDiceAndroidViewModel.diceDelete(true)

        stateFlowDice = bagCardDiceAndroidViewModel.stateFlowCardDice.value
        assertTrue(stateFlowDice.diceDelete)
    }

    @Test
    fun diceCardClonePressed() = runTest {
        val dialogBagAndroidViewModel = getDialogBagAndroidViewModel()

        val bagCardDiceAndroidViewModel = dialogBagAndroidViewModel.cardDiceAndroidViewModel
        var stateFlowDice = bagCardDiceAndroidViewModel.stateFlowCardDice.value
        assertFalse(stateFlowDice.diceClone)

        bagCardDiceAndroidViewModel.diceClone(true)

        stateFlowDice = bagCardDiceAndroidViewModel.stateFlowCardDice.value
        assertTrue(stateFlowDice.diceClone)
    }

    @Test
    fun diceSidesPositionInit() {
        val dialogBagAndroidViewModel = getDialogBagAndroidViewModel()

        val bagCardDiceAndroidViewModel = dialogBagAndroidViewModel.cardDiceAndroidViewModel

        assertEquals(3.0f, bagCardDiceAndroidViewModel.diceSidesPosition(8))
        assertEquals(4.0f, bagCardDiceAndroidViewModel.diceSidesPosition(10))
        assertEquals(5.0f, bagCardDiceAndroidViewModel.diceSidesPosition(12))
    }
}
