package com.github.jameshnsears.chance.data.repository.settings

import com.github.jameshnsears.chance.data.domain.Settings
import com.github.jameshnsears.chance.data.protocolbuffer.SettingsProtocolBuffer
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

        settingsProtocolBufferBuilder.setBagDemoBag(settings.bagDemoBag)

        settingsProtocolBufferBuilder.setRollSequentially(settings.rollSequentially)
        settingsProtocolBufferBuilder.setRollHistory(settings.rollHistory)
        settingsProtocolBufferBuilder.setRollDiceTitle(settings.rollDiceTitle)
        settingsProtocolBufferBuilder.setRollSideNumber(settings.rollSideNumber)
        settingsProtocolBufferBuilder.setRollScore(settings.rollScore)
        settingsProtocolBufferBuilder.setRollSound(settings.rollSound)
    }
}
