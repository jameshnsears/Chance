package com.github.jameshnsears.chance.ui.dialog.dice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.jameshnsears.chance.common.utility.UtilityAndroidUnitTestHelper
import com.github.jameshnsears.chance.data.common.repo.RepositoryFactory
import com.github.jameshnsears.chance.data.domain.core.Dice
import com.github.jameshnsears.chance.data.domain.core.Side
import com.github.jameshnsears.chance.data.domain.core.bag.testdouble.BagDataTestDouble
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before

open class DialogDiceUnitTestUnitTestHelper : UtilityAndroidUnitTestHelper() {
    protected lateinit var repositoryFactory: RepositoryFactory
    protected val createdViewModels = mutableListOf<ViewModel>()

    @Before
    fun setUp() {
        repositoryFactory = RepositoryFactory()
        runBlocking { repositoryFactory.resetStorage() }
    }

    @After
    fun tearDown() {
        createdViewModels.forEach { it.viewModelScope.cancel() }
        createdViewModels.clear()
        repositoryFactory.close()
    }

    protected fun getDialogBagAndroidViewModel(
        dice: Dice = BagDataTestDouble().d6,
        side: Side = dice.sides[0]
    ): DialogDiceAndroidViewModel {
        val repositoryBag = repositoryFactory.repositoryBag
        runBlocking(Dispatchers.Main) {
            repositoryBag.store(
                BagDataTestDouble().allDice(),
            )
        }

        return DialogDiceAndroidViewModel(
            application(),
            repositoryBag,
            dice,
            side,
            repositoryFactory.repositoryRoll,
            repositoryFactory.repositoryGroup,
        ).also { createdViewModels.add(it) }
    }
}
