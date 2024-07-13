package com.github.jameshnsears.chance.ui.dialog.bag

import com.github.jameshnsears.chance.data.domain.core.Dice
import com.github.jameshnsears.chance.data.domain.core.Side
import com.github.jameshnsears.chance.data.domain.core.bag.testdouble.BagDataTestDouble
import com.github.jameshnsears.chance.data.utility.UtilityDataHelper
import com.github.jameshnsears.chance.utility.android.UtilityAndroidHelper
import kotlinx.coroutines.runBlocking

open class DialogBagUnitTestHelper : UtilityAndroidHelper() {
    protected fun getDialogBagAndroidViewModel(
        dice: Dice = BagDataTestDouble().d6,
        side: Side = dice.sides[0]
    ): DialogBagAndroidViewModel {
        val repositoryBag = UtilityDataHelper().repositoryBag
        runBlocking {
            repositoryBag.store(
                BagDataTestDouble().allDice,
            )
        }

        return DialogBagAndroidViewModel(
            getApplication(),
            repositoryBag,
            dice,
            side,
        )
    }
}
