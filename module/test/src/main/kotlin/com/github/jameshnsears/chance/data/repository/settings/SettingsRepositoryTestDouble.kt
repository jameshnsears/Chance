package com.github.jameshnsears.chance.data.repository.settings

import com.github.jameshnsears.chance.data.domain.Settings
import com.github.jameshnsears.chance.data.protocolbuffer.SettingsProtocolBuffer
import com.google.protobuf.util.JsonFormat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SettingsRepositoryTestDouble private constructor() :
    SettingsRepositoryInterface {
    companion object {
        private var instance: SettingsRepositoryTestDouble? = null

        fun getInstance(): SettingsRepositoryTestDouble {
            if (instance == null) {
                instance = SettingsRepositoryTestDouble()
            }
            return instance!!
        }
    }

    private lateinit var settings: Settings

    override suspend fun fetch(): Flow<Settings> = flow {
        emit(settings)
    }

    override suspend fun store(newSettings: Settings) {
        settings = newSettings
    }

    override suspend fun export(): String {
        val settingsProtocolBufferBuilder: SettingsProtocolBuffer.Builder =
            SettingsProtocolBuffer.newBuilder()

        mapSettingsIntoSettingsProtocolBufferBuilder(settings, settingsProtocolBufferBuilder)

        return JsonFormat.printer().print(settingsProtocolBufferBuilder.build())
    }

    override suspend fun import(json: String) {
        settings = Settings()

        val settingsProtocolBufferBuilder: SettingsProtocolBuffer.Builder =
            SettingsProtocolBuffer.newBuilder()

        JsonFormat.parser().merge(json, settingsProtocolBufferBuilder)

        val settingsProtocolBuffer = settingsProtocolBufferBuilder.build()

        val settings = Settings()

        settings.tabRowChance = settingsProtocolBuffer.tabRowChance

        settings.bagZoom = settingsProtocolBuffer.bagZoom
        settings.bagDemoBag = settingsProtocolBuffer.bagDemoBag

        settings.rollSequentially = settingsProtocolBuffer.rollSequentially
        settings.rollZoom = settingsProtocolBuffer.rollZoom
        settings.rollHistory = settingsProtocolBuffer.rollHistory
        settings.rollTitle = settingsProtocolBuffer.rollTitle
        settings.rollSideNumber = settingsProtocolBuffer.rollSideNumber
        settings.rollTotal = settingsProtocolBuffer.rollTotal
        settings.rollSound = settingsProtocolBuffer.rollSound

        store(settings)
    }
}
