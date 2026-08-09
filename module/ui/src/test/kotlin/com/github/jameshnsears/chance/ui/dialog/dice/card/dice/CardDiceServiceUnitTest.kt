package com.github.jameshnsears.chance.ui.dialog.dice.card.dice

import com.github.jameshnsears.chance.data.common.repo.RepositoryFactory
import com.github.jameshnsears.chance.data.domain.core.DiceRollValues
import com.github.jameshnsears.chance.data.domain.core.bag.testdouble.BagDataTestDouble
import com.github.jameshnsears.chance.data.domain.core.roll.Roll
import com.github.jameshnsears.chance.ui.dialog.dice.DialogDiceUnitTestUnitTestHelper
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CardDiceServiceUnitTest : DialogDiceUnitTestUnitTestHelper() {
    @Test
    fun diceCardModify() = runTest {
        val diceInDialogBag = BagDataTestDouble().d4

        val dialogBagAndroidViewModel = getDialogBagAndroidViewModel(
            diceInDialogBag, diceInDialogBag.sides[0]
        )

        val bagCardDiceAndroidViewModel = dialogBagAndroidViewModel.cardDiceService
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

        val bagCardDiceAndroidViewModel = dialogBagAndroidViewModel.cardDiceService
        assertTrue(bagCardDiceAndroidViewModel.stateFlowCardDice.value.diceCanBeSaved)

        bagCardDiceAndroidViewModel.diceTitle("newTitle")
        assertTrue(bagCardDiceAndroidViewModel.stateFlowCardDice.value.diceCanBeSaved)
    }

    @Test
    fun diceCardTitleIsNotUnique() = runTest {
        val dialogBagAndroidViewModel = getDialogBagAndroidViewModel()

        val bagCardDiceAndroidViewModel = dialogBagAndroidViewModel.cardDiceService

        bagCardDiceAndroidViewModel.diceTitle(BagDataTestDouble().d8.title)
        assertFalse(bagCardDiceAndroidViewModel.stateFlowCardDice.value.diceCanBeCloned)

        bagCardDiceAndroidViewModel.diceTitle("")
        assertFalse(bagCardDiceAndroidViewModel.stateFlowCardDice.value.diceCanBeSaved)
        assertFalse(bagCardDiceAndroidViewModel.stateFlowCardDice.value.diceCanBeCloned)

        bagCardDiceAndroidViewModel.diceTitle(BagDataTestDouble().d10.title)
        assertFalse(bagCardDiceAndroidViewModel.stateFlowCardDice.value.diceCanBeSaved)
        assertFalse(bagCardDiceAndroidViewModel.stateFlowCardDice.value.diceCanBeCloned)
    }

    @Test
    fun diceCardModifySidesMoreAndLess() = runTest {
        val dialogBagAndroidViewModel = getDialogBagAndroidViewModel()

        val bagCardDiceAndroidViewModel = dialogBagAndroidViewModel.cardDiceService
        var stateFlowDice = bagCardDiceAndroidViewModel.stateFlowCardDice.value
        assertEquals(6, stateFlowDice.diceSidesSize)

        bagCardDiceAndroidViewModel.diceSidesSize("20")

        stateFlowDice = bagCardDiceAndroidViewModel.stateFlowCardDice.value
        assertEquals(20, stateFlowDice.diceSidesSize)

        bagCardDiceAndroidViewModel.diceSidesSize("2")

        stateFlowDice = bagCardDiceAndroidViewModel.stateFlowCardDice.value
        assertEquals(2, stateFlowDice.diceSidesSize)
    }

    @Test
    fun diceCardModifySidesInvalid() = runTest {
        val dialogBagAndroidViewModel = getDialogBagAndroidViewModel()

        val bagCardDiceAndroidViewModel = dialogBagAndroidViewModel.cardDiceService
        assertTrue(bagCardDiceAndroidViewModel.stateFlowCardDice.value.diceCanBeSaved)

        bagCardDiceAndroidViewModel.diceSidesSize((DiceRollValues.SIDES_MIN - 1).toString())
        assertFalse(bagCardDiceAndroidViewModel.stateFlowCardDice.value.diceCanBeSaved)

        bagCardDiceAndroidViewModel.diceSidesSize((DiceRollValues.SIDES_MAX + 1).toString())
        assertFalse(bagCardDiceAndroidViewModel.stateFlowCardDice.value.diceCanBeSaved)

        bagCardDiceAndroidViewModel.diceSidesSize("")
        assertFalse(bagCardDiceAndroidViewModel.stateFlowCardDice.value.diceCanBeSaved)

        bagCardDiceAndroidViewModel.diceSidesSize(DiceRollValues.SIDES_MIN.toString())
        assertTrue(bagCardDiceAndroidViewModel.stateFlowCardDice.value.diceCanBeSaved)

        bagCardDiceAndroidViewModel.diceSidesSize(DiceRollValues.SIDES_MAX.toString())
        assertTrue(bagCardDiceAndroidViewModel.stateFlowCardDice.value.diceCanBeSaved)
    }

    @Test
    fun diceCardDeleteNotPossible() = runTest {
        val dialogBagAndroidViewModel = getDialogBagAndroidViewModel(
            BagDataTestDouble().d2, BagDataTestDouble().d2.sides[0]
        )

        dialogBagAndroidViewModel.repositoryBag.store(
            mutableListOf(
                BagDataTestDouble().d2,
            ),
        )

        assertFalse(dialogBagAndroidViewModel.cardDiceService.diceCanBeDeleted())
    }

    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    @Test
    fun diceCardModifySidesDeletesRollHistory() = runTest {
        RepositoryFactory().resetStorage()

        val dialogBagAndroidViewModel = getDialogBagAndroidViewModel()
        val repositoryBag = dialogBagAndroidViewModel.repositoryBag
        val repositoryRoll = dialogBagAndroidViewModel.repositoryRoll

        val diceInRepo = repositoryBag.fetch().first().first { it.title == "d6" }

        // Add a roll for this dice to ensure we have something to delete
        val roll = Roll(
            uuidDice = diceInRepo.uuid,
            side = diceInRepo.sides[0]
        )
        repositoryRoll.store(System.currentTimeMillis(), listOf(roll))

        val cardDiceService = CardDiceService(
            repositoryBag,
            repositoryRoll,
            diceInRepo
        )

        val initialRolls = repositoryRoll.fetch().first()
        assertTrue(initialRolls.values.any { rolls -> rolls.any { it.uuidDice == diceInRepo.uuid } })

        cardDiceService.diceSidesSize("10")

        advanceUntilIdle()

        val updatedRolls = repositoryRoll.fetch().first()
        val hasRollsForDice = updatedRolls.values.any { rolls -> rolls.any { it.uuidDice == diceInRepo.uuid } }
        assertFalse("Roll history for dice should have been deleted", hasRollsForDice)
    }
}
