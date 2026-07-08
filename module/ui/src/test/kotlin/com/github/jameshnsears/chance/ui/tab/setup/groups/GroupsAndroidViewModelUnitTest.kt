package com.github.jameshnsears.chance.ui.tab.setup.groups

import com.github.jameshnsears.chance.common.utility.UtilityAndroidUnitTestHelper
import com.github.jameshnsears.chance.data.common.repo.RepositoryFactory
import com.github.jameshnsears.chance.data.domain.core.Side
import com.github.jameshnsears.chance.data.domain.core.group.Group
import com.github.jameshnsears.chance.data.domain.core.roll.Roll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GroupsAndroidViewModelUnitTest : UtilityAndroidUnitTestHelper() {
    private lateinit var viewModel: GroupsAndroidViewModel

    @Before
    fun setUp() = runTest {
        RepositoryFactory().resetStorage()
        val repositoryFactory = RepositoryFactory()
        viewModel = GroupsAndroidViewModel(
            application(),
            repositoryFactory.repositoryBag,
            repositoryFactory.repositoryGroup,
            repositoryFactory.repositoryRoll
        )
    }

    @Test
    fun onSaveExistingGroupNameChanged() = runTest {
        val originalGroup = Group(name = "Original Name", uuidDice = listOf("dice-uuid"))
        viewModel.repositoryGroup.store(listOf(originalGroup))

        // Add some roll history for this group
        viewModel.repositoryRoll.store(
            linkedMapOf(
                1L to listOf(Roll(uuidDice = "dice-uuid", side = Side(), uuidGroup = originalGroup.uuid))
            )
        )

        // Simulate name change in draft
        viewModel.onNameChange(originalGroup, "New Name")

        viewModel.onSave(originalGroup)

        val groupHistory = viewModel.stateFlowGroupHistory.value
        assertEquals(1, groupHistory.size)
        val savedGroup = groupHistory[0]
        assertEquals("New Name", savedGroup.name)
        assertEquals(originalGroup.uuid, savedGroup.uuid)

        val rollHistory = viewModel.repositoryRoll.fetch().first()
        assertEquals(1, rollHistory.size)
    }

    @Test
    fun onSaveExistingGroupDiceChanged() = runTest {
        val originalGroup = Group(name = "Group Name", uuidDice = listOf("dice-uuid-1"))
        viewModel.repositoryGroup.store(listOf(originalGroup))

        // Add some roll history for this group
        viewModel.repositoryRoll.store(
            linkedMapOf(
                1L to listOf(Roll(uuidDice = "dice-uuid-1", side = Side(), uuidGroup = originalGroup.uuid))
            )
        )

        // Simulate dice change in draft (add a dice)
        viewModel.onUuidDiceChange(originalGroup, "dice-uuid-2", 1)

        viewModel.onSave(originalGroup)

        val groupHistory = viewModel.stateFlowGroupHistory.value
        assertEquals(1, groupHistory.size)
        val savedGroup = groupHistory[0]
        assertEquals(2, savedGroup.uuidDice.size)
        assertNotEquals(originalGroup.uuid, savedGroup.uuid)

        val rollHistory = viewModel.repositoryRoll.fetch().first()
        assertTrue(rollHistory.isEmpty())
    }

    @Test
    fun onSaveExistingGroupNotesChanged() = runTest {
        val originalGroup = Group(name = "Group Name", uuidDice = listOf("dice-uuid-1"), notes = "Original Notes")
        viewModel.repositoryGroup.store(listOf(originalGroup))

        // Add some roll history for this group
        viewModel.repositoryRoll.store(
            linkedMapOf(
                1L to listOf(Roll(uuidDice = "dice-uuid-1", side = Side(), uuidGroup = originalGroup.uuid))
            )
        )

        // Simulate notes change in draft
        viewModel.onNotesChange(originalGroup, "New Notes")

        viewModel.onSave(originalGroup)

        val groupHistory = viewModel.stateFlowGroupHistory.value
        assertEquals(1, groupHistory.size)
        val savedGroup = groupHistory[0]
        assertEquals("New Notes", savedGroup.notes)
        assertEquals(originalGroup.uuid, savedGroup.uuid)

        val rollHistory = viewModel.repositoryRoll.fetch().first()
        assertEquals(1, rollHistory.size)
    }
}
