package com.github.jameshnsears.chance.ui.tab.rolls

import com.github.jameshnsears.chance.common.utility.UtilityAndroidUnitTestHelper
import com.github.jameshnsears.chance.data.common.repo.RepositoryFactory
import com.github.jameshnsears.chance.data.domain.core.bag.testdouble.BagDataTestDouble
import com.github.jameshnsears.chance.data.domain.core.group.testdouble.GroupDataTestDouble
import com.github.jameshnsears.chance.data.repo.api.bag.RepositoryBagInterface
import com.github.jameshnsears.chance.data.repo.api.group.RepositoryGroupInterface
import io.mockk.coVerify
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RollsSelectionHelperTest : UtilityAndroidUnitTestHelper() {
    @Test
    fun markDiceAsSelected() = runTest {
        val repositoryBag = spyk<RepositoryBagInterface>(RepositoryFactory().repositoryBag)
        val repositoryGroup = spyk<RepositoryGroupInterface>(RepositoryFactory().repositoryGroup)
        val helper = RollsSelectionHelper(repositoryBag, repositoryGroup)
        val diceBag = BagDataTestDouble().allDice()
        val dice = diceBag[0]

        helper.markDiceAsSelected(dice, true, this)
        advanceUntilIdle()

        coVerify { repositoryBag.store(dice.uuid, true) }
    }

    @Test
    fun markGroupAsSelected() = runTest {
        val repositoryBag = spyk<RepositoryBagInterface>(RepositoryFactory().repositoryBag)
        val repositoryGroup = spyk<RepositoryGroupInterface>(RepositoryFactory().repositoryGroup)
        val helper = RollsSelectionHelper(repositoryBag, repositoryGroup)
        val bagData = BagDataTestDouble()
        val groupHistory = GroupDataTestDouble(bagData).groupHistory
        val group = groupHistory[0]

        helper.markGroupAsSelected(group, true, this)
        advanceUntilIdle()

        coVerify { repositoryGroup.store(group.uuid, true) }
    }
}
