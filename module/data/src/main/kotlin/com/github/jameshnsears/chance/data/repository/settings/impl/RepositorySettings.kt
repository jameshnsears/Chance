package com.github.jameshnsears.chance.data.repository.settings.impl

import android.annotation.SuppressLint
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.github.jameshnsears.chance.data.domain.proto.SettingsProtocolBuffer
import com.github.jameshnsears.chance.data.domain.state.Settings
import com.github.jameshnsears.chance.data.repository.settings.RepositorySettingsInterface
import com.google.protobuf.util.JsonFormat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import timber.log.Timber


class RepositorySettings private constructor(private val context: Context) :
    RepositorySettingsInterface {
    companion object {
        @SuppressLint("StaticFieldLeak")
        private var instance: RepositorySettings? = null

        fun getInstance(context: Context): RepositorySettings {
            if (instance == null) {
                instance = RepositorySettings(context)
            }
            return instance!!
        }
    }

    override suspend fun fetch(): Flow<Settings> = flow {
        val settings: Settings = context.settingsDataStore.data
            .map { settingsProtocolBuffer ->
                Settings(
                    tabRowChance = settingsProtocolBuffer.tabRowChance,

                    resize = settingsProtocolBuffer.resize,

                    rollIndexTime = settingsProtocolBuffer.rollIndexTime,
                    rollScore = settingsProtocolBuffer.rollScore,

                    diceTitle = settingsProtocolBuffer.diceTitle,
                    sideNumber = settingsProtocolBuffer.sideNumber,
                    behaviour = settingsProtocolBuffer.behaviour,
                    sideDescription = settingsProtocolBuffer.sideDescription,
                    sideSVG = settingsProtocolBuffer.sideSVG,

                    rollSound = settingsProtocolBuffer.rollSound,
                )
            }.first()

        emit(settings)
    }

    override suspend fun store(newSettings: Settings) {
        Timber.d(newSettings.toString())

        context.settingsDataStore.updateData {
            val settingsProtocolBufferBuilder = it.toBuilder()
            mapSettingsIntoSettingsProtocolBufferBuilder(newSettings, settingsProtocolBufferBuilder)
            settingsProtocolBufferBuilder.build()
        }
    }

    override suspend fun exportJson(): String =
        JsonFormat.printer().includingDefaultValueFields()
            .print(context.settingsDataStore.data.first())

    override suspend fun importJson(json: String) {
        context.settingsDataStore.updateData {
            val settingsProtocolBuffer: SettingsProtocolBuffer.Builder =
                SettingsProtocolBuffer.newBuilder()
            JsonFormat.parser().merge(json, settingsProtocolBuffer)
            settingsProtocolBuffer.build()
        }
    }

    override suspend fun clear() {
        context.settingsDataStore.updateData {
            SettingsProtocolBuffer.newBuilder().build()
        }
    }
}

val Context.settingsDataStore: DataStore<SettingsProtocolBuffer> by dataStore(
    // /data/data/com.github.jameshnsears.chance.test.test/files/datastore
    fileName = "settings.pb",
    serializer = SettingsProtocolBufferSerializer,
)
