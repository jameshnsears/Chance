package com.github.jameshnsears.chance.ui.dialog.dice.card.dice

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
import com.github.jameshnsears.chance.data.common.repo.RepositoryFactory
import com.github.jameshnsears.chance.data.domain.core.bag.testdouble.BagDataTestDouble
import com.github.jameshnsears.chance.data.repo.api.bag.RepositoryBagInterface
import com.github.jameshnsears.chance.ui.dialog.confirm.DialogConfirmTestTag
import com.github.jameshnsears.chance.ui.dialog.dice.ButtonFeatureTestTag
import com.github.jameshnsears.chance.ui.dialog.dice.DialogBag
import com.github.jameshnsears.chance.ui.dialog.dice.DialogDiceAndroidViewModel
import com.github.jameshnsears.chance.ui.dialog.dice.DialogDiceAndroidViewModelFactory
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
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
            val dialogDiceAndroidViewModel: DialogDiceAndroidViewModel = viewModel(
                factory = DialogDiceAndroidViewModelFactory(
                    application = LocalContext.current.applicationContext as Application,
                    repositoryBag as RepositoryBagInterface,
                    diceInDialogBag.d2,
                    diceInDialogBag.d2.sides[1],
                )
            )

            ChanceTheme {
                DialogBag(
                    showDialog,
                    dialogDiceAndroidViewModel,
                )
            }
        }

        composeRule.onNodeWithText(getString(R.string.dialog_bag_dice)).performClick()

        val diceTitle = composeRule.onNodeWithTag(CardDiceTestTag.DICE_TITLE)
        composeRule.waitForIdle()
        diceTitle.assertTextContains(diceInDialogBag.d2.title)

        diceTitle.performTextClearance()
        val newTitle = "heads / tails"
        diceTitle.performTextInput(newTitle)
        composeRule.waitForIdle()

        diceTitle.assert(hasText(newTitle))
    }

    @Test
    fun changeSidesConfirmDialog() = runTest {
        val repositoryBag = RepositoryFactory(
            InstrumentationRegistry.getInstrumentation().targetContext
        ).repositoryBag

        val showDialog = mutableStateOf(true)
        val diceInDialogBag = BagDataTestDouble()

        composeRule.setContent {
            val dialogDiceAndroidViewModel: DialogDiceAndroidViewModel = viewModel(
                factory = DialogDiceAndroidViewModelFactory(
                    application = LocalContext.current.applicationContext as Application,
                    repositoryBag as RepositoryBagInterface,
                    diceInDialogBag.d2,
                    diceInDialogBag.d2.sides[0],
                )
            )

            ChanceTheme {
                DialogBag(
                    showDialog,
                    dialogDiceAndroidViewModel,
                )
            }
        }

        // Navigate to Dice tab
        composeRule.onNodeWithText(getString(R.string.dialog_bag_dice)).performClick()

        // Change sides
        val diceSides = composeRule.onNodeWithTag(CardDiceTestTag.DICE_SIDES)
        diceSides.performTextClearance()
        diceSides.performTextInput("10")
        composeRule.waitForIdle()

        // Click Save
        composeRule.onNodeWithTag(ButtonFeatureTestTag.BUTTON_FEATURE_SAVE).performClick()
        composeRule.waitForIdle()

        // Assert DialogConfirm is shown
        composeRule.onNodeWithText(getString(R.string.dialog_bag_save_confirmation)).assertExists()
        composeRule.onNodeWithText(getString(R.string.dialog_bag_save_confirmation_question)).assertExists()

        // Click OK in confirmation dialog
        composeRule.onNodeWithTag(DialogConfirmTestTag.OK).performClick()
        composeRule.waitForIdle()

        // Assert DialogBag is closed
        assertFalse(showDialog.value)
    }
}
