package com.github.jameshnsears.chance.ui.tab.bag.dialog.bag

import com.github.jameshnsears.chance.data.common.repository.RepositoryFactory
import com.github.jameshnsears.chance.data.domain.core.Dice
import com.github.jameshnsears.chance.data.domain.core.Side
import com.github.jameshnsears.chance.data.domain.core.bag.testdouble.BagDataTestDouble
import com.github.jameshnsears.chance.data.repo.impl.bag.testdouble.RepositoryBagProtocolBufferTestDouble
import com.github.jameshnsears.chance.ui.dialog.bag.DialogBagAndroidViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class DialogBagAndroidViewModelUnitTest : DialogBagUnitTestUnitTestHelper() {
    @Test
    fun dialogBagRollCardExplodeValueAfterChangeInDiceSides() = runTest {
        val dialogBagAndroidViewModel = dialogBagAndroidViewModel(BagDataTestDouble().d12)

        assertTrue(
            dialogBagAndroidViewModel.cardRollService.stateFlowCardRoll.value.rollExplodeAvailableValues.size == 12
        )

        dialogBagAndroidViewModel.cardDiceService.diceSidesSize("8")

        val updatedState = dialogBagAndroidViewModel.cardDiceService.stateFlowCardDice.first { it.diceSidesSize == 8 }

        assertEquals(8, updatedState.diceSidesSize)
    }

    @Test
    fun updateRepositoryBagWithNewSizedDiceOfSameSize() = runTest {
        val bagDataTestDouble = BagDataTestDouble()

        val dialogBagAndroidViewModel = dialogBagAndroidViewModel(bagDataTestDouble.d12)

        val originalDice =
            dialogBagAndroidViewModel.repositoryBag.fetch(bagDataTestDouble.d12.epoch).first()
        val originalSides = originalDice.sides

        assertEquals(12, originalSides.size)

        val newSides = dialogBagAndroidViewModel.updateRepositoryBagWithNewSizedDice(
            originalDice,
            originalDice.sides.size
        )

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
        dialogBagAndroidViewModel.cardSideService.sideNumberColour(newSideNumberColour)
        dialogBagAndroidViewModel.cardSideService.sideApplyToAllNumberColour(true)

        val newDescription = "newDescription"
        dialogBagAndroidViewModel.cardSideService.sideDescription(
            newDescription
        )
        val newDescriptionColour = "newDescriptionColour"
        dialogBagAndroidViewModel.cardSideService.sideDescriptionColour(
            newDescriptionColour
        )
        dialogBagAndroidViewModel.cardSideService.sideApplyToAllDescription(true)

        dialogBagAndroidViewModel.cardSideService.sideImageSvgClear()
        dialogBagAndroidViewModel.cardSideService.sideApplyToAllSvg(true)

        dialogBagAndroidViewModel.save(
            dialogBagAndroidViewModel.cardDiceService.stateFlowCardDice.value,
            dialogBagAndroidViewModel.cardRollService.stateFlowCardRoll.value,
            dialogBagAndroidViewModel.cardSideService.stateFlowCardSide.value
        )

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
        dialogBagAndroidViewModel.cardSideService.sideNumberColour(newSideNumberColour)
        dialogBagAndroidViewModel.cardSideService.sideApplyToAllNumberColour(true)

        dialogBagAndroidViewModel.save(
            dialogBagAndroidViewModel.cardDiceService.stateFlowCardDice.value,
            dialogBagAndroidViewModel.cardRollService.stateFlowCardRoll.value,
            dialogBagAndroidViewModel.cardSideService.stateFlowCardSide.value
        )
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

        dialogBagAndroidViewModel.cardDiceService.diceSidesSize("8")

        dialogBagAndroidViewModel.save(
            dialogBagAndroidViewModel.cardDiceService.stateFlowCardDice.value,
            dialogBagAndroidViewModel.cardRollService.stateFlowCardRoll.value,
            dialogBagAndroidViewModel.cardSideService.stateFlowCardSide.value
        )

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

        dialogBagAndroidViewModel.cardDiceService.diceSidesSize("12")

        assertEquals(1, dialogBagAndroidViewModel.repositoryBag.fetch().first().size)

        dialogBagAndroidViewModel.save(
            dialogBagAndroidViewModel.cardDiceService.stateFlowCardDice.value,
            dialogBagAndroidViewModel.cardRollService.stateFlowCardRoll.value,
            dialogBagAndroidViewModel.cardSideService.stateFlowCardSide.value
        )

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
        dialogBagAndroidViewModel.cardSideService.sideNumberColour(newSideNumberColour)
        dialogBagAndroidViewModel.cardSideService.sideApplyToAllSvg(true)

        dialogBagAndroidViewModel.save(
            dialogBagAndroidViewModel.cardDiceService.stateFlowCardDice.value,
            dialogBagAndroidViewModel.cardRollService.stateFlowCardRoll.value,
            dialogBagAndroidViewModel.cardSideService.stateFlowCardSide.value
        )

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
        dialogBagAndroidViewModel.cardDiceService.diceTitle(newDiceTitle)

        dialogBagAndroidViewModel.save(
            dialogBagAndroidViewModel.cardDiceService.stateFlowCardDice.value,
            dialogBagAndroidViewModel.cardRollService.stateFlowCardRoll.value,
            dialogBagAndroidViewModel.cardSideService.stateFlowCardSide.value
        )

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

        dialogBagAndroidViewModel.cardDiceService.diceSidesSize("6")

        dialogBagAndroidViewModel.save(
            dialogBagAndroidViewModel.cardDiceService.stateFlowCardDice.value,
            dialogBagAndroidViewModel.cardRollService.stateFlowCardRoll.value,
            dialogBagAndroidViewModel.cardSideService.stateFlowCardSide.value
        )

        val savedDice = dialogBagAndroidViewModel.repositoryBag.fetch().first()[0]
        assertNotEquals(originalDice.epoch, savedDice.epoch)
    }

    @Test
    fun dialogBagSaveAfterNotModifyingAnythingMultipleDice() = runTest {
        val bagDataTestDouble = BagDataTestDouble()

        val sampleBagTestDataAllDice = bagDataTestDouble.allDice

        val repositoryBag = RepositoryBagProtocolBufferTestDouble.getInstance(sampleBagTestDataAllDice)

        val dialogBagAndroidViewModel = DialogBagAndroidViewModel(
            application(), repositoryBag, sampleBagTestDataAllDice[2],
            sampleBagTestDataAllDice[2].sides[0]
        )

        dialogBagAndroidViewModel.save(
            dialogBagAndroidViewModel.cardDiceService.stateFlowCardDice.value,
            dialogBagAndroidViewModel.cardRollService.stateFlowCardRoll.value,
            dialogBagAndroidViewModel.cardSideService.stateFlowCardSide.value
        )

        assertNotEquals(
            sampleBagTestDataAllDice, dialogBagAndroidViewModel.repositoryBag.fetch().first()
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun dialogBagDelete() = runTest {
        val bagDataTestDouble = BagDataTestDouble()

        val diceToDelete = bagDataTestDouble.d4

        val repositoryBag = RepositoryBagProtocolBufferTestDouble.getInstance(
            mutableListOf(
                bagDataTestDouble.d2, diceToDelete, bagDataTestDouble.d6
            )
        )

        val dialogBagAndroidViewModel = DialogBagAndroidViewModel(
            application(), repositoryBag, diceToDelete, diceToDelete.sides[0]
        )

        dialogBagAndroidViewModel.delete()

        advanceUntilIdle()

        val remainingDice = repositoryBag.fetch().first()

        assertEquals(2, remainingDice.size)
        assertFalse(remainingDice.contains(diceToDelete))
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
            application(), repositoryBag, diceToClone, diceToClone.sides[0]
        )

        val newTitleForClonedDice = bagDataTestDouble.d12.title + " clone"
        dialogBagAndroidViewModel.cardDiceService.diceTitle(newTitleForClonedDice)
        dialogBagAndroidViewModel.clone(
            dialogBagAndroidViewModel.cardDiceService.stateFlowCardDice.value,
            dialogBagAndroidViewModel.cardRollService.stateFlowCardRoll.value
        )

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
        repositoryRoll.store(repositoryFactory.rollHistoryTestDouble)
        assertEquals(2, repositoryRoll.fetch().first().size)

        val dialogBagAndroidViewModel = DialogBagAndroidViewModel(
            application(),
            repositoryBag,
            repositoryFactory.bagDataTestDouble.allDice[0],
            repositoryFactory.bagDataTestDouble.allDice[0].sides[0]
        )

        dialogBagAndroidViewModel.save(
            dialogBagAndroidViewModel.cardDiceService.stateFlowCardDice.value,
            dialogBagAndroidViewModel.cardRollService.stateFlowCardRoll.value,
            dialogBagAndroidViewModel.cardSideService.stateFlowCardSide.value
        )

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
        repositoryRoll.store(repositoryFactory.rollHistoryTestDouble)
        assertEquals(2, repositoryRoll.fetch().first().size)

        val dialogBagAndroidViewModel = DialogBagAndroidViewModel(
            application(),
            repositoryBag,
            repositoryFactory.bagDataTestDouble.allDice[0],
            repositoryFactory.bagDataTestDouble.allDice[0].sides[0]
        )

        dialogBagAndroidViewModel.cardDiceService.diceSidesSize("4")

        dialogBagAndroidViewModel.save(
            dialogBagAndroidViewModel.cardDiceService.stateFlowCardDice.value,
            dialogBagAndroidViewModel.cardRollService.stateFlowCardRoll.value,
            dialogBagAndroidViewModel.cardSideService.stateFlowCardSide.value
        )

        assertEquals(8, repositoryBag.fetch().first().size)

        assertEquals(1, repositoryRoll.fetch().first().size)
    }

    private suspend fun dialogBagAndroidViewModel(dice: Dice): DialogBagAndroidViewModel {
        val repositoryBag = RepositoryFactory().repositoryBag
        repositoryBag.store(mutableListOf(dice))

        return DialogBagAndroidViewModel(
            application(), repositoryBag, dice, dice.sides[0]
        )
    }
}
