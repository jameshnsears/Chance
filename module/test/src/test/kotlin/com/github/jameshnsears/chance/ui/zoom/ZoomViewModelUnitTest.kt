package com.github.jameshnsears.chance.ui.zoom

import com.github.jameshnsears.chance.data.bag.repository.BagRepositoryMock
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Test

class ZoomViewModelUnitTest {
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun exerciseViewModel() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        try {
            val viewModel = ZoomViewModel(BagRepositoryMock)

            val bagDemo = viewModel.bagDemo()

            assertEquals(4, bagDemo.size)

            bagDemo.forEach { dice ->
                println(dice)
            }

        } finally {
            Dispatchers.resetMain()
        }
    }
}
