package com.github.jameshnsears.chance.data.utility

import android.content.Context
import com.github.jameshnsears.chance.common.BuildConfig
import com.github.jameshnsears.chance.data.domain.core.bag.testdouble.BagDataTestDouble
import com.github.jameshnsears.chance.data.domain.core.roll.testdouble.RollHistoryDataTestDouble
import com.github.jameshnsears.chance.data.repository.bag.impl.RepositoryBagImpl
import com.github.jameshnsears.chance.data.repository.bag.testdouble.RepositoryBagTestDouble
import com.github.jameshnsears.chance.data.repository.roll.impl.RepositoryRollImpl
import com.github.jameshnsears.chance.data.repository.roll.testdouble.RepositoryRollTestDouble
import com.github.jameshnsears.chance.data.repository.settings.impl.RepositorySettingsImpl
import com.github.jameshnsears.chance.data.repository.settings.testdouble.RepositorySettingsTestDouble
import com.github.jameshnsears.chance.utility.feature.UtilityFeature

class UtilityDataHelper(context: Context? = null) {
    val repositorySettings = if (BuildConfig.DEBUG)
        if (UtilityFeature.isEnabled(UtilityFeature.Flag.USE_PROD_REPO))
            RepositorySettingsImpl.getInstance(context!!)
        else
            RepositorySettingsTestDouble.getInstance()
    else
        RepositorySettingsImpl.getInstance(context!!)

    ///////////////////////////////////////////////////

    val bagDataTestDouble = BagDataTestDouble()

    val repositoryBag = if (BuildConfig.DEBUG)
        if (UtilityFeature.isEnabled(UtilityFeature.Flag.USE_PROD_REPO))
            RepositoryBagImpl.getInstance(context!!)
        else
            RepositoryBagTestDouble.getInstance(bagDataTestDouble.allDice)
    else
        RepositoryBagImpl.getInstance(context!!)

    ///////////////////////////////////////////////////

    val rollHistoryDataTestDouble = RollHistoryDataTestDouble(bagDataTestDouble).rollHistory

    val repositoryRoll = if (BuildConfig.DEBUG)
        if (UtilityFeature.isEnabled(UtilityFeature.Flag.USE_PROD_REPO)) {
            RepositoryRollImpl.getInstance(context!!)
        } else {
            RepositoryRollTestDouble.getInstance(rollHistoryDataTestDouble)
        } else {
        RepositoryRollImpl.getInstance(context!!)
    }
}
