package com.github.jameshnsears.chance.data.repository.settings

import android.content.Context
import com.github.jameshnsears.chance.data.domain.Settings
import com.github.jameshnsears.chance.data.protocolbuffer.SettingsProtocolBuffer
import com.google.protobuf.util.JsonFormat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map


class SettingsRepository private constructor(val context: Context) :
    SettingsRepositoryInterface {
    companion object {
        private var instance: SettingsRepository? = null

        fun getInstance(context: Context): SettingsRepository {
            if (instance == null) {
                instance = SettingsRepository(context)
            }
            return instance!!
        }
    }

    override suspend fun fetch(): Flow<Settings> = flow {
        val settings: Settings = context.settingsDataStore.data
            .map { settingsProtocolBuffer ->
                // map SettingsProtocolBuffer into Settings
                Settings(
                    tabRowChance = settingsProtocolBuffer.tabRowChance,
                    bagDemoBag = settingsProtocolBuffer.bagDemoBag,
                    rollSequentially = settingsProtocolBuffer.rollSequentially,
                    rollHistory = settingsProtocolBuffer.rollHistory,
                    rollScore = settingsProtocolBuffer.rollScore,
                    rollDiceTitle = settingsProtocolBuffer.rollDiceTitle,
                    rollSideNumber = settingsProtocolBuffer.rollSideNumber,
                    rollSound = settingsProtocolBuffer.rollSound
                )
            }.first()

        emit(settings)
    }

    override suspend fun store(settings: Settings) {
        context.settingsDataStore.updateData { it ->
            mapSeetingsIntoSettingsProtocolBuffer(settings, it).build()
        }
    }

    fun mapSeetingsIntoSettingsProtocolBuffer(
        settings: Settings,
        settingsProtocolBuffer: SettingsProtocolBuffer
    ): SettingsProtocolBuffer.Builder {
        var settingsProtocolBufferBuilder = settingsProtocolBuffer.toBuilder()
        mapSettingsIntoSettingsProtocolBufferBuilder(settings, settingsProtocolBufferBuilder)
        return settingsProtocolBufferBuilder
    }

    override suspend fun export(): String =
        JsonFormat.printer().print(context.settingsDataStore.data.first())

    override suspend fun import(json: String) {
        context.settingsDataStore.updateData {
            val settingsProtocolBuffer: SettingsProtocolBuffer.Builder =
                SettingsProtocolBuffer.newBuilder()
            JsonFormat.parser().merge(json, settingsProtocolBuffer)
            settingsProtocolBuffer.build()
        }
    }

    suspend fun empty() {
        context.settingsDataStore.updateData {
            SettingsProtocolBuffer.newBuilder().build()
        }
    }
}
