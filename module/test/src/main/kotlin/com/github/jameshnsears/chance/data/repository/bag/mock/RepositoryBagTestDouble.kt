package com.github.jameshnsears.chance.data.repository.bag.mock

import com.github.jameshnsears.chance.data.domain.proto.DiceBagProtocolBuffer
import com.github.jameshnsears.chance.data.domain.state.Dice
import com.github.jameshnsears.chance.data.domain.state.DiceBag
import com.github.jameshnsears.chance.data.domain.state.Side
import com.github.jameshnsears.chance.data.repository.bag.RepositoryBagInterface
import com.google.protobuf.util.JsonFormat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RepositoryBagTestDouble private constructor() :
    RepositoryBagInterface {
    companion object {
        private var instance: RepositoryBagTestDouble? = null

        fun getInstance(): RepositoryBagTestDouble {
            if (instance == null) {
                instance = RepositoryBagTestDouble()
            }
            return instance!!
        }
    }

    private lateinit var diceBag: DiceBag

    override suspend fun exportJson(): String {
        val diceBagProtocolBufferBuilder: DiceBagProtocolBuffer.Builder =
            DiceBagProtocolBuffer.newBuilder()

        mapDiceBagIntoDiceBagProtocolBufferBuilder(diceBag, diceBagProtocolBufferBuilder)

        return JsonFormat.printer().includingDefaultValueFields()
            .print(diceBagProtocolBufferBuilder.build())
    }

    override suspend fun importJson(json: String) {
        diceBag = mutableListOf()

        val diceBagProtocolBufferBuilder: DiceBagProtocolBuffer.Builder =
            DiceBagProtocolBuffer.newBuilder()

        JsonFormat.parser().merge(json, diceBagProtocolBufferBuilder)

        val newDiceBag = mutableListOf<Dice>()

        diceBagProtocolBufferBuilder.diceList.forEach { diceProtocolBuffer ->
            val dice = Dice()
            dice.epoch = diceProtocolBuffer.epoch

            val sides = mutableListOf<Side>()
            diceProtocolBuffer.sideList.forEach { sideProtocolBuffer ->
                val side = Side()
                side.number = sideProtocolBuffer.number
                side.numberColour = sideProtocolBuffer.numberColour
                side.imageBase64 = sideProtocolBuffer.imageBase64
                side.imageDrawableId = sideProtocolBuffer.imageDrawableId
                side.description = sideProtocolBuffer.description
                side.descriptionStringsId = sideProtocolBuffer.descriptionStringsId
                side.descriptionColour = sideProtocolBuffer.descriptionColour

                sides.add(side)
            }

            dice.sides = sides

            dice.title = diceProtocolBuffer.title
            dice.titleStringsId = diceProtocolBuffer.titleStringsId
            dice.colour = diceProtocolBuffer.colour
            dice.selected = diceProtocolBuffer.selected

            dice.multiplier = diceProtocolBuffer.multiplier
            dice.multiplierValue = diceProtocolBuffer.multiplierValue

            dice.explode = diceProtocolBuffer.explode
            dice.explodeWhen = diceProtocolBuffer.explodeWhen
            dice.explodeValue = diceProtocolBuffer.explodeValue

            dice.modifyScore = diceProtocolBuffer.modifyScore
            dice.modifyScoreValue = diceProtocolBuffer.modifyScoreValue

            newDiceBag.add(dice)
        }

        store(newDiceBag)
    }

    override suspend fun fetch(): Flow<DiceBag> = flow {
        emit(diceBag)
    }

    override suspend fun fetch(epoch: Long): Flow<Dice> = flow {
        emit(diceBag.firstOrNull { it.epoch == epoch } ?: Dice())
    }

    override suspend fun store(newDiceBag: DiceBag) {
        diceBag = newDiceBag
    }
}
