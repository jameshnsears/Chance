package com.github.jameshnsears.chance.data.repo.api.locked

import kotlinx.coroutines.flow.Flow

interface RepositoryLockedInterface {
    fun fetch(): Flow<Set<String>>
    suspend fun store(lockedRollIndices: Set<String>)
    suspend fun clear()
}
