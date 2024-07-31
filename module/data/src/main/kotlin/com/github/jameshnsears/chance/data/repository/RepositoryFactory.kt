package com.github.jameshnsears.chance.data.repository

import android.content.Context
import com.github.jameshnsears.chance.common.BuildConfig
import com.github.jameshnsears.chance.data.domain.core.bag.impl.BagDataImpl
import com.github.jameshnsears.chance.data.domain.core.bag.testdouble.BagDataTestDouble
import com.github.jameshnsears.chance.data.domain.core.roll.impl.RollHistoryDataImpl
import com.github.jameshnsears.chance.data.domain.core.roll.testdouble.RollHistoryDataTestDouble
import com.github.jameshnsears.chance.data.domain.core.settings.impl.SettingsDataImpl
import com.github.jameshnsears.chance.data.domain.core.settings.testdouble.SettingsDataTestDouble
import com.github.jameshnsears.chance.data.repository.bag.impl.RepositoryBagImpl
import com.github.jameshnsears.chance.data.repository.bag.testdouble.RepositoryBagTestDouble
import com.github.jameshnsears.chance.data.repository.roll.impl.RepositoryRollImpl
import com.github.jameshnsears.chance.data.repository.roll.testdouble.RepositoryRollTestDouble
import com.github.jameshnsears.chance.data.repository.settings.impl.RepositorySettingsImpl
import com.github.jameshnsears.chance.data.repository.settings.testdouble.RepositorySettingsTestDouble
import com.github.jameshnsears.chance.utility.feature.UtilityFeature

class RepositoryFactory(context: Context? = null) {
    val settingsImpl = SettingsDataImpl()
    val settingsTestDouble = SettingsDataTestDouble()

    val repositorySettings = if (BuildConfig.DEBUG)
        if (UtilityFeature.isEnabled(UtilityFeature.Flag.USE_PROTO_REPO))
            RepositorySettingsImpl.getInstance(context!!, settingsImpl)
        else
            RepositorySettingsTestDouble.getInstance(settingsTestDouble)
    else
        RepositorySettingsImpl.getInstance(context!!, settingsImpl)

    ///////////////////////////////////////////////////

    val bagDataImpl = BagDataImpl()
    val bagDataTestDouble = BagDataTestDouble()

    val repositoryBag = if (BuildConfig.DEBUG)
        if (UtilityFeature.isEnabled(UtilityFeature.Flag.USE_PROTO_REPO))
            RepositoryBagImpl.getInstance(context!!, bagDataImpl.allDice)
        else
            RepositoryBagTestDouble.getInstance(bagDataTestDouble.allDice)
    else
        RepositoryBagImpl.getInstance(context!!, bagDataImpl.allDice)

    ///////////////////////////////////////////////////

    val rollHistoryDataImpl = RollHistoryDataImpl(bagDataImpl).rollHistory
    val rollHistoryDataTestDouble = RollHistoryDataTestDouble(bagDataTestDouble).rollHistory

    val repositoryRoll = if (BuildConfig.DEBUG)
        if (UtilityFeature.isEnabled(UtilityFeature.Flag.USE_PROTO_REPO))
            RepositoryRollImpl.getInstance(context!!, rollHistoryDataImpl)
        else
            RepositoryRollTestDouble.getInstance(rollHistoryDataTestDouble)
    else
        RepositoryRollImpl.getInstance(context!!, rollHistoryDataImpl)
}
