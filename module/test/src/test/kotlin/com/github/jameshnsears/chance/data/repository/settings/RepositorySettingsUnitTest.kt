package com.github.jameshnsears.chance.data.repository.settings

import com.github.jameshnsears.chance.data.repository.settings.mock.RepositorySettingsTestDouble
import com.github.jameshnsears.chance.data.sample.settings.SampleSettings
import com.github.jameshnsears.chance.utility.android.UtilityAndroid
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class RepositorySettingsUnitTest : UtilityAndroid() {
    @Test
    fun storeAndFetch() = runTest {
        val repositorySettingsTestDouble = RepositorySettingsTestDouble.getInstance()

        val settings = SampleSettings.settings

        repositorySettingsTestDouble.store(settings)

        assertEquals(settings, repositorySettingsTestDouble.fetch().first())
    }
}
