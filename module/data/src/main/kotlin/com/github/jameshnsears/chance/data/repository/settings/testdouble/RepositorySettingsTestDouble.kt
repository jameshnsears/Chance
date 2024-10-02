package com.github.jameshnsears.chance.data.repository.settings.testdouble

import com.github.jameshnsears.chance.data.domain.core.settings.SettingsDataInterface
import com.github.jameshnsears.chance.data.domain.core.settings.testdouble.SettingsDataTestDouble
import com.github.jameshnsears.chance.data.domain.proto.SettingsProtocolBuffer
import com.github.jameshnsears.chance.data.repository.settings.RepositorySettingsInterface
import com.google.protobuf.Descriptors
import com.google.protobuf.util.JsonFormat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking

class RepositorySettingsTestDouble private constructor() :
    RepositorySettingsInterface {
    companion object {
        private var instance: RepositorySettingsTestDouble? = null

        fun getInstance(
            settingsData: SettingsDataInterface
        ): RepositorySettingsTestDouble {
            synchronized(this) {
                if (instance == null) {
                    runBlocking {
                        instance = RepositorySettingsTestDouble()

                        instance!!.settings = settingsData
                    }
                }
            }
            return instance!!
        }
    }

    private var settings: SettingsDataInterface? = null

    override suspend fun jsonExport(): String {
        val settingsProtocolBufferBuilder: SettingsProtocolBuffer.Builder =
            SettingsProtocolBuffer.newBuilder()

        mapSettingsIntoSettingsProtocolBufferBuilder(settings!!, settingsProtocolBufferBuilder)

        val fieldsToAlwaysOutput: MutableSet<Descriptors.FieldDescriptor> = HashSet()
        fieldsToAlwaysOutput.add(SettingsProtocolBuffer.getDescriptor().findFieldByName("resize"))
        fieldsToAlwaysOutput.add(SettingsProtocolBuffer.getDescriptor().findFieldByName("rollIndexTime"))
        fieldsToAlwaysOutput.add(SettingsProtocolBuffer.getDescriptor().findFieldByName("rollScore"))
        fieldsToAlwaysOutput.add(SettingsProtocolBuffer.getDescriptor().findFieldByName("diceTitle"))
        fieldsToAlwaysOutput.add(SettingsProtocolBuffer.getDescriptor().findFieldByName("sideNumber"))
        fieldsToAlwaysOutput.add(SettingsProtocolBuffer.getDescriptor().findFieldByName("behaviour"))
        fieldsToAlwaysOutput.add(SettingsProtocolBuffer.getDescriptor().findFieldByName("sideDescription"))
        fieldsToAlwaysOutput.add(SettingsProtocolBuffer.getDescriptor().findFieldByName("sideSVG"))
        fieldsToAlwaysOutput.add(SettingsProtocolBuffer.getDescriptor().findFieldByName("rollSound"))

        return JsonFormat.printer().includingDefaultValueFields(fieldsToAlwaysOutput)
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
        newSettings.rollBehaviour = settingsProtocolBuffer.behaviour
        newSettings.sideDescription = settingsProtocolBuffer.sideDescription
        newSettings.sideSVG = settingsProtocolBuffer.sideSVG

        newSettings.rollSound = settingsProtocolBuffer.rollSound

        return newSettings
    }

    override suspend fun fetch(): Flow<SettingsDataInterface> = flow {
        emit(settings!!)
    }

    override suspend fun store(settingsData: SettingsDataInterface) {
        settings = settingsData
    }

    override suspend fun clear() {
        settings = null
    }
}
