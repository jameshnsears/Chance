package com.github.jameshnsears.chance.data.common.repository

import android.content.Context
import com.github.jameshnsears.chance.common.utility.feature.UtilityFeature
import com.github.jameshnsears.chance.data.common.BuildConfig
import com.github.jameshnsears.chance.data.domain.core.bag.impl.BagDataImpl
import com.github.jameshnsears.chance.data.domain.core.bag.testdouble.BagDataTestDouble
import com.github.jameshnsears.chance.data.domain.core.roll.impl.RollHistoryDataImpl
import com.github.jameshnsears.chance.data.domain.core.roll.testdouble.RollHistoryDataTestDouble
import com.github.jameshnsears.chance.data.domain.core.settings.impl.SettingsDataImpl
import com.github.jameshnsears.chance.data.domain.core.settings.testdouble.SettingsDataTestDouble
import com.github.jameshnsears.chance.data.repo.impl.bag.impl.RepositoryBagProtocolBufferImpl
import com.github.jameshnsears.chance.data.repo.impl.bag.testdouble.RepositoryBagProtocolBufferTestDouble
import com.github.jameshnsears.chance.data.repo.impl.roll.impl.RepositoryRollProtocolBufferImpl
import com.github.jameshnsears.chance.data.repo.impl.roll.testdouble.RepositoryRollProtocolBufferTestDouble
import com.github.jameshnsears.chance.data.repo.impl.settings.impl.RepositorySettingsProtocolBufferImpl
import com.github.jameshnsears.chance.data.repo.impl.settings.testdouble.RepositorySettingsProtocolBufferTestDouble

class RepositoryFactory(val context: Context? = null) {
    val settingsImpl = SettingsDataImpl()
    val settingsTestDouble = SettingsDataTestDouble()

    val repositorySettings = if (BuildConfig.DEBUG)
        if (UtilityFeature.isEnabled(UtilityFeature.Flag.REPO_PROTOCOL_BUFFER))
            RepositorySettingsProtocolBufferImpl.getInstance(context!!, settingsImpl)
        else
            RepositorySettingsProtocolBufferTestDouble.getInstance(settingsTestDouble)
    else
        RepositorySettingsProtocolBufferImpl.getInstance(context!!, settingsImpl)

    ///////////////////////////////////////////////////

    private val bagDataImpl by lazy { BagDataImpl(context) }
    val bagDataTestDouble = BagDataTestDouble()

    val repositoryBag = if (BuildConfig.DEBUG)
        if (UtilityFeature.isEnabled(UtilityFeature.Flag.REPO_PROTOCOL_BUFFER))
            RepositoryBagProtocolBufferImpl.getInstance(context!!, bagDataImpl.allDice)
        else
            RepositoryBagProtocolBufferTestDouble.getInstance(bagDataTestDouble.allDice)
    else
        RepositoryBagProtocolBufferImpl.getInstance(context!!, bagDataImpl.allDice)

    ///////////////////////////////////////////////////

    private val rollHistoryDataImpl by lazy { RollHistoryDataImpl(bagDataImpl).rollHistory }
    val rollHistoryTestDouble = RollHistoryDataTestDouble(bagDataTestDouble).rollHistory

    val repositoryRoll = if (BuildConfig.DEBUG)
        if (UtilityFeature.isEnabled(UtilityFeature.Flag.REPO_PROTOCOL_BUFFER))
            RepositoryRollProtocolBufferImpl.getInstance(context!!, rollHistoryDataImpl)
        else
            RepositoryRollProtocolBufferTestDouble.getInstance(rollHistoryTestDouble)
    else
        RepositoryRollProtocolBufferImpl.getInstance(context!!, rollHistoryDataImpl)

    ///////////////////////////////////////////////////

    suspend fun resetStorage() {
        if (BuildConfig.DEBUG)
            if (UtilityFeature.isEnabled(UtilityFeature.Flag.REPO_PROTOCOL_BUFFER)) {
                repositorySettings.store(settingsImpl)
                repositoryBag.store(bagDataImpl.allDice)
                repositoryRoll.store(rollHistoryDataImpl)
            } else {
                repositorySettings.store(settingsTestDouble)
                repositoryBag.store(bagDataTestDouble.allDice)
                repositoryRoll.store(rollHistoryTestDouble)
            }
        else {
            repositorySettings.store(settingsImpl)
            repositoryBag.store(bagDataImpl.allDice)
            repositoryRoll.store(rollHistoryDataImpl)
        }
    }
}
