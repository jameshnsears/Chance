package com.github.jameshnsears.chance.ui.tab.setup.groups

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.jameshnsears.chance.common.utility.UtilityAndroidUnitTestHelper
import com.github.jameshnsears.chance.data.common.repo.RepositoryFactory
import com.github.jameshnsears.chance.data.domain.core.group.Group
import com.github.jameshnsears.chance.data.domain.core.roll.Roll
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GroupsAndroidViewModelUnitTest : UtilityAndroidUnitTestHelper() {
    private lateinit var repositoryFactory: RepositoryFactory
    private val createdViewModels = mutableListOf<ViewModel>()

    @Before
    fun setUp() = runTest {
        repositoryFactory = RepositoryFactory()
        repositoryFactory.resetStorage()
    }

    @After
    fun tearDown() {
        createdViewModels.forEach { it.viewModelScope.cancel() }
        createdViewModels.clear()
        repositoryFactory.close()
    }

    private fun viewModel(): GroupsAndroidViewModel {
        return GroupsAndroidViewModel(
            application(),
            repositoryFactory.repositoryBag,
            repositoryFactory.repositoryGroup,
            repositoryFactory.repositoryRoll
        ).also { createdViewModels.add(it) }
    }

    @Test
    fun onSaveExistingGroupNameChanged() = runTest {
        val viewModel = viewModel()
        val d6 = repositoryFactory.bagDataTestDouble.d6
        val originalGroup = Group(name = "Original Name", uuidDice = listOf(d6.uuid))
        viewModel.repositoryGroup.store(listOf(originalGroup))
        viewModel.stateFlowGroupHistory.first { it.any { g -> g.uuid == originalGroup.uuid } }

        // Add some roll history for this group
        viewModel.repositoryRoll.store(
            linkedMapOf(
                1L to listOf(Roll(uuidDice = d6.uuid, side = d6.sides[0], uuidGroup = originalGroup.uuid))
            )
        )

        // Simulate name change in draft
        viewModel.onNameChange(originalGroup, "New Name")

        viewModel.onSave(originalGroup)

        val groupHistory = viewModel.stateFlowGroupHistory.first {
            it.size == 1 && it[0].name == "New Name"
        }
        assertEquals(1, groupHistory.size)
        val savedGroup = groupHistory[0]
        assertEquals("New Name", savedGroup.name)
        assertEquals(originalGroup.uuid, savedGroup.uuid)

        val rollHistory = viewModel.repositoryRoll.fetch().first()
        assertEquals(1, rollHistory.size)
    }

    @Test
    fun onSaveExistingGroupDiceChanged() = runTest {
        val viewModel = viewModel()
        val d6 = repositoryFactory.bagDataTestDouble.d6
        val d4 = repositoryFactory.bagDataTestDouble.d4
        val originalGroup = Group(name = "Group Name", uuidDice = listOf(d6.uuid))
        viewModel.repositoryGroup.store(listOf(originalGroup))
        viewModel.stateFlowGroupHistory.first { it.any { g -> g.uuid == originalGroup.uuid } }

        // Add some roll history for this group
        viewModel.repositoryRoll.store(
            linkedMapOf(
                1L to listOf(Roll(uuidDice = d6.uuid, side = d6.sides[0], uuidGroup = originalGroup.uuid))
            )
        )

        // Simulate dice change in draft (add a dice)
        viewModel.onUuidDiceChange(originalGroup, d4.uuid, 1)

        viewModel.onSave(originalGroup)

        val groupHistory = viewModel.stateFlowGroupHistory.first {
            it.size == 1 && it[0].uuidDice.size == 2
        }
        assertEquals(1, groupHistory.size)
        val savedGroup = groupHistory[0]
        assertEquals(2, savedGroup.uuidDice.size)
        assertNotEquals(originalGroup.uuid, savedGroup.uuid)

        val rollHistory = viewModel.repositoryRoll.fetch().first()
        assertTrue(rollHistory.isEmpty())
    }

    @Test
    fun onDelete() = runTest {
        val viewModel = viewModel()
        val d6 = repositoryFactory.bagDataTestDouble.d6
        val group = Group(name = "To Delete")
        viewModel.repositoryGroup.store(listOf(group))
        viewModel.stateFlowGroupHistory.first { it.any { g -> g.uuid == group.uuid } }

        // Add roll history
        viewModel.repositoryRoll.store(
            linkedMapOf(
                1L to listOf(Roll(uuidDice = d6.uuid, side = d6.sides[0], uuidGroup = group.uuid))
            )
        )

        viewModel.onDelete(group)

        viewModel.stateFlowGroupHistory.first { it.isEmpty() }
        val rollHistory = viewModel.repositoryRoll.fetch().first { it.isEmpty() }
        assertTrue(rollHistory.isEmpty())
    }

    @Test
    fun moveUpDown() = runTest {
        val viewModel = viewModel()
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
        val viewModel = viewModel()
        val group = Group(name = "Valid", uuidDice = listOf("d6"))
        assertTrue(viewModel.canSave(group))

        val emptyName = group.copy(name = "")
        assertTrue(!viewModel.canSave(emptyName))

        val noDice = group.copy(uuidDice = emptyList())
        assertTrue(!viewModel.canSave(noDice))

        val duplicateName = Group(name = "Valid")
        viewModel.repositoryGroup.store(listOf(duplicateName))
        // Need to wait for it to be reflected in stateFlowGroupHistory
        viewModel.stateFlowGroupHistory.first { it.any { g -> g.name == "Valid" } }

        val anotherValid = Group(name = "Another", uuidDice = listOf("d6"))
        assertTrue(viewModel.canSave(anotherValid))

        val duplicate = Group(name = "Valid", uuidDice = listOf("d6"))
        assertTrue(!viewModel.canSave(duplicate))
    }
}
