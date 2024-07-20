package com.github.jameshnsears.chance.data.repository.settings.testdouble

import com.github.jameshnsears.chance.data.domain.core.settings.Settings
import com.github.jameshnsears.chance.data.domain.proto.SettingsProtocolBuffer
import com.github.jameshnsears.chance.data.repository.settings.RepositorySettingsInterface
import com.google.protobuf.util.JsonFormat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

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

    override suspend fun jsonExport(): String {
        val settingsProtocolBufferBuilder: SettingsProtocolBuffer.Builder =
            SettingsProtocolBuffer.newBuilder()

        mapSettingsIntoSettingsProtocolBufferBuilder(settings, settingsProtocolBufferBuilder)

        return JsonFormat.printer().includingDefaultValueFields()
            .print(settingsProtocolBufferBuilder.build())
    }

    override suspend fun jsonImport(json: String) {
        store(jsomImportProcess(json))
    }

    override suspend fun fetch(): Flow<Settings> = flow {
        emit(settings)
    }

    override suspend fun store(newSettings: Settings) {
        settings = newSettings
    }

    override suspend fun clear() {
        settings = Settings()
    }
}
