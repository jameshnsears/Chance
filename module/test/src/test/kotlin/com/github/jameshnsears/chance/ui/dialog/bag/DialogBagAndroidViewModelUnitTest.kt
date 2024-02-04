package com.github.jameshnsears.chance.ui.dialog.bag

import android.app.Application
import com.github.jameshnsears.chance.data.repository.bag.BagDemoSampleData
import com.github.jameshnsears.chance.data.repository.bag.BagSampleData
import com.github.jameshnsears.chance.data.repository.bag.DiceBagRepositoryTestDouble
import com.github.jameshnsears.chance.utils.rule.RuleMainDispatcher
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class DialogBagAndroidViewModelUnitTest {
    @get:Rule
    val ruleMainDispatcher = RuleMainDispatcher()

    @Test
    fun checkViewModelShowsCorrectDiceAndSideDetails() = runTest {
        val bagRepository = DiceBagRepositoryTestDouble.getInstance()
        bagRepository.store(
            listOf(
                BagDemoSampleData.diceHeadsTails,
            ),
        )

        val application = mockk<Application>()

        val dice = bagRepository.fetch()[0]
        val side = dice.sides[0]

        val mockDiceTitle = "Heads / Tails"
        every { application.getString(dice.titleStringsId) } returns mockDiceTitle

        val mockSideDescription = "Heads"
        every { application.getString(side.descriptionStringsId) } returns mockSideDescription

        val dialogBagAndroidViewModel = getDialogBagAndroidViewModel(application, bagRepository)

        assertEquals(dialogBagAndroidViewModel.sideNumber.value, side.number)
        assertEquals(dialogBagAndroidViewModel.sideColour.value, side.colour)
        assertEquals(dialogBagAndroidViewModel.sideDescription.value, mockSideDescription)

        assertEquals(dialogBagAndroidViewModel.diceSidesSliderPosition.value, 0.0f)
        assertEquals(dialogBagAndroidViewModel.diceTitle.value, mockDiceTitle)
        assertEquals(dialogBagAndroidViewModel.diceColour.value, dice.colour)
        assertFalse(dialogBagAndroidViewModel.diceCanBeDeleted.value)
    }

    @Test
    fun checkViewModelCanDeleteDice() = runTest {
        val bagRepository = DiceBagRepositoryTestDouble.getInstance()
        bagRepository.store(BagSampleData.allDice)

        val application = mockk<Application>()
        every { application.getString(any()) } returns ""

        val dialogBagAndroidViewModel: DialogBagAndroidViewModel =
            getDialogBagAndroidViewModel(application, bagRepository)

        assertTrue(dialogBagAndroidViewModel.diceCanBeDeleted.value)
    }

    private fun getDialogBagAndroidViewModel(
        application: Application,
        bagRepository: DiceBagRepositoryTestDouble,
    ): DialogBagAndroidViewModel {
        val dice = runBlocking {
            bagRepository.fetch()[0]
        }
        val side = dice.sides[0]

        return DialogBagAndroidViewModel(
            application,
            bagRepository,
            dice,
            side,
        )
    }
}
