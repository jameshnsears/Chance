package com.github.jameshnsears.chance.data.repo.impl.locked.impl

import android.annotation.SuppressLint
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.dataStore
import com.github.jameshnsears.chance.data.domain.proto.LockedProtocolBuffer
import com.github.jameshnsears.chance.data.repo.impl.locked.RepositoryLockedProtocolBufferInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.withContext
import timber.log.Timber

class RepositoryLockedProtocolBufferImpl private constructor(private val context: Context) :
    RepositoryLockedProtocolBufferInterface {
    companion object {
        @SuppressLint("StaticFieldLeak")
        private var instance: RepositoryLockedProtocolBufferImpl? = null

        fun getInstance(
            context: Context
        ): RepositoryLockedProtocolBufferImpl {
            if (instance == null) {
                instance = RepositoryLockedProtocolBufferImpl(context)
            }

            return instance!!
        }
    }

    private val repositoryScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private val lockedFlow = context.lockedDataStore.data
        .map { lockedProtocolBuffer ->
            val startTime = System.currentTimeMillis()
            val locked = mapLockedProtocolBufferIntoLockedSet(lockedProtocolBuffer)
            locked
        }.shareIn(
            scope = repositoryScope,
            started = SharingStarted.WhileSubscribed(5000),
            replay = 1
        )

    override fun fetch(): Flow<Set<String>> = lockedFlow

    override suspend fun store(lockedRollIndices: Set<String>) {
        withContext(Dispatchers.IO) {
            Timber.d("repositoryLocked.STORE ============================================")
            Timber.d("repositoryLocked.size=${lockedRollIndices.size}")

            context.lockedDataStore.updateData {
                val lockedProtocolBufferBuilder = LockedProtocolBuffer.newBuilder()
                mapLockedSetIntoLockedProtocolBufferBuilder(
                    lockedRollIndices,
                    lockedProtocolBufferBuilder
                )
                lockedProtocolBufferBuilder.build()
            }
        }
    }

    override suspend fun clear() {
        withContext(Dispatchers.IO) {
            context.lockedDataStore.updateData {
                it.toBuilder().clear().build()
            }
        }
    }
}

val Context.lockedDataStore: DataStore<LockedProtocolBuffer> by dataStore(
    fileName = "locked.pb",
    serializer = RepositoryLockedProtocolBufferSerializer,
    corruptionHandler = ReplaceFileCorruptionHandler {
        LockedProtocolBuffer.getDefaultInstance()
    },
)
