package com.github.jameshnsears.chance.data.common.repo

import android.content.Context
import androidx.sqlite.db.SupportSQLiteDatabase
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
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first

class RepositoryFactory(val context: Context? = null) {
    private val isTestDoubleMode get() = UtilityFeature.isEnabled(UtilityFeature.Flag.REPO_SQLDELIGHT_TEST_DOUBLE)

    // Data Providers (Lazy)
    val settingsImpl by lazy { SettingsDataImpl() }
    val settingsTestDouble by lazy { SettingsDataTestDouble() }
    val bagDataImpl by lazy { BagDataImpl(context) }
    val bagDataTestDouble by lazy { BagDataTestDouble() }
    val groupDataImplObject by lazy { GroupDataImpl(context, bagDataImpl) }
    val groupDataImpl by lazy { groupDataImplObject.groupHistory }
    val groupDataTestDoubleObject by lazy { GroupDataTestDouble(bagDataTestDouble) }
    val groupDataTestDouble by lazy { groupDataTestDoubleObject.groupHistory }
    val rollHistoryDataImpl by lazy { RollHistoryDataImpl(bagDataImpl, groupDataImplObject).rollHistory }
    val rollHistoryTestDouble by lazy { RollHistoryDataTestDouble(bagDataTestDouble, groupDataTestDoubleObject).rollHistory }

    // Active defaults based on mode
    private val defaultSettings get() = if (isTestDoubleMode) settingsTestDouble else settingsImpl
    private val defaultBagData get() = if (isTestDoubleMode) bagDataTestDouble else bagDataImpl
    private val defaultGroupData get() = if (isTestDoubleMode) groupDataTestDouble else groupDataImpl
    private val defaultRollHistory get() = if (isTestDoubleMode) rollHistoryTestDouble else rollHistoryDataImpl

    private var driver: app.cash.sqldelight.db.SqlDriver? = null

    val database by lazy {
        val newDriver = if (context != null && !isTestDoubleMode) {
            AndroidSqliteDriver(
                schema = ChanceDatabase.Schema,
                context = context,
                name = "chance.db",
                callback = object : AndroidSqliteDriver.Callback(ChanceDatabase.Schema) {
                    override fun onConfigure(db: SupportSQLiteDatabase) {
                        super.onConfigure(db)
                        db.enableWriteAheadLogging()
                        db.execSQL("PRAGMA foreign_keys=ON;")
                        db.execSQL("PRAGMA synchronous=NORMAL;")
                    }
                }
            )
        } else {
            SqlDelightTestDriver.create()
        }
        driver = newDriver
        ChanceDatabase(newDriver)
    }

    private val repositoryScope = kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.IO + kotlinx.coroutines.SupervisorJob())

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

    fun close() {
        repositoryScope.cancel()
        driver?.close()
    }

    suspend fun initialize() {
        if (BuildConfig.DEBUG && UtilityFeature.isEnabled(UtilityFeature.Flag.REPO_SQLDELIGHT_EMPTY_AT_STARTUP)) {
            clearAll()
        }

        seedIfEmpty()
        pruneCache()
    }

    suspend fun resetStorage() {
        clearAll()
        seedStorage()
    }

    private suspend fun clearAll() {
        repositoryRoll.clear()
        repositoryGroup.clear()
        repositoryBag.clear()
        repositorySettings.clear()
        repositoryLocked.clear()
        repositorySideCache.clear()
    }

    private suspend fun seedIfEmpty() {
        if (repositorySettings.fetch().first().resizeZoom == 0f) {
            repositorySettings.store(defaultSettings)
        }

        if (repositoryBag.fetch().first().isEmpty()) {
            repositoryBag.store(defaultBagData.allDice())
        }

        if (repositoryGroup.fetch().first().isEmpty()) {
            repositoryGroup.store(defaultGroupData)
        }

        if (repositoryRoll.fetch().first().isEmpty()) {
            repositoryRoll.store(defaultRollHistory)
        }
    }

    private suspend fun seedStorage() {
        repositorySettings.store(defaultSettings)
        repositoryBag.store(defaultBagData.allDice())
        repositoryGroup.store(defaultGroupData)
        repositoryRoll.store(defaultRollHistory)
    }

    private suspend fun pruneCache() {
        val referencedUuids = repositoryBag.fetchReferencedSideUuids() +
            repositoryRoll.fetchReferencedSideUuids()
        repositorySideCache.prune(referencedUuids)
    }
}
