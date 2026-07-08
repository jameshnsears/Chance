package com.github.jameshnsears.chance.data.repo.impl.settings

import com.github.jameshnsears.chance.data.domain.core.settings.SettingsDataInterface
import com.github.jameshnsears.chance.data.domain.core.settings.impl.SettingsDataImpl
import com.github.jameshnsears.chance.data.domain.proto.SettingsProtocolBuffer
import com.github.jameshnsears.chance.data.repo.api.settings.RepositorySettingsInterface
import com.google.protobuf.util.JsonFormat

interface RepositorySettingsProtocolBufferInterface : RepositorySettingsInterface {
    fun mapSettingsIntoSettingsProtocolBufferBuilder(
        settingsData: SettingsDataInterface,
        settingsProtocolBufferBuilder: SettingsProtocolBuffer.Builder,
    ) {
        settingsProtocolBufferBuilder.setResizeZoom(settingsData.resizeZoom)

        settingsProtocolBufferBuilder.setRollIndexTime(settingsData.rollIndexTime)
        settingsProtocolBufferBuilder.setRollScore(settingsData.rollScore)
        settingsProtocolBufferBuilder.setRollScoreTTS(settingsData.rollScoreTTS)

        settingsProtocolBufferBuilder.setDiceTitle(settingsData.diceTitle)
        settingsProtocolBufferBuilder.setSideNumber(settingsData.sideNumber)
        settingsProtocolBufferBuilder.setBehaviour(settingsData.rollBehaviour)
        settingsProtocolBufferBuilder.setSideDescription(settingsData.sideDescription)
        settingsProtocolBufferBuilder.setSideSVG(settingsData.sideSVG)

        settingsProtocolBufferBuilder.setHaptics(settingsData.haptics)
        settingsProtocolBufferBuilder.setRollSound(settingsData.rollSound)
        settingsProtocolBufferBuilder.setShuffle(settingsData.shuffle)
        settingsProtocolBufferBuilder.setShakeToRoll(settingsData.shakeToRoll)

        settingsProtocolBufferBuilder.setGroupTitle(settingsData.groupTitle)

        settingsProtocolBufferBuilder.build()
    }

    fun mapSettingsProtocolBufferIntoSettings(
        settingsProtocolBuffer: SettingsProtocolBuffer,
    ): SettingsDataInterface {
        return SettingsDataImpl(
            resizeZoom = settingsProtocolBuffer.resizeZoom,

            rollIndexTime = settingsProtocolBuffer.rollIndexTime,
            rollScore = settingsProtocolBuffer.rollScore,
            rollScoreTTS = settingsProtocolBuffer.rollScoreTTS,

            diceTitle = settingsProtocolBuffer.diceTitle,
            sideNumber = settingsProtocolBuffer.sideNumber,
            rollBehaviour = settingsProtocolBuffer.behaviour,
            sideDescription = settingsProtocolBuffer.sideDescription,
            sideSVG = settingsProtocolBuffer.sideSVG,

            haptics = settingsProtocolBuffer.haptics,
            rollSound = settingsProtocolBuffer.rollSound,
            shuffle = settingsProtocolBuffer.shuffle,
            shakeToRoll = settingsProtocolBuffer.shakeToRoll,
            groupTitle = settingsProtocolBuffer.groupTitle,
        )
    }

    fun jsonImportProcess(json: String): SettingsDataInterface {
        val settingsProtocolBufferBuilder: SettingsProtocolBuffer.Builder =
            SettingsProtocolBuffer.newBuilder()

        JsonFormat.parser().merge(json, settingsProtocolBufferBuilder)

        return mapSettingsProtocolBufferIntoSettings(settingsProtocolBufferBuilder.build())
    }
}
