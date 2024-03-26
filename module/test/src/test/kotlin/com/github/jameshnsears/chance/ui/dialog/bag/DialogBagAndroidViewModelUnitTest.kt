package com.github.jameshnsears.chance.ui.dialog.bag

import com.github.jameshnsears.chance.data.domain.state.Dice
import com.github.jameshnsears.chance.data.repository.bag.mock.RepositoryBagTestDouble
import com.github.jameshnsears.chance.data.sample.bag.SampleBag
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test

class DialogBagAndroidViewModelUnitTest : DialogBagUnitTestHelper() {
    @Test
    fun dialogBagRollCardExplodeAfterChangeInDiceSides() = runTest {
        val dialogBagAndroidViewModel = dialogBagAndroidViewModel(SampleBag.d12)

        assertTrue(
            dialogBagAndroidViewModel.cardRollViewModel
                .stateFlowCardRoll.value
                .rollExplodeAvailableValues.size == 12
        )

        dialogBagAndroidViewModel.cardDiceAndroidViewModel.diceSidesSize("8")

        assertTrue(
            dialogBagAndroidViewModel.cardRollViewModel
                .stateFlowCardRoll.value
                .rollExplodeAvailableValues.size == 8
        )
    }

    @Test
    fun alignDiceSidesWithDiceBagWithSidesEqual() = runTest {
        val dialogBagAndroidViewModel = dialogBagAndroidViewModel(SampleBag.d12)

        val originalDice = dialogBagAndroidViewModel.repositoryBag.fetch(SampleBag.d12.epoch).first()
        val originalSides = originalDice.sides

        assertEquals(12, originalSides.size)

        val newSides = dialogBagAndroidViewModel.alignDiceSidesWithDiceBag()

        assertEquals(12, newSides.size)

        newSides.forEachIndexed { index, newSide ->
            assertEquals(newSide, originalSides[index])
        }
    }

    @Test
    fun alignDiceSidesWithDiceBagWithSidesFewer() = runTest {
        fail("todo wip")
    }

    @Test
    fun alignDiceSidesWithDiceBagWithSidesGreater() = runTest {
        fail("todo wip")
    }

    @Test
    fun dialogBagSaveAfterNotModifyingAnything() = runTest {
        val dialogBagAndroidViewModel = dialogBagAndroidViewModel(SampleBag.d12)

        val originalDice = dialogBagAndroidViewModel.repositoryBag.fetch(SampleBag.d12.epoch).first()

        assertEquals(SampleBag.d12.title, originalDice.title)

        val newDiceTitle = "newDiceTitle"
        dialogBagAndroidViewModel.cardDiceAndroidViewModel.diceTitle(newDiceTitle)

        dialogBagAndroidViewModel.save()

        val savedDice = dialogBagAndroidViewModel.repositoryBag.fetch(SampleBag.d12.epoch).first()
        assertEquals(newDiceTitle, savedDice.title)
    }

    @Test
    fun dialogBagSaveNotPossibleAsTitleNotUnique() = runTest {
        fail("todo")
    }

    @Test
    fun dialogBagSaveWithCloneTicked() = runTest {
        fail("todo")
    }

    @Test
    fun dialogBagSaveWithDeleteTicked() = runTest {
        fail("todo + include androidTest for all of the above")
    }

    private suspend fun dialogBagAndroidViewModel(dice: Dice): DialogBagAndroidViewModel {
        val repositoryBag = RepositoryBagTestDouble.getInstance()
        repositoryBag.store(mutableListOf(dice))

        return DialogBagAndroidViewModel(
            getApplication(),
            repositoryBag,
            dice,
            dice.sides[0]
        )
    }
}
