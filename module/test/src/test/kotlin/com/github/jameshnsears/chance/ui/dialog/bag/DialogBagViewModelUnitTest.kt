package com.github.jameshnsears.chance.ui.dialog.bag

import com.github.jameshnsears.chance.data.bag.repository.BagRepositoryMock
import com.github.jameshnsears.chance.data.bag.sample.BagSampleData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Test

class DialogBagViewModelUnitTest {
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun exerciseViewModel() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        try {
            val bagRepository = BagRepositoryMock
            bagRepository.store(BagSampleData.twoDice)

            val viewModel = DialogBagViewModel(
                bagRepository,
                0
            )

            val newSliderSidesPosition = 3.0f
            val expectedSliderSidesPosition = 8
            testSliderSides(
                viewModel,
                newSliderSidesPosition,
                expectedSliderSidesPosition
            )

            testDescription(viewModel)

            testOk(
                viewModel,
                expectedSliderSidesPosition
            )
        } finally {
            Dispatchers.resetMain()
        }
    }

    private fun testSliderSides(
        viewModel: DialogBagViewModel,
        newSliderPosition: Float,
        expectedSliderPosition: Int
    ) {
        assertEquals(0f, viewModel.sliderSidesPosition.value)
        assertEquals(2, viewModel.fetchCurrentSliderSidesPosition())

        viewModel.updateCurrentSliderSidesPosition(newSliderPosition)
        assertEquals(newSliderPosition, viewModel.sliderSidesPosition.value)
        assertEquals(expectedSliderPosition, viewModel.fetchCurrentSliderSidesPosition())
    }

    private fun testDescription(viewModel: DialogBagViewModel) {
        assertEquals("d2", viewModel.diceTitle.value)

        val newDescription = ""
        viewModel.updateDiceTitle(newDescription)
        assertEquals(newDescription, viewModel.diceTitle.value)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun TestScope.testOk(
        viewModel: DialogBagViewModel,
        expectedSides: Int,
    ) {
        assertEquals(2, viewModel.fetchDice(viewModel.diceIndex).sides.size)
        assertEquals("d2", viewModel.fetchDice(viewModel.diceIndex).title)

        viewModel.ok()
        advanceUntilIdle()

        assertEquals(expectedSides, viewModel.fetchDice(viewModel.diceIndex).sides.size)
        assertEquals("", viewModel.fetchDice(viewModel.diceIndex).title)
    }
}
