package com.github.jameshnsears.chance.data.repo.impl.roll.impl

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.github.jameshnsears.chance.data.domain.core.Side
import com.github.jameshnsears.chance.data.domain.core.bag.DiceBag
import com.github.jameshnsears.chance.data.domain.core.roll.Roll
import com.github.jameshnsears.chance.data.domain.core.roll.RollHistory
import com.github.jameshnsears.chance.data.repo.api.bag.RepositoryBagInterface
import com.github.jameshnsears.chance.data.repo.api.roll.RepositoryRollInterface
import com.github.jameshnsears.chance.data.repo.api.side.RepositorySideCacheInterface
import com.github.jameshnsears.chance.data.repo.impl.sqldelight.ChanceDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.withContext
import com.github.jameshnsears.chance.data.repo.impl.sqldelight.Roll as RollEntity

class RepositoryRollSqlDelightImpl(
    private val database: ChanceDatabase,
    private val repositoryBag: RepositoryBagInterface,
    private val repositorySideCache: RepositorySideCacheInterface? = null
) : RepositoryRollInterface {

    private val repositoryScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private val rollHistoryFlow: Flow<RollHistory> = combine(
        database.rollQueries.getRolls().asFlow().mapToList(Dispatchers.IO),
        repositoryBag.fetch(),
        repositorySideCache?.fetch() ?: kotlinx.coroutines.flow.flowOf(emptyMap())
    ) { rollEntities: List<RollEntity>, diceBag: DiceBag, sideCache: Map<String, String> ->
        val history = LinkedHashMap<Long, MutableList<Roll>>()
        rollEntities.forEach { entity ->
            val rollList = history.getOrPut(entity.epoch) { mutableListOf() }
            rollList.add(entity.toRoll(diceBag, sideCache))
        }
        history as RollHistory
    }.shareIn(
        scope = repositoryScope,
        started = SharingStarted.WhileSubscribed(5000),
        replay = 1
    )

    override fun fetch(): Flow<RollHistory> = rollHistoryFlow

    override suspend fun store(newRollHistory: RollHistory) {
        withContext(Dispatchers.IO) {
            database.transaction {
                database.rollQueries.clear()
                newRollHistory.forEach { (epoch, rolls) ->
                    rolls.forEach { roll ->
                        database.rollQueries.saveRoll(
                            epoch = epoch,
                            multiplierIndex = roll.multiplierIndex.toLong(),
                            explodeIndex = roll.explodeIndex.toLong(),
                            scoreAdjustment = roll.scoreAdjustment.toLong(),
                            score = roll.score.toLong(),
                            sideUuid = roll.side.uuid,
                            groupUuid = roll.uuidGroup
                        )
                    }
                }
            }
        }
    }

    override suspend fun store(epoch: Long, rollList: List<Roll>) {
        withContext(Dispatchers.IO) {
            database.transaction {
                rollList.forEach { roll ->
                    database.rollQueries.saveRoll(
                        epoch = epoch,
                        multiplierIndex = roll.multiplierIndex.toLong(),
                        explodeIndex = roll.explodeIndex.toLong(),
                        scoreAdjustment = roll.scoreAdjustment.toLong(),
                        score = roll.score.toLong(),
                        sideUuid = roll.side.uuid,
                        groupUuid = roll.uuidGroup
                    )
                }
            }
        }
    }

    override suspend fun delete(diceUuid: String) {
        withContext(Dispatchers.IO) {
            database.rollQueries.deleteRollsForDice(diceUuid)
        }
    }

    override suspend fun removeLatest() {
        withContext(Dispatchers.IO) {
            database.rollQueries.removeLatestRoll()
        }
    }

    override suspend fun fetchReferencedSideUuids(): Set<String> {
        return withContext(Dispatchers.IO) {
            database.rollQueries.getAllSideUuidsFromRolls().executeAsList().toSet()
        }
    }

    override suspend fun clear() {
        withContext(Dispatchers.IO) {
            database.rollQueries.clear()
        }
    }

    override suspend fun jsonExport(): String = withContext(Dispatchers.IO) {
        val history = fetch().first()
        jacksonObjectMapper().writeValueAsString(history)
    }

    override suspend fun jsonImport(json: String) {
        val rollHistory = jacksonObjectMapper().readValue<RollHistory>(json)
        store(rollHistory)
    }

    private fun RollEntity.toRoll(diceBag: DiceBag, sideCache: Map<String, String>): Roll {
        val sideFromBag = diceBag.flatMap { it.sides }.find { it.uuid == sideUuid }
        val side = sideFromBag?.copy() ?: Side(uuid = sideUuid)

        if (side.imageBase64.isEmpty()) {
            val base64 = sideCache[sideUuid] ?: ""
            if (base64.isNotEmpty()) {
                side.imageBase64 = base64
            }
        }

        return Roll(
            uuidDice = diceBag.find { dice -> dice.sides.any { it.uuid == sideUuid } }?.uuid ?: "",
            side = side,
            multiplierIndex = multiplierIndex.toInt(),
            explodeIndex = explodeIndex.toInt(),
            scoreAdjustment = scoreAdjustment.toInt(),
            score = score.toInt(),
            uuidGroup = groupUuid ?: ""
        )
    }
}
