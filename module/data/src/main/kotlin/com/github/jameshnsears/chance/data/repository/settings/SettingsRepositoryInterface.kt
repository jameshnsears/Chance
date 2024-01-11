package com.github.jameshnsears.chance.data.repository.settings

import com.github.jameshnsears.chance.data.domain.Dice
import com.github.jameshnsears.chance.data.domain.Settings
import com.github.jameshnsears.chance.data.domain.Side
import com.github.jameshnsears.chance.data.protocolbuffer.DiceProtocolBuffer
import com.github.jameshnsears.chance.data.protocolbuffer.SettingsProtocolBuffer
import com.github.jameshnsears.chance.data.protocolbuffer.SideProtocolBuffer
import com.github.jameshnsears.chance.data.repository.ImportExportRepositoryInterface
import kotlinx.coroutines.flow.Flow

interface SettingsRepositoryInterface : ImportExportRepositoryInterface {
    suspend fun fetch(): Flow<Settings>
    suspend fun store(newSettings: Settings)

    fun mapSettingsIntoSettingsProtocolBufferBuilder(
        settings: Settings,
        settingsProtocolBufferBuilder: SettingsProtocolBuffer.Builder
    ) {
        settingsProtocolBufferBuilder.setTabRowChance(settings.tabRowChance)

        settingsProtocolBufferBuilder.setBagZoom(settings.bagZoom)
        settingsProtocolBufferBuilder.setBagDemoBag(settings.bagDemoBag)

        for (dice: Dice in settings.rollSelectedDice) {
            val diceProtocolBuffer = DiceProtocolBuffer.newBuilder()
                .setTitle(dice.title)

            for (side: Side in dice.sides) {
                diceProtocolBuffer.addSide(
                    SideProtocolBuffer.newBuilder()
                        .setNumber(side.number)
                        .setDescription(side.description)
                        .build()
                )
            }

            diceProtocolBuffer.build()

            settingsProtocolBufferBuilder.addRollSelectedDice(diceProtocolBuffer)
        }

        settingsProtocolBufferBuilder.setRollSequentially(settings.rollSequentially)
        settingsProtocolBufferBuilder.setRollZoom(settings.rollZoom)
        settingsProtocolBufferBuilder.setRollHistory(settings.rollHistory)
        settingsProtocolBufferBuilder.setRollTitle(settings.rollTitle)
        settingsProtocolBufferBuilder.setRollSideNumber(settings.rollSideNumber)
        settingsProtocolBufferBuilder.setRollTotal(settings.rollTotal)
        settingsProtocolBufferBuilder.setRollSound(settings.rollSound)
    }
}
