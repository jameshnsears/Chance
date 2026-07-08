package com.github.jameshnsears.chance.data.repo.api.settings

import com.github.jameshnsears.chance.common.utility.UtilityAndroidUnitTestHelper
import com.github.jameshnsears.chance.data.domain.core.settings.SettingsDataInterface
import com.github.jameshnsears.chance.data.domain.core.settings.impl.SettingsDataImpl
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class RepositorySettingsUnitTest : UtilityAndroidUnitTestHelper() {
    @Test
    fun fetchSettings() = runTest {
        val mockRepository = mockk<RepositorySettingsInterface>()
        val settingsData: SettingsDataInterface = SettingsDataImpl(resizeZoom = 3.0f)

        coEvery { mockRepository.fetch() } returns flowOf(settingsData)

        val result = mockRepository.fetch().first()
        assertEquals(3.0f, result.resizeZoom)
    }

    @Test
    fun storeSettings() = runTest {
        val mockRepository = mockk<RepositorySettingsInterface>(relaxed = true)
        val settingsData: SettingsDataInterface = SettingsDataImpl(resizeZoom = 4.0f)

        mockRepository.store(settingsData)

        coVerify { mockRepository.store(settingsData) }
    }
}
