package com.github.jameshnsears.chance.ui.dialog.dice

import android.app.Application
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeLeft
import androidx.compose.ui.test.swipeRight
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.test.platform.app.InstrumentationRegistry
import com.github.jameshnsears.chance.common.ui.AndroidTestHelper
import com.github.jameshnsears.chance.common.ui.theme.ChanceTheme
import com.github.jameshnsears.chance.data.common.repo.RepositoryFactory
import com.github.jameshnsears.chance.data.domain.core.bag.testdouble.BagDataTestDouble
import com.github.jameshnsears.chance.data.repo.api.bag.RepositoryBagInterface
import com.github.jameshnsears.chance.ui.dialog.dice.card.dice.CardDiceTestTag
import com.github.jameshnsears.chance.ui.dialog.dice.card.face.CardFaceTestTag
import com.github.jameshnsears.chance.ui.dialog.dice.card.roll.CardRollTestTag
import kotlinx.coroutines.test.runTest
import org.junit.Test

class DialogDiceTabsCircularTest : AndroidTestHelper() {
    @Test
    fun circularSwiping() = runTest {
        val repositoryBag = RepositoryFactory(
            InstrumentationRegistry.getInstrumentation().targetContext
        ).repositoryBag

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
                    onClose = { },
                    dialogDiceAndroidViewModel = dialogDiceAndroidViewModel,
                )
            }
        }

        // Initially on Dice tab
        composeRule.onNodeWithTag(DialogDiceTabTestTag.TAB_DICE).assertIsSelected()
        composeRule.onNodeWithTag(CardDiceTestTag.DICE_TITLE).assertExists()

        // Swipe Left from Dice -> Side
        composeRule.onNodeWithTag(CardDiceTestTag.DICE_TITLE).performTouchInput { swipeLeft() }
        composeRule.waitForIdle()
        composeRule.onNodeWithTag(DialogDiceTabTestTag.TAB_SIDE).assertIsSelected()

        // Swipe Left from Side -> Roll
        composeRule.onNodeWithTag(CardFaceTestTag.SIDE_NUMBER).performTouchInput { swipeLeft() }
        composeRule.waitForIdle()
        composeRule.onNodeWithTag(DialogDiceTabTestTag.TAB_ROLL).assertIsSelected()

        // Circular Swipe Left from Roll -> Dice
        composeRule.onNodeWithTag(CardRollTestTag.ROLL_MULTIPLIER_VALUE).performTouchInput { swipeLeft() }
        composeRule.waitForIdle()
        composeRule.onNodeWithTag(DialogDiceTabTestTag.TAB_DICE).assertIsSelected()

        // Circular Swipe Right from Dice -> Roll
        composeRule.onNodeWithTag(CardDiceTestTag.DICE_TITLE).performTouchInput { swipeRight() }
        composeRule.waitForIdle()
        composeRule.onNodeWithTag(DialogDiceTabTestTag.TAB_ROLL).assertIsSelected()

        // Circular Swipe Right from Roll -> Side
        composeRule.onNodeWithTag(CardRollTestTag.ROLL_MULTIPLIER_VALUE).performTouchInput { swipeRight() }
        composeRule.waitForIdle()
        composeRule.onNodeWithTag(DialogDiceTabTestTag.TAB_SIDE).assertIsSelected()
    }

    @Test
    fun tabClicks() = runTest {
        val repositoryBag = RepositoryFactory(
            InstrumentationRegistry.getInstrumentation().targetContext
        ).repositoryBag

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
                    onClose = { },
                    dialogDiceAndroidViewModel = dialogDiceAndroidViewModel,
                )
            }
        }

        // Click on Roll tab
        composeRule.onNodeWithTag(DialogDiceTabTestTag.TAB_ROLL).performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithTag(DialogDiceTabTestTag.TAB_ROLL).assertIsSelected()

        // Click on Dice tab
        composeRule.onNodeWithTag(DialogDiceTabTestTag.TAB_DICE).performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithTag(DialogDiceTabTestTag.TAB_DICE).assertIsSelected()

        // Click on Side tab
        composeRule.onNodeWithTag(DialogDiceTabTestTag.TAB_SIDE).performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithTag(DialogDiceTabTestTag.TAB_SIDE).assertIsSelected()
    }
}
