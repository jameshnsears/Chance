package com.github.jameshnsears.chance.data.repo.impl.group.impl

import android.annotation.SuppressLint
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.dataStore
import com.github.jameshnsears.chance.data.domain.core.group.GroupHistory
import com.github.jameshnsears.chance.data.domain.proto.GroupHistoryProtocolBuffer
import com.github.jameshnsears.chance.data.domain.proto.GroupProtocolBuffer
import com.github.jameshnsears.chance.data.repo.impl.group.RepositoryGroupProtocolBufferInterface
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

class RepositoryGroupProtocolBufferImpl private constructor(private val context: Context) :
    RepositoryGroupProtocolBufferInterface {
    companion object {
        @SuppressLint("StaticFieldLeak")
        private var instance: RepositoryGroupProtocolBufferImpl? = null

        fun getInstance(
            context: Context
        ): RepositoryGroupProtocolBufferImpl {
            if (instance == null) {
                instance = RepositoryGroupProtocolBufferImpl(context)
            }

            return instance!!
        }
    }

    private val repositoryScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private val groupHistoryFlow = context.groupHistoryDataStore.data
        .map { groupHistoryProtocolBuffer ->
            val startTime = System.currentTimeMillis()
            val groupHistory = mapGroupHistoryProtocolBufferIntoGroupHistory(groupHistoryProtocolBuffer)

            Timber.d("repositoryGroup.FETCH ============================================")
            Timber.d("repositoryGroup.size=${groupHistory.size}; mapping_time=${System.currentTimeMillis() - startTime}ms")

            groupHistory
        }.shareIn(
            scope = repositoryScope,
            started = SharingStarted.WhileSubscribed(5000),
            replay = 1
        )

    override suspend fun jsonExport(): String = withContext(Dispatchers.IO) {
        val fieldsToAlwaysOutput: MutableSet<Descriptors.FieldDescriptor> = HashSet()
        fieldsToAlwaysOutput.add(GroupProtocolBuffer.getDescriptor().findFieldByName("name"))
        fieldsToAlwaysOutput.add(GroupProtocolBuffer.getDescriptor().findFieldByName("notes"))
        fieldsToAlwaysOutput.add(GroupProtocolBuffer.getDescriptor().findFieldByName("displayIndex"))
        fieldsToAlwaysOutput.add(GroupProtocolBuffer.getDescriptor().findFieldByName("selected"))

        JsonFormat.printer().includingDefaultValueFields(fieldsToAlwaysOutput)
            .print(context.groupHistoryDataStore.data.first())
    }

    override suspend fun jsonImport(json: String) {
        store(jsonImportProcess(json))
    }

    override fun fetch(): Flow<GroupHistory> = groupHistoryFlow

    override suspend fun store(newGroupHistory: GroupHistory) {
        withContext(Dispatchers.IO) {
            Timber.d("repositoryGroup.STORE ============================================")
            Timber.d("repositoryGroup.size=${newGroupHistory.size}")

            context.groupHistoryDataStore.updateData {
                val groupHistoryProtocolBufferBuilder = GroupHistoryProtocolBuffer.newBuilder()
                mapGroupHistoryIntoGroupHistoryProtocolBufferBuilder(
                    newGroupHistory,
                    groupHistoryProtocolBufferBuilder
                )
                groupHistoryProtocolBufferBuilder.build()
            }
        }
    }

    override suspend fun clear() {
        withContext(Dispatchers.IO) {
            context.groupHistoryDataStore.updateData {
                it.toBuilder().clear().build()
            }
        }
    }
}

val Context.groupHistoryDataStore: DataStore<GroupHistoryProtocolBuffer> by dataStore(
    fileName = "group.pb",
    serializer = RepositoryGroupProtocolBufferSerializer,
    corruptionHandler = ReplaceFileCorruptionHandler {
        GroupHistoryProtocolBuffer.getDefaultInstance()
    },
)
