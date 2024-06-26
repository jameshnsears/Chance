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

        settingsProtocolBufferBuilder.setResize(settings.resize)

        settingsProtocolBufferBuilder.setRollIndexTime(settings.rollIndexTime)
        settingsProtocolBufferBuilder.setRollScore(settings.rollScore)

        settingsProtocolBufferBuilder.setDiceTitle(settings.diceTitle)
        settingsProtocolBufferBuilder.setSideNumber(settings.sideNumber)
        settingsProtocolBufferBuilder.setBehaviour(settings.behaviour)
        settingsProtocolBufferBuilder.setSideDescription(settings.sideDescription)
        settingsProtocolBufferBuilder.setSideSVG(settings.sideSVG)

        settingsProtocolBufferBuilder.setRollSound(settings.rollSound)

        settingsProtocolBufferBuilder.build()
    }
}
