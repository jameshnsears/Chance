package com.github.jameshnsears.chance.data.repository.settings

import com.github.jameshnsears.chance.data.protocolbuffer.DiceProtocolBuffer
import com.github.jameshnsears.chance.data.protocolbuffer.SettingsProtocolBuffer
import com.github.jameshnsears.chance.data.protocolbuffer.SideProtocolBuffer
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class SettingsRepositoryUnitTest {
    @Test
    fun demoProtocolBuffer() {
        val settingsProtocolBuffer: SettingsProtocolBuffer = SettingsProtocolBuffer.newBuilder()
            .addRollSelectedDice(
                DiceProtocolBuffer.newBuilder()
                    .setTitle("heads / tails")
                    .addSide(
                        SideProtocolBuffer.newBuilder()
                            .setNumber(2)
                            .setDescription("heads")
                            .build()
                    )
                    .addSide(
                        SideProtocolBuffer.newBuilder()
                            .setNumber(1)
                            .setDescription("tails")
                            .build()
                    )
                    .setSelected(false)
                    .build()
            )
            .setBagZoom(5)
            .setRollZoom(10)
            .build()

        val serialisation = settingsProtocolBuffer.toByteArray()

        val import = SettingsProtocolBuffer.parseFrom(serialisation)

        assertEquals(
            settingsProtocolBuffer.rollSelectedDiceList[0].sideList.size,
            import.rollSelectedDiceList[0].sideList.size
        )

        assertEquals(
            settingsProtocolBuffer.bagZoom,
            import.bagZoom
        )

        assertEquals(
            settingsProtocolBuffer.rollZoom,
            import.rollZoom
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `store and fetch`() = runTest {
        val settingsRepositoryTestDouble = SettingsRepositoryTestDouble.getInstance()

        val settings = SettingsSampleData.headsTails

        runBlocking {
            settingsRepositoryTestDouble.store(settings)

            assertEquals(settings, settingsRepositoryTestDouble.fetch().first())
        }
    }
}
