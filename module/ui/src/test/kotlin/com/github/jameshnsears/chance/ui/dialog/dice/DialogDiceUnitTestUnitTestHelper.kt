package com.github.jameshnsears.chance.ui.dialog.dice

import com.github.jameshnsears.chance.common.utility.UtilityAndroidUnitTestHelper
import com.github.jameshnsears.chance.data.common.repo.RepositoryFactory
import com.github.jameshnsears.chance.data.domain.core.Dice
import com.github.jameshnsears.chance.data.domain.core.Side
import com.github.jameshnsears.chance.data.domain.core.bag.testdouble.BagDataTestDouble
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

open class DialogDiceUnitTestUnitTestHelper : UtilityAndroidUnitTestHelper() {
    protected fun getDialogBagAndroidViewModel(
        dice: Dice = BagDataTestDouble().d6,
        side: Side = dice.sides[0]
    ): DialogDiceAndroidViewModel {
        val repositoryBag = RepositoryFactory().repositoryBag
        runBlocking(Dispatchers.Main) {
            repositoryBag.store(
                BagDataTestDouble().allDice,
            )
        }

        return DialogDiceAndroidViewModel(
            application(),
            repositoryBag,
            dice,
            side,
        )
    }
}
