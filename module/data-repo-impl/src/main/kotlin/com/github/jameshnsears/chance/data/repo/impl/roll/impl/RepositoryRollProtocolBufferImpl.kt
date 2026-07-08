package com.github.jameshnsears.chance.data.repo.impl.roll.impl

import android.annotation.SuppressLint
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.dataStore
import com.github.jameshnsears.chance.data.domain.core.roll.RollHistory
import com.github.jameshnsears.chance.data.domain.proto.RollHistoryProtocolBuffer
import com.github.jameshnsears.chance.data.repo.impl.roll.RepositoryRollProtocolBufferInterface
import com.google.protobuf.util.JsonFormat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import timber.log.Timber

class RepositoryRollProtocolBufferImpl private constructor(private val context: Context) :
    RepositoryRollProtocolBufferInterface {
    companion object {
        @SuppressLint("StaticFieldLeak")
        private var instance: RepositoryRollProtocolBufferImpl? = null

        fun getInstance(
            context: Context,
            rollHistory: RollHistory
        ): RepositoryRollProtocolBufferImpl {
            if (instance == null) {
                instance = RepositoryRollProtocolBufferImpl(context)
            }

            return instance!!
        }
    }

    override suspend fun jsonExport(): String = withContext(Dispatchers.IO) {
        JsonFormat.printer()
            .print(context.rollDataStore.data.first())
    }

    override suspend fun jsonImport(json: String) {
        store(jsonImportProcess(json))
    }

    override suspend fun fetch(): Flow<RollHistory> = context.rollDataStore.data
        .map { rollHistoryProtocolBuffer ->
            Timber.d("repositoryRoll.FETCH.start ============================================")

            val rollHistory = mapRollHistoryProtocolBufferIntoRollHistory(rollHistoryProtocolBuffer)

            Timber.d("repositoryRoll.FETCH.end ============================================")
            Timber.d("repositoryRoll.size=${rollHistory.size}")

            rollHistory
        }.flowOn(Dispatchers.IO)

    override suspend fun store(newRollHistory: RollHistory) {
        withContext(Dispatchers.IO) {
            Timber.d("repositoryRoll.STORE ============================================")
            Timber.d("repositoryRoll.size=${newRollHistory.size}")

            context.rollDataStore.updateData {
                val rollHistoryProtocolBufferBuilder = RollHistoryProtocolBuffer.newBuilder()
                mapRollHistoryIntoRollHistoryProtocolBufferBuilder(
                    newRollHistory,
                    rollHistoryProtocolBufferBuilder
                )
                rollHistoryProtocolBufferBuilder.build()
            }
        }
    }

    override suspend fun clear() {
        withContext(Dispatchers.IO) {
            context.rollDataStore.updateData {
                it.toBuilder().clear().build()
            }
        }
    }
}

val Context.rollDataStore: DataStore<RollHistoryProtocolBuffer> by dataStore(
    // /data/data/com.github.jameshnsears.chance.test.test/files/datastore
    fileName = "roll.pb",
    serializer = RepositoryRollProtocolBufferSerializer,
    corruptionHandler = ReplaceFileCorruptionHandler {
        RollHistoryProtocolBuffer.getDefaultInstance()
    },
)
