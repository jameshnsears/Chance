package com.github.jameshnsears.chance.data.repository.roll

import com.github.jameshnsears.chance.data.domain.core.roll.RollHistory
import com.github.jameshnsears.chance.data.repository.RepositoryImportExportInterface
import kotlinx.coroutines.flow.Flow
import timber.log.Timber

interface RepositoryRollInterface : RepositoryImportExportInterface {
    suspend fun fetch(): Flow<RollHistory>
    suspend fun store(newRollHistory: RollHistory)

    fun traceUuid(rollHistory: RollHistory) {
        rollHistory.forEach { (_, rollList) ->
            rollList.forEach { roll ->
                Timber.d("roll: dice.epoch=${roll.diceEpoch}; side.uuid=${roll.side.uuid}")
            }
        }
    }
}
