package com.github.jameshnsears.chance.ui.dialog.bag

import com.github.jameshnsears.chance.data.repository.bag.RepositoryBagTestDouble
import com.github.jameshnsears.chance.data.sample.bag.SampleBag
import com.github.jameshnsears.chance.utility.android.UtilityAndroid
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

open class DialogBagUnitTestHelper : UtilityAndroid() {
    protected fun getDialogBagAndroidViewModel(): DialogBagAndroidViewModel {
        val repositoryBag = RepositoryBagTestDouble.getInstance()

        runBlocking {
            repositoryBag.store(
                SampleBag.allDice,
            )
        }

        val dice = runBlocking {
            repositoryBag.fetch().first()[0]
        }

        return DialogBagAndroidViewModel(
            getApplication(),
            repositoryBag,
            dice,
            dice.sides[0],
        )
    }
}
