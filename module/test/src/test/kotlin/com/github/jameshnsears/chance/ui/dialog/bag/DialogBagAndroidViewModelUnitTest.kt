package com.github.jameshnsears.chance.ui.dialog.bag

import android.app.Application
import com.github.jameshnsears.chance.MainDispatcherRule
import com.github.jameshnsears.chance.data.repository.bag.BagDemoSampleData
import com.github.jameshnsears.chance.data.repository.bag.BagRepositoryTestDouble
import com.github.jameshnsears.chance.data.repository.bag.BagSampleData
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test

class DialogBagAndroidViewModelUnitTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun confirmDiceAndSideDetailsAvailable() = runTest {
        val bagRepository = BagRepositoryTestDouble.getInstance()
        bagRepository.store(
            listOf(
                BagDemoSampleData.diceHeadsTails
            )
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
        assertFalse(dialogBagAndroidViewModel.diceCanBeDeleted())
    }

    @Test
    fun diceCanBeDeleted() = runTest {
        val bagRepository = BagRepositoryTestDouble.getInstance()
        bagRepository.store(BagSampleData.allDice)

        val application = mockk<Application>()
        every { application.getString(any()) } returns ""

        val dialogBagAndroidViewModel = getDialogBagAndroidViewModel(application, bagRepository)

        assertTrue(dialogBagAndroidViewModel.diceCanBeDeleted())
    }

    @Ignore
    @Test
    fun todo() = runTest {
        val bagRepository = BagRepositoryTestDouble.getInstance()
        bagRepository.store(
            listOf(
                BagDemoSampleData.diceHeadsTails
            )
        )

        val application = mockk<Application>()
        every { application.getString(any()) } returns ""

        val dialogBagAndroidViewModel = getDialogBagAndroidViewModel(application, bagRepository)

        val sideColour = "sideColour"
        dialogBagAndroidViewModel.sideColour(sideColour)

        val sideDescription = "sideDescription"
        dialogBagAndroidViewModel.sideDescription(sideDescription)

        /////////////////

        dialogBagAndroidViewModel.diceSidesSliderPosition(2.0f)

        val diceTitle = "diceTitle"
        dialogBagAndroidViewModel.diceTitle(diceTitle)

        val diceColour = "diceColour"
        dialogBagAndroidViewModel.diceColour(diceColour)

        fail("todo")

        dialogBagAndroidViewModel.save()

        val diceBag = bagRepository.fetch()
        assertEquals(diceBag.size, 1)

        val dice = diceBag[0]
        val side = dice.sides[0]
        assertEquals(side.colour, sideColour)
        assertEquals(side.description, sideDescription)

        assertEquals(dice.sides.size, 6)
        assertEquals(dice.title, diceTitle)
        assertEquals(dice.colour, diceColour)
    }

    private fun getDialogBagAndroidViewModel(
        application: Application,
        bagRepository: BagRepositoryTestDouble
    ): DialogBagAndroidViewModel {
        val dice = runBlocking {
            bagRepository.fetch()[0]
        }
        val side = dice.sides[0]

        return DialogBagAndroidViewModel(
            application,
            bagRepository,
            dice,
            side
        )
    }
}
