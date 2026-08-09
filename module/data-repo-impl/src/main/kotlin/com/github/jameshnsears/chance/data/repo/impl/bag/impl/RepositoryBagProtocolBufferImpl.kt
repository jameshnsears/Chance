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
import com.github.jameshnsears.chance.data.domain.proto.SideProtocolBuffer
import com.github.jameshnsears.chance.data.repo.impl.bag.RepositoryBagProtocolBufferInterface
import com.google.protobuf.Descriptors
import com.google.protobuf.util.JsonFormat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.withContext
import timber.log.Timber

class RepositoryBagProtocolBufferImpl private constructor(private val context: Context) :
    RepositoryBagProtocolBufferInterface {
    companion object {
        @SuppressLint("StaticFieldLeak")
        private var instance: RepositoryBagProtocolBufferImpl? = null

        fun getInstance(
            context: Context
        ): RepositoryBagProtocolBufferImpl {
            if (instance == null) {
                instance = RepositoryBagProtocolBufferImpl(context)
            }

            return instance!!
        }
    }

    private val repositoryScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private val diceBagFlow = context.diceBagDataStore.data
        .map { bagProtocolBuffer ->
            val startTime = System.currentTimeMillis()
            val diceBag = mapBagProtocolBufferIntoDiceBag(bagProtocolBuffer)

            Timber.d("repositoryBag.FETCH ============================================")
            Timber.d("repositoryBag.size=${diceBag.size}; mapping_time=${System.currentTimeMillis() - startTime}ms")

            diceBag
        }.shareIn(
            scope = repositoryScope,
            started = SharingStarted.WhileSubscribed(5000),
            replay = 1
        )

    override suspend fun jsonExport(): String = withContext(Dispatchers.IO) {
        val fieldsToAlwaysOutput: MutableSet<Descriptors.FieldDescriptor> = HashSet()
        fieldsToAlwaysOutput.add(DiceProtocolBuffer.getDescriptor().findFieldByName("title"))
        fieldsToAlwaysOutput.add(DiceProtocolBuffer.getDescriptor().findFieldByName("colour"))
        fieldsToAlwaysOutput.add(DiceProtocolBuffer.getDescriptor().findFieldByName("selected"))
        fieldsToAlwaysOutput.add(DiceProtocolBuffer.getDescriptor().findFieldByName("multiplierValue"))
        fieldsToAlwaysOutput.add(DiceProtocolBuffer.getDescriptor().findFieldByName("explode"))
        fieldsToAlwaysOutput.add(DiceProtocolBuffer.getDescriptor().findFieldByName("explodeWhen"))
        fieldsToAlwaysOutput.add(DiceProtocolBuffer.getDescriptor().findFieldByName("explodeValue"))
        fieldsToAlwaysOutput.add(DiceProtocolBuffer.getDescriptor().findFieldByName("modifyScore"))
        fieldsToAlwaysOutput.add(DiceProtocolBuffer.getDescriptor().findFieldByName("modifyScoreValue"))
        fieldsToAlwaysOutput.add(DiceProtocolBuffer.getDescriptor().findFieldByName("displayIndex"))
        fieldsToAlwaysOutput.add(SideProtocolBuffer.getDescriptor().findFieldByName("number"))
        fieldsToAlwaysOutput.add(SideProtocolBuffer.getDescriptor().findFieldByName("numberColour"))
        fieldsToAlwaysOutput.add(SideProtocolBuffer.getDescriptor().findFieldByName("imageDrawableId"))
        fieldsToAlwaysOutput.add(SideProtocolBuffer.getDescriptor().findFieldByName("imageBase64"))
        fieldsToAlwaysOutput.add(SideProtocolBuffer.getDescriptor().findFieldByName("description"))
        fieldsToAlwaysOutput.add(SideProtocolBuffer.getDescriptor().findFieldByName("descriptionColour"))

        val bagProtocolBuffer = context.diceBagDataStore.data.first()
        val diceBag = mapBagProtocolBufferIntoDiceBag(bagProtocolBuffer)

        val exportedBuilder = BagProtocolBuffer.newBuilder()
        mapDiceBagIntoBagProtocolBufferBuilderForExport(
            diceBag,
            exportedBuilder
        )

        JsonFormat.printer().includingDefaultValueFields(fieldsToAlwaysOutput)
            .print(exportedBuilder.build())
    }

    override suspend fun jsonImport(json: String) {
        store(jsonImportProcess(json))
    }

    override fun fetch(): Flow<DiceBag> = diceBagFlow

    override fun fetch(uuid: String): Flow<Dice> = diceBagFlow
        .map { diceBag ->
            diceBag.find { it.uuid == uuid } ?: Dice()
        }

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
