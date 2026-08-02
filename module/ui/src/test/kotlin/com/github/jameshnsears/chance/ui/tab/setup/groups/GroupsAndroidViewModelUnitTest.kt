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
    fun onDelete() = runTest {
        val group = Group(name = "To Delete")
        viewModel.repositoryGroup.store(listOf(group))

        // Add roll history
        viewModel.repositoryRoll.store(
            linkedMapOf(
                1L to listOf(Roll(uuidDice = "d", side = Side(), uuidGroup = group.uuid))
            )
        )

        viewModel.onDelete(group)

        val groupHistory = viewModel.stateFlowGroupHistory.value
        assertTrue(groupHistory.isEmpty())

        val rollHistory = viewModel.repositoryRoll.fetch().first()
        assertTrue(rollHistory.isEmpty())
    }

    @Test
    fun moveUpDown() = runTest {
        val group1 = Group(name = "G1", displayIndex = 0)
        val group2 = Group(name = "G2", displayIndex = 1)
        viewModel.repositoryGroup.store(listOf(group1, group2))

        // Load initial state
        viewModel.stateFlowGroupHistory.first { it.size == 2 }

        viewModel.moveDown(group1)
        var history = viewModel.stateFlowGroupHistory.value
        assertEquals("G2", history[0].name)
        assertEquals("G1", history[1].name)
        assertEquals(0, history[0].displayIndex)
        assertEquals(1, history[1].displayIndex)

        viewModel.moveUp(group1)
        history = viewModel.stateFlowGroupHistory.value
        assertEquals("G1", history[0].name)
        assertEquals("G2", history[1].name)
    }

    @Test
    fun canSave() = runTest {
        val group = Group(name = "Valid", uuidDice = listOf("d6"))
        assertTrue(viewModel.canSave(group))

        val emptyName = group.copy(name = "")
        assertTrue(!viewModel.canSave(emptyName))

        val noDice = group.copy(uuidDice = emptyList())
        assertTrue(!viewModel.canSave(noDice))

        val duplicateName = Group(name = "Valid")
        viewModel.repositoryGroup.store(listOf(duplicateName))
        // Need to wait for it to be reflected in stateFlowGroupHistory
        viewModel.stateFlowGroupHistory.first { it.isNotEmpty() }

        val anotherValid = Group(name = "Another", uuidDice = listOf("d6"))
        assertTrue(viewModel.canSave(anotherValid))

        val duplicate = Group(name = "Valid", uuidDice = listOf("d6"))
        assertTrue(!viewModel.canSave(duplicate))
    }
}
