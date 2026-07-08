package com.github.jameshnsears.chance.data.repo.api.group

import com.github.jameshnsears.chance.data.domain.core.group.GroupHistory
import com.github.jameshnsears.chance.data.repo.api.RepositoryImportExportInterface
import kotlinx.coroutines.flow.Flow
import timber.log.Timber

interface RepositoryGroupInterface : RepositoryImportExportInterface {
    suspend fun fetch(): Flow<GroupHistory>
    suspend fun store(newGroupHistory: GroupHistory)

    fun traceUuid(groupHistory: GroupHistory) {
        groupHistory.forEach { group ->
            Timber.d("group: uuid=${group.uuid}; name=${group.name}")
        }
    }
}
