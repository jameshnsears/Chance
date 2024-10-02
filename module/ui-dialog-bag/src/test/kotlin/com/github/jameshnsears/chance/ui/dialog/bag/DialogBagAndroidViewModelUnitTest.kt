package com.github.jameshnsears.chance.ui.dialog.bag

import com.github.jameshnsears.chance.data.domain.core.Dice
import com.github.jameshnsears.chance.data.domain.core.Side
import com.github.jameshnsears.chance.data.domain.core.bag.testdouble.BagDataTestDouble
import com.github.jameshnsears.chance.data.repository.RepositoryFactory
import com.github.jameshnsears.chance.data.repository.bag.testdouble.RepositoryBagTestDouble
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class DialogBagAndroidViewModelUnitTest : DialogBagUnitTestHelper() {
    @Test
    fun dialogBagRollCardExplodeValueAfterChangeInDiceSides() = runTest {
        val dialogBagAndroidViewModel = dialogBagAndroidViewModel(BagDataTestDouble().d12)

        assertTrue(
            dialogBagAndroidViewModel.cardRollViewModel.stateFlowCardRoll.value.rollExplodeAvailableValues.size == 12
        )

        dialogBagAndroidViewModel.cardDiceViewModel.diceSidesSize("8")

        assertTrue(
            dialogBagAndroidViewModel.cardRollViewModel.stateFlowCardRoll.value.rollExplodeAvailableValues.size == 8
        )
    }

    @Test
    fun updateRepositoryBagWithNewSizedDiceOfSameSize() = runTest {
        val bagDataTestDouble = BagDataTestDouble()

        val dialogBagAndroidViewModel = dialogBagAndroidViewModel(bagDataTestDouble.d12)

        val originalDice =
            dialogBagAndroidViewModel.repositoryBag.fetch(bagDataTestDouble.d12.epoch).first()
        val originalSides = originalDice.sides

        assertEquals(12, originalSides.size)

        val newSides = dialogBagAndroidViewModel.updateRepositoryBagWithNewSizedDice(originalDice)

        assertEquals(newSides, originalSides)
    }

    private fun List<Side>.deepCopy(): List<Side> {
        return this.map { it.copy() }
    }

    @Test
    fun cardSideDescriptionAndSvgApplyToAll() = runTest {
        val bagDataTestDouble = BagDataTestDouble()

        val dialogBagAndroidViewModel = dialogBagAndroidViewModel(bagDataTestDouble.d6)

        val originalDice =
            dialogBagAndroidViewModel.repositoryBag.fetch(bagDataTestDouble.d6.epoch).first()

        val originalSides = originalDice.sides.deepCopy()

        assertEquals(6, originalSides.size)

        val newSideNumberColour = "newSideNumberColour"
        dialogBagAndroidViewModel.cardSideAndroidViewModel.sideNumberColour(newSideNumberColour)
        dialogBagAndroidViewModel.cardSideAndroidViewModel.sideApplyToAllNumberColour(true)

        val newDescription = "newDescription"
        dialogBagAndroidViewModel.cardSideAndroidViewModel.sideDescription(
            newDescription
        )
        val newDescriptionColour = "newDescriptionColour"
        dialogBagAndroidViewModel.cardSideAndroidViewModel.sideDescriptionColour(
            newDescriptionColour
        )
        dialogBagAndroidViewModel.cardSideAndroidViewModel.sideApplyToAllDescription(true)

        dialogBagAndroidViewModel.cardSideAndroidViewModel.sideImageSvgClear()
        dialogBagAndroidViewModel.cardSideAndroidViewModel.sideApplyToAllSvg(true)

        dialogBagAndroidViewModel.save()

        val newSides =
            dialogBagAndroidViewModel.repositoryBag.fetch(bagDataTestDouble.d6.epoch).first().sides

        for (newSide in newSides.indices) {
            assertEquals(newSideNumberColour, newSides[newSide].numberColour)

            assertEquals(newDescription, newSides[newSide].description)
            assertEquals(newDescriptionColour, newSides[newSide].descriptionColour)

            assertEquals("", newSides[newSide].imageBase64)
            assertEquals(0, newSides[newSide].imageDrawableId)
        }
    }

    @Test
    fun cardSideNumberColourApplyToAll() = runTest {
        val bagDataTestDouble = BagDataTestDouble()

        val dialogBagAndroidViewModel = dialogBagAndroidViewModel(bagDataTestDouble.diceStory)

        val originalDice =
            dialogBagAndroidViewModel.repositoryBag.fetch(bagDataTestDouble.diceStory.epoch).first()

        val originalSides = originalDice.sides.deepCopy()

        val newSideNumberColour = "newSideNumberColour"
        dialogBagAndroidViewModel.cardSideAndroidViewModel.sideNumberColour(newSideNumberColour)
        dialogBagAndroidViewModel.cardSideAndroidViewModel.sideApplyToAllNumberColour(true)

        dialogBagAndroidViewModel.save()

        val newSides =
            dialogBagAndroidViewModel.repositoryBag.fetch(bagDataTestDouble.diceStory.epoch)
                .first().sides

        for (newSide in newSides.indices) {
            assertEquals(newSideNumberColour, newSides[newSide].numberColour)

            assertEquals(originalSides[newSide].imageBase64, newSides[newSide].imageBase64)
            assertEquals(originalSides[newSide].imageDrawableId, newSides[newSide].imageDrawableId)
            assertEquals(originalSides[newSide].description, newSides[newSide].description)
            assertEquals(
                originalSides[newSide].descriptionColour,
                newSides[newSide].descriptionColour
            )
        }
    }

    @Test
    fun dialogBagAlignDiceSidesWithDiceBagWithSidesFewer() = runTest {
        val bagDataTestDouble = BagDataTestDouble()

        val dialogBagAndroidViewModel = dialogBagAndroidViewModel(bagDataTestDouble.d20)

        val originalDice =
            dialogBagAndroidViewModel.repositoryBag.fetch(bagDataTestDouble.d20.epoch).first()
        val originalSides = originalDice.sides

        assertEquals(20, originalSides.size)

        dialogBagAndroidViewModel.cardDiceViewModel.diceSidesSize("8")

        dialogBagAndroidViewModel.save()

        val newDice = dialogBagAndroidViewModel.repositoryBag.fetch().first()[0]

        assertTrue(originalDice.epoch != newDice.epoch)

        val newSides = newDice.sides

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
        val bagDataTestDouble = BagDataTestDouble()

        val dialogBagAndroidViewModel = dialogBagAndroidViewModel(bagDataTestDouble.d6)

        assertEquals(1, dialogBagAndroidViewModel.repositoryBag.fetch().first().size)

        val originalDice =
            dialogBagAndroidViewModel.repositoryBag.fetch(bagDataTestDouble.d6.epoch).first()
        val originalSides = originalDice.sides

        assertEquals(6, originalSides.size)

        dialogBagAndroidViewModel.cardDiceViewModel.diceSidesSize("12")

        assertEquals(1, dialogBagAndroidViewModel.repositoryBag.fetch().first().size)

        dialogBagAndroidViewModel.save()

        val diceBag = dialogBagAndroidViewModel.repositoryBag.fetch().first()
        assertEquals(1, diceBag.size)

        val newDice = diceBag[0]
        assertNotEquals(originalDice.epoch, newDice.epoch)

        val newSides =
            dialogBagAndroidViewModel.repositoryBag.fetch(newDice.epoch)
                .first().sides

        assertEquals(12, newSides.size)

        for (newSidesIndex in originalSides.indices) {
            assertEquals(
                newSides[newSidesIndex].numberColour,
                originalSides[newSidesIndex].numberColour
            )
            assertEquals(
                newSides[newSidesIndex].imageBase64,
                originalSides[newSidesIndex].imageBase64
            )
            assertEquals(
                newSides[newSidesIndex].description,
                originalSides[newSidesIndex].description
            )
        }
    }

    @Test
    fun dialogBagSaveWithApplyToAll() = runTest {
        val dialogBagAndroidViewModel = dialogBagAndroidViewModel(BagDataTestDouble().d12)

        val newSideNumberColour = "FF112233"
        dialogBagAndroidViewModel.cardSideAndroidViewModel.sideNumberColour(newSideNumberColour)
        dialogBagAndroidViewModel.cardSideAndroidViewModel.sideApplyToAllSvg(true)

        dialogBagAndroidViewModel.save()

        val d12 =
            dialogBagAndroidViewModel.repositoryBag.fetch(BagDataTestDouble().d12.epoch).first()
        d12.sides.forEach {
            assertTrue(it.numberColour == newSideNumberColour)
        }
    }

    @Test
    fun dialogBagSaveAfterNotModifyingAnythingSingleDice() = runTest {
        val bagDataTestDouble = BagDataTestDouble()

        val dialogBagAndroidViewModel = dialogBagAndroidViewModel(bagDataTestDouble.d12)

        val originalDice =
            dialogBagAndroidViewModel.repositoryBag.fetch(bagDataTestDouble.d12.epoch).first()

        assertEquals(bagDataTestDouble.d12.title, originalDice.title)

        val newDiceTitle = "newDiceTitle"
        dialogBagAndroidViewModel.cardDiceViewModel.diceTitle(newDiceTitle)

        dialogBagAndroidViewModel.save()

        val savedDice =
            dialogBagAndroidViewModel.repositoryBag.fetch(bagDataTestDouble.d12.epoch).first()
        assertEquals(newDiceTitle, savedDice.title)

    }

    @Test
    fun dialogBagSaveAfterModifyingNumberOfSides() = runTest {
        val bagDataTestDouble = BagDataTestDouble()

        val dialogBagAndroidViewModel = dialogBagAndroidViewModel(bagDataTestDouble.d12)

        val originalDice =
            dialogBagAndroidViewModel.repositoryBag.fetch(bagDataTestDouble.d12.epoch).first()

        dialogBagAndroidViewModel.cardDiceViewModel.diceSidesSize("6")

        dialogBagAndroidViewModel.save()

        val savedDice = dialogBagAndroidViewModel.repositoryBag.fetch().first()[0]
        assertNotEquals(originalDice.epoch, savedDice.epoch)
    }

    @Test
    fun dialogBagSaveAfterNotModifyingAnythingMultipleDice() = runTest {
        val bagDataTestDouble = BagDataTestDouble()

        val sampleBagTestDataAllDice = bagDataTestDouble.allDice

        val repositoryBag = RepositoryBagTestDouble.getInstance(sampleBagTestDataAllDice)

        val dialogBagAndroidViewModel = DialogBagAndroidViewModel(
            getApplication(), repositoryBag, sampleBagTestDataAllDice[2],
            sampleBagTestDataAllDice[2].sides[0]
        )

        dialogBagAndroidViewModel.save()

        // each save changes Dice.uui
        assertNotEquals(
            sampleBagTestDataAllDice, dialogBagAndroidViewModel.repositoryBag.fetch().first()
        )
    }

    @Test
    fun dialogBagDelete() = runTest {
        val bagDataTestDouble = BagDataTestDouble()

        val diceToDelete = bagDataTestDouble.d4

        val repositoryBag = RepositoryBagTestDouble.getInstance(
            mutableListOf(
                bagDataTestDouble.d2, diceToDelete, bagDataTestDouble.d6
            )
        )

        val dialogBagAndroidViewModel = DialogBagAndroidViewModel(
            getApplication(), repositoryBag, diceToDelete, diceToDelete.sides[0]
        )

        dialogBagAndroidViewModel.delete()

        val remainingDice = repositoryBag.fetch().first()

        assertEquals(2, remainingDice.size)

        assertEquals(bagDataTestDouble.d2, remainingDice[0])
        assertEquals(bagDataTestDouble.d6, remainingDice[1])
    }

    @Test
    fun dialogBagCloneWithSidesSame() = runTest {
        val bagDataTestDouble = BagDataTestDouble()

        val diceToClone = bagDataTestDouble.d12

        val repositoryBag = RepositoryFactory().repositoryBag
        repositoryBag.store(
            mutableListOf(
                bagDataTestDouble.d10, diceToClone, bagDataTestDouble.d20
            )
        )

        val dialogBagAndroidViewModel = DialogBagAndroidViewModel(
            getApplication(), repositoryBag, diceToClone, diceToClone.sides[0]
        )

        val newTitleForClonedDice = bagDataTestDouble.d12.title + " clone"
        dialogBagAndroidViewModel.cardDiceViewModel.diceTitle(newTitleForClonedDice)
        dialogBagAndroidViewModel.clone()

        val diceBagWithClonedDice = repositoryBag.fetch().first()

        assertEquals(4, diceBagWithClonedDice.size)

        assertEquals(bagDataTestDouble.d10, diceBagWithClonedDice[0])
        assertEquals(diceToClone, diceBagWithClonedDice[1])

        assertNotEquals(diceToClone, diceBagWithClonedDice[2])
        assertEquals(newTitleForClonedDice, diceBagWithClonedDice[2].title)

        assertEquals(bagDataTestDouble.d20, diceBagWithClonedDice[3])
    }

    @Test
    fun removeRollSequenceWithDiceThatBeenDeleted() = runTest {
        val repositoryFactory = RepositoryFactory()

        val repositoryBag = repositoryFactory.repositoryBag
        repositoryBag.store(repositoryFactory.bagDataTestDouble.allDice)
        assertEquals(8, repositoryBag.fetch().first().size)

        val repositoryRoll = repositoryFactory.repositoryRoll
        repositoryRoll.store(repositoryFactory.rollHistoryDataTestDouble)
        assertEquals(2, repositoryRoll.fetch().first().size)

        val dialogBagAndroidViewModel = DialogBagAndroidViewModel(
            getApplication(),
            repositoryBag,
            repositoryFactory.bagDataTestDouble.allDice[0],
            repositoryFactory.bagDataTestDouble.allDice[0].sides[0]
        )

        dialogBagAndroidViewModel.save()

        assertEquals(8, repositoryBag.fetch().first().size)

        dialogBagAndroidViewModel.delete()

        assertEquals(7, repositoryBag.fetch().first().size)

        assertEquals(1, repositoryRoll.fetch().first().size)
    }

    @Test
    fun removeRollSequenceWithDiceWhereNumberOfSidesChanged() = runTest {
        val repositoryFactory = RepositoryFactory()

        val repositoryBag = repositoryFactory.repositoryBag
        repositoryBag.store(repositoryFactory.bagDataTestDouble.allDice)
        assertEquals(8, repositoryBag.fetch().first().size)

        val repositoryRoll = repositoryFactory.repositoryRoll
        repositoryRoll.store(repositoryFactory.rollHistoryDataTestDouble)
        assertEquals(2, repositoryRoll.fetch().first().size)

        val dialogBagAndroidViewModel = DialogBagAndroidViewModel(
            getApplication(),
            repositoryBag,
            repositoryFactory.bagDataTestDouble.allDice[0],
            repositoryFactory.bagDataTestDouble.allDice[0].sides[0]
        )

        dialogBagAndroidViewModel.cardDiceViewModel.diceSidesSize("4")

        dialogBagAndroidViewModel.save()

        assertEquals(8, repositoryBag.fetch().first().size)

        assertEquals(1, repositoryRoll.fetch().first().size)
    }

    private suspend fun dialogBagAndroidViewModel(dice: Dice): DialogBagAndroidViewModel {
        val repositoryBag = RepositoryFactory().repositoryBag
        repositoryBag.store(mutableListOf(dice))

        return DialogBagAndroidViewModel(
            getApplication(), repositoryBag, dice, dice.sides[0]
        )
    }
}
