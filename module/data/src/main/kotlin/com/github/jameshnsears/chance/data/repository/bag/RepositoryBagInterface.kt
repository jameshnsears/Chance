package com.github.jameshnsears.chance.data.repository.bag

import com.github.jameshnsears.chance.data.domain.core.Dice
import com.github.jameshnsears.chance.data.domain.core.bag.DiceBag
import com.github.jameshnsears.chance.data.repository.RepositoryImportExportInterface
import kotlinx.coroutines.flow.Flow
import timber.log.Timber

interface RepositoryBagInterface : RepositoryImportExportInterface {
    suspend fun fetch(): Flow<DiceBag>
    suspend fun fetch(epoch: Long): Flow<Dice>
    suspend fun store(newDiceBag: DiceBag)

    fun traceUuid(diceBag: DiceBag) {
        diceBag.forEach {
            it.sides.forEach { side ->
                Timber.d("bag: dice.uuid=${it.uuid}; dice.epoch=${it.epoch}; side.uuid=${side.uuid}")
            }
        }
    }
}
