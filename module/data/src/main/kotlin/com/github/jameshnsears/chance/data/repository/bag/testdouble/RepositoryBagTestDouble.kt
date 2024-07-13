package com.github.jameshnsears.chance.data.repository.bag.testdouble

import com.github.jameshnsears.chance.data.domain.core.Dice
import com.github.jameshnsears.chance.data.domain.core.Side
import com.github.jameshnsears.chance.data.domain.core.bag.DiceBag
import com.github.jameshnsears.chance.data.domain.proto.BagProtocolBuffer
import com.github.jameshnsears.chance.data.repository.bag.RepositoryBagInterface
import com.google.protobuf.util.JsonFormat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class RepositoryBagTestDouble private constructor() :
    RepositoryBagInterface {
    companion object {
        private var instance: RepositoryBagTestDouble? = null

        fun getInstance(diceBag: DiceBag): RepositoryBagTestDouble {
            if (instance == null) {
                instance = RepositoryBagTestDouble()
                instance!!.diceBag = diceBag
            }
            return instance!!
        }
    }

    private lateinit var diceBag: DiceBag

    override suspend fun exportJson(): String {
        val BagProtocolBufferBuilder: BagProtocolBuffer.Builder =
            BagProtocolBuffer.newBuilder()

        mapDiceBagIntoBagProtocolBufferBuilder(diceBag, BagProtocolBufferBuilder)

        return JsonFormat.printer().includingDefaultValueFields()
            .print(BagProtocolBufferBuilder.build())
    }

    override suspend fun importJson(json: String) {
        diceBag = mutableListOf()

        val BagProtocolBufferBuilder: BagProtocolBuffer.Builder =
            BagProtocolBuffer.newBuilder()

        JsonFormat.parser().merge(json, BagProtocolBufferBuilder)

        val newDiceBag = mutableListOf<Dice>()

        BagProtocolBufferBuilder.diceList.forEach { diceProtocolBuffer ->
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
                side.descriptionColour = sideProtocolBuffer.descriptionColour

                sides.add(side)
            }

            dice.sides = sides

            dice.title = diceProtocolBuffer.title
            dice.colour = diceProtocolBuffer.colour
            dice.selected = diceProtocolBuffer.selected

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
        Timber.d(newDiceBag.toString())
        diceBag = newDiceBag
    }

    override suspend fun clear() {
        diceBag.clear()
    }
}
