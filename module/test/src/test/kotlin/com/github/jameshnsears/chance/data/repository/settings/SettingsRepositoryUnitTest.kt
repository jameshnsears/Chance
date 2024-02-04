package com.github.jameshnsears.chance.data.repository.settings

import com.github.jameshnsears.chance.data.protocolbuffer.SettingsProtocolBuffer
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class SettingsRepositoryUnitTest {
    @Test
    fun demoProtocolBuffer() {
        val settingsProtocolBuffer: SettingsProtocolBuffer = SettingsProtocolBuffer.newBuilder()
            .setBagDemoBag(true)
            .build()

        val serialisation = settingsProtocolBuffer.toByteArray()

        val import = SettingsProtocolBuffer.parseFrom(serialisation)

        assertEquals(
            settingsProtocolBuffer.bagDemoBag,
            import.bagDemoBag,
        )
    }

    @Test
    fun storeAndFetchFromRepository() = runTest {
        val settingsRepositoryTestDouble = SettingsRepositoryTestDouble.getInstance()

        val settings = SettingsSampleData.settings

        settingsRepositoryTestDouble.store(settings)

        assertEquals(settings, settingsRepositoryTestDouble.fetch().first())
    }
}
