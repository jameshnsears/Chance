package com.github.jameshnsears.chance.data.repository.bag.impl

import android.annotation.SuppressLint
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.github.jameshnsears.chance.data.domain.proto.DiceBagProtocolBuffer
import com.github.jameshnsears.chance.data.domain.proto.DiceProtocolBuffer
import com.github.jameshnsears.chance.data.domain.state.Dice
import com.github.jameshnsears.chance.data.domain.state.DiceBag
import com.github.jameshnsears.chance.data.domain.state.Side
import com.github.jameshnsears.chance.data.repository.bag.RepositoryBagInterface
import com.google.protobuf.util.JsonFormat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import timber.log.Timber

class RepositoryBagImpl private constructor(private val context: Context) :
    RepositoryBagInterface {
    companion object {
        @SuppressLint("StaticFieldLeak")
        private var instance: RepositoryBagImpl? = null

        fun getInstance(context: Context): RepositoryBagImpl {
            if (instance == null) {
                instance = RepositoryBagImpl(context)
            }
            return instance!!
        }
    }

    override suspend fun fetch(): Flow<DiceBag> = flow {
        val diceBag = mutableListOf<Dice>()

        context.diceBagDataStore.data
            .map { diceBagProtocolBuffer ->
                diceBagProtocolBuffer.diceList.forEach { diceProtocolBuffer ->
                    diceBag.add(
                        Dice(
                            epoch = diceProtocolBuffer.epoch,
                            sides = sides(diceProtocolBuffer),
                            title = diceProtocolBuffer.title,
                            colour = diceProtocolBuffer.colour,
                            selected = diceProtocolBuffer.selected,
                            multiplierValue = diceProtocolBuffer.multiplierValue,
                            explode = diceProtocolBuffer.explode,
                            explodeWhen = diceProtocolBuffer.explodeWhen,
                            explodeValue = diceProtocolBuffer.explodeValue,
                            modifyScore = diceProtocolBuffer.modifyScore,
                            modifyScoreValue = diceProtocolBuffer.modifyScoreValue
                        )
                    )
                }
            }.first()

        emit(diceBag)
    }

    override suspend fun fetch(epoch: Long): Flow<Dice> = flow {
        val dice = Dice()
        context.diceBagDataStore.data
            .map { diceBagProtocolBuffer ->
                diceBagProtocolBuffer.diceList.forEach { diceProtocolBuffer ->

                    if (epoch == diceProtocolBuffer.epoch) {
                        dice.epoch = diceProtocolBuffer.epoch
                        dice.sides = sides(diceProtocolBuffer)
                        dice.title = diceProtocolBuffer.title
                        dice.colour = diceProtocolBuffer.colour
                        dice.selected = diceProtocolBuffer.selected
                        dice.multiplierValue = diceProtocolBuffer.multiplierValue
                        dice.explode = diceProtocolBuffer.explode
                        dice.explodeWhen = diceProtocolBuffer.explodeWhen
                        dice.explodeValue = diceProtocolBuffer.explodeValue
                        dice.modifyScore = diceProtocolBuffer.modifyScore
                        dice.modifyScoreValue = diceProtocolBuffer.modifyScoreValue
                    }
                }
            }.first()

        emit(dice)
    }

    private fun sides(diceProtocolBuffer: DiceProtocolBuffer): MutableList<Side> {
        val sides = mutableListOf<Side>()
        diceProtocolBuffer.sideList.forEach { sideProtocolBuffer ->

            sides.add(
                Side(
                    number = sideProtocolBuffer.number,
                    numberColour = sideProtocolBuffer.numberColour,
                    imageBase64 = sideProtocolBuffer.imageBase64,
                    imageDrawableId = sideProtocolBuffer.imageDrawableId,
                    description = sideProtocolBuffer.description,
                    descriptionColour = sideProtocolBuffer.descriptionColour
                )
            )
        }
        return sides
    }

    override suspend fun store(newDiceBag: DiceBag) {
        Timber.d(newDiceBag.toString())

        context.diceBagDataStore.updateData {
            val diceBagProtocolBufferBuilder = it.toBuilder()
            mapDiceBagIntoDiceBagProtocolBufferBuilder(
                newDiceBag,
                diceBagProtocolBufferBuilder
            )
            diceBagProtocolBufferBuilder.build()
        }
    }

    override suspend fun exportJson(): String =
        JsonFormat.printer().includingDefaultValueFields()
            .print(context.diceBagDataStore.data.first())

    override suspend fun importJson(json: String) {
        context.diceBagDataStore.updateData {
            val diceBagProtocolBuffer: DiceBagProtocolBuffer.Builder =
                DiceBagProtocolBuffer.newBuilder()
            JsonFormat.parser().merge(json, diceBagProtocolBuffer)
            diceBagProtocolBuffer.build()
        }
    }

    override suspend fun clear() {
        context.diceBagDataStore.updateData {
            DiceBagProtocolBuffer.newBuilder().build()
        }
    }
}

val Context.diceBagDataStore: DataStore<DiceBagProtocolBuffer> by dataStore(
    // /data/data/com.github.jameshnsears.chance.test.test/files/datastore
    fileName = "bag.pb",
    serializer = DiceBagProtocolBufferSerializer,
)