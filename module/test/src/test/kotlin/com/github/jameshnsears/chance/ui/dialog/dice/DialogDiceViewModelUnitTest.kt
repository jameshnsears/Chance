package com.github.jameshnsears.chance.ui.dialog.dice

import com.github.jameshnsears.chance.data.repository.dice.DiceRepositoryMock
import com.github.jameshnsears.chance.data.repository.dice.sample.DiceSampleData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
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

            testSliderSides(viewModel)

            testDescription(viewModel)

            testSliderPenaltyBonus(viewModel)

            testOk(viewModel)
        } finally {
            Dispatchers.resetMain()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun TestScope.testOk(
        viewModel: DialogDiceViewModel) {
        assertEquals(2, viewModel.diceModel.fetchDice(viewModel.diceIndex).sides.size)
        assertEquals("d2", viewModel.diceModel.fetchDice(viewModel.diceIndex).description)
        assertEquals(3, viewModel.diceModel.fetchDice(viewModel.diceIndex).penaltyBonus)

        viewModel.ok()
        advanceUntilIdle()

        assertEquals(8, viewModel.diceModel.fetchDice(viewModel.diceIndex).sides.size)
        assertEquals("", viewModel.diceModel.fetchDice(viewModel.diceIndex).description)
        assertEquals(0, viewModel.diceModel.fetchDice(viewModel.diceIndex).penaltyBonus)
    }

    private fun testSliderSides(viewModel: DialogDiceViewModel) {
        assertEquals(0f, viewModel.sliderSidesPosition.value)
        assertEquals(2, viewModel.fetchCurrentSliderSidesPosition())

        val newSliderPosition = 3.0f
        viewModel.updateCurrentSliderSidesPosition(newSliderPosition)
        assertEquals(newSliderPosition, viewModel.sliderSidesPosition.value)
        assertEquals(8, viewModel.fetchCurrentSliderSidesPosition())
    }

    private fun testDescription(viewModel: DialogDiceViewModel) {
        assertEquals("d2", viewModel.description.value)

        val newDescription = ""
        viewModel.updateCurrentDescription(newDescription)
        assertEquals(newDescription, viewModel.description.value)
    }

    private fun testSliderPenaltyBonus(viewModel: DialogDiceViewModel) {
        assertEquals(6.0f, viewModel.sliderPenaltyBonusPosition.value)
        assertEquals(3, viewModel.fetchCurrentSliderPenaltyBonusPosition())

        val newSliderPosition = 3.0f
        viewModel.updateCurrentSliderPenaltyBonusPosition(newSliderPosition)
        assertEquals(newSliderPosition, viewModel.sliderSidesPosition.value)
        assertEquals(0, viewModel.fetchCurrentSliderPenaltyBonusPosition())
    }
}
