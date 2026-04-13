package com.github.jameshnsears.chance.ui.dialog.bag.card.dice

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.test.platform.app.InstrumentationRegistry
import com.github.jameshnsears.chance.common.R
import com.github.jameshnsears.chance.common.ui.AndroidTestHelper
import com.github.jameshnsears.chance.common.ui.theme.ChanceTheme
import com.github.jameshnsears.chance.data.common.repository.RepositoryFactory
import com.github.jameshnsears.chance.data.domain.core.bag.testdouble.BagDataTestDouble
import com.github.jameshnsears.chance.data.repo.api.bag.RepositoryBagInterface
import com.github.jameshnsears.chance.ui.dialog.bag.DialogBag
import com.github.jameshnsears.chance.ui.dialog.bag.DialogBagAndroidViewModel
import com.github.jameshnsears.chance.ui.dialog.bag.DialogBagAndroidViewModelFactory
import kotlinx.coroutines.test.runTest
import org.junit.Test

class CardDiceTest : AndroidTestHelper() {
    @Test
    fun changeTitle() = runTest {
        val repositoryBag = RepositoryFactory(
            InstrumentationRegistry.getInstrumentation().targetContext
        ).repositoryBag

        val showDialog = mutableStateOf(true)

        val diceInDialogBag = BagDataTestDouble()

        composeRule.setContent {
            val dialogBagAndroidViewModel: DialogBagAndroidViewModel = viewModel(
                factory = DialogBagAndroidViewModelFactory(
                    application = LocalContext.current.applicationContext as Application,
                    repositoryBag as RepositoryBagInterface,
                    diceInDialogBag.d2,
                    diceInDialogBag.d2.sides[1],
                )
            )

            ChanceTheme {
                DialogBag(
                    showDialog,
                    dialogBagAndroidViewModel,
                )
            }
        }

        composeRule.onNodeWithText(getString(R.string.dialog_bag_dice)).performClick()

        val diceTitle = composeRule.onNodeWithTag(BagCardDiceTestTag.DICE_TITLE)
        composeRule.waitForIdle()
        diceTitle.assertTextContains(diceInDialogBag.d2.title)

        diceTitle.performTextClearance()
        val newTitle = "heads / tails"
        diceTitle.performTextInput(newTitle)
        composeRule.waitForIdle()

        diceTitle.assert(hasText(newTitle))
    }
}
