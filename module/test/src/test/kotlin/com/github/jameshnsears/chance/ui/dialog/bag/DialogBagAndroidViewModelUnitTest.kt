package com.github.jameshnsears.chance.ui.dialog.bag

import com.github.jameshnsears.chance.data.domain.state.Dice
import com.github.jameshnsears.chance.data.repository.bag.mock.RepositoryBagTestDouble
import com.github.jameshnsears.chance.data.sample.bag.SampleBag
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.test.assertNotEquals

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

        val originalDice =
            dialogBagAndroidViewModel.repositoryBag.fetch(SampleBag.d12.epoch).first()
        val originalSides = originalDice.sides

        assertEquals(12, originalSides.size)

        val newSides = dialogBagAndroidViewModel.alignDiceSidesWithDiceBag()

        assertEquals(newSides, originalSides)
    }

    @Test
    fun alignDiceSidesWithDiceBagWithSidesFewer() = runTest {
        val dialogBagAndroidViewModel = dialogBagAndroidViewModel(SampleBag.d20)

        val originalDice =
            dialogBagAndroidViewModel.repositoryBag.fetch(SampleBag.d20.epoch).first()
        val originalSides = originalDice.sides

        assertEquals(20, originalSides.size)

        dialogBagAndroidViewModel.cardDiceAndroidViewModel.diceSidesSize("8")

        val newSides = dialogBagAndroidViewModel.alignDiceSidesWithDiceBag()

        assertEquals(8, newSides.size)

        for (newSidesIndex in 0..newSides.size - 1) {
            val originalSidesIndex = (originalSides.size - newSides.size) + newSidesIndex

            assertEquals(
                newSides[newSidesIndex].numberColour,
                originalSides[originalSidesIndex].numberColour
            )
            assertEquals(
                newSides[newSidesIndex].imageBase64,
                originalSides[originalSidesIndex].imageBase64
            )
            assertEquals(
                newSides[newSidesIndex].imageDrawableId,
                originalSides[originalSidesIndex].imageDrawableId
            )
            assertEquals(
                newSides[newSidesIndex].description,
                originalSides[originalSidesIndex].description
            )
            assertEquals(
                newSides[newSidesIndex].descriptionStringsId,
                originalSides[originalSidesIndex].descriptionStringsId
            )
            assertEquals(
                newSides[newSidesIndex].descriptionColour,
                originalSides[originalSidesIndex].descriptionColour
            )
        }
    }

    @Test
    fun alignDiceSidesWithDiceBagWithSidesGreater() = runTest {
        val dialogBagAndroidViewModel = dialogBagAndroidViewModel(SampleBag.d6)

        val originalDice =
            dialogBagAndroidViewModel.repositoryBag.fetch(SampleBag.d6.epoch).first()
        val originalSides = originalDice.sides

        assertEquals(6, originalSides.size)

        dialogBagAndroidViewModel.cardDiceAndroidViewModel.diceSidesSize("12")

        val newSides = dialogBagAndroidViewModel.alignDiceSidesWithDiceBag()

        assertEquals(12, newSides.size)

        for (newSidesIndex in 0..originalSides.size - 1) {
            assertEquals(
                newSides[newSidesIndex].numberColour,
                originalSides[newSidesIndex].numberColour
            )
            assertEquals(
                newSides[newSidesIndex].imageBase64,
                originalSides[newSidesIndex].imageBase64
            )
            assertEquals(
                newSides[newSidesIndex].imageDrawableId,
                originalSides[newSidesIndex].imageDrawableId
            )
            assertEquals(
                newSides[newSidesIndex].description,
                originalSides[newSidesIndex].description
            )
            assertEquals(
                newSides[newSidesIndex].descriptionStringsId,
                originalSides[newSidesIndex].descriptionStringsId
            )
            assertEquals(
                newSides[newSidesIndex].descriptionColour,
                originalSides[newSidesIndex].descriptionColour
            )
        }
    }

    @Test
    fun dialogBagSaveAfterNotModifyingAnything() = runTest {
        val dialogBagAndroidViewModel = dialogBagAndroidViewModel(SampleBag.d12)

        val originalDice =
            dialogBagAndroidViewModel.repositoryBag.fetch(SampleBag.d12.epoch).first()

        assertEquals(SampleBag.d12.title, originalDice.title)

        val newDiceTitle = "newDiceTitle"
        dialogBagAndroidViewModel.cardDiceAndroidViewModel.diceTitle(newDiceTitle)

        dialogBagAndroidViewModel.save()

        val savedDice = dialogBagAndroidViewModel.repositoryBag.fetch(SampleBag.d12.epoch).first()
        assertEquals(newDiceTitle, savedDice.title)
    }

    @Test
    fun dialogBagSaveNotPossibleAsTitleNotUnique() = runTest {
        val repositoryBag = RepositoryBagTestDouble.getInstance()
        repositoryBag.store(SampleBag.allDice)

        val diceToSave = SampleBag.d2

        val dialogBagAndroidViewModel = DialogBagAndroidViewModel(
            getApplication(),
            repositoryBag,
            diceToSave,
            diceToSave.sides[0]
        )

        dialogBagAndroidViewModel.cardDiceAndroidViewModel.diceTitle(SampleBag.d4.title)

        dialogBagAndroidViewModel.save()

        assertEquals(
            diceToSave,
            dialogBagAndroidViewModel.repositoryBag.fetch(diceToSave.epoch).first()
        )
    }

    @Test
    fun dialogBagSaveWithCloneTicked() = runTest {
        val originalDice = SampleBag.d2
        val dialogBagAndroidViewModel = dialogBagAndroidViewModel(originalDice)
        assertEquals(1, dialogBagAndroidViewModel.repositoryBag.fetch().first().size)

        dialogBagAndroidViewModel.cardDiceAndroidViewModel.diceClone(true)
        val newTitle = SampleBag.d2.title + " clone"
        dialogBagAndroidViewModel.cardDiceAndroidViewModel.diceTitle(newTitle)
        dialogBagAndroidViewModel.save()

        val savedDice = dialogBagAndroidViewModel.repositoryBag.fetch().first()

        assertEquals(2, savedDice.size)
        assertEquals(savedDice[0], originalDice)
        assertNotEquals(savedDice[0], savedDice[1])
        assertEquals(newTitle, savedDice[1].title)
    }

    @Test
    fun dialogBagSaveWithDeleteTickedAgainstDiceMiddle() = runTest {
        val startingDice = SampleBag.allDice
        val repositoryBag = RepositoryBagTestDouble.getInstance()
        repositoryBag.store(startingDice)

        val diceToDelete = SampleBag.d8

        val dialogBagAndroidViewModel = DialogBagAndroidViewModel(
            getApplication(),
            repositoryBag,
            diceToDelete,
            diceToDelete.sides[0]
        )

        assertEquals(7, dialogBagAndroidViewModel.repositoryBag.fetch().first().size)

        dialogBagAndroidViewModel.cardDiceAndroidViewModel.diceDelete(true)
        dialogBagAndroidViewModel.save()

        val remainingDice = dialogBagAndroidViewModel.repositoryBag.fetch().first()

        assertEquals(6, remainingDice.size)
        assertEquals(startingDice[0], remainingDice[0])
        assertEquals(startingDice[1], remainingDice[1])
        assertEquals(startingDice[2], remainingDice[2])
        assertEquals(startingDice[4], remainingDice[3])
        assertEquals(startingDice[5], remainingDice[4])
        assertEquals(startingDice[6], remainingDice[5])
    }

    @Test
    fun dialogBagSaveWithDeleteTickedAgainstDiceFirst() = runTest {
        val startingDice = SampleBag.allDice
        val repositoryBag = RepositoryBagTestDouble.getInstance()
        repositoryBag.store(startingDice)

        val diceToDelete = SampleBag.d2

        val dialogBagAndroidViewModel = DialogBagAndroidViewModel(
            getApplication(),
            repositoryBag,
            diceToDelete,
            diceToDelete.sides[0]
        )

        assertEquals(7, dialogBagAndroidViewModel.repositoryBag.fetch().first().size)

        dialogBagAndroidViewModel.cardDiceAndroidViewModel.diceDelete(true)
        dialogBagAndroidViewModel.save()

        val remainingDice = dialogBagAndroidViewModel.repositoryBag.fetch().first()

        assertEquals(6, remainingDice.size)
        assertEquals(startingDice[1], remainingDice[0])
        assertEquals(startingDice[2], remainingDice[1])
        assertEquals(startingDice[3], remainingDice[2])
        assertEquals(startingDice[4], remainingDice[3])
        assertEquals(startingDice[5], remainingDice[4])
        assertEquals(startingDice[6], remainingDice[5])
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
