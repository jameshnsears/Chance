package com.github.jameshnsears.chance.data.repo.impl.settings

import com.github.jameshnsears.chance.data.domain.core.settings.SettingsDataInterface
import com.github.jameshnsears.chance.data.domain.proto.SettingsProtocolBuffer
import com.github.jameshnsears.chance.data.repo.api.settings.RepositorySettingsInterface

interface RepositorySettingsProtocolBufferInterface : RepositorySettingsInterface {
    fun mapSettingsIntoSettingsProtocolBufferBuilder(
        settingsData: SettingsDataInterface,
        settingsProtocolBufferBuilder: SettingsProtocolBuffer.Builder,
    ) {
        settingsProtocolBufferBuilder.setResize(settingsData.resize)

        settingsProtocolBufferBuilder.setRollIndexTime(settingsData.rollIndexTime)
        settingsProtocolBufferBuilder.setRollScore(settingsData.rollScore)

        settingsProtocolBufferBuilder.setDiceTitle(settingsData.diceTitle)
        settingsProtocolBufferBuilder.setSideNumber(settingsData.sideNumber)
        settingsProtocolBufferBuilder.setBehaviour(settingsData.rollBehaviour)
        settingsProtocolBufferBuilder.setSideDescription(settingsData.sideDescription)
        settingsProtocolBufferBuilder.setSideSVG(settingsData.sideSVG)

        settingsProtocolBufferBuilder.setRollSound(settingsData.rollSound)
        settingsProtocolBufferBuilder.setShuffle(settingsData.shuffle)

        settingsProtocolBufferBuilder.build()
    }

    fun jsonImportProcess(json: String): SettingsDataInterface
}
