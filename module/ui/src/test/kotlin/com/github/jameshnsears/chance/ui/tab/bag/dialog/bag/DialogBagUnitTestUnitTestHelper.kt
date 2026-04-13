package com.github.jameshnsears.chance.ui.tab.bag.dialog.bag

import com.github.jameshnsears.chance.common.utility.UtilityAndroidUnitTestHelper
import com.github.jameshnsears.chance.data.common.repository.RepositoryFactory
import com.github.jameshnsears.chance.data.domain.core.Dice
import com.github.jameshnsears.chance.data.domain.core.Side
import com.github.jameshnsears.chance.data.domain.core.bag.testdouble.BagDataTestDouble
import com.github.jameshnsears.chance.ui.dialog.bag.DialogBagAndroidViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

open class DialogBagUnitTestUnitTestHelper : UtilityAndroidUnitTestHelper() {
    protected fun getDialogBagAndroidViewModel(
        dice: Dice = BagDataTestDouble().d6,
        side: Side = dice.sides[0]
    ): DialogBagAndroidViewModel {
        val repositoryBag = RepositoryFactory().repositoryBag
        runBlocking(Dispatchers.Main) {
            repositoryBag.store(
                BagDataTestDouble().allDice,
            )
        }

        return DialogBagAndroidViewModel(
            application(),
            repositoryBag,
            dice,
            side,
        )
    }
}
