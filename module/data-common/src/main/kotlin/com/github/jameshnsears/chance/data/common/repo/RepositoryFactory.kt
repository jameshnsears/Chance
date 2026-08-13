package com.github.jameshnsears.chance.data.common.repo

import android.content.Context
import com.github.jameshnsears.chance.common.utility.feature.UtilityFeature
import com.github.jameshnsears.chance.data.common.BuildConfig
import com.github.jameshnsears.chance.data.domain.core.bag.impl.BagDataImpl
import com.github.jameshnsears.chance.data.domain.core.bag.testdouble.BagDataTestDouble
import com.github.jameshnsears.chance.data.domain.core.group.impl.GroupDataImpl
import com.github.jameshnsears.chance.data.domain.core.group.testdouble.GroupDataTestDouble
import com.github.jameshnsears.chance.data.domain.core.roll.impl.RollHistoryDataImpl
import com.github.jameshnsears.chance.data.domain.core.roll.testdouble.RollHistoryDataTestDouble
import com.github.jameshnsears.chance.data.domain.core.settings.impl.SettingsDataImpl
import com.github.jameshnsears.chance.data.domain.core.settings.testdouble.SettingsDataTestDouble
import com.github.jameshnsears.chance.data.repo.api.side.RepositorySideCacheInterface
import com.github.jameshnsears.chance.data.repo.impl.bag.impl.RepositoryBagProtocolBufferImpl
import com.github.jameshnsears.chance.data.repo.impl.bag.testdouble.RepositoryBagProtocolBufferTestDouble
import com.github.jameshnsears.chance.data.repo.impl.group.impl.RepositoryGroupProtocolBufferImpl
import com.github.jameshnsears.chance.data.repo.impl.group.testdouble.RepositoryGroupProtocolBufferTestDouble
import com.github.jameshnsears.chance.data.repo.impl.locked.impl.RepositoryLockedProtocolBufferImpl
import com.github.jameshnsears.chance.data.repo.impl.locked.testdouble.RepositoryLockedProtocolBufferTestDouble
import com.github.jameshnsears.chance.data.repo.impl.roll.impl.RepositoryRollProtocolBufferImpl
import com.github.jameshnsears.chance.data.repo.impl.roll.testdouble.RepositoryRollProtocolBufferTestDouble
import com.github.jameshnsears.chance.data.repo.impl.settings.impl.RepositorySettingsProtocolBufferImpl
import com.github.jameshnsears.chance.data.repo.impl.settings.testdouble.RepositorySettingsProtocolBufferTestDouble
import com.github.jameshnsears.chance.data.repo.impl.side.impl.RepositorySideCacheProtocolBufferImpl
import com.github.jameshnsears.chance.data.repo.impl.side.testdouble.RepositorySideCacheProtocolBufferTestDouble
import kotlinx.coroutines.flow.first

class RepositoryFactory(val context: Context? = null) {
    // ========================================================================
    // 1. Non-Repository Variables (Eagerly initialized in strict dependency order)
    // ========================================================================

    // Settings (No dependencies)
    val settingsImpl = SettingsDataImpl()
    val settingsTestDouble = SettingsDataTestDouble()

    // Bag (Independent, requires context)
    val bagDataImpl by lazy { BagDataImpl(context) }
    val bagDataTestDouble by lazy { BagDataTestDouble() }

    // Group (Depends on Bag)
    val groupDataImplObject = GroupDataImpl(context, bagDataImpl)
    val groupDataImpl = groupDataImplObject.groupHistory

    val groupDataTestDoubleObject = GroupDataTestDouble(bagDataTestDouble)
    val groupDataTestDouble = groupDataTestDoubleObject.groupHistory

    // Roll History (Depends on Bag and Group)
    val rollHistoryDataImpl = RollHistoryDataImpl(bagDataImpl, groupDataImplObject).rollHistory
    val rollHistoryTestDouble = RollHistoryDataTestDouble(bagDataTestDouble, groupDataTestDoubleObject).rollHistory

    // ========================================================================
    // 2. Repositories (Guaranteed to initialize AFTER all above variables exist)
    // ========================================================================

    val repositorySideCache: RepositorySideCacheInterface by lazy {
        if (context != null) {
            if (BuildConfig.DEBUG && !UtilityFeature.isEnabled(UtilityFeature.Flag.REPO_PROTOCOL_BUFFER_PROD)) {
                RepositorySideCacheProtocolBufferTestDouble.getInstance()
            } else {
                RepositorySideCacheProtocolBufferImpl.getInstance(context)
            }
        } else {
            RepositorySideCacheProtocolBufferTestDouble.getInstance()
        }
    }

    val repositorySettings by lazy {
        if (context != null) {
            if (BuildConfig.DEBUG && !UtilityFeature.isEnabled(UtilityFeature.Flag.REPO_PROTOCOL_BUFFER_PROD)) {
                RepositorySettingsProtocolBufferTestDouble.getInstance(settingsTestDouble)
            } else {
                RepositorySettingsProtocolBufferImpl.getInstance(context)
            }
        } else {
            RepositorySettingsProtocolBufferTestDouble.getInstance(settingsTestDouble)
        }
    }

