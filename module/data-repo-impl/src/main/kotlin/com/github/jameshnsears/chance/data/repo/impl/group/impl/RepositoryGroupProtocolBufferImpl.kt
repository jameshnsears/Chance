package com.github.jameshnsears.chance.data.repo.impl.group.impl

import android.annotation.SuppressLint
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.dataStore
import com.github.jameshnsears.chance.data.domain.core.group.GroupHistory
import com.github.jameshnsears.chance.data.domain.proto.GroupHistoryProtocolBuffer
import com.github.jameshnsears.chance.data.repo.impl.group.RepositoryGroupProtocolBufferInterface
import com.google.protobuf.util.JsonFormat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import timber.log.Timber

class RepositoryGroupProtocolBufferImpl private constructor(private val context: Context) :
    RepositoryGroupProtocolBufferInterface {
    companion object {
        @SuppressLint("StaticFieldLeak")
        private var instance: RepositoryGroupProtocolBufferImpl? = null

        fun getInstance(
            context: Context,
            groupHistory: GroupHistory
        ): RepositoryGroupProtocolBufferImpl {
            if (instance == null) {
                instance = RepositoryGroupProtocolBufferImpl(context)
            }

            return instance!!
        }
    }

    override suspend fun jsonExport(): String = withContext(Dispatchers.IO) {
        JsonFormat.printer().print(context.groupHistoryDataStore.data.first())
    }

    override suspend fun jsonImport(json: String) {
        store(jsonImportProcess(json))
    }

    override suspend fun fetch(): Flow<GroupHistory> = context.groupHistoryDataStore.data
        .map { groupHistoryProtocolBuffer ->
            val groupHistory = mapGroupHistoryProtocolBufferIntoGroupHistory(groupHistoryProtocolBuffer)

            Timber.d("repositoryGroup.FETCH ============================================")
            Timber.d("repositoryGroup.size=${groupHistory.size}")

            groupHistory
        }.flowOn(Dispatchers.IO)

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
