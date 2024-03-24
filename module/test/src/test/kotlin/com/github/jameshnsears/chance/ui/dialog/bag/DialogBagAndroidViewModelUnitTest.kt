package com.github.jameshnsears.chance.ui.dialog.bag

import android.app.Application
import com.github.jameshnsears.chance.data.domain.state.Dice
import com.github.jameshnsears.chance.data.domain.state.Side
import com.github.jameshnsears.chance.data.repository.bag.RepositoryBagInterface
import com.github.jameshnsears.chance.data.repository.bag.mock.RepositoryBagTestDouble
import com.github.jameshnsears.chance.data.sample.bag.SampleBag
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test

class DialogBagAndroidViewModelUnitTest : DialogBagUnitTestHelper() {
    @Test
    fun dialogBagRollCardExplodeAfterChangeInDiceSides() = runTest {
        val originalDice = SampleBag.d12

        val repositoryBag = RepositoryBagTestDouble.getInstance()
        repositoryBag.store(mutableListOf(SampleBag.d12))

        val dialogBagAndroidViewModel = DialogBagAndroidViewModel(
            mockk<Application>(),
            repositoryBag,
            originalDice,
            originalDice.sides[0]
        )

        assertTrue(
            dialogBagAndroidViewModel.cardRollViewModel
            .stateFlowCardRoll.value
            .rollExplodeAvailableValues.size == 12
        )

        dialogBagAndroidViewModel.cardDiceAndroidViewModel.diceSidesSize("8")

        assertTrue(
            dialogBagAndroidViewModel.cardRollViewModel
                .stateFlowCardRoll.value
                .rollExplodeAvailableValues.size == 8
        )

        fail("todo -- use a flow to emit diceSidesSize that cardRollViewModel collects?")
    }

    @Test
    fun dialogBagSaveAfterModifyingAllCards() = runTest {
        // need to check saved to repository!
        fail("todo")
    }

    @Test
    fun dialogBagSaveNotPossibleAsTitleNotUnique() = runTest {
        fail("todo")
    }

    @Test
    fun dialogBagSaveWithCloneTicked() = runTest {
        fail("todo")
    }

    @Test
    fun dialogBagSaveWithDeleteTicked() = runTest {
        fail("todo + include androidTest for all of the above")
    }
}
