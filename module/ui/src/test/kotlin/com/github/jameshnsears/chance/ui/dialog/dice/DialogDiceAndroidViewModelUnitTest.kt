package com.github.jameshnsears.chance.ui.dialog.dice

import com.github.jameshnsears.chance.data.common.repo.RepositoryFactory
import com.github.jameshnsears.chance.data.domain.core.Dice
import com.github.jameshnsears.chance.data.domain.core.Side
import com.github.jameshnsears.chance.data.domain.core.bag.testdouble.BagDataTestDouble
import com.github.jameshnsears.chance.data.domain.core.group.Group
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class DialogDiceAndroidViewModelUnitTest : DialogDiceUnitTestUnitTestHelper() {
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
            dialogBagAndroidViewModel.repositoryBag.fetch(bagDataTestDouble.d12.uuid).first()
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

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun cardSideDescriptionAndSvgApplyToAll() = runTest {
        val bagDataTestDouble = BagDataTestDouble()

        val dialogBagAndroidViewModel = dialogBagAndroidViewModel(bagDataTestDouble.d6)

        val originalDice =
            dialogBagAndroidViewModel.repositoryBag.fetch(bagDataTestDouble.d6.uuid).first()

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

        advanceUntilIdle()

        val newSides =
            dialogBagAndroidViewModel.repositoryBag.fetch(bagDataTestDouble.d6.uuid).first().sides

        for (newSide in newSides.indices) {
            assertEquals(newSideNumberColour, newSides[newSide].numberColour)

            assertEquals(newDescription, newSides[newSide].description)
            assertEquals(newDescriptionColour, newSides[newSide].descriptionColour)

            assertEquals("", newSides[newSide].imageBase64)
            assertEquals(0, newSides[newSide].imageDrawableId)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun cardSideNumberColourApplyToAll() = runTest {
        val bagDataTestDouble = BagDataTestDouble()

        val dialogBagAndroidViewModel = dialogBagAndroidViewModel(bagDataTestDouble.diceStory)

        val originalDice =
            dialogBagAndroidViewModel.repositoryBag.fetch(bagDataTestDouble.diceStory.uuid).first()

        val originalSides = originalDice.sides.deepCopy()

        val newSideNumberColour = "newSideNumberColour"
        dialogBagAndroidViewModel.cardSideService.sideNumberColour(newSideNumberColour)
        dialogBagAndroidViewModel.cardSideService.sideApplyToAllNumberColour(true)

        dialogBagAndroidViewModel.save(
            dialogBagAndroidViewModel.cardDiceService.stateFlowCardDice.value,
            dialogBagAndroidViewModel.cardRollService.stateFlowCardRoll.value,
            dialogBagAndroidViewModel.cardSideService.stateFlowCardSide.value
        )

        advanceUntilIdle()

        val newSides =
            dialogBagAndroidViewModel.repositoryBag.fetch(bagDataTestDouble.diceStory.uuid)
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

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun dialogBagAlignDiceSidesWithDiceBagWithSidesFewer() = runTest {
        val bagDataTestDouble = BagDataTestDouble()

        val dialogBagAndroidViewModel = dialogBagAndroidViewModel(bagDataTestDouble.d20)

        val originalDice =
            dialogBagAndroidViewModel.repositoryBag.fetch(bagDataTestDouble.d20.uuid).first()
        val originalSides = originalDice.sides

        assertEquals(20, originalSides.size)

        dialogBagAndroidViewModel.cardDiceService.diceSidesSize("8")

        dialogBagAndroidViewModel.save(
            dialogBagAndroidViewModel.cardDiceService.stateFlowCardDice.value,
            dialogBagAndroidViewModel.cardRollService.stateFlowCardRoll.value,
            dialogBagAndroidViewModel.cardSideService.stateFlowCardSide.value
        )

        advanceUntilIdle()

        val newDice = dialogBagAndroidViewModel.repositoryBag.fetch().first()[0]

        assertTrue(originalDice.uuid != newDice.uuid)

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

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun dialogBagAlignDiceSidesWithDiceBagWithSidesGreater() = runTest {
        val bagDataTestDouble = BagDataTestDouble()

        val dialogBagAndroidViewModel = dialogBagAndroidViewModel(bagDataTestDouble.d6)

        assertEquals(1, dialogBagAndroidViewModel.repositoryBag.fetch().first().size)

        val originalDice =
            dialogBagAndroidViewModel.repositoryBag.fetch(bagDataTestDouble.d6.uuid).first()
        val originalSides = originalDice.sides

        assertEquals(6, originalSides.size)

        dialogBagAndroidViewModel.cardDiceService.diceSidesSize("12")

        assertEquals(1, dialogBagAndroidViewModel.repositoryBag.fetch().first().size)

        dialogBagAndroidViewModel.save(
            dialogBagAndroidViewModel.cardDiceService.stateFlowCardDice.value,
            dialogBagAndroidViewModel.cardRollService.stateFlowCardRoll.value,
            dialogBagAndroidViewModel.cardSideService.stateFlowCardSide.value
        )

        advanceUntilIdle()

        val diceBag = dialogBagAndroidViewModel.repositoryBag.fetch().first()
        assertEquals(1, diceBag.size)

        val newDice = diceBag[0]
        assertNotEquals(originalDice.uuid, newDice.uuid)

        val newSides =
            dialogBagAndroidViewModel.repositoryBag.fetch(newDice.uuid)
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

    @OptIn(ExperimentalCoroutinesApi::class)
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

        advanceUntilIdle()

        val d12 =
            dialogBagAndroidViewModel.repositoryBag.fetch(BagDataTestDouble().d12.uuid).first()
        d12.sides.forEach {
            assertTrue(it.numberColour == newSideNumberColour)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun dialogBagSaveAfterNotModifyingAnythingSingleDice() = runTest {
        val bagDataTestDouble = BagDataTestDouble()

        val dialogBagAndroidViewModel = dialogBagAndroidViewModel(bagDataTestDouble.d12)

        val originalDice =
            dialogBagAndroidViewModel.repositoryBag.fetch(bagDataTestDouble.d12.uuid).first()

        assertEquals(bagDataTestDouble.d12.title, originalDice.title)

        val newDiceTitle = "newDiceTitle"
        dialogBagAndroidViewModel.cardDiceService.diceTitle(newDiceTitle)

        dialogBagAndroidViewModel.save(
            dialogBagAndroidViewModel.cardDiceService.stateFlowCardDice.value,
            dialogBagAndroidViewModel.cardRollService.stateFlowCardRoll.value,
            dialogBagAndroidViewModel.cardSideService.stateFlowCardSide.value
        )

        advanceUntilIdle()

        val savedDice =
            dialogBagAndroidViewModel.repositoryBag.fetch(bagDataTestDouble.d12.uuid).first()
        assertEquals(newDiceTitle, savedDice.title)

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun dialogBagSaveAfterModifyingNumberOfSides() = runTest {
        val bagDataTestDouble = BagDataTestDouble()

        val dialogBagAndroidViewModel = dialogBagAndroidViewModel(bagDataTestDouble.d12)

        val originalDice =
            dialogBagAndroidViewModel.repositoryBag.fetch(bagDataTestDouble.d12.uuid).first()

        dialogBagAndroidViewModel.cardDiceService.diceSidesSize("6")

        dialogBagAndroidViewModel.save(
            dialogBagAndroidViewModel.cardDiceService.stateFlowCardDice.value,
            dialogBagAndroidViewModel.cardRollService.stateFlowCardRoll.value,
            dialogBagAndroidViewModel.cardSideService.stateFlowCardSide.value
        )

        advanceUntilIdle()

        val savedDice = dialogBagAndroidViewModel.repositoryBag.fetch().first()[0]
        assertNotEquals(originalDice.uuid, savedDice.uuid)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun dialogBagSaveAfterNotModifyingAnythingMultipleDice() = runTest {
        val bagDataTestDouble = BagDataTestDouble()
        val sampleBagTestDataAllDice = bagDataTestDouble.allDice

        val repositoryBag = RepositoryFactory().repositoryBag
        repositoryBag.store(sampleBagTestDataAllDice)

        val dialogDiceAndroidViewModel = DialogDiceAndroidViewModel(
            application(), repositoryBag, sampleBagTestDataAllDice[2],
            sampleBagTestDataAllDice[2].sides[0]
        )

        dialogDiceAndroidViewModel.save(
            dialogDiceAndroidViewModel.cardDiceService.stateFlowCardDice.value,
            dialogDiceAndroidViewModel.cardRollService.stateFlowCardRoll.value,
            dialogDiceAndroidViewModel.cardSideService.stateFlowCardSide.value
        )

        advanceUntilIdle()

        // After save, the dice in the repository should be equivalent (even if not identical instances)
        val fetchedDiceBag = repositoryBag.fetch().first()
        assertEquals(sampleBagTestDataAllDice.size, fetchedDiceBag.size)
        // We use title and epoch comparison as serialization might affect data class equality slightly if defaults differ
        for (i in sampleBagTestDataAllDice.indices) {
            assertEquals(sampleBagTestDataAllDice[i].uuid, fetchedDiceBag[i].uuid)
            assertEquals(sampleBagTestDataAllDice[i].title, fetchedDiceBag[i].title)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun dialogBagDelete() = runTest {
        val bagDataTestDouble = BagDataTestDouble()
        val diceToDelete = bagDataTestDouble.d4

        val repositoryBag = RepositoryFactory().repositoryBag
        repositoryBag.store(
            mutableListOf(
                bagDataTestDouble.d2, diceToDelete, bagDataTestDouble.d6
            )
        )

        val dialogDiceAndroidViewModel = DialogDiceAndroidViewModel(
            application(), repositoryBag, diceToDelete, diceToDelete.sides[0]
        )

        dialogDiceAndroidViewModel.delete()

        advanceUntilIdle()

        val remainingDice = repositoryBag.fetch().first()

        assertEquals(2, remainingDice.size)
        assertFalse(remainingDice.any { it.uuid == diceToDelete.uuid })
    }

    @OptIn(ExperimentalCoroutinesApi::class)
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

        // Fetch from repo to get the "repo version" (with same epochs as what's in the repo)
        val initialDiceBag = repositoryBag.fetch().first()
        val d12FromRepo = initialDiceBag[1]

        val dialogDiceAndroidViewModel = DialogDiceAndroidViewModel(
            application(), repositoryBag, d12FromRepo, d12FromRepo.sides[0]
        )

        val newTitleForClonedDice = d12FromRepo.title + " clone"
        dialogDiceAndroidViewModel.cardDiceService.diceTitle(newTitleForClonedDice)

        dialogDiceAndroidViewModel.clone(
            dialogDiceAndroidViewModel.cardDiceService.stateFlowCardDice.value,
            dialogDiceAndroidViewModel.cardRollService.stateFlowCardRoll.value
        )

        advanceUntilIdle()

        val diceBagWithClonedDice = repositoryBag.fetch().first()

        assertEquals(4, diceBagWithClonedDice.size)

        assertEquals(initialDiceBag[0].epoch, diceBagWithClonedDice[0].epoch)
        assertEquals(initialDiceBag[1].epoch, diceBagWithClonedDice[1].epoch)

        // The clone
        assertEquals(newTitleForClonedDice, diceBagWithClonedDice[2].title)
        assertNotEquals(initialDiceBag[1].uuid, diceBagWithClonedDice[2].uuid)

        assertEquals(initialDiceBag[2].uuid, diceBagWithClonedDice[3].uuid)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun removeRollSequenceWithDiceThatBeenDeleted() = runTest {
        val repositoryFactory = RepositoryFactory()

        val repositoryBag = repositoryFactory.repositoryBag
        repositoryBag.store(repositoryFactory.bagDataTestDouble.allDice)
        assertEquals(7, repositoryBag.fetch().first().size)

        val repositoryRoll = repositoryFactory.repositoryRoll
        repositoryRoll.store(repositoryFactory.rollHistoryTestDouble)
        assertEquals(2, repositoryRoll.fetch().first().size)

        val dialogDiceAndroidViewModel = DialogDiceAndroidViewModel(
            application(),
            repositoryBag,
            repositoryFactory.bagDataTestDouble.allDice[0],
            repositoryFactory.bagDataTestDouble.allDice[0].sides[0]
        )

        dialogDiceAndroidViewModel.save(
            dialogDiceAndroidViewModel.cardDiceService.stateFlowCardDice.value,
            dialogDiceAndroidViewModel.cardRollService.stateFlowCardRoll.value,
            dialogDiceAndroidViewModel.cardSideService.stateFlowCardSide.value
        )

        advanceUntilIdle()

        assertEquals(7, repositoryBag.fetch().first().size)

        dialogDiceAndroidViewModel.delete()

        advanceUntilIdle()

        assertEquals(6, repositoryBag.fetch().first().size)

        assertEquals(1, repositoryRoll.fetch().first().size)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun removeRollSequenceWithDiceWhereNumberOfSidesChanged() = runTest {
        val repositoryFactory = RepositoryFactory()

        val repositoryBag = repositoryFactory.repositoryBag
        repositoryBag.store(repositoryFactory.bagDataTestDouble.allDice)
        assertEquals(7, repositoryBag.fetch().first().size)

        val repositoryRoll = repositoryFactory.repositoryRoll
        repositoryRoll.store(repositoryFactory.rollHistoryTestDouble)
        assertEquals(2, repositoryRoll.fetch().first().size)

        val dialogDiceAndroidViewModel = DialogDiceAndroidViewModel(
            application(),
            repositoryBag,
            repositoryFactory.bagDataTestDouble.allDice[0],
            repositoryFactory.bagDataTestDouble.allDice[0].sides[0]
        )

        dialogDiceAndroidViewModel.cardDiceService.diceSidesSize("4")

        dialogDiceAndroidViewModel.save(
            dialogDiceAndroidViewModel.cardDiceService.stateFlowCardDice.value,
            dialogDiceAndroidViewModel.cardRollService.stateFlowCardRoll.value,
            dialogDiceAndroidViewModel.cardSideService.stateFlowCardSide.value
        )

        advanceUntilIdle()

        assertEquals(7, repositoryBag.fetch().first().size)

        assertEquals(1, repositoryRoll.fetch().first().size)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun removeGroupWithDiceThatBeenDeleted() = runTest {
        val repositoryFactory = RepositoryFactory()

        val repositoryBag = repositoryFactory.repositoryBag
        val allDice = repositoryFactory.bagDataTestDouble.allDice
        repositoryBag.store(allDice)

        val repositoryGroup = repositoryFactory.repositoryGroup
        val diceToDelete = allDice[0]
        val diceToKeep = allDice[1]

        val groupWithDiceToDelete = Group(name = "Group 1", uuidDice = listOf(diceToDelete.uuid))
        val groupWithBothDice =
            Group(name = "Group 2", uuidDice = listOf(diceToDelete.uuid, diceToKeep.uuid))

        repositoryGroup.store(listOf(groupWithDiceToDelete, groupWithBothDice))
        assertEquals(2, repositoryGroup.fetch().first().size)

        val dialogDiceAndroidViewModel = DialogDiceAndroidViewModel(
            application(),
            repositoryBag,
            diceToDelete,
            diceToDelete.sides[0]
        )

        dialogDiceAndroidViewModel.delete()

        advanceUntilIdle()

        val remainingGroups = repositoryGroup.fetch().first()
        assertEquals(1, remainingGroups.size)
        assertEquals("Group 2", remainingGroups[0].name)
        assertEquals(1, remainingGroups[0].uuidDice.size)
        assertEquals(diceToKeep.uuid, remainingGroups[0].uuidDice[0])
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun migrateGroupDiceEpochWhenNumberOfSidesChanged() = runTest {
        val repositoryFactory = RepositoryFactory()

        val repositoryBag = repositoryFactory.repositoryBag
        val allDice = repositoryFactory.bagDataTestDouble.allDice
        repositoryBag.store(allDice)

        val repositoryGroup = repositoryFactory.repositoryGroup
        val diceToChange = allDice[0]
        val group = Group(name = "Group", uuidDice = listOf(diceToChange.uuid))
        repositoryGroup.store(listOf(group))

        val dialogDiceAndroidViewModel = DialogDiceAndroidViewModel(
            application(),
            repositoryBag,
            diceToChange,
            diceToChange.sides[0]
        )

        dialogDiceAndroidViewModel.cardDiceService.diceSidesSize("4")

        dialogDiceAndroidViewModel.save(
            dialogDiceAndroidViewModel.cardDiceService.stateFlowCardDice.value,
            dialogDiceAndroidViewModel.cardRollService.stateFlowCardRoll.value,
            dialogDiceAndroidViewModel.cardSideService.stateFlowCardSide.value
        )

        advanceUntilIdle()

        val remainingGroups = repositoryGroup.fetch().first()
        assertEquals(1, remainingGroups.size)
        assertNotEquals(diceToChange.uuid, remainingGroups[0].uuidDice[0])
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun deleteDiceRemovesGroupIfItWasTheLastDiceInThatGroup() = runTest {
        val repositoryFactory = RepositoryFactory()
        val repositoryBag = repositoryFactory.repositoryBag
        val repositoryGroup = repositoryFactory.repositoryGroup

        // 1. Setup Dice Bag
        val d6 = BagDataTestDouble().d6
        val d20 = BagDataTestDouble().d20
        repositoryBag.store(mutableListOf(d6, d20))

        // 2. Setup Groups
        // Group A has only D6
        val groupA = Group(name = "Group A", uuidDice = listOf(d6.uuid))
        // Group B has D6 and D20
        val groupB = Group(name = "Group B", uuidDice = listOf(d6.uuid, d20.uuid))
        repositoryGroup.store(listOf(groupA, groupB))

        // 3. Delete D6
        val dialogDiceAndroidViewModel = DialogDiceAndroidViewModel(
            application(),
            repositoryBag,
            d6,
            d6.sides[0]
        )
        dialogDiceAndroidViewModel.delete()
        advanceUntilIdle()

        // 4. Validate
        val remainingGroups = repositoryGroup.fetch().first()

        // Group A should be deleted because D6 was its only dice
        assertFalse("Group A should have been deleted", remainingGroups.any { it.name == "Group A" })

        // Group B should still exist but without D6
        val remainingGroupB = remainingGroups.find { it.name == "Group B" }
        assertTrue("Group B should still exist", remainingGroupB != null)
        assertEquals("Group B should now have only 1 dice", 1, remainingGroupB?.uuidDice?.size)
        assertEquals("Group B should contain D20", d20.uuid, remainingGroupB?.uuidDice?.get(0))

        // Total remaining groups should be 1
        assertEquals(1, remainingGroups.size)
    }

    private suspend fun dialogBagAndroidViewModel(dice: Dice): DialogDiceAndroidViewModel {
        val repositoryBag = RepositoryFactory().repositoryBag
        repositoryBag.store(mutableListOf(dice))

        return DialogDiceAndroidViewModel(
            application(), repositoryBag, dice, dice.sides[0]
        )
    }
}
