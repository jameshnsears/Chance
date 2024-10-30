package com.github.jameshnsears.chance.data.repository

import android.content.Context
import com.github.jameshnsears.chance.data.BuildConfig
import com.github.jameshnsears.chance.data.domain.core.bag.impl.BagDataImpl
import com.github.jameshnsears.chance.data.domain.core.bag.testdouble.BagDataTestDouble
import com.github.jameshnsears.chance.data.domain.core.roll.impl.RollHistoryDataImpl
import com.github.jameshnsears.chance.data.domain.core.roll.testdouble.RollHistoryDataTestDouble
import com.github.jameshnsears.chance.data.domain.core.settings.impl.SettingsDataImpl
import com.github.jameshnsears.chance.data.domain.core.settings.testdouble.SettingsDataTestDouble
import com.github.jameshnsears.chance.data.repository.bag.impl.RepositoryBagProtocolBufferImpl
import com.github.jameshnsears.chance.data.repository.bag.testdouble.RepositoryBagProtocolBufferTestDouble
import com.github.jameshnsears.chance.data.repository.roll.impl.RepositoryRollProtocolBufferImpl
import com.github.jameshnsears.chance.data.repository.roll.testdouble.RepositoryRollProtocolBufferTestDouble
import com.github.jameshnsears.chance.data.repository.settings.impl.RepositorySettingsProtocolBufferImpl
import com.github.jameshnsears.chance.data.repository.settings.testdouble.RepositorySettingsProtocolBufferTestDouble
import com.github.jameshnsears.chance.utility.feature.UtilityFeature

class RepositoryFactory(context: Context? = null) {
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

    private val bagDataImpl = BagDataImpl(context)
    val bagDataTestDouble = BagDataTestDouble()

    val repositoryBag = if (BuildConfig.DEBUG)
        if (UtilityFeature.isEnabled(UtilityFeature.Flag.REPO_PROTOCOL_BUFFER))
            RepositoryBagProtocolBufferImpl.getInstance(context!!, bagDataImpl.allDice)
        else
            RepositoryBagProtocolBufferTestDouble.getInstance(bagDataTestDouble.allDice)
    else
        RepositoryBagProtocolBufferImpl.getInstance(context!!, bagDataImpl.allDice)

    ///////////////////////////////////////////////////

    private val rollHistoryDataImpl = RollHistoryDataImpl(bagDataImpl).rollHistory
    val rollHistoryDataTestDouble = RollHistoryDataTestDouble(bagDataTestDouble).rollHistory

    val repositoryRoll = if (BuildConfig.DEBUG)
        if (UtilityFeature.isEnabled(UtilityFeature.Flag.REPO_PROTOCOL_BUFFER))
            RepositoryRollProtocolBufferImpl.getInstance(context!!, rollHistoryDataImpl)
        else
            RepositoryRollProtocolBufferTestDouble.getInstance(rollHistoryDataTestDouble)
    else
        RepositoryRollProtocolBufferImpl.getInstance(context!!, rollHistoryDataImpl)
}
