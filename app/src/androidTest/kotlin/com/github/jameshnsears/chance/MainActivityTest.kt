package com.github.jameshnsears.chance

import androidx.test.platform.app.InstrumentationRegistry
import com.github.jameshnsears.chance.data.repository.bag.impl.RepositoryBag
import com.github.jameshnsears.chance.data.sample.bag.SampleBagTestData
import com.github.jameshnsears.chance.utility.logging.UtilityLoggingInstrumentedHelper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class MainActivityTest : UtilityLoggingInstrumentedHelper() {
    @Test
    fun switchTabs() = runTest {

    }
}
