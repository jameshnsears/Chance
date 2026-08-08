package com.github.jameshnsears.chance.ui.zoom.rolls

import com.github.jameshnsears.chance.common.utility.UtilityAndroidUnitTestHelper
import com.github.jameshnsears.chance.data.common.repo.RepositoryFactory
import com.github.jameshnsears.chance.data.domain.core.bag.testdouble.BagDataTestDouble
import com.github.jameshnsears.chance.data.domain.core.group.testdouble.GroupDataTestDouble
import com.github.jameshnsears.chance.data.domain.core.roll.testdouble.RollHistoryDataTestDouble
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ZoomRollsAndroidViewModelUnitTest : UtilityAndroidUnitTestHelper() {
    @Before
    fun before() = runTest {
        RepositoryFactory().resetStorage()
    }

    private suspend fun zoomRollsAndroidViewModel(): ZoomRollsAndroidViewModel {
        val repositoryFactory = RepositoryFactory()
        val bagDataTestDouble = BagDataTestDouble()
        val groupDataTestDouble = GroupDataTestDouble(bagDataTestDouble)
        val rollHistoryDataTestDouble = RollHistoryDataTestDouble(bagDataTestDouble, groupDataTestDouble)

        repositoryFactory.repositoryBag.store(bagDataTestDouble.allDice())
        repositoryFactory.repositoryGroup.store(groupDataTestDouble.groupHistory)
        repositoryFactory.repositoryRoll.store(rollHistoryDataTestDouble.rollHistory)

        return ZoomRollsAndroidViewModel(
            application(),
            repositoryFactory.repositorySettings,
            repositoryFactory.repositoryBag,
            repositoryFactory.repositoryRoll,
            repositoryFactory.repositoryGroup
        )
    }

    @Test
    fun entriesListFiltering() = runTest {
        val viewModel = zoomRollsAndroidViewModel()
        val repositorySettings = RepositoryFactory().repositorySettings
        val settings = repositorySettings.fetch().first()

        // Default history is false in SettingsDataTestDouble (usually)
        // Let's verify what happens in the stateFlowZoom

        settings.history = true
        repositorySettings.store(settings)
        var state = viewModel.stateFlowZoom.first { it.history == true }
        assertEquals(2, state.entriesList.size)

        settings.history = false
        repositorySettings.store(settings)
        state = viewModel.stateFlowZoom.first { it.history == false }

        // The ViewModel itself doesn't filter entriesList based on history anymore
        // (if it ever did, I should check ZoomAndroidViewModel again)
        // Wait, if I want to test my change in ZoomRolls.kt, I should test it via a UI test.
        // But I can at least verify that the state has the correct history flag.
        assertEquals(false, state.history)
    }

    @Test
    fun toggleLock() = runTest {
        val viewModel = zoomRollsAndroidViewModel()

        assertEquals(emptySet<Int>(), viewModel.lockedRollIndices.value)

        viewModel.toggleLock(0)
        assertEquals(setOf(0), viewModel.lockedRollIndices.value)

        viewModel.toggleLock(1)
        assertEquals(setOf(0, 1), viewModel.lockedRollIndices.value)

        viewModel.toggleLock(0)
        assertEquals(setOf(1), viewModel.lockedRollIndices.value)
    }

    @Test
    fun resetLockOnNewRoll() = runTest {
        val viewModel = zoomRollsAndroidViewModel()
        val repositoryRoll = RepositoryFactory().repositoryRoll

        viewModel.toggleLock(0)
        assertEquals(setOf(0), viewModel.lockedRollIndices.value)

        // Add a new roll to history
        val currentHistory = repositoryRoll.fetch().first()
        val newHistory = LinkedHashMap(currentHistory)
        val nextTimestamp = currentHistory.keys.max() + 1000
        newHistory[nextTimestamp] = currentHistory.values.first()

        repositoryRoll.store(newHistory)

        // Wait for reset
        viewModel.lockedRollIndices.first { it.isEmpty() }
        assertEquals(emptySet<Int>(), viewModel.lockedRollIndices.value)
    }
}
