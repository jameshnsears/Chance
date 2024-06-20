package com.github.jameshnsears.chance.ui.dialog.bag

import com.github.jameshnsears.chance.data.domain.state.Dice
import com.github.jameshnsears.chance.data.repository.bag.mock.RepositoryBagTestDouble
import com.github.jameshnsears.chance.data.sample.bag.SampleBagTestData
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class DialogBagAndroidViewModelUnitTest : DialogBagUnitTestHelper() {
    @Test
    fun dialogBagRollCardExplodeValueAfterChangeInDiceSides() = runTest {
        val dialogBagAndroidViewModel = dialogBagAndroidViewModel(SampleBagTestData().d12)

        assertTrue(
            dialogBagAndroidViewModel.cardRollViewModel
                .stateFlowCardRoll.value
                .rollExplodeAvailableValues.size == 12
        )

        dialogBagAndroidViewModel.cardDiceViewModel.diceSidesSize("8")

        assertTrue(
            dialogBagAndroidViewModel.cardRollViewModel
                .stateFlowCardRoll.value
                .rollExplodeAvailableValues.size == 8
        )
    }

    @Test
    fun dialogBagAlignDiceSidesWithDiceBagWithSidesEqual() = runTest {
        val sampleBagTestData = SampleBagTestData()

        val dialogBagAndroidViewModel = dialogBagAndroidViewModel(sampleBagTestData.d12)

        val originalDice =
            dialogBagAndroidViewModel.repositoryBag.fetch(sampleBagTestData.d12.epoch).first()
        val originalSides = originalDice.sides

        assertEquals(12, originalSides.size)

        val newSides = dialogBagAndroidViewModel.alignDiceSidesWithCardDice()

        assertEquals(newSides, originalSides)
    }

    @Test
    fun dialogBagAlignDiceSidesWithDiceBagWithSidesFewer() = runTest {
        val sampleBagTestData = SampleBagTestData()

        val dialogBagAndroidViewModel = dialogBagAndroidViewModel(sampleBagTestData.d20)

        val originalDice =
            dialogBagAndroidViewModel.repositoryBag.fetch(sampleBagTestData.d20.epoch).first()
        val originalSides = originalDice.sides

        assertEquals(20, originalSides.size)

        dialogBagAndroidViewModel.cardDiceViewModel.diceSidesSize("8")

        val newSides = dialogBagAndroidViewModel.alignDiceSidesWithCardDice()

        assertEquals(8, newSides.size)

        for (newSidesIndex in newSides.indices) {
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
                newSides[newSidesIndex].descriptionColour,
                originalSides[originalSidesIndex].descriptionColour
            )
        }
    }

    @Test
    fun dialogBagAlignDiceSidesWithDiceBagWithSidesGreater() = runTest {
        val sampleBagTestData = SampleBagTestData()

        val dialogBagAndroidViewModel = dialogBagAndroidViewModel(sampleBagTestData.d6)

        val originalDice =
            dialogBagAndroidViewModel.repositoryBag.fetch(sampleBagTestData.d6.epoch).first()
        val originalSides = originalDice.sides

        assertEquals(6, originalSides.size)

        dialogBagAndroidViewModel.cardDiceViewModel.diceSidesSize("12")

        val newSides = dialogBagAndroidViewModel.alignDiceSidesWithCardDice()

        assertEquals(12, newSides.size)

        for (newSidesIndex in originalSides.indices) {
            assertEquals(
                newSides[newSidesIndex].numberColour,
                originalSides[newSidesIndex].numberColour
            )
            assertNotEquals(
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
                newSides[newSidesIndex].descriptionColour,
                originalSides[newSidesIndex].descriptionColour
            )
        }
    }

    @Test
    fun dialogBagSaveWithApplyToAll() = runTest {
        val dialogBagAndroidViewModel = dialogBagAndroidViewModel(SampleBagTestData().d12)

        val newSideNumberColour = "FF112233"
        dialogBagAndroidViewModel.cardSideAndroidViewModel.sideNumberColour(newSideNumberColour)
        dialogBagAndroidViewModel.cardSideAndroidViewModel.sideApplyToAll(true)

        dialogBagAndroidViewModel.save()

        val d12 =
            dialogBagAndroidViewModel.repositoryBag.fetch(SampleBagTestData().d12.epoch).first()
        d12.sides.forEach {
            assertTrue(it.numberColour == newSideNumberColour)
        }
    }

    @Test
    fun dialogBagSaveAfterNotModifyingAnythingSingleDice() = runTest {
        val sampleBagTestData = SampleBagTestData()

        val dialogBagAndroidViewModel = dialogBagAndroidViewModel(sampleBagTestData.d12)

        val originalDice =
            dialogBagAndroidViewModel.repositoryBag.fetch(sampleBagTestData.d12.epoch).first()

        assertEquals(sampleBagTestData.d12.title, originalDice.title)

        val newDiceTitle = "newDiceTitle"
        dialogBagAndroidViewModel.cardDiceViewModel.diceTitle(newDiceTitle)

        dialogBagAndroidViewModel.save()

        val savedDice =
            dialogBagAndroidViewModel.repositoryBag.fetch(sampleBagTestData.d12.epoch).first()
        assertEquals(newDiceTitle, savedDice.title)

    }

    @Test
    fun dialogBagSaveAfterModifyingNumberOfSides() = runTest {
        val sampleBagTestData = SampleBagTestData()

        val dialogBagAndroidViewModel = dialogBagAndroidViewModel(sampleBagTestData.d12)

        val originalDice =
            dialogBagAndroidViewModel.repositoryBag.fetch(sampleBagTestData.d12.epoch).first()

        dialogBagAndroidViewModel.cardDiceViewModel.diceSidesSize("6")

        dialogBagAndroidViewModel.save()

        val savedDice = dialogBagAndroidViewModel.repositoryBag.fetch().first()[0]
        assertNotEquals(originalDice.epoch, savedDice.epoch)
    }

    @Test
    fun dialogBagSaveAfterNotModifyingAnythingMultipleDice() = runTest {
        val sampleBagTestData = SampleBagTestData()

        val sampleBagTestDataAllDice = sampleBagTestData.allDice

        val repositoryBag = RepositoryBagTestDouble.getInstance()
        repositoryBag.store(sampleBagTestDataAllDice)

        val dialogBagAndroidViewModel = DialogBagAndroidViewModel(
            getApplication(),
            repositoryBag,
            sampleBagTestDataAllDice[2],                // d6
            sampleBagTestDataAllDice[2].sides[0]
        )

        dialogBagAndroidViewModel.save()

        // each save changes Dice.uui
        assertNotEquals(
            sampleBagTestDataAllDice,
            dialogBagAndroidViewModel.repositoryBag.fetch().first()
        )
    }

    @Test
    fun dialogBagDelete() = runTest {
        val sampleBagTestData = SampleBagTestData()

        val diceToDelete = sampleBagTestData.d4

        val repositoryBag = RepositoryBagTestDouble.getInstance()
        repositoryBag.store(
            mutableListOf(
                sampleBagTestData.d2,
                diceToDelete,
                sampleBagTestData.d6
            )
        )

        val dialogBagAndroidViewModel = DialogBagAndroidViewModel(
            getApplication(),
            repositoryBag,
            diceToDelete,
            diceToDelete.sides[0]
        )

        dialogBagAndroidViewModel.delete()

        val remainingDice = repositoryBag.fetch().first()

        assertEquals(2, remainingDice.size)

        assertEquals(sampleBagTestData.d2, remainingDice[0])
        assertEquals(sampleBagTestData.d6, remainingDice[1])
    }

    @Test
    fun dialogBagCloneWithSidesSame() = runTest {
        val sampleBagTestData = SampleBagTestData()

        val diceToClone = sampleBagTestData.d12

        val repositoryBag = RepositoryBagTestDouble.getInstance()
        repositoryBag.store(
            mutableListOf(
                sampleBagTestData.d10,
                diceToClone,
                sampleBagTestData.d20
            )
        )

        val dialogBagAndroidViewModel = DialogBagAndroidViewModel(
            getApplication(),
            repositoryBag,
            diceToClone,
            diceToClone.sides[0]
        )

        val newTitleForClonedDice = sampleBagTestData.d12.title + " clone"
        dialogBagAndroidViewModel.cardDiceViewModel.diceTitle(newTitleForClonedDice)
        dialogBagAndroidViewModel.clone()

        val diceBagWithClonedDice = repositoryBag.fetch().first()

        assertEquals(4, diceBagWithClonedDice.size)

        assertEquals(sampleBagTestData.d10, diceBagWithClonedDice[0])
        assertEquals(diceToClone, diceBagWithClonedDice[1])

        assertNotEquals(diceToClone, diceBagWithClonedDice[2])
        assertEquals(newTitleForClonedDice, diceBagWithClonedDice[2].title)

        assertEquals(sampleBagTestData.d20, diceBagWithClonedDice[3])
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
