package com.github.jameshnsears.chance.data.common.repo

import android.content.Context
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
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
import com.github.jameshnsears.chance.data.repo.impl.bag.impl.RepositoryBagSqlDelightImpl
import com.github.jameshnsears.chance.data.repo.impl.group.impl.RepositoryGroupSqlDelightImpl
import com.github.jameshnsears.chance.data.repo.impl.locked.impl.RepositoryLockedSqlDelightImpl
import com.github.jameshnsears.chance.data.repo.impl.roll.impl.RepositoryRollSqlDelightImpl
import com.github.jameshnsears.chance.data.repo.impl.settings.impl.RepositorySettingsSqlDelightImpl
import com.github.jameshnsears.chance.data.repo.impl.side.impl.RepositorySideCacheSqlDelightImpl
import com.github.jameshnsears.chance.data.repo.impl.sqldelight.ChanceDatabase
import com.github.jameshnsears.chance.data.repo.impl.utility.SqlDelightTestDriver
import kotlinx.coroutines.flow.first

class RepositoryFactory(val context: Context? = null) {
    val settingsImpl = SettingsDataImpl()
    val settingsTestDouble = SettingsDataTestDouble()
    val bagDataImpl by lazy { BagDataImpl(context) }
    val bagDataTestDouble by lazy { BagDataTestDouble() }
    val groupDataImplObject = GroupDataImpl(context, bagDataImpl)
    val groupDataImpl = groupDataImplObject.groupHistory
    val groupDataTestDoubleObject = GroupDataTestDouble(bagDataTestDouble)
    val groupDataTestDouble = groupDataTestDoubleObject.groupHistory
    val rollHistoryDataImpl = RollHistoryDataImpl(bagDataImpl, groupDataImplObject).rollHistory
    val rollHistoryTestDouble = RollHistoryDataTestDouble(bagDataTestDouble, groupDataTestDoubleObject).rollHistory

    val database by lazy {
        if (context != null) {
            val driver = AndroidSqliteDriver(ChanceDatabase.Schema, context, "chance.db")
            ChanceDatabase(driver)
        } else {
            val driver = SqlDelightTestDriver.create()
            ChanceDatabase(driver)
        }
    }
    val repositorySideCache: RepositorySideCacheInterface by lazy {
        RepositorySideCacheSqlDelightImpl(database)
    }

    val repositorySettings by lazy {
        RepositorySettingsSqlDelightImpl(database)
    }

    val repositoryBag by lazy {
        RepositoryBagSqlDelightImpl(database, repositorySideCache)
    }

    val repositoryGroup by lazy {
        RepositoryGroupSqlDelightImpl(database)
    }

    val repositoryRoll by lazy {
        RepositoryRollSqlDelightImpl(database, repositoryBag, repositorySideCache)
    }

    val repositoryLocked by lazy {
        RepositoryLockedSqlDelightImpl(database)
    }
    suspend fun initialize() {
        if (BuildConfig.DEBUG && UtilityFeature.isEnabled(UtilityFeature.Flag.REPO_SQLDELIGHT_EMPTY_AT_STARTUP)) {
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

        repositorySettings.store(settingsImpl)
        repositoryBag.store(bagDataImpl.allDice())
        repositoryGroup.store(groupDataImpl)
        repositoryRoll.store(rollHistoryDataImpl)
    }

    private suspend fun pruneCache() {
        val referencedUuids = repositoryBag.fetchReferencedSideUuids() +
            repositoryRoll.fetchReferencedSideUuids()
        repositorySideCache.prune(referencedUuids)
    }
}
