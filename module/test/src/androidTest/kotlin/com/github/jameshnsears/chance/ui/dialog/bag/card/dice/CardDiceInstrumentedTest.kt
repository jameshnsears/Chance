package com.github.jameshnsears.chance.ui.dialog.bag.card.dice

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.test.platform.app.InstrumentationRegistry
import com.github.jameshnsears.chance.data.sample.bag.SampleBag
import com.github.jameshnsears.chance.ui.dialog.bag.DialogBagInstrumentedHelper
import com.github.jameshnsears.chance.ui.dialog.bag.card.dice.compose.BagCardDiceTestTag
import com.github.jameshnsears.chance.ui.dialog.bag.compose.DialogBag
import com.github.jameshnsears.chance.ui.theme.ChanceTheme
import org.junit.Assert.fail
import org.junit.Test

class CardDiceInstrumentedTest : DialogBagInstrumentedHelper() {
    @Test
    fun modifyDiceCard() {
        val showDialog = mutableStateOf(true)

        composeTestRule.setContent {
            ChanceTheme {
                DialogBag(
                    showDialog,
                    repositoryBag,
                    SampleBag.d2,
                    SampleBag.d2.sides[1],
                )
            }
        }

        val diceTitle = composeTestRule.onNodeWithTag(BagCardDiceTestTag.DICE_TITLE)
        diceTitle.assertTextContains(
            InstrumentationRegistry.getInstrumentation().targetContext.getString(
                SampleBag.d2.titleStringsId
            ),
        )

        diceTitle.performTextClearance()
        val newTitle = "heads / tails"
        diceTitle.performTextInput(newTitle)
        composeTestRule.waitForIdle()

        diceTitle.assert(hasText(newTitle))

        fail("todo - test that change to diceTitle saved into storage")
    }
}
