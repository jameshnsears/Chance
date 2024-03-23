package com.github.jameshnsears.chance.ui.dialog.bag

import com.github.jameshnsears.chance.data.domain.state.Dice
import com.github.jameshnsears.chance.data.domain.state.Side
import com.github.jameshnsears.chance.data.repository.bag.mock.RepositoryBagTestDouble
import com.github.jameshnsears.chance.data.sample.bag.SampleBag
import com.github.jameshnsears.chance.utility.android.UtilityAndroid
import kotlinx.coroutines.runBlocking

open class DialogBagUnitTestHelper : UtilityAndroid() {
    protected fun getDialogBagAndroidViewModel(
        dice: Dice = SampleBag.d6,
        side: Side = dice.sides[0]
    ): DialogBagAndroidViewModel {
        val repositoryBag = RepositoryBagTestDouble.getInstance()

        runBlocking {
            repositoryBag.store(
                SampleBag.allDice,
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
