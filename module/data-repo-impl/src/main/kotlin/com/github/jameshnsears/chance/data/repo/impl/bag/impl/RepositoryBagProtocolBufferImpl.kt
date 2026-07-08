package com.github.jameshnsears.chance.data.repo.impl.bag.impl

import android.annotation.SuppressLint
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.dataStore
import com.github.jameshnsears.chance.data.domain.core.Dice
import com.github.jameshnsears.chance.data.domain.core.bag.DiceBag
import com.github.jameshnsears.chance.data.domain.proto.BagProtocolBuffer
import com.github.jameshnsears.chance.data.domain.proto.DiceProtocolBuffer
import com.github.jameshnsears.chance.data.repo.impl.bag.RepositoryBagProtocolBufferInterface
import com.google.protobuf.Descriptors
import com.google.protobuf.util.JsonFormat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
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

    override suspend fun fetch(): Flow<DiceBag> = context.diceBagDataStore.data
        .map { bagProtocolBuffer ->
            val diceBag = mapBagProtocolBufferIntoDiceBag(bagProtocolBuffer)

            Timber.d("repositoryBag.FETCH ============================================")
            Timber.d("repositoryBag.size=${diceBag.size}")

            diceBag
        }.flowOn(Dispatchers.IO)

    override suspend fun fetch(uuid: String): Flow<Dice> = context.diceBagDataStore.data
        .map { bagProtocolBuffer ->
            var dice = Dice()

            bagProtocolBuffer.diceList.forEach { diceProtocolBuffer ->
                if (uuid == diceProtocolBuffer.uuid) {
                    dice = mapDiceProtocolBufferIntoDice(diceProtocolBuffer)
                }
            }
            dice
        }.flowOn(Dispatchers.IO)

    override suspend fun store(newDiceBag: DiceBag) {
        withContext(Dispatchers.IO) {
            Timber.d("repositoryBag.STORE ============================================")
            Timber.d("repositoryBag.size=${newDiceBag.size}")

            context.diceBagDataStore.updateData {
                val bagProtocolBufferBuilder = BagProtocolBuffer.newBuilder()
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
    corruptionHandler = ReplaceFileCorruptionHandler {
        BagProtocolBuffer.getDefaultInstance()
    },
)
