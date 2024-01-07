package com.github.jameshnsears.chance.ui.dialog.bag

import android.app.Application
import com.github.jameshnsears.chance.data.bag.demo.BagDemoData
import com.github.jameshnsears.chance.data.bag.repository.BagRepositoryMock
import com.github.jameshnsears.chance.data.bag.sample.BagSampleData
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test

class DialogBagAndroidViewModelUnitTest {
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun confirmDiceAndSideDetailsAvailable() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        try {
            val bagRepository = BagRepositoryMock
            bagRepository.store(
                listOf(
                    BagDemoData.diceHeadsTails
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
            assertEquals(dialogBagAndroidViewModel.sideImageFilename.value, side.imageFilename)
            assertEquals(dialogBagAndroidViewModel.sideDescription.value, mockSideDescription)

            assertEquals(dialogBagAndroidViewModel.diceSidesSliderPosition.value, 0.0f)
            assertEquals(dialogBagAndroidViewModel.diceTitle.value, mockDiceTitle)
            assertEquals(dialogBagAndroidViewModel.diceColour.value, dice.colour)
            assertFalse(dialogBagAndroidViewModel.diceCanBeDeleted())
        } finally {
            Dispatchers.resetMain()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun diceCanBeDeleted() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        try {
            val bagRepository = BagRepositoryMock
            bagRepository.store(BagSampleData.allDice)

            val application = mockk<Application>()
            every { application.getString(any()) } returns ""

            val dialogBagAndroidViewModel = getDialogBagAndroidViewModel(application, bagRepository)

            assertTrue(dialogBagAndroidViewModel.diceCanBeDeleted())
        } finally {
            Dispatchers.resetMain()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun save() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        try {
            val bagRepository = BagRepositoryMock
            bagRepository.store(
                listOf(
                    BagDemoData.diceHeadsTails
                )
            )

            val application = mockk<Application>()
            every { application.getString(any()) } returns ""

            val dialogBagAndroidViewModel = getDialogBagAndroidViewModel(application, bagRepository)

            val sideColour = "sideColour"
            dialogBagAndroidViewModel.sideColour(sideColour)

            val sideImageFilename = "sideImageFilename"
            dialogBagAndroidViewModel.sideImageFilename(sideImageFilename)

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
            assertEquals(side.imageFilename, sideImageFilename)
            assertEquals(side.description, sideDescription)

            assertEquals(dice.sides.size, 6)
            assertEquals(dice.title, diceTitle)
            assertEquals(dice.colour, diceColour)
        } finally {
            Dispatchers.resetMain()
        }
    }

    private fun getDialogBagAndroidViewModel(
        application: Application,
        bagRepository: BagRepositoryMock
    ): DialogBagAndroidViewModel {
        val dice = bagRepository.fetch()[0]
        val side = dice.sides[0]

        return DialogBagAndroidViewModel(
            application,
            bagRepository,
            dice,
            side
        )
    }
}
