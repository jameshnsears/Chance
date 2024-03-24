package com.github.jameshnsears.chance.ui.dialog.bag.card.roll

import com.github.jameshnsears.chance.data.domain.state.DiceRollValues
import com.github.jameshnsears.chance.data.sample.bag.SampleBag
import com.github.jameshnsears.chance.ui.dialog.bag.DialogBagUnitTestHelper
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test

class CardRollViewModelUnitTest : DialogBagUnitTestHelper() {
    /*
    explode:

    ticked

    value drop down list must == # of sides of dice

    if < then value drop down list must remove lowest side #

    if > then value drop down list must remove highest side #
     */
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
    fun rollCardExplode() = runTest {
        fail("todo")
    }

    @Test
    fun rollCardExplodeAfterChangeInDiceSides() = runTest {
        fail("todo")
    }

    @Test
    fun rollCardExplodeLessThan() = runTest {
        // update explode if < then change value range to exclude 1
        fail("todo")
    }

    @Test
    fun rollCardExplodeGreaterThan() = runTest {
        // update explode if > then change value range to exclude max side number

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
