package com.github.jameshnsears.chance.data.repo.api.roll

import com.github.jameshnsears.chance.data.domain.core.roll.Roll
import com.github.jameshnsears.chance.data.domain.core.roll.RollHistory
import com.github.jameshnsears.chance.data.repo.api.RepositoryImportExportInterface
import kotlinx.coroutines.flow.Flow
import timber.log.Timber

interface RepositoryRollInterface : RepositoryImportExportInterface {
    fun fetch(): Flow<RollHistory>
    suspend fun store(newRollHistory: RollHistory)

    suspend fun store(epoch: Long, rollList: List<Roll>)

    suspend fun removeLatest()

    fun traceUuid(rollHistory: RollHistory) {
        rollHistory.forEach { (_, rollList) ->
            rollList.forEach { roll ->
                Timber.d("roll: dice.uuid=${roll.uuidDice}; side.uuid=${roll.side.uuid}")
            }
        }
    }
}
