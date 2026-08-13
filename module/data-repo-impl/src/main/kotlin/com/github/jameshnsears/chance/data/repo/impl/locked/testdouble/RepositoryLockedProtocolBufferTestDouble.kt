package com.github.jameshnsears.chance.data.repo.impl.locked.testdouble

import com.github.jameshnsears.chance.data.domain.proto.LockedProtocolBuffer
import com.github.jameshnsears.chance.data.repo.impl.locked.RepositoryLockedProtocolBufferInterface
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

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
            val locked = mapLockedProtocolBufferIntoLockedSet(lockedProtocolBuffer)
            locked
        }

    override suspend fun store(lockedRollIndices: Set<String>) {
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
