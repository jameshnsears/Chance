package com.github.jameshnsears.chance.ui.dialog.dice

import android.app.Application
import androidx.compose.ui.platform.LocalContext
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
import org.junit.Assume
import org.junit.Test

class DialogDiceTabsCircularTest : AndroidTestHelper() {
    @Test
    fun circularSwiping() {
        Assume.assumeFalse(isCI)

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
        waitForSelected(DialogDiceTabTestTag.TAB_DICE)
        composeRule.onNodeWithTag(CardDiceTestTag.DICE_TITLE).assertExists()

        // Swipe Left from Dice -> Side
        composeRule.onNodeWithTag(CardDiceTestTag.DICE_TITLE).performTouchInput { swipeLeft() }
        waitForSelected(DialogDiceTabTestTag.TAB_SIDE)

        // Swipe Left from Side -> Roll
        composeRule.onNodeWithTag(CardFaceTestTag.SIDE_NUMBER).performTouchInput { swipeLeft() }
        waitForSelected(DialogDiceTabTestTag.TAB_ROLL)

        // Circular Swipe Left from Roll -> Dice
        composeRule.onNodeWithTag(CardRollTestTag.ROLL_MULTIPLIER_VALUE).performTouchInput { swipeLeft() }
        waitForSelected(DialogDiceTabTestTag.TAB_DICE)

        // Circular Swipe Right from Dice -> Roll
        composeRule.onNodeWithTag(CardDiceTestTag.DICE_TITLE).performTouchInput { swipeRight() }
        waitForSelected(DialogDiceTabTestTag.TAB_ROLL)

        // Circular Swipe Right from Roll -> Side
        composeRule.onNodeWithTag(CardRollTestTag.ROLL_MULTIPLIER_VALUE).performTouchInput { swipeRight() }
        waitForSelected(DialogDiceTabTestTag.TAB_SIDE)
    }

    @Test
    fun tabClicks() {
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
        waitForSelected(DialogDiceTabTestTag.TAB_ROLL)

        // Click on Dice tab
        composeRule.onNodeWithTag(DialogDiceTabTestTag.TAB_DICE).performClick()
        waitForSelected(DialogDiceTabTestTag.TAB_DICE)

        // Click on Side tab
        composeRule.onNodeWithTag(DialogDiceTabTestTag.TAB_SIDE).performClick()
        waitForSelected(DialogDiceTabTestTag.TAB_SIDE)
    }
}
