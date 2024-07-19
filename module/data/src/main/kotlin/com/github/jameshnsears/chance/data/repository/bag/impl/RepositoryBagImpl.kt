package com.github.jameshnsears.chance.data.repository.bag.impl

import android.annotation.SuppressLint
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.github.jameshnsears.chance.data.domain.core.Dice
import com.github.jameshnsears.chance.data.domain.core.bag.DiceBag
import com.github.jameshnsears.chance.data.domain.core.bag.impl.BagDataImpl
import com.github.jameshnsears.chance.data.domain.proto.BagProtocolBuffer
import com.github.jameshnsears.chance.data.repository.bag.RepositoryBagInterface
import com.google.protobuf.util.JsonFormat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import timber.log.Timber

class RepositoryBagImpl private constructor(private val context: Context) :
    RepositoryBagInterface {
    companion object {
        @SuppressLint("StaticFieldLeak")
        private var instance: RepositoryBagImpl? = null

        fun getInstance(
            context: Context,
            defaultBag: DiceBag = BagDataImpl().allDice
        ): RepositoryBagImpl {
            if (instance == null) {
                instance = RepositoryBagImpl(context)
                runBlocking {
                    val existingDiceBagSize = instance!!.fetch().first().size
                    Timber.d("RepositoryBagImpl.size=${existingDiceBagSize}")
                    if (existingDiceBagSize == 0)
                        instance!!.store(defaultBag)
                }
            }
            return instance!!
        }
    }

    override suspend fun fetch(): Flow<DiceBag> = flow {
        val diceBag = mutableListOf<Dice>()

        context.diceBagDataStore.data
            .map { bagProtocolBuffer ->
                bagProtocolBuffer.diceList.forEach { diceProtocolBuffer ->
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
            .map { bagProtocolBuffer ->
                bagProtocolBuffer.diceList.forEach { diceProtocolBuffer ->

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

    override suspend fun store(newDiceBag: DiceBag) {
        clear()

        context.diceBagDataStore.updateData {
            val bagProtocolBufferBuilder = it.toBuilder()
            mapDiceBagIntoBagProtocolBufferBuilder(
                newDiceBag,
                bagProtocolBufferBuilder
            )
            bagProtocolBufferBuilder.build()
        }
    }

    override suspend fun jsonExport(): String =
        JsonFormat.printer().includingDefaultValueFields()
            .print(context.diceBagDataStore.data.first())

    override suspend fun jsonImport(json: String) {
        context.diceBagDataStore.updateData {

            val bagProtocolBuffer: BagProtocolBuffer.Builder =
                BagProtocolBuffer.newBuilder()

            JsonFormat.parser().merge(json, bagProtocolBuffer)

            bagProtocolBuffer.build()
        }
    }

    override suspend fun clear() {
        context.diceBagDataStore.updateData {
            BagProtocolBuffer.newBuilder().build()
        }
    }
}

val Context.diceBagDataStore: DataStore<BagProtocolBuffer> by dataStore(
    // /data/data/com.github.jameshnsears.chance.test.test/files/datastore
    fileName = "bag.pb",
    serializer = BagProtocolBufferSerializer,
)