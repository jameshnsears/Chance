package com.github.jameshnsears.chance.data.repo.impl.settings.testdouble

import com.github.jameshnsears.chance.data.domain.core.settings.SettingsDataInterface
import com.github.jameshnsears.chance.data.domain.proto.SettingsProtocolBuffer
import com.github.jameshnsears.chance.data.repo.impl.settings.RepositorySettingsProtocolBufferInterface
import com.google.protobuf.Descriptors
import com.google.protobuf.util.JsonFormat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import timber.log.Timber

class RepositorySettingsProtocolBufferTestDouble private constructor() :
    RepositorySettingsProtocolBufferInterface {
    private val settingsProtocolBufferStateFlow =
        MutableStateFlow(SettingsProtocolBuffer.getDefaultInstance())

    private var initialized = false

    companion object {
        private var instance: RepositorySettingsProtocolBufferTestDouble? = null

        fun getInstance(
            settingsData: SettingsDataInterface,
        ): RepositorySettingsProtocolBufferTestDouble {
            if (instance == null) {
                instance = RepositorySettingsProtocolBufferTestDouble()
            }

            if (!instance!!.initialized) {
                instance!!.updateStateFlow(settingsData)
                instance!!.initialized = true
            }
            return instance!!
        }
    }

    private fun updateStateFlow(settingsData: SettingsDataInterface) {
        val settingsProtocolBufferBuilder = SettingsProtocolBuffer.newBuilder()
        mapSettingsIntoSettingsProtocolBufferBuilder(settingsData, settingsProtocolBufferBuilder)
        settingsProtocolBufferStateFlow.value = settingsProtocolBufferBuilder.build()
    }

    override suspend fun jsonExport(): String {
        val fieldsToAlwaysOutput: MutableSet<Descriptors.FieldDescriptor> = HashSet()
        fieldsToAlwaysOutput.add(SettingsProtocolBuffer.getDescriptor().findFieldByName("resizeZoom"))
        fieldsToAlwaysOutput.add(SettingsProtocolBuffer.getDescriptor().findFieldByName("rollIndexTime"))
        fieldsToAlwaysOutput.add(SettingsProtocolBuffer.getDescriptor().findFieldByName("rollScore"))
        fieldsToAlwaysOutput.add(SettingsProtocolBuffer.getDescriptor().findFieldByName("diceTitle"))
        fieldsToAlwaysOutput.add(SettingsProtocolBuffer.getDescriptor().findFieldByName("sideNumber"))
        fieldsToAlwaysOutput.add(SettingsProtocolBuffer.getDescriptor().findFieldByName("behaviour"))
        fieldsToAlwaysOutput.add(SettingsProtocolBuffer.getDescriptor().findFieldByName("sideDescription"))
        fieldsToAlwaysOutput.add(SettingsProtocolBuffer.getDescriptor().findFieldByName("sideSVG"))
        fieldsToAlwaysOutput.add(SettingsProtocolBuffer.getDescriptor().findFieldByName("rollSound"))
        fieldsToAlwaysOutput.add(SettingsProtocolBuffer.getDescriptor().findFieldByName("shuffle"))
        fieldsToAlwaysOutput.add(SettingsProtocolBuffer.getDescriptor().findFieldByName("haptics"))
        fieldsToAlwaysOutput.add(SettingsProtocolBuffer.getDescriptor().findFieldByName("rollScoreTTS"))
        fieldsToAlwaysOutput.add(SettingsProtocolBuffer.getDescriptor().findFieldByName("shakeToRoll"))
        fieldsToAlwaysOutput.add(SettingsProtocolBuffer.getDescriptor().findFieldByName("groupTitle"))
        fieldsToAlwaysOutput.add(SettingsProtocolBuffer.getDescriptor().findFieldByName("history"))

        return JsonFormat.printer().includingDefaultValueFields(fieldsToAlwaysOutput)
            .print(settingsProtocolBufferStateFlow.value)
    }

    override suspend fun jsonImport(json: String) {
        store(jsonImportProcess(json))
    }

    override fun fetch(): Flow<SettingsDataInterface> = settingsProtocolBufferStateFlow
        .map { settingsProtocolBuffer ->
            val settings = mapSettingsProtocolBufferIntoSettings(settingsProtocolBuffer)

            Timber.d("repositorySettings.FETCH ============================================")
            Timber.d("repositorySettings.resizeZoom=${settings.resizeZoom}")

            settings
        }

    override suspend fun store(settingsData: SettingsDataInterface) {
        Timber.d("repositorySettings.STORE ============================================")
        Timber.d("repositorySettings.resizeZoom=${settingsData.resizeZoom}")

        updateStateFlow(settingsData)
    }

    override suspend fun clear() {
        settingsProtocolBufferStateFlow.value = SettingsProtocolBuffer.getDefaultInstance()
    }
}
