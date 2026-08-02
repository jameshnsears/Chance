package com.github.jameshnsears.chance.ui.dialog.dice

import com.github.jameshnsears.chance.data.common.repo.RepositoryFactory
import com.github.jameshnsears.chance.data.domain.core.Dice
import com.github.jameshnsears.chance.data.domain.core.Side
import com.github.jameshnsears.chance.data.domain.core.bag.testdouble.BagDataTestDouble
import com.github.jameshnsears.chance.data.domain.core.group.Group
import com.github.jameshnsears.chance.data.domain.core.roll.Roll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
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

        val newSides = dialogBagAndroidViewModel.dialogDiceService.updateRepositoryBagWithNewSizedDice(
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
        val sampleBagTestDataAllDice = bagDataTestDouble.allDice()

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
        repositoryBag.store(repositoryFactory.bagDataTestDouble.allDice())
        assertEquals(7, repositoryBag.fetch().first().size)

        val repositoryRoll = repositoryFactory.repositoryRoll
        repositoryRoll.store(repositoryFactory.rollHistoryTestDouble)
        assertEquals(2, repositoryRoll.fetch().first().size)

        val dialogDiceAndroidViewModel = DialogDiceAndroidViewModel(
            application(),
            repositoryBag,
            repositoryFactory.bagDataTestDouble.allDice()[0],
            repositoryFactory.bagDataTestDouble.allDice()[0].sides[0]
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
        repositoryBag.store(repositoryFactory.bagDataTestDouble.allDice())
        assertEquals(7, repositoryBag.fetch().first().size)

        val repositoryRoll = repositoryFactory.repositoryRoll
        repositoryRoll.store(repositoryFactory.rollHistoryTestDouble)
        assertEquals(2, repositoryRoll.fetch().first().size)

        val dialogDiceAndroidViewModel = DialogDiceAndroidViewModel(
            application(),
            repositoryBag,
            repositoryFactory.bagDataTestDouble.allDice()[0],
            repositoryFactory.bagDataTestDouble.allDice()[0].sides[0]
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
        val allDice = repositoryFactory.bagDataTestDouble.allDice()
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
        val allDice = repositoryFactory.bagDataTestDouble.allDice()
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

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun deleteDiceRemovesAllDependentRollsAndGroupReferences() = runTest {
        val bagDataTestDouble = BagDataTestDouble()
        val diceToDelete = bagDataTestDouble.d4
        val diceToKeep = bagDataTestDouble.d6

        // 1. Setup dice bag with just these two dice
        val repositoryBag = RepositoryFactory().repositoryBag
        repositoryBag.store(mutableListOf(diceToDelete, diceToKeep))
        assertEquals(2, repositoryBag.fetch().first().size)

        // 2. Setup a group containing both dice
        val group = Group(name = "Test Group", uuidDice = listOf(diceToDelete.uuid, diceToKeep.uuid))
        val repositoryGroup = RepositoryFactory().repositoryGroup
        repositoryGroup.store(listOf(group))
        assertEquals(1, repositoryGroup.fetch().first().size)

        // 3. Setup roll history:
        //    Epoch A: standalone roll of diceToDelete (uuidGroup = "")
        //    Epoch B: group roll of both dice together (uuidGroup = group.uuid)
        //    Epoch C: standalone roll of diceToKeep (should survive deletion)
        val repositoryRoll = RepositoryFactory().repositoryRoll
        repositoryRoll.store(
            linkedMapOf(
                1L to listOf(
                    Roll(uuidDice = diceToDelete.uuid, side = diceToDelete.sides[0], score = 4, uuidGroup = "")
                ),
                2L to listOf(
                    Roll(uuidDice = diceToDelete.uuid, side = diceToDelete.sides[0], score = 4, uuidGroup = group.uuid),
                    Roll(uuidDice = diceToKeep.uuid, side = diceToKeep.sides[0], score = 6, uuidGroup = group.uuid)
                ),
                3L to listOf(
                    Roll(uuidDice = diceToKeep.uuid, side = diceToKeep.sides[0], score = 6, uuidGroup = "")
                ),
            )
        )
        assertEquals(3, repositoryRoll.fetch().first().size)

        // 4. Delete diceToDelete
        val dialogDiceAndroidViewModel = DialogDiceAndroidViewModel(
            application(), repositoryBag, diceToDelete, diceToDelete.sides[0]
        )
        dialogDiceAndroidViewModel.delete()
        advanceUntilIdle()

        // 5a. Dice bag: diceToDelete is gone
        val remainingDice = repositoryBag.fetch().first()
        assertEquals(1, remainingDice.size)
        assertFalse(remainingDice.any { it.uuid == diceToDelete.uuid })
        assertEquals(diceToKeep.uuid, remainingDice[0].uuid)

        // 5b. Groups: diceToDelete UUID removed from group; diceToKeep still present
        val remainingGroups = repositoryGroup.fetch().first()
        assertEquals(1, remainingGroups.size)
        assertFalse("deleted dice UUID should not be in group", remainingGroups[0].uuidDice.contains(diceToDelete.uuid))
        assertTrue("kept dice UUID should remain in group", remainingGroups[0].uuidDice.contains(diceToKeep.uuid))
        assertEquals(1, remainingGroups[0].uuidDice.size)

        // 5c. Roll history: only the standalone roll of diceToKeep survives
        val remainingRolls = repositoryRoll.fetch().first()
        assertEquals("only the standalone roll of diceToKeep should remain", 1, remainingRolls.size)

        // The surviving epoch is the standalone roll of diceToKeep
        val survivingEpoch = remainingRolls[3L]
        assertEquals(1, survivingEpoch!!.size)
        assertEquals(diceToKeep.uuid, survivingEpoch[0].uuidDice)
        assertEquals("", survivingEpoch[0].uuidGroup)
    }

    // ========================================================================
    // Save and Clone: Roll & Group Semantics
    // ========================================================================

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun clonePreservesRollAndGroupSemantics() = runTest {
        val repositoryFactory = RepositoryFactory()
        val bagDataTestDouble = BagDataTestDouble()
        val repositoryBag = repositoryFactory.repositoryBag
        val repositoryRoll = repositoryFactory.repositoryRoll
        val repositoryGroup = repositoryFactory.repositoryGroup

        // 1. Seed the dice bag with 3 dice
        val d10 = bagDataTestDouble.d10
        val d12 = bagDataTestDouble.d12
        val d20 = bagDataTestDouble.d20
        repositoryBag.store(mutableListOf(d10, d12, d20))

        // 2. Seed a group containing d12
        val group = Group(name = "Test Group", uuidDice = listOf(d12.uuid))
        repositoryGroup.store(listOf(group))

        // 3. Seed roll history: one standalone roll of d12, one group roll
        repositoryRoll.store(
            linkedMapOf(
                100L to listOf(
                    Roll(uuidDice = d12.uuid, side = d12.sides[0], score = 12, uuidGroup = "")
                ),
                200L to listOf(
                    Roll(uuidDice = d10.uuid, side = d10.sides[0], score = 10, uuidGroup = group.uuid),
                    Roll(uuidDice = d12.uuid, side = d12.sides[0], score = 12, uuidGroup = group.uuid)
                ),
            )
        )

        // 4. Clone d12 with a new title
        val d12FromRepo = repositoryBag.fetch().first()[1] // d12 is at index 1
        val dialogDiceAndroidViewModel = DialogDiceAndroidViewModel(
            application(), repositoryBag, d12FromRepo, d12FromRepo.sides[0]
        )
        val cloneTitle = "d12 clone"
        dialogDiceAndroidViewModel.cardDiceService.diceTitle(cloneTitle)

        dialogDiceAndroidViewModel.clone(
            dialogDiceAndroidViewModel.cardDiceService.stateFlowCardDice.value,
            dialogDiceAndroidViewModel.cardRollService.stateFlowCardRoll.value
        )
        advanceUntilIdle()

        // 5a. Dice bag: now has 4 dice, original d12 unchanged, clone adjacent
        val diceBag = repositoryBag.fetch().first()
        assertEquals("bag should have 4 dice after clone", 4, diceBag.size)

        // Original dice positions preserved
        assertEquals(d10.uuid, diceBag[0].uuid)
        assertEquals(d12.uuid, diceBag[1].uuid)

        // Clone sits right after original
        assertEquals(cloneTitle, diceBag[2].title)
        assertNotEquals("clone must have new UUID", d12.uuid, diceBag[2].uuid)

        // Original d20 at end
        assertEquals(d20.uuid, diceBag[3].uuid)

        // Original d12 properties unchanged
        assertEquals(d12.title, diceBag[1].title)
        assertEquals(d12.sides.size, diceBag[1].sides.size)

        // Clone copies d12 properties
        assertEquals(d12.sides.size, diceBag[2].sides.size)
        assertEquals(d12.multiplierValue, diceBag[2].multiplierValue)
        assertEquals(d12.explode, diceBag[2].explode)

        // Display indices are sequential
        diceBag.forEachIndexed { index, dice ->
            assertEquals("displayIndex must be sequential after clone", index, dice.displayIndex)
        }

        // 5b. Group semantics: clone does NOT add the cloned dice to any group
        val groups = repositoryGroup.fetch().first()
        assertEquals("group count unchanged by clone", 1, groups.size)
        assertEquals("group should still reference only original d12", 1, groups[0].uuidDice.size)
        assertTrue("group still contains original d12 UUID", groups[0].uuidDice.contains(d12.uuid))
        assertFalse("group should NOT contain the cloned dice UUID", groups[0].uuidDice.contains(diceBag[2].uuid))

        // 5c. Roll semantics: clone does NOT affect roll history
        val rollHistory = repositoryRoll.fetch().first()
        assertEquals("roll history size unchanged by clone", 2, rollHistory.size)

        // Both epochs still reference the original d12 UUID
        val epoch100 = rollHistory[100L]!!
        assertEquals(1, epoch100.size)
        assertEquals(d12.uuid, epoch100[0].uuidDice)

        val epoch200 = rollHistory[200L]!!
        assertEquals(2, epoch200.size)
        assertTrue(epoch200.any { it.uuidDice == d12.uuid })
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun saveWithSameSideCountPreservesRollAndGroupSemantics() = runTest {
        val repositoryFactory = RepositoryFactory()
        val bagDataTestDouble = BagDataTestDouble()
        val repositoryBag = repositoryFactory.repositoryBag
        val repositoryRoll = repositoryFactory.repositoryRoll
        val repositoryGroup = repositoryFactory.repositoryGroup

        // 1. Setup dice bag with d12
        val d12 = bagDataTestDouble.d12
        repositoryBag.store(mutableListOf(d12))

        // 2. Setup a group referencing d12
        val group = Group(name = "My Group", uuidDice = listOf(d12.uuid))
        repositoryGroup.store(listOf(group))

        // 3. Setup roll history with d12 rolls
        repositoryRoll.store(
            linkedMapOf(
                1L to listOf(
                    Roll(uuidDice = d12.uuid, side = d12.sides[0], score = 12, uuidGroup = group.uuid)
                ),
                2L to listOf(
                    Roll(uuidDice = d12.uuid, side = d12.sides[5], score = 7, uuidGroup = "")
                ),
            )
        )
        assertEquals(2, repositoryRoll.fetch().first().size)

        // 4. Save with the same number of sides (only title change)
        val d12FromRepo = repositoryBag.fetch().first()[0]
        val dialogDiceAndroidViewModel = DialogDiceAndroidViewModel(
            application(), repositoryBag, d12FromRepo, d12FromRepo.sides[0]
        )
        val newTitle = "d12 Renamed"
        dialogDiceAndroidViewModel.cardDiceService.diceTitle(newTitle)

        dialogDiceAndroidViewModel.save(
            dialogDiceAndroidViewModel.cardDiceService.stateFlowCardDice.value,
            dialogDiceAndroidViewModel.cardRollService.stateFlowCardRoll.value,
            dialogDiceAndroidViewModel.cardSideService.stateFlowCardSide.value
        )
        advanceUntilIdle()

        // 5a. Dice bag: same dice count, same UUID, title updated
        val diceBag = repositoryBag.fetch().first()
        assertEquals(1, diceBag.size)
        assertEquals("dice UUID preserved on save with same side count", d12.uuid, diceBag[0].uuid)
        assertEquals(newTitle, diceBag[0].title)

        // 5b. Group semantics: group references the same dice UUID (unchanged)
        val groups = repositoryGroup.fetch().first()
        assertEquals(1, groups.size)
        assertEquals("group UUID reference unchanged by save with same sides", d12.uuid, groups[0].uuidDice[0])

        // 5c. Roll semantics: all roll history preserved (same dice UUID still exists)
        val rollHistory = repositoryRoll.fetch().first()
        assertEquals("roll history preserved on save with same sides", 2, rollHistory.size)

        assertTrue(
            "epoch 1 still references d12 UUID",
            rollHistory[1L]!!.all { it.uuidDice == d12.uuid })
        assertTrue(
            "epoch 2 still references d12 UUID",
            rollHistory[2L]!!.all { it.uuidDice == d12.uuid })
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun saveWithChangedSideMigratesGroupAndCleansUpRollSemantics() = runTest {
        val repositoryFactory = RepositoryFactory()
        val bagDataTestDouble = BagDataTestDouble()
        val repositoryBag = repositoryFactory.repositoryBag
        val repositoryRoll = repositoryFactory.repositoryRoll
        val repositoryGroup = repositoryFactory.repositoryGroup

        // 1. Setup dice bag with multiple dice
        val d6 = bagDataTestDouble.d6
        val d12 = bagDataTestDouble.d12
        val d20 = bagDataTestDouble.d20
        repositoryBag.store(mutableListOf(d6, d12, d20))

        // 2. Setup two groups: one with only d12, one with d12 + d20
        val soloGroup = Group(name = "Solo Group", uuidDice = listOf(d12.uuid))
        val multiGroup = Group(name = "Multi Group", uuidDice = listOf(d12.uuid, d20.uuid))
        repositoryGroup.store(listOf(soloGroup, multiGroup))
        assertEquals(2, repositoryGroup.fetch().first().size)

        // 3. Setup roll history across 3 epochs:
        //    Epoch 1: d6 solo roll (should survive untouched)
        //    Epoch 2: d12 group roll with multiGroup (should be removed — dice changed sides)
        //    Epoch 3: d12 solo roll (should be removed — dice changed sides)
        repositoryRoll.store(
            linkedMapOf(
                10L to listOf(
                    Roll(uuidDice = d6.uuid, side = d6.sides[0], score = 6, uuidGroup = "")
                ),
                20L to listOf(
                    Roll(uuidDice = d12.uuid, side = d12.sides[0], score = 12, uuidGroup = multiGroup.uuid),
                    Roll(uuidDice = d20.uuid, side = d20.sides[0], score = 20, uuidGroup = multiGroup.uuid)
                ),
                30L to listOf(
                    Roll(uuidDice = d12.uuid, side = d12.sides[0], score = 12, uuidGroup = "")
                ),
            )
        )
        assertEquals(3, repositoryRoll.fetch().first().size)

        // 4. Save d12 with fewer sides (12 → 6) so UUID changes
        val d12FromRepo = repositoryBag.fetch().first()[1]
        val dialogDiceAndroidViewModel = DialogDiceAndroidViewModel(
            application(), repositoryBag, d12FromRepo, d12FromRepo.sides[0]
        )
        dialogDiceAndroidViewModel.cardDiceService.diceSidesSize("6")

        dialogDiceAndroidViewModel.save(
            dialogDiceAndroidViewModel.cardDiceService.stateFlowCardDice.value,
            dialogDiceAndroidViewModel.cardRollService.stateFlowCardRoll.value,
            dialogDiceAndroidViewModel.cardSideService.stateFlowCardSide.value
        )
        advanceUntilIdle()

        // 5a. Dice bag: same count, but d12 now has a new UUID
        val diceBag = repositoryBag.fetch().first()
        assertEquals("dice bag should still have 3 dice", 3, diceBag.size)
        assertNotEquals("d12 must have new UUID after side count change", d12.uuid, diceBag[1].uuid)

        // 5b. Group semantics: d12's UUID migrated in both groups
        val groups = repositoryGroup.fetch().first()
        assertEquals("both groups preserved", 2, groups.size)

        val migratedSoloGroup = groups.find { it.name == "Solo Group" }!!
        assertEquals("solo group now uses new dice UUID", 1, migratedSoloGroup.uuidDice.size)
        assertNotEquals("solo group no longer uses old d12 UUID", d12.uuid, migratedSoloGroup.uuidDice[0])
        assertEquals("solo group uses new d12 UUID", diceBag[1].uuid, migratedSoloGroup.uuidDice[0])

        val migratedMultiGroup = groups.find { it.name == "Multi Group" }!!
        assertEquals("multi group has 2 dice", 2, migratedMultiGroup.uuidDice.size)
        assertTrue(
            "multi group contains migrated d12 UUID",
            migratedMultiGroup.uuidDice.contains(diceBag[1].uuid)
        )
        assertTrue(
            "multi group still contains d20 UUID",
            migratedMultiGroup.uuidDice.contains(d20.uuid)
        )
        assertFalse(
            "multi group no longer contains old d12 UUID",
            migratedMultiGroup.uuidDice.contains(d12.uuid)
        )

        // 5c. Roll semantics: only epochs NOT referencing the old d12 UUID survive
        val rollHistory = repositoryRoll.fetch().first()
        assertEquals("only the d6 solo roll should remain", 1, rollHistory.size)
        val survivingEpoch = rollHistory[10L]
        assertNotNull("epoch 10 (d6 solo roll) survives", survivingEpoch)
        assertEquals(d6.uuid, survivingEpoch!![0].uuidDice)

        // Epochs 20 and 30 are gone because they contained the old d12 UUID
        assertNull("epoch 20 (d12 group roll) removed", rollHistory[20L])
        assertNull("epoch 30 (d12 solo roll) removed", rollHistory[30L])
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun saveWithChangedSideMigratesGroupAndRollSemanticsInCombinedScenario() =
        runTest {
            val repositoryFactory = RepositoryFactory()
            val bagDataTestDouble = BagDataTestDouble()
            val repositoryBag = repositoryFactory.repositoryBag
            val repositoryRoll = factoryRoll(repositoryFactory)
            val repositoryGroup = repositoryFactory.repositoryGroup

            // 1. Two dice in bag
            val d6 = bagDataTestDouble.d6
            val d20 = bagDataTestDouble.d20
            repositoryBag.store(mutableListOf(d6, d20))

            // 2. Group with ONLY d6
            val group = Group(name = "Only D6", uuidDice = listOf(d6.uuid))
            repositoryGroup.store(listOf(group))

            // 3. Roll history: epoch 1 is a group roll (d6+d20), epoch 2 is d20 solo
            repositoryRoll.store(
                linkedMapOf(
                    1L to listOf(
                        Roll(uuidDice = d6.uuid, side = d6.sides[0], score = 6, uuidGroup = group.uuid),
                        Roll(uuidDice = d20.uuid, side = d20.sides[0], score = 20, uuidGroup = group.uuid)
                    ),
                    2L to listOf(
                        Roll(uuidDice = d20.uuid, side = d20.sides[0], score = 20, uuidGroup = "")
                    ),
                )
            )

            // 4. Change d6 sides (6 → 4) so its UUID changes
            val d6FromRepo = repositoryBag.fetch().first()[0]
            val dialogDiceAndroidViewModel = DialogDiceAndroidViewModel(
                application(), repositoryBag, d6FromRepo, d6FromRepo.sides[0]
            )
            dialogDiceAndroidViewModel.cardDiceService.diceSidesSize("4")
            dialogDiceAndroidViewModel.save(
                dialogDiceAndroidViewModel.cardDiceService.stateFlowCardDice.value,
                dialogDiceAndroidViewModel.cardRollService.stateFlowCardRoll.value,
                dialogDiceAndroidViewModel.cardSideService.stateFlowCardSide.value
            )
            advanceUntilIdle()

            // 5a. Bag: still 2 dice, d6 has new UUID
            val bag = repositoryBag.fetch().first()
            assertEquals(2, bag.size)
            val newD6Uuid = bag[0].uuid
            assertNotEquals("d6 UUID changed after side count change", d6.uuid, newD6Uuid)

            // 5b. Group: survives because migrateRepositoryGroupUuid runs BEFORE
            //     updateRepositoryGroupWhereDiceBeenDeleted. The group now references
            //     the new d6 UUID.
            val groups = repositoryGroup.fetch().first()
            assertEquals("group with migrated UUID survives", 1, groups.size)
            assertEquals("Only D6", groups[0].name)
            assertEquals("group now references new d6 UUID", newD6Uuid, groups[0].uuidDice[0])
            assertFalse("group no longer references old d6 UUID", groups[0].uuidDice.contains(d6.uuid))

            // 5c. Roll history: epoch 1 (d6 in the group roll) is removed because the
            //     old d6 UUID was purged. Epoch 2 (d20 solo) survives.
            val rolls = repositoryRoll.fetch().first()
            assertEquals("only epoch 2 (d20 solo) survives", 1, rolls.size)
            val survivingEpoch = rolls[2L]
            assertNotNull("epoch 2 (d20) survives", survivingEpoch)
            assertEquals(d20.uuid, survivingEpoch!![0].uuidDice)
        }

    private suspend fun dialogBagAndroidViewModel(dice: Dice): DialogDiceAndroidViewModel {
        val repositoryBag = RepositoryFactory().repositoryBag
        repositoryBag.store(mutableListOf(dice))

        return DialogDiceAndroidViewModel(
            application(), repositoryBag, dice, dice.sides[0]
        )
    }

    private fun factoryRoll(repositoryFactory: RepositoryFactory) = repositoryFactory.repositoryRoll
}
