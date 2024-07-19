package com.github.jameshnsears.chance.data.repository.settings.testdouble

import com.github.jameshnsears.chance.data.domain.core.settings.Settings
import com.github.jameshnsears.chance.data.domain.proto.SettingsProtocolBuffer
import com.github.jameshnsears.chance.data.repository.settings.RepositorySettingsInterface
import com.google.protobuf.util.JsonFormat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class RepositorySettingsTestDouble private constructor() :
    RepositorySettingsInterface {
    companion object {
        private var instance: RepositorySettingsTestDouble? = null

        fun getInstance(settings: Settings = Settings()): RepositorySettingsTestDouble {
            if (instance == null) {
                instance = RepositorySettingsTestDouble()
                instance!!.settings = settings
            }
            return instance!!
        }
    }

    private lateinit var settings: Settings

    override suspend fun fetch(): Flow<Settings> = flow {
        emit(settings)
    }

    override suspend fun store(newSettings: Settings) {
        Timber.d(newSettings.toString())
        settings = newSettings
    }

    override suspend fun jsonExport(): String {
        val settingsProtocolBufferBuilder: SettingsProtocolBuffer.Builder =
            SettingsProtocolBuffer.newBuilder()

        mapSettingsIntoSettingsProtocolBufferBuilder(settings, settingsProtocolBufferBuilder)

        return JsonFormat.printer().includingDefaultValueFields()
            .print(settingsProtocolBufferBuilder.build())
    }

    override suspend fun jsonImport(json: String) {
        settings = Settings()

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

        store(newSettings)
    }

    override suspend fun clear() {
        settings = Settings()
    }
}
