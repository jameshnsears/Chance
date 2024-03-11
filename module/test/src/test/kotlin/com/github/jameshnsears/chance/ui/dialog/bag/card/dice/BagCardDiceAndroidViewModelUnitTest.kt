package com.github.jameshnsears.chance.ui.dialog.bag.card.dice

import com.github.jameshnsears.chance.data.domain.state.Dice
import com.github.jameshnsears.chance.data.repository.bag.RepositoryBagTestDouble
import com.github.jameshnsears.chance.data.sample.bag.SampleBag
import com.github.jameshnsears.chance.ui.dialog.bag.DialogBagAndroidViewModel
import com.github.jameshnsears.chance.ui.dialog.bag.DialogBagUnitTestHelper
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test

class BagCardDiceAndroidViewModelUnitTest : DialogBagUnitTestHelper() {
    @Test
    fun diceCardModify() = runTest {
        val dialogBagAndroidViewModel = getDialogBagAndroidViewModel()
        val dice = SampleBag.d2
        assertStateDice(dialogBagAndroidViewModel, dice)

        val bagCardDiceAndroidViewModel = dialogBagAndroidViewModel.bagCardDiceAndroidViewModel
        val diceChanged = dice.copy(
            title = "2d",
            colour = "FF00FF00",
        )

        /*
DiceState(
    diceTitle = diceTitleInit(),
    diceTitleIsUnique = true,
    diceSidesSize = dice.sides.size,
    diceSidesPosition = diceSidesPositionInit(dice.sides.size),
    diceColour = dice.colour,
    diceClone = false,
    diceDelete = false,
    diceCanBeDeleted = false
)

        assertEquals(dice.title, stateFlowDice.diceTitle)
        assertTrue(stateFlowDice.diceTitleIsUnique)
        assertEquals(dice.sides.size, stateFlowDice.diceSidesSize)
        assertEquals(dice.colour, stateFlowDice.diceColour)
        assertFalse(stateFlowDice.diceClone)
        assertFalse(stateFlowDice.diceDelete)
        assertEquals(true, stateFlowDice.diceCanBeDeleted)


public final data class Dice(
    val epoch: Long = UtilityEpochTimeGenerator.now(),
    val sides: List<Side> = emptyList(),
    val title: String = "",
    val titleStringsId: Int = 0,
    val colour: String = "FF000000",
    val selected: Boolean = false,
    val multiplier: Boolean = false,
    val multiplierValue: Int = 0,
    val explode: Boolean = false,
    val explodeWhen: String = "",
    val explodeValue: Int = 0,
    val modifyScore: Boolean = false,
    val modifyScoreValue: Int = 0
)
         */

        fail("todo")
    }

    @Test
    fun diceCardTitleUnique() = runTest {
        fail("todo")
    }

    @Test
    fun diceCardTitleNotUnique() = runTest {
        fail("todo")
    }

    @Test
    fun diceCardModifySidesMore() = runTest {
        fail("todo")
    }

    @Test
    fun diceCardModifySidesFewer() = runTest {
        fail("todo")
    }

    @Test
    fun diceCardDeleteNotPossible() = runTest {
        val repositoryBag = RepositoryBagTestDouble.getInstance()
        repositoryBag.store(
            mutableListOf(
                SampleBag.d2,
            ),
        )

        assertFalse(getDialogBagAndroidViewModel().bagCardDiceAndroidViewModel.diceCanBeDeleted())
    }

    @Test
    fun diceCardDeletePossible() = runTest {
        val repositoryBag = RepositoryBagTestDouble.getInstance()
        repositoryBag.store(
            mutableListOf(
                SampleBag.d2,
                SampleBag.d4
            ),
        )

        assertTrue(getDialogBagAndroidViewModel().bagCardDiceAndroidViewModel.diceCanBeDeleted())
    }

    private fun assertStateDice(
        dialogBagAndroidViewModel: DialogBagAndroidViewModel,
        dice: Dice
    ) {
        val stateFlowDice =
            dialogBagAndroidViewModel.bagCardDiceAndroidViewModel.stateFlowDice.value

        assertEquals(dice.title, stateFlowDice.diceTitle)
        assertTrue(stateFlowDice.diceTitleIsUnique)
        assertEquals(dice.sides.size, stateFlowDice.diceSidesSize)
        assertEquals(dice.colour, stateFlowDice.diceColour)
        assertFalse(stateFlowDice.diceClone)
        assertFalse(stateFlowDice.diceDelete)
        assertEquals(true, stateFlowDice.diceCanBeDeleted)
    }
}
