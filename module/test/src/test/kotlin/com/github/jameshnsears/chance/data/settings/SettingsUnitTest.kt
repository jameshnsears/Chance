package com.github.jameshnsears.chance.data.settings

import com.github.jameshnsears.chance.data.protocolbuffer.SettingsProtocolBuffer
import com.github.jameshnsears.chance.data.settings.repository.SettingsRepositoryMock
import com.github.jameshnsears.chance.data.settings.sample.SettingsSampleData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class SettingsUnitTest {
    @Test
    fun demoProtocolBuffer() {
        val serialisation = SettingsSampleData.settingsProtocolBuffer.toByteArray()

        val import = SettingsProtocolBuffer.parseFrom(serialisation)

        assertEquals(
            SettingsSampleData.settingsProtocolBuffer.rollSelectedDiceList[0].sideList.size,
            import.rollSelectedDiceList[0].sideList.size
        )

        assertEquals(
            SettingsSampleData.settingsProtocolBuffer.bagZoom,
            import.bagZoom
        )

        assertEquals(
            SettingsSampleData.settingsProtocolBuffer.rollZoom,
            import.rollZoom
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun confirmRepositoryMockWorks() = runTest {
        val settingsRepositoryMock = SettingsRepositoryMock

        val settings = SettingsSampleData.headsTails

        runBlocking {
            settingsRepositoryMock.store(context = null, settings)
        }

        assertEquals(settings, settingsRepositoryMock.fetch(context = null).first())
    }
}
