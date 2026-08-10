package com.github.jameshnsears.chance.data.repo.impl.locked.testdouble

import com.github.jameshnsears.chance.data.domain.proto.LockedProtocolBuffer
import com.github.jameshnsears.chance.data.repo.impl.locked.RepositoryLockedProtocolBufferInterface
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import timber.log.Timber

class RepositoryLockedProtocolBufferTestDouble private constructor() :
    RepositoryLockedProtocolBufferInterface {
    private val lockedProtocolBufferStateFlow =
        MutableStateFlow(LockedProtocolBuffer.getDefaultInstance())

    companion object {
        private var instance: RepositoryLockedProtocolBufferTestDouble? = null

        fun getInstance(): RepositoryLockedProtocolBufferTestDouble {
            if (instance == null) {
                instance = RepositoryLockedProtocolBufferTestDouble()
            }
            return instance!!
        }
    }

    override fun fetch(): Flow<Set<String>> = lockedProtocolBufferStateFlow
        .map { lockedProtocolBuffer ->
            Timber.d("repositoryLocked.FETCH ============================================")
            val locked = mapLockedProtocolBufferIntoLockedSet(lockedProtocolBuffer)
            Timber.d("repositoryLocked.size=${locked.size}")
            locked
        }

    override suspend fun store(lockedRollIndices: Set<String>) {
        Timber.d("repositoryLocked.STORE ============================================")
        Timber.d("repositoryLocked.size=${lockedRollIndices.size}")

        val lockedProtocolBufferBuilder = LockedProtocolBuffer.newBuilder()
        mapLockedSetIntoLockedProtocolBufferBuilder(
            lockedRollIndices,
            lockedProtocolBufferBuilder
        )
        lockedProtocolBufferStateFlow.value = lockedProtocolBufferBuilder.build()
    }

    override suspend fun clear() {
        lockedProtocolBufferStateFlow.value = LockedProtocolBuffer.getDefaultInstance()
    }
}
