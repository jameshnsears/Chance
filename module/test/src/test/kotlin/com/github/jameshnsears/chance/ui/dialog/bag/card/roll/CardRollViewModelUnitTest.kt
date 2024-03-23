package com.github.jameshnsears.chance.ui.dialog.bag.card.roll

import com.github.jameshnsears.chance.data.domain.state.Dice
import com.github.jameshnsears.chance.ui.dialog.bag.DialogBagAndroidViewModel
import com.github.jameshnsears.chance.ui.dialog.bag.DialogBagUnitTestHelper
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Test

class CardRollViewModelUnitTest : DialogBagUnitTestHelper() {
    @Test
    fun rollCardMultiplier() = runTest {
        fail("todo")
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

        fail("todo")
    }

    private fun assertStateRoll(
        dialogBagAndroidViewModel: DialogBagAndroidViewModel,
        dice: Dice
    ) {
        val stateFlowRoll =
            dialogBagAndroidViewModel.cardRollViewModel.stateFlowCardRoll.value

        assertEquals(dice.multiplier, stateFlowRoll.rollMultiplier)
        assertEquals(dice.multiplierValue, stateFlowRoll.rollMultiplierValue)
        assertEquals(dice.explode, stateFlowRoll.rollExplode)
        assertEquals(dice.explodeWhen, stateFlowRoll.rollExplodeWhen)
        assertEquals(dice.explodeValue, stateFlowRoll.rollExplodeValue)
        assertEquals(dice.modifyScore, stateFlowRoll.rollModifyScore)
        assertEquals(dice.modifyScoreValue, stateFlowRoll.rollModifyScoreValue)
    }
}
