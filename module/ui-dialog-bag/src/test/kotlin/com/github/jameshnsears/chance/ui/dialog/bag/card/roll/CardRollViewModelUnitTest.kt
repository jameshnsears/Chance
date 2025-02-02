package com.github.jameshnsears.chance.ui.dialog.bag.card.roll

import com.github.jameshnsears.chance.data.domain.core.Dice
import com.github.jameshnsears.chance.data.domain.core.DiceRollValues
import com.github.jameshnsears.chance.data.domain.core.bag.testdouble.BagDataTestDouble
import com.github.jameshnsears.chance.ui.dialog.bag.DialogBagAndroidViewModel
import com.github.jameshnsears.chance.ui.dialog.bag.DialogBagUnitTestHelper
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Test

class CardRollViewModelUnitTest : DialogBagUnitTestHelper() {
    @Test
    fun rollCardMultiplier() = runTest {
        val (diceInDialogBag, dialogBagAndroidViewModel) = getDialogBagAndroidViewModel(
            BagDataTestDouble().d4
        )

        val pair = cardRollViewModelCardRollStatePair(dialogBagAndroidViewModel)
        val cardRollViewModel = pair.first
        var stateFlow = pair.second

        assertTrue(stateFlow.rollMultiplierValue == 2)
        assertTrue(stateFlow.rollMultiplierValue == diceInDialogBag.multiplierValue)

        val newMultiplierValue = DiceRollValues.multiplierValues[1]

        cardRollViewModel.rollMultiplierValue(newMultiplierValue)

        stateFlow = cardRollViewModel.stateFlowCardRoll.value

        assertTrue(stateFlow.rollMultiplierValue == newMultiplierValue.toInt())
    }

    @Test
    fun rollCardExplodeEquals() = runTest {
        val (diceInDialogBag, dialogBagAndroidViewModel) = getDialogBagAndroidViewModel(
            BagDataTestDouble().d6
        )

        var (cardRollViewModel, stateFlow) = cardRollViewModelCardRollStatePair(
            dialogBagAndroidViewModel
        )

        assertTrue(stateFlow.rollExplode == diceInDialogBag.explode)
        assertTrue(stateFlow.rollExplodeWhen == DiceRollValues.explodeWhenValues[0])
        assertTrue(stateFlow.rollExplodeValue == 1)
        assertTrue(stateFlow.rollExplodeAvailableValues.size == 6)

        val newExplode = true
        val newExplodeValue = 4

        cardRollViewModel.rollExplode(newExplode)
        cardRollViewModel.rollExplodeWhen(DiceRollValues.explodeWhenValues[0])
        cardRollViewModel.rollExplodeValue(newExplodeValue.toString())

        stateFlow = cardRollViewModel.stateFlowCardRoll.value

        assertTrue(stateFlow.rollExplode == newExplode)
        assertTrue(stateFlow.rollExplodeValue == newExplodeValue)
    }

    @Test
    fun rollCardExplodeLessThan() = runTest {
        // if < then value drop down list must remove lowest side #
        val (_, dialogBagAndroidViewModel) = getDialogBagAndroidViewModel(BagDataTestDouble().d20)

        val (cardRollViewModel, _) = cardRollViewModelCardRollStatePair(
            dialogBagAndroidViewModel
        )

        cardRollViewModel.rollExplodeWhen(DiceRollValues.explodeWhenValues[1])

        val stateFlow = cardRollViewModel.stateFlowCardRoll.value

        assertTrue(stateFlow.rollExplodeAvailableValues.size == BagDataTestDouble().d20.sides.size - 1)
        assertTrue(stateFlow.rollExplodeAvailableValues.first() == "2")
        assertTrue(stateFlow.rollExplodeAvailableValues.last() == "20")
    }

    @Test
    fun rollCardExplodeGreaterThan() = runTest {
        // if > then value drop down list must remove highest side #
        val (_, dialogBagAndroidViewModel) = getDialogBagAndroidViewModel(BagDataTestDouble().d20)

        val (cardRollViewModel, _) = cardRollViewModelCardRollStatePair(
            dialogBagAndroidViewModel
        )

        cardRollViewModel.rollExplodeWhen(DiceRollValues.explodeWhenValues[2])

        val stateFlow = cardRollViewModel.stateFlowCardRoll.value

        assertTrue(stateFlow.rollExplodeAvailableValues.size == BagDataTestDouble().d20.sides.size - 1)
        assertTrue(stateFlow.rollExplodeAvailableValues.first() == "1")
        assertTrue(stateFlow.rollExplodeAvailableValues.last() == "19")
    }

    @Test
    fun rollCardScore() = runTest {
        val (diceInDialogBag, dialogBagAndroidViewModel) = getDialogBagAndroidViewModel(
            BagDataTestDouble().d4
        )

        var (cardRollViewModel, stateFlow) = cardRollViewModelCardRollStatePair(
            dialogBagAndroidViewModel
        )

        assertTrue(stateFlow.rollModifyScore == diceInDialogBag.modifyScore)
        assertTrue(stateFlow.rollModifyScoreValue == DiceRollValues.modifyScoreValues[0].toInt())
        assertTrue(stateFlow.rollModifyScoreValue == diceInDialogBag.modifyScoreValue)

        val newModifyScore = true
        val newModifyScoreValue = DiceRollValues.multiplierValues[1]

        cardRollViewModel.rollModifyScore(newModifyScore)
        cardRollViewModel.rollModifyScoreValue(newModifyScoreValue)

        stateFlow = cardRollViewModel.stateFlowCardRoll.value

        assertTrue(stateFlow.rollModifyScore == newModifyScore)
        assertTrue(stateFlow.rollModifyScoreValue == newModifyScoreValue.toInt())
    }

    private fun cardRollViewModelCardRollStatePair(dialogBagAndroidViewModel: DialogBagAndroidViewModel):
        Pair<CardRollViewModel, CardRollState> {
        return Pair(
            dialogBagAndroidViewModel.cardRollViewModel,
            dialogBagAndroidViewModel.cardRollViewModel.stateFlowCardRoll.value
        )
    }

    private fun getDialogBagAndroidViewModel(dice: Dice): Pair<Dice, DialogBagAndroidViewModel> {
        val dialogBagAndroidViewModel = getDialogBagAndroidViewModel(
            dice, dice.sides[0]
        )

        return Pair(dice, dialogBagAndroidViewModel)
    }
}
