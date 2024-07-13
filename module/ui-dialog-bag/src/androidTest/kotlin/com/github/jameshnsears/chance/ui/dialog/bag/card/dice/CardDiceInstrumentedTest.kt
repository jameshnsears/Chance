package com.github.jameshnsears.chance.ui.dialog.bag.card.dice

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import com.github.jameshnsears.chance.data.domain.core.bag.testdouble.BagDataTestDouble
import com.github.jameshnsears.chance.ui.dialog.bag.DialogBagInstrumentedHelper
import com.github.jameshnsears.chance.ui.dialog.bag.card.dice.compose.BagCardDiceTestTag
import com.github.jameshnsears.chance.ui.dialog.bag.compose.DialogBag
import com.github.jameshnsears.chance.ui.theme.ChanceTheme
import kotlinx.coroutines.test.runTest
import org.junit.Test

class CardDiceInstrumentedTest : DialogBagInstrumentedHelper() {
    @Test
    fun changeTitle() = runTest {
        val showDialog = mutableStateOf(true)

        val diceInDialogBag = BagDataTestDouble()

        composeRule.setContent {
            ChanceTheme {
                DialogBag(
                    showDialog,
                    repositoryBag,
                    diceInDialogBag.d2,
                    diceInDialogBag.d2.sides[1],
                )
            }
        }

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
