package com.github.jameshnsears.chance.ui.dialog.dice

import com.github.jameshnsears.chance.data.repository.dice.DiceRepositoryMock
import com.github.jameshnsears.chance.data.repository.dice.sample.DiceSampleData
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

class DialogDiceViewModelUnitTest {
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun exerciseViewModel() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        try {
            val diceRepository = DiceRepositoryMock
            diceRepository.store(DiceSampleData.twoDice)

            val viewModel = DialogDiceViewModel(
                diceRepository,
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

            val newSliderPenaltyBonusPosition = 3.0f
            val expectedSliderPenaltyBonusPosition = 0
            testSliderPenaltyBonus(
                viewModel,
                newSliderPenaltyBonusPosition,
                expectedSliderPenaltyBonusPosition
            )

            testOk(
                viewModel,
                expectedSliderSidesPosition,
                expectedSliderPenaltyBonusPosition
            )
        } finally {
            Dispatchers.resetMain()
        }
    }

    private fun testSliderSides(
        viewModel: DialogDiceViewModel,
        newSliderPosition: Float,
        expectedSliderPosition: Int
    ) {
        assertEquals(0f, viewModel.sliderSidesPosition.value)
        assertEquals(2, viewModel.fetchCurrentSliderSidesPosition())

        viewModel.updateCurrentSliderSidesPosition(newSliderPosition)
        assertEquals(newSliderPosition, viewModel.sliderSidesPosition.value)
        assertEquals(expectedSliderPosition, viewModel.fetchCurrentSliderSidesPosition())
    }

    private fun testDescription(viewModel: DialogDiceViewModel) {
        assertEquals("d2", viewModel.description.value)

        val newDescription = ""
        viewModel.updateCurrentDescription(newDescription)
        assertEquals(newDescription, viewModel.description.value)
    }

    private fun testSliderPenaltyBonus(
        viewModel: DialogDiceViewModel,
        newSliderPosition: Float,
        expectedSliderPosition: Int
    ) {
        assertEquals(6.0f, viewModel.sliderPenaltyBonusPosition.value)
        assertEquals(3, viewModel.fetchCurrentSliderPenaltyBonusPosition())

        viewModel.updateCurrentSliderPenaltyBonusPosition(newSliderPosition)
        assertEquals(newSliderPosition, viewModel.sliderSidesPosition.value)
        assertEquals(expectedSliderPosition, viewModel.fetchCurrentSliderPenaltyBonusPosition())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun TestScope.testOk(
        viewModel: DialogDiceViewModel,
        expectedSides: Int,
        expectedPenaltyBonus: Int
    ) {
        assertEquals(2, viewModel.fetchDice(viewModel.diceIndex).sides.size)
        assertEquals("d2", viewModel.fetchDice(viewModel.diceIndex).description)
        assertEquals(3, viewModel.fetchDice(viewModel.diceIndex).penaltyBonus)

        viewModel.ok()
        advanceUntilIdle()

        assertEquals(expectedSides, viewModel.fetchDice(viewModel.diceIndex).sides.size)
        assertEquals("", viewModel.fetchDice(viewModel.diceIndex).description)
        assertEquals(expectedPenaltyBonus, viewModel.fetchDice(viewModel.diceIndex).penaltyBonus)
    }
}
