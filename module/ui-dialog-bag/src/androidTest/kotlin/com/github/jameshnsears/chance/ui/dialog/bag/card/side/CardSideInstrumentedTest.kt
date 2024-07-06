package com.github.jameshnsears.chance.ui.dialog.bag.card.side

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import com.github.jameshnsears.chance.data.sample.bag.SampleBagTestData
import com.github.jameshnsears.chance.ui.dialog.bag.card.side.compose.BagCardSideTestTag
import com.github.jameshnsears.chance.ui.dialog.bag.compose.DialogBag
import com.github.jameshnsears.chance.ui.theme.ChanceTheme
import org.junit.Test

class CardSideInstrumentedTest :
    com.github.jameshnsears.chance.ui.dialog.bag.DialogBagInstrumentedHelper() {
    @Test
    fun changeDescription() {
        val showDialog = mutableStateOf(true)

        val diceInDialogBag = SampleBagTestData()

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

        val sideDescription = composeRule.onNodeWithTag(BagCardSideTestTag.SIDE_DESCRIPTION)
        sideDescription.assertTextContains(diceInDialogBag.d2.sides[1].description)

        sideDescription.performTextClearance()
        val newTitle = "heads / tails"
        sideDescription.performTextInput(newTitle)
        composeRule.waitForIdle()

        sideDescription.assert(hasText(newTitle))
    }
}