    val repositoryBag by lazy {
        if (context != null) {
            if (BuildConfig.DEBUG && !UtilityFeature.isEnabled(UtilityFeature.Flag.REPO_PROTOCOL_BUFFER_PROD)) {
                RepositoryBagProtocolBufferTestDouble.getInstance(bagDataTestDouble.allDiceList(), repositorySideCache)
            } else {
                RepositoryBagProtocolBufferImpl.getInstance(context, repositorySideCache)
            }
        } else {
            RepositoryBagProtocolBufferTestDouble.getInstance(bagDataTestDouble.allDiceList(), repositorySideCache)
        }
    }

    val repositoryGroup by lazy {
        if (context != null) {
            if (BuildConfig.DEBUG && !UtilityFeature.isEnabled(UtilityFeature.Flag.REPO_PROTOCOL_BUFFER_PROD)) {
                RepositoryGroupProtocolBufferTestDouble.getInstance(groupDataTestDouble)
            } else {
                RepositoryGroupProtocolBufferImpl.getInstance(context)
            }
        } else {
            RepositoryGroupProtocolBufferTestDouble.getInstance(groupDataTestDouble)
        }
    }

    val repositoryRoll by lazy {
        if (context != null) {
            if (BuildConfig.DEBUG && !UtilityFeature.isEnabled(UtilityFeature.Flag.REPO_PROTOCOL_BUFFER_PROD)) {
                RepositoryRollProtocolBufferTestDouble.getInstance(rollHistoryTestDouble, repositoryBag, repositorySideCache)
            } else {
                RepositoryRollProtocolBufferImpl.getInstance(context, repositoryBag, repositorySideCache)
            }
        } else {
            RepositoryRollProtocolBufferTestDouble.getInstance(rollHistoryTestDouble, repositoryBag, repositorySideCache)
        }
    }

    val repositoryLocked by lazy {
        if (context != null) {
            if (BuildConfig.DEBUG && !UtilityFeature.isEnabled(UtilityFeature.Flag.REPO_PROTOCOL_BUFFER_PROD)) {
                RepositoryLockedProtocolBufferTestDouble.getInstance()
            } else {
                RepositoryLockedProtocolBufferImpl.getInstance(context)
            }
        } else {
            RepositoryLockedProtocolBufferTestDouble.getInstance()
        }
    }

    // ========================================================================
    // 3. Operations
    // ========================================================================

    suspend fun initialize() {
        if (BuildConfig.DEBUG && UtilityFeature.isEnabled(UtilityFeature.Flag.REPO_PROTOCOL_BUFFER_EMPTY_AT_STARTUP)) {
            repositorySettings.clear()
            repositoryBag.clear()
            repositoryGroup.clear()
            repositoryRoll.clear()
            repositoryLocked.clear()
            repositorySideCache.clear()
        }

        if (repositorySettings.fetch().first().resizeZoom == 0f) {
            repositorySettings.store(settingsImpl)
        }

        if (repositoryBag.fetch().first().isEmpty()) {
            repositoryBag.store(bagDataImpl.allDice())
        }

        if (repositoryGroup.fetch().first().isEmpty()) {
            repositoryGroup.store(groupDataImpl)
        }

        if (repositoryRoll.fetch().first().isEmpty()) {
            repositoryRoll.store(rollHistoryDataImpl)
            repositoryRoll.traceUuid(rollHistoryDataImpl)
        }

        pruneCache()
    }

    suspend fun resetStorage() {
        repositoryLocked.clear()
        repositorySideCache.clear()

        if (BuildConfig.DEBUG) {
            if (UtilityFeature.isEnabled(UtilityFeature.Flag.REPO_PROTOCOL_BUFFER_PROD)) {
                repositorySettings.store(settingsImpl)
                repositoryBag.store(bagDataImpl.allDice())
                repositoryGroup.store(groupDataImpl)
                repositoryRoll.store(rollHistoryDataImpl)
            } else {
                repositorySettings.store(settingsTestDouble)
                repositoryBag.store(bagDataTestDouble.allDice())
                repositoryGroup.store(groupDataTestDouble)
                repositoryRoll.store(rollHistoryTestDouble)
            }
        } else {
            repositorySettings.store(settingsImpl)
            repositoryBag.store(bagDataImpl.allDice())
            repositoryGroup.store(groupDataImpl)
            repositoryRoll.store(rollHistoryDataImpl)
        }
    }

    private suspend fun pruneCache() {
        val referencedUuids = mutableSetOf<String>()
        repositoryBag.fetch().first().forEach { dice ->
            dice.sides.forEach { side ->
                referencedUuids.add(side.uuid)
            }
        }
        repositoryRoll.fetch().first().values.forEach { rolls ->
            rolls.forEach { roll ->
                referencedUuids.add(roll.side.uuid)
            }
        }
        repositorySideCache.prune(referencedUuids)
    }
}
