package com.github.jameshnsears.chance.data.repository.settings.mock

import com.github.jameshnsears.chance.data.domain.proto.SettingsProtocolBuffer
import com.github.jameshnsears.chance.data.domain.state.Settings
import com.github.jameshnsears.chance.data.repository.settings.RepositorySettingsInterface
import com.google.protobuf.util.JsonFormat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class RepositorySettingsTestDouble private constructor() :
    RepositorySettingsInterface {
    companion object {
        private var instance: RepositorySettingsTestDouble? = null

        fun getInstance(): RepositorySettingsTestDouble {
            if (instance == null) {
                instance = RepositorySettingsTestDouble()
            }
            return instance!!
        }
    }

    private lateinit var settings: Settings

    override suspend fun fetch(): Flow<Settings> = flow {
        emit(settings)
    }

    override suspend fun store(newSettings: Settings) {
        CoroutineScope(Dispatchers.IO).launch {
            settings = newSettings
        }
    }

    override suspend fun exportJson(): String {
        val settingsProtocolBufferBuilder: SettingsProtocolBuffer.Builder =
            SettingsProtocolBuffer.newBuilder()

        mapSettingsIntoSettingsProtocolBufferBuilder(settings, settingsProtocolBufferBuilder)

        return JsonFormat.printer().includingDefaultValueFields()
            .print(settingsProtocolBufferBuilder.build())
    }

    override suspend fun importJson(json: String) {
        settings = Settings()

        val settingsProtocolBufferBuilder: SettingsProtocolBuffer.Builder =
            SettingsProtocolBuffer.newBuilder()

        JsonFormat.parser().merge(json, settingsProtocolBufferBuilder)

        val settingsProtocolBuffer = settingsProtocolBufferBuilder.build()

        val newSettings = Settings()

        newSettings.tabRowChance = settingsProtocolBuffer.tabRowChance

        newSettings.rollSequentially = settingsProtocolBuffer.rollSequentially
        newSettings.rollTime = settingsProtocolBuffer.rollTime
        newSettings.rollDiceTitle = settingsProtocolBuffer.rollDiceTitle
        newSettings.rollSideNumber = settingsProtocolBuffer.rollSideNumber
        newSettings.rollScore = settingsProtocolBuffer.rollScore
        newSettings.rollSound = settingsProtocolBuffer.rollSound

        store(newSettings)
    }
}
