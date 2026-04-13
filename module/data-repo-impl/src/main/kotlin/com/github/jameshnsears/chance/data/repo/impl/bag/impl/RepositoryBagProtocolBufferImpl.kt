package com.github.jameshnsears.chance.data.repo.impl.bag.impl

import android.annotation.SuppressLint
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.github.jameshnsears.chance.common.utility.feature.UtilityFeature
import com.github.jameshnsears.chance.data.domain.core.Dice
import com.github.jameshnsears.chance.data.domain.core.bag.DiceBag
import com.github.jameshnsears.chance.data.domain.proto.BagProtocolBuffer
import com.github.jameshnsears.chance.data.domain.proto.DiceProtocolBuffer
import com.github.jameshnsears.chance.data.repo.impl.BuildConfig
import com.github.jameshnsears.chance.data.repo.impl.bag.RepositoryBagProtocolBufferInterface
import com.google.protobuf.Descriptors
import com.google.protobuf.util.JsonFormat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber


class RepositoryBagProtocolBufferImpl private constructor(private val context: Context) :
    RepositoryBagProtocolBufferInterface {
    companion object {
        @SuppressLint("StaticFieldLeak")
        private var instance: RepositoryBagProtocolBufferImpl? = null

        fun getInstance(
            context: Context,
            diceBag: DiceBag
        ): RepositoryBagProtocolBufferImpl {
            if (instance == null) {
                instance = RepositoryBagProtocolBufferImpl(context)

                CoroutineScope(Dispatchers.IO).launch {
                    if (BuildConfig.DEBUG) {
                        if (!UtilityFeature.isEnabled(UtilityFeature.Flag.REPO_PROTOCOL_BUFFER)) {
                            instance!!.clear()
                        }
                    }

                    if (instance!!.fetch().first().isEmpty())
                        instance!!.store(diceBag)
                }
            }

            return instance!!
        }
    }

    override suspend fun jsonExport(): String = withContext(Dispatchers.IO) {
        val fieldsToAlwaysOutput: MutableSet<Descriptors.FieldDescriptor> = HashSet()
        fieldsToAlwaysOutput.add(DiceProtocolBuffer.getDescriptor().findFieldByName("selected"))

        JsonFormat.printer().includingDefaultValueFields(fieldsToAlwaysOutput)
            .print(context.diceBagDataStore.data.first())
    }

    override suspend fun jsonImport(json: String) {
        store(jsonImportProcess(json))
    }

    override suspend fun fetch(): Flow<DiceBag> = flow {
        val diceBag = mutableListOf<Dice>()

        context.diceBagDataStore.data
            .map { bagProtocolBuffer ->
                bagProtocolBuffer.diceList.forEach { diceProtocolBuffer ->
                    diceBag.add(
                        Dice(
                            epoch = diceProtocolBuffer.epoch,

                            sides = jsonImportProcessSides(diceProtocolBuffer),

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

        Timber.d("repositoryBag.FETCH ============================================")
        Timber.d("repositoryBag.size=${diceBag.size}")

        emit(diceBag)
    }.flowOn(Dispatchers.IO)

    override suspend fun fetch(epoch: Long): Flow<Dice> = flow {
        val dice = Dice()

        context.diceBagDataStore.data
            .map { bagProtocolBuffer ->
                bagProtocolBuffer.diceList.forEach { diceProtocolBuffer ->
                    if (epoch == diceProtocolBuffer.epoch) {
                        dice.epoch = diceProtocolBuffer.epoch

                        dice.sides = jsonImportProcessSides(diceProtocolBuffer)

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
    }.flowOn(Dispatchers.IO)

    override suspend fun store(newDiceBag: DiceBag) {
        withContext(Dispatchers.IO) {
            clear()

            Timber.d("repositoryBag.STORE ============================================")
            Timber.d("repositoryBag.size=${newDiceBag.size}")

            context.diceBagDataStore.updateData {
                val bagProtocolBufferBuilder = it.toBuilder()
                mapDiceBagIntoBagProtocolBufferBuilder(
                    newDiceBag,
                    bagProtocolBufferBuilder
                )
                bagProtocolBufferBuilder.build()
            }
        }
    }

    override suspend fun clear() {
        withContext(Dispatchers.IO) {
            context.diceBagDataStore.updateData {
                it.toBuilder().clear().build()
            }
        }
    }
}

val Context.diceBagDataStore: DataStore<BagProtocolBuffer> by dataStore(
    // /data/data/com.github.jameshnsears.chance.test.test/files/datastore
    fileName = "bag.pb",
    serializer = RepositoryBagProtocolBufferSerializer,
)
