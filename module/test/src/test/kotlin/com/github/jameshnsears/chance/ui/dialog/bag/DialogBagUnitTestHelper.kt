package com.github.jameshnsears.chance.ui.dialog.bag

import com.github.jameshnsears.chance.data.domain.state.Dice
import com.github.jameshnsears.chance.data.domain.state.Side
import com.github.jameshnsears.chance.data.repository.bag.mock.RepositoryBagTestDouble
import com.github.jameshnsears.chance.data.sample.bag.SampleBagTestData
import com.github.jameshnsears.chance.utility.android.UtilityAndroidHelper
import kotlinx.coroutines.runBlocking

open class DialogBagUnitTestHelper : UtilityAndroidHelper() {
    protected fun getDialogBagAndroidViewModel(
        dice: Dice = SampleBagTestData().d6,
        side: Side = dice.sides[0]
    ): DialogBagAndroidViewModel {
        val repositoryBag = RepositoryBagTestDouble.getInstance()

        runBlocking {
            repositoryBag.store(
                SampleBagTestData().allDice,
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
