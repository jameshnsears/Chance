package com.github.jameshnsears.chance.data.settings.sample

import com.github.jameshnsears.chance.data.domain.Dice
import com.github.jameshnsears.chance.data.domain.Settings
import com.github.jameshnsears.chance.data.domain.Side
import com.github.jameshnsears.chance.data.protocolbuffer.DiceProtocolBuffer
import com.github.jameshnsears.chance.data.protocolbuffer.SettingsProtocolBuffer
import com.github.jameshnsears.chance.data.protocolbuffer.SideProtocolBuffer

class SettingsSampleData {
    companion object {
        val headsTails = Settings(
            rollSelectedDice = listOf(
                Dice(
                    title = "Heads / Tails",
                    sides = listOf(
                        Side(
                            number = 2,
                            description = "Heads"
                        ),
                        Side(
                            number = 1,
                            description = "Tails"
                        )
                    )
                )
            ),
            bagZoom = 5,
            rollZoom = 10
        )

        val settingsProtocolBuffer = SettingsProtocolBuffer.newBuilder()
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
                    .build()
            )
            .setBagZoom(5)
            .setRollZoom(10)
            .build()
    }
}