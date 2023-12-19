package com.github.jameshnsears.chance.ui.zoom.bag

import com.github.jameshnsears.chance.data.bag.repository.BagRepositoryMock
import com.github.jameshnsears.chance.data.bag.sample.BagSampleData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Test

class ZoomBagViewModelUnitTest {
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun exerciseViewModel() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        try {
            val bagRepository = BagRepositoryMock
            bagRepository.store(BagSampleData.allDice)

            val viewModel = ZoomBagViewModel(bagRepository)

            assertEquals(7, viewModel.bagRepository.fetch().size)

            // TODO test the functions of the viewmodel!


        } finally {
            Dispatchers.resetMain()
        }
    }
}
