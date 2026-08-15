package com.github.jameshnsears.chance.data.repo.impl.bag.impl

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.github.jameshnsears.chance.data.domain.core.Dice
import com.github.jameshnsears.chance.data.domain.core.Side
import com.github.jameshnsears.chance.data.domain.core.bag.DiceBag
import com.github.jameshnsears.chance.data.repo.api.bag.RepositoryBagInterface
import com.github.jameshnsears.chance.data.repo.api.side.RepositorySideCacheInterface
import com.github.jameshnsears.chance.data.repo.impl.sqldelight.ChanceDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.withContext
import com.github.jameshnsears.chance.data.repo.impl.sqldelight.Dice as DiceEntity
import com.github.jameshnsears.chance.data.repo.impl.sqldelight.Side as SideEntity

class RepositoryBagSqlDelightImpl(
    private val database: ChanceDatabase,
    private val repositorySideCache: RepositorySideCacheInterface? = null
) : RepositoryBagInterface {

    private val repositoryScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private val diceBagFlow: Flow<DiceBag> = combine(
        database.diceQueries.getAllDice().asFlow().mapToList(Dispatchers.IO),
        database.sideQueries.getAllSides().asFlow().mapToList(Dispatchers.IO),
        repositorySideCache?.fetch() ?: kotlinx.coroutines.flow.flowOf(emptyMap())
    ) { diceEntities: List<DiceEntity>, sideEntities: List<SideEntity>, sideCache: Map<String, String> ->
        val sidesByDice = sideEntities.groupBy { it.diceUuid }
        diceEntities.map { diceEntity ->
            diceEntity.toDice(sidesByDice[diceEntity.uuid] ?: emptyList(), sideCache)
        }.toMutableList()
    }.shareIn(
        scope = repositoryScope,
        started = SharingStarted.WhileSubscribed(5000),
        replay = 1
    )

    override fun fetch(): Flow<DiceBag> = diceBagFlow

    override fun fetch(uuid: String): Flow<Dice> = diceBagFlow.map { bag ->
        bag.find { it.uuid == uuid } ?: Dice()
    }

    override fun fetchSelected(): Flow<Set<String>> = database.diceQueries.getSelectedDiceUuids()
        .asFlow()
        .mapToList(Dispatchers.IO)
        .map { it.toSet() }
        .distinctUntilChanged()

    override suspend fun store(newDiceBag: DiceBag) {
        withContext(Dispatchers.IO) {
            val imageEntries = mutableMapOf<String, String>()
            newDiceBag.forEach { dice ->
                dice.sides.forEach { side ->
                    if (side.imageBase64.isNotEmpty()) {
                        imageEntries[side.uuid] = side.imageBase64
                    }
                }
            }

            repositorySideCache?.store(imageEntries)

            database.transaction {
                database.diceQueries.clear()
                database.sideQueries.clear()

                newDiceBag.forEachIndexed { index, dice ->
                    database.diceQueries.saveDice(
                        uuid = dice.uuid,
                        title = dice.title,
                        colour = dice.colour,
                        selected = dice.selected,
                        multiplierValue = dice.multiplierValue.toLong(),
                        explode = dice.explode,
                        explodeWhen = dice.explodeWhen,
                        explodeValue = dice.explodeValue.toLong(),
                        modifyScore = dice.modifyScore,
                        modifyScoreValue = dice.modifyScoreValue.toLong(),
                        displayIndex = index.toLong()
                    )

                    dice.sides.forEach { side ->
                        database.sideQueries.saveSide(
                            uuid = side.uuid,
                            diceUuid = dice.uuid,
                            number = side.number.toLong(),
                            numberColour = side.numberColour,
                            imageDrawableId = if (side.imageDrawableId != 0) side.imageDrawableId.toLong() else null,
                            imageUuid = if (side.imageBase64.isNotEmpty()) side.uuid else null,
                            description = side.description,
                            descriptionColour = side.descriptionColour
                        )
                    }
                }
            }
        }
    }

    override suspend fun store(diceUuid: String, selected: Boolean) {
        withContext(Dispatchers.IO) {
            database.diceQueries.updateDiceSelection(selected, diceUuid)
        }
    }

    override suspend fun fetchReferencedSideUuids(): Set<String> {
        return withContext(Dispatchers.IO) {
            database.sideQueries.getAllSideUuids().executeAsList().toSet()
        }
    }

    override suspend fun clear() {
        withContext(Dispatchers.IO) {
            database.transaction {
                database.diceQueries.clear()
                database.sideQueries.clear()
            }
        }
    }

    override suspend fun jsonExport(): String = withContext(Dispatchers.IO) {
        val bag = fetch().first()
        jacksonObjectMapper().writeValueAsString(bag)
    }

    override suspend fun jsonImport(json: String) {
        val diceBag = jacksonObjectMapper().readValue<DiceBag>(json)
        store(diceBag)
    }

    private fun DiceEntity.toDice(sideEntities: List<SideEntity>, sideCache: Map<String, String>): Dice {
        return Dice(
            uuid = uuid,
            title = title,
            colour = colour,
            selected = selected,
            multiplierValue = multiplierValue.toInt(),
            explode = explode,
            explodeWhen = explodeWhen ?: "",
            explodeValue = explodeValue?.toInt() ?: 0,
            modifyScore = modifyScore,
            modifyScoreValue = modifyScoreValue?.toInt() ?: 0,
            displayIndex = displayIndex.toInt(),
            sides = sideEntities.map { it.toSide(sideCache) }
        )
    }

    private fun SideEntity.toSide(sideCache: Map<String, String>): Side {
        val base64 = sideCache[uuid] ?: ""
        return Side(
            uuid = uuid,
            number = number.toInt(),
            numberColour = numberColour,
            imageDrawableId = imageDrawableId?.toInt() ?: 0,
            imageBase64 = base64,
            description = description ?: "",
            descriptionColour = descriptionColour
        )
    }
}
