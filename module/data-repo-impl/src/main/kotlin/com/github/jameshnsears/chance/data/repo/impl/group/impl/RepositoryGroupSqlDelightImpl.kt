package com.github.jameshnsears.chance.data.repo.impl.group.impl

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.github.jameshnsears.chance.data.domain.core.group.Group
import com.github.jameshnsears.chance.data.domain.core.group.GroupHistory
import com.github.jameshnsears.chance.data.repo.api.group.RepositoryGroupInterface
import com.github.jameshnsears.chance.data.repo.impl.sqldelight.ChanceDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.withContext
import com.github.jameshnsears.chance.data.repo.impl.sqldelight.DiceGroup as GroupEntity

class RepositoryGroupSqlDelightImpl(
    private val database: ChanceDatabase
) : RepositoryGroupInterface {

    private val repositoryScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private val groupHistoryFlow = database.diceGroupQueries.getAllDiceGroups()
        .asFlow()
        .mapToList(Dispatchers.IO)
        .map { groupEntities ->
            groupEntities.map { groupEntity ->
                val diceUuids = database.diceGroupDiceQueries.getDiceForGroup(groupEntity.uuid).executeAsList()
                groupEntity.toGroup(diceUuids)
            }
        }.shareIn(
            scope = repositoryScope,
            started = SharingStarted.WhileSubscribed(5000),
            replay = 1
        )

    override fun fetch(): Flow<GroupHistory> = groupHistoryFlow

    override fun fetchSelected(): Flow<Set<String>> = database.diceGroupQueries.getSelectedDiceGroupUuids()
        .asFlow()
        .mapToList(Dispatchers.IO)
        .map { it.toSet() }
        .distinctUntilChanged()

    override suspend fun store(newGroupHistory: GroupHistory) {
        withContext(Dispatchers.IO) {
            database.transaction {
                database.diceGroupQueries.clear()
                database.diceGroupDiceQueries.clear()

                newGroupHistory.forEachIndexed { index, group ->
                    database.diceGroupQueries.saveDiceGroup(
                        uuid = group.uuid,
                        name = group.name,
                        notes = group.notes,
                        displayIndex = index.toLong(),
                        selected = group.selected
                    )

                    group.uuidDice.forEach { diceUuid ->
                        database.diceGroupDiceQueries.saveDiceGroupDice(
                            groupUuid = group.uuid,
                            diceUuid = diceUuid
                        )
                    }
                }
            }
        }
    }

    override suspend fun store(groupUuid: String, selected: Boolean) {
        withContext(Dispatchers.IO) {
            database.diceGroupQueries.updateDiceGroupSelection(selected, groupUuid)
        }
    }

    override suspend fun clear() {
        withContext(Dispatchers.IO) {
            database.transaction {
                database.diceGroupQueries.clear()
                database.diceGroupDiceQueries.clear()
            }
        }
    }

    override suspend fun jsonExport(): String = withContext(Dispatchers.IO) {
        val history = fetch().first()
        jacksonObjectMapper().writeValueAsString(history)
    }

    override suspend fun jsonImport(json: String) {
        val groupHistory = jacksonObjectMapper().readValue<GroupHistory>(json)
        store(groupHistory)
    }

    private fun GroupEntity.toGroup(diceUuids: List<String>): Group {
        return Group(
            uuid = uuid,
            name = name,
            uuidDice = diceUuids,
            notes = notes ?: "",
            selected = selected,
            displayIndex = displayIndex.toInt()
        )
    }
}
