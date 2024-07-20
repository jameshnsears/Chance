package com.github.jameshnsears.chance.data.repository.settings

import com.github.jameshnsears.chance.data.domain.core.settings.Settings
import com.github.jameshnsears.chance.data.domain.proto.SettingsProtocolBuffer
import com.github.jameshnsears.chance.data.repository.RepositoryImportExportInterface
import com.google.protobuf.util.JsonFormat
import kotlinx.coroutines.flow.Flow

interface RepositorySettingsInterface : RepositoryImportExportInterface {
    suspend fun fetch(): Flow<Settings>
    suspend fun store(newSettings: Settings)

    fun mapSettingsIntoSettingsProtocolBufferBuilder(
        settings: Settings,
        settingsProtocolBufferBuilder: SettingsProtocolBuffer.Builder,
    ) {
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

    fun jsomImportProcess(json: String): Settings {
        val settingsProtocolBufferBuilder: SettingsProtocolBuffer.Builder =
            SettingsProtocolBuffer.newBuilder()

        JsonFormat.parser().merge(json, settingsProtocolBufferBuilder)

        val settingsProtocolBuffer = settingsProtocolBufferBuilder.build()

        val newSettings = Settings()

        newSettings.resize = settingsProtocolBuffer.resize

        newSettings.rollIndexTime = settingsProtocolBuffer.rollIndexTime
        newSettings.rollScore = settingsProtocolBuffer.rollScore

        newSettings.diceTitle = settingsProtocolBuffer.diceTitle
        newSettings.sideNumber = settingsProtocolBuffer.sideNumber
        newSettings.behaviour = settingsProtocolBuffer.behaviour
        newSettings.sideDescription = settingsProtocolBuffer.sideDescription
        newSettings.sideSVG = settingsProtocolBuffer.sideSVG

        newSettings.rollSound = settingsProtocolBuffer.rollSound

        return newSettings
    }
}
