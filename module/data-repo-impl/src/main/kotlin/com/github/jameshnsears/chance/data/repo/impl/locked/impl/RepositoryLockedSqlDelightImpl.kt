package com.github.jameshnsears.chance.data.repo.impl.locked.impl

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.github.jameshnsears.chance.data.repo.api.locked.RepositoryLockedInterface
import com.github.jameshnsears.chance.data.repo.impl.sqldelight.ChanceDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.withContext

class RepositoryLockedSqlDelightImpl(
    private val database: ChanceDatabase
) : RepositoryLockedInterface {

    private val repositoryScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private val lockedFlow = database.lockedQueries.getLocked()
        .asFlow()
        .mapToList(Dispatchers.IO)
        .map { it.toSet() }
        .shareIn(
            scope = repositoryScope,
            started = SharingStarted.WhileSubscribed(5000),
            replay = 1
        )

    override fun fetch(): Flow<Set<String>> = lockedFlow

    override suspend fun store(lockedRollIndices: Set<String>) {
        withContext(Dispatchers.IO) {
            database.transaction {
                database.lockedQueries.clear()
                lockedRollIndices.forEach { diceUuid ->
                    database.lockedQueries.saveLocked(diceUuid)
                }
            }
        }
    }

    override suspend fun clear() {
        withContext(Dispatchers.IO) {
            database.lockedQueries.clear()
        }
    }
}
