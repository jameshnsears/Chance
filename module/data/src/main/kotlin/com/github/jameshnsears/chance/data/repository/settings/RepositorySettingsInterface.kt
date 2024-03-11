package com.github.jameshnsears.chance.data.repository.settings

import com.github.jameshnsears.chance.data.domain.proto.SettingsProtocolBuffer
import com.github.jameshnsears.chance.data.domain.state.Settings
import com.github.jameshnsears.chance.data.repository.RepositoryImportExportInterface
import kotlinx.coroutines.flow.Flow

interface RepositorySettingsInterface : RepositoryImportExportInterface {
    suspend fun fetch(): Flow<Settings>
    suspend fun store(newSettings: Settings)

    fun mapSettingsIntoSettingsProtocolBufferBuilder(
        settings: Settings,
        settingsProtocolBufferBuilder: SettingsProtocolBuffer.Builder,
    ) {
        settingsProtocolBufferBuilder.setTabRowChance(settings.tabRowChance)

        settingsProtocolBufferBuilder.setRollSequentially(settings.rollSequentially)
        settingsProtocolBufferBuilder.setRollTime(settings.rollTime)
        settingsProtocolBufferBuilder.setRollDiceTitle(settings.rollDiceTitle)
        settingsProtocolBufferBuilder.setRollSideNumber(settings.rollSideNumber)
        settingsProtocolBufferBuilder.setRollScore(settings.rollScore)
        settingsProtocolBufferBuilder.setRollSound(settings.rollSound)
        settingsProtocolBufferBuilder.build()
    }
}
