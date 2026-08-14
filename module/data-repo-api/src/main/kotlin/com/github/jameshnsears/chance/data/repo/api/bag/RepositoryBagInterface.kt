package com.github.jameshnsears.chance.data.repo.api.bag

import com.github.jameshnsears.chance.data.domain.core.Dice
import com.github.jameshnsears.chance.data.domain.core.bag.DiceBag
import com.github.jameshnsears.chance.data.repo.api.RepositoryImportExportInterface
import kotlinx.coroutines.flow.Flow
import timber.log.Timber

interface RepositoryBagInterface : RepositoryImportExportInterface {
    fun fetch(): Flow<DiceBag>
    fun fetch(uuid: String): Flow<Dice>
    fun fetchSelected(): Flow<Set<String>>
    suspend fun store(newDiceBag: DiceBag)
    suspend fun store(diceUuid: String, selected: Boolean)

    /**
     * Returns the side UUIDs currently referenced by the persisted bag.
     * Implementations must read the raw persisted data directly (not a derived
     * async flow) so callers can rely on an up-to-date result immediately after
     * a [store].
     */
    suspend fun fetchReferencedSideUuids(): Set<String>

    fun traceUuid(diceBag: DiceBag) {
        diceBag.forEach {
            it.sides.forEach { side ->
                Timber.d("bag: dice.uuid=${it.uuid}; side.uuid=${side.uuid}")
            }
        }
    }
}
