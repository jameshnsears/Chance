package com.github.jameshnsears.chance.data.repo.impl.settings.impl

import android.annotation.SuppressLint
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.github.jameshnsears.chance.common.utility.feature.UtilityFeature
import com.github.jameshnsears.chance.data.domain.core.settings.SettingsDataInterface
import com.github.jameshnsears.chance.data.domain.core.settings.impl.SettingsDataImpl
import com.github.jameshnsears.chance.data.domain.proto.SettingsProtocolBuffer
import com.github.jameshnsears.chance.data.repo.impl.BuildConfig
import com.github.jameshnsears.chance.data.repo.impl.settings.RepositorySettingsProtocolBufferInterface
import com.google.protobuf.Descriptors
import com.google.protobuf.util.JsonFormat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class RepositorySettingsProtocolBufferImpl private constructor(private val context: Context) :
    RepositorySettingsProtocolBufferInterface {
    companion object {
        @SuppressLint("StaticFieldLeak")
        private var instance: RepositorySettingsProtocolBufferImpl? = null

        fun getInstance(
            context: Context,
            settings: SettingsDataInterface = SettingsDataImpl()
        ): RepositorySettingsProtocolBufferImpl {
            if (instance == null) {
                instance = RepositorySettingsProtocolBufferImpl(context)

                CoroutineScope(Dispatchers.IO).launch {
                    if (BuildConfig.DEBUG) {
                        if (!UtilityFeature.isEnabled(UtilityFeature.Flag.REPO_PROTOCOL_BUFFER)) {
                            instance!!.clear()
                        }
                    }

                    if (instance!!.fetch().first().resize == 0) {
                        instance!!.store(settings)
                    }
                }
            }

            return instance!!
        }
    }

    override suspend fun jsonExport(): String = withContext(Dispatchers.IO) {
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
        fieldsToAlwaysOutput.add(SettingsProtocolBuffer.getDescriptor().findFieldByName("shuffle"))

        JsonFormat.printer().includingDefaultValueFields(fieldsToAlwaysOutput)
            .print(context.settingsDataStore.data.first())
    }

    override suspend fun jsonImport(json: String) {
        store(jsonImportProcess(json))
    }

    override fun jsonImportProcess(json: String): SettingsDataInterface {
        val settingsProtocolBufferBuilder: SettingsProtocolBuffer.Builder =
            SettingsProtocolBuffer.newBuilder()

        JsonFormat.parser().merge(json, settingsProtocolBufferBuilder)

        val settingsProtocolBuffer = settingsProtocolBufferBuilder.build()

        val newSettings = SettingsDataImpl()

        newSettings.resize = settingsProtocolBuffer.resize

        newSettings.rollIndexTime = settingsProtocolBuffer.rollIndexTime
        newSettings.rollScore = settingsProtocolBuffer.rollScore

        newSettings.diceTitle = settingsProtocolBuffer.diceTitle
        newSettings.sideNumber = settingsProtocolBuffer.sideNumber
        newSettings.rollBehaviour = settingsProtocolBuffer.behaviour
        newSettings.sideDescription = settingsProtocolBuffer.sideDescription
        newSettings.sideSVG = settingsProtocolBuffer.sideSVG

        newSettings.rollSound = settingsProtocolBuffer.rollSound
        newSettings.shuffle = settingsProtocolBuffer.shuffle

        return newSettings
    }

    override suspend fun fetch(): Flow<SettingsDataImpl> = flow {
        val settings = context.settingsDataStore.data
            .map { settingsProtocolBuffer ->
                SettingsDataImpl(
                    resize = settingsProtocolBuffer.resize,

                    rollIndexTime = settingsProtocolBuffer.rollIndexTime,
                    rollScore = settingsProtocolBuffer.rollScore,

                    diceTitle = settingsProtocolBuffer.diceTitle,
                    sideNumber = settingsProtocolBuffer.sideNumber,
                    rollBehaviour = settingsProtocolBuffer.behaviour,
                    sideDescription = settingsProtocolBuffer.sideDescription,
                    sideSVG = settingsProtocolBuffer.sideSVG,

                    rollSound = settingsProtocolBuffer.rollSound,
                    shuffle = settingsProtocolBuffer.shuffle,
                )
            }.first()

        Timber.d("repositorySettings.FETCH ============================================")
        Timber.d("repositorySettings.resize=${settings.resize}")

        emit(settings)
    }.flowOn(Dispatchers.IO)

    override suspend fun store(settingsData: SettingsDataInterface) {
        withContext(Dispatchers.IO) {
            Timber.d("repositorySettings.STORE ============================================")
            Timber.d("repositorySettings.resize=${settingsData.resize}")

            context.settingsDataStore.updateData {
                val settingsProtocolBufferBuilder = it.toBuilder()
                mapSettingsIntoSettingsProtocolBufferBuilder(
                    settingsData,
                    settingsProtocolBufferBuilder
                )
                settingsProtocolBufferBuilder.build()
            }
        }
    }

    override suspend fun clear() {
        withContext(Dispatchers.IO) {
            context.settingsDataStore.updateData {
                it.toBuilder().clear().build()
            }
        }
    }
}

val Context.settingsDataStore: DataStore<SettingsProtocolBuffer> by dataStore(
    // /data/data/com.github.jameshnsears.chance.test.test/files/datastore
    fileName = "settings.pb",
    serializer = RepositorySettingsProtocolBufferSerializer,
)
