package com.github.jameshnsears.chance.data.repository.settings.testdouble

import com.github.jameshnsears.chance.data.domain.core.settings.SettingsDataInterface
import com.github.jameshnsears.chance.data.domain.core.settings.testdouble.SettingsDataTestDouble
import com.github.jameshnsears.chance.data.domain.proto.SettingsProtocolBuffer
import com.github.jameshnsears.chance.data.repository.settings.RepositorySettingsInterface
import com.google.protobuf.util.JsonFormat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RepositorySettingsTestDouble private constructor() :
    RepositorySettingsInterface {
    companion object {
        private var instance: RepositorySettingsTestDouble? = null

        fun getInstance(
            settingsData: SettingsDataInterface = SettingsDataTestDouble()
        ): RepositorySettingsTestDouble {
            if (instance == null) {
                instance = RepositorySettingsTestDouble()
                instance!!.settings = settingsData
            }
            return instance!!
        }
    }

    private lateinit var settings: SettingsDataInterface

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

    override fun jsomImportProcess(json: String): SettingsDataInterface {
        val settingsProtocolBufferBuilder: SettingsProtocolBuffer.Builder =
            SettingsProtocolBuffer.newBuilder()

        JsonFormat.parser().merge(json, settingsProtocolBufferBuilder)

        val settingsProtocolBuffer = settingsProtocolBufferBuilder.build()

        val newSettings = SettingsDataTestDouble()

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

    override suspend fun fetch(): Flow<SettingsDataInterface> = flow {
        emit(settings)
    }

    override suspend fun store(settingsData: SettingsDataInterface) {
        settings = settingsData
    }

    override suspend fun clear() {
        settings = SettingsDataTestDouble()
    }
}
