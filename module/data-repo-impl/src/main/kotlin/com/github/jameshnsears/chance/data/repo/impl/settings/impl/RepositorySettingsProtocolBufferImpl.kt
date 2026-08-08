package com.github.jameshnsears.chance.data.repo.impl.settings.impl

import android.annotation.SuppressLint
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.dataStore
import com.github.jameshnsears.chance.data.domain.core.settings.SettingsDataInterface
import com.github.jameshnsears.chance.data.domain.proto.SettingsProtocolBuffer
import com.github.jameshnsears.chance.data.repo.impl.settings.RepositorySettingsProtocolBufferInterface
import com.google.protobuf.Descriptors
import com.google.protobuf.util.JsonFormat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.withContext
import timber.log.Timber

class RepositorySettingsProtocolBufferImpl private constructor(private val context: Context) :
    RepositorySettingsProtocolBufferInterface {
    companion object {
        @SuppressLint("StaticFieldLeak")
        private var instance: RepositorySettingsProtocolBufferImpl? = null

        fun getInstance(
            context: Context
        ): RepositorySettingsProtocolBufferImpl {
            if (instance == null) {
                instance = RepositorySettingsProtocolBufferImpl(context)
            }

            return instance!!
        }
    }

    private val repositoryScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private val settingsFlow = context.settingsDataStore.data
        .map { settingsProtocolBuffer ->
            val startTime = System.currentTimeMillis()
            val settings = mapSettingsProtocolBufferIntoSettings(settingsProtocolBuffer)

            Timber.d("repositorySettings.FETCH ============================================")
            Timber.d("repositorySettings.resizeZoom=${settings.resizeZoom}; mapping_time=${System.currentTimeMillis() - startTime}ms")

            settings
        }.shareIn(
            scope = repositoryScope,
            started = SharingStarted.WhileSubscribed(5000),
            replay = 1
        )

    override suspend fun jsonExport(): String = withContext(Dispatchers.IO) {
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
        fieldsToAlwaysOutput.add(SettingsProtocolBuffer.getDescriptor().findFieldByName("layout"))
        fieldsToAlwaysOutput.add(SettingsProtocolBuffer.getDescriptor().findFieldByName("history"))

        JsonFormat.printer().includingDefaultValueFields(fieldsToAlwaysOutput)
            .print(context.settingsDataStore.data.first())
    }

    override suspend fun jsonImport(json: String) {
        store(jsonImportProcess(json))
    }

    override fun fetch(): Flow<SettingsDataInterface> = settingsFlow

    override suspend fun store(settingsData: SettingsDataInterface) {
        withContext(Dispatchers.IO) {
            Timber.d("repositorySettings.STORE ============================================")
            Timber.d("repositorySettings.resizeZoom=${settingsData.resizeZoom}")

            context.settingsDataStore.updateData {
                val settingsProtocolBufferBuilder = SettingsProtocolBuffer.newBuilder()
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
    corruptionHandler = ReplaceFileCorruptionHandler {
        SettingsProtocolBuffer.getDefaultInstance()
    },
)
