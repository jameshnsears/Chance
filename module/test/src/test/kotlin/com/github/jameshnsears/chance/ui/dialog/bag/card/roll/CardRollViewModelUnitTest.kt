package com.github.jameshnsears.chance.ui.dialog.bag.card.roll

import com.github.jameshnsears.chance.data.domain.state.DiceRollValues
import com.github.jameshnsears.chance.data.sample.bag.SampleBag
import com.github.jameshnsears.chance.ui.dialog.bag.DialogBagUnitTestHelper
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test

class CardRollViewModelUnitTest : DialogBagUnitTestHelper() {
    @Test
    fun rollCardMultiplier() = runTest {
        val diceInDialogBag = SampleBag.d4

        val dialogBagAndroidViewModel = getDialogBagAndroidViewModel(
            diceInDialogBag, diceInDialogBag.sides[0]
        )

        val cardRollViewModel = dialogBagAndroidViewModel.cardRollViewModel

        var stateFlow = cardRollViewModel.stateFlowCardRoll.value

        assertTrue(stateFlow.rollMultiplier == diceInDialogBag.multiplier)
        assertTrue(stateFlow.rollMultiplierValue == DiceRollValues.multiplierValues[0].toInt())
        assertTrue(stateFlow.rollMultiplierValue == diceInDialogBag.multiplierValue)

        val newMultiplier = true
        val newMultiplierValue = DiceRollValues.multiplierValues[1]

        cardRollViewModel.rollMultiplier(newMultiplier)
        cardRollViewModel.rollMultiplierValue(newMultiplierValue)

        stateFlow = cardRollViewModel.stateFlowCardRoll.value

        assertTrue(stateFlow.rollMultiplier == newMultiplier)
        assertTrue(stateFlow.rollMultiplierValue == newMultiplierValue.toInt())
    }

    @Test
    fun rollCardExplodeEquals() = runTest {
        val diceInDialogBag = SampleBag.d6

        val dialogBagAndroidViewModel = getDialogBagAndroidViewModel(
            diceInDialogBag, diceInDialogBag.sides[0]
        )

        val cardRollViewModel = dialogBagAndroidViewModel.cardRollViewModel

        var stateFlow = cardRollViewModel.stateFlowCardRoll.value

        assertTrue(stateFlow.rollExplode == diceInDialogBag.explode)
        assertTrue(stateFlow.rollExplodeWhen == DiceRollValues.explodeWhenValues[0])
        assertTrue(stateFlow.rollExplodeValue == 1)
        assertTrue(stateFlow.rollExplodeAvailableValues.size == 6)

        val newExplode = true
        val newExplodeValue = 3

        cardRollViewModel.rollExplode(newExplode)
        cardRollViewModel.rollExplodeValue(newExplodeValue.toString())

        stateFlow = cardRollViewModel.stateFlowCardRoll.value

        assertTrue(stateFlow.rollExplode == newExplode)
        assertTrue(stateFlow.rollExplodeValue == newExplodeValue)
    }

    @Test
    fun rollCardExplodeLessThan() = runTest {
        // if < then value drop down list must remove lowest side #
        fail("todo")
    }

    @Test
    fun rollCardExplodeGreaterThan() = runTest {
        // if > then value drop down list must remove highest side #
        fail("todo")
    }

    @Test
    fun rollCardScore() = runTest {
        val diceInDialogBag = SampleBag.d4

        val dialogBagAndroidViewModel = getDialogBagAndroidViewModel(
            diceInDialogBag, diceInDialogBag.sides[0]
        )

        val cardRollViewModel = dialogBagAndroidViewModel.cardRollViewModel

        var stateFlow = cardRollViewModel.stateFlowCardRoll.value

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
}
