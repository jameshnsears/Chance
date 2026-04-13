package com.github.jameshnsears.chance.ui.dialog.bag.card.roll

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.github.jameshnsears.chance.common.ui.AndroidTestHelper
import com.github.jameshnsears.chance.data.domain.core.Dice
import com.github.jameshnsears.chance.data.domain.core.Side
import io.mockk.spyk
import io.mockk.verify
import org.junit.Test

class CardRollTest : AndroidTestHelper() {
    @Test
    fun rollMultiplier() {
        val dice = Dice(sides = listOf(Side(number = 1), Side(number = 2)))
        val viewModel = spyk(CardRollService(dice))

        composeRule.setContent {
            BagCardRoll(viewModel)
        }

        composeRule.onNodeWithTag(BagCardRollTestTag.ROLL_MULTIPLIER_VALUE).assertIsDisplayed()
        composeRule.onNodeWithTag(BagCardRollTestTag.ROLL_MULTIPLIER_VALUE).performClick()

        // select "2" from dropdown
        composeRule.onNodeWithText("2").performClick()

        verify { viewModel.rollMultiplierValue("2") }
    }

    @Test
    fun rollExplode() {
        val dice = Dice(sides = listOf(Side(number = 1), Side(number = 2)))
        val viewModel = spyk(CardRollService(dice))

        composeRule.setContent {
            BagCardRoll(viewModel)
        }

        // initially off
        composeRule.onNodeWithTag(BagCardRollTestTag.ROLL_EXPLODE_CHECKBOX).assertIsOff()

        // toggle on
        composeRule.onNodeWithTag(BagCardRollTestTag.ROLL_EXPLODE_CHECKBOX).performClick()
        composeRule.onNodeWithTag(BagCardRollTestTag.ROLL_EXPLODE_CHECKBOX).assertIsOn()

        verify { viewModel.rollExplode(true) }

        // change "when" to "<"
        composeRule.onNodeWithTag(BagCardRollTestTag.ROLL_EXPLODE_WHEN).performClick()
        composeRule.onNodeWithText("<").performClick()

        verify { viewModel.rollExplodeWhen("<") }
    }

    @Test
    fun rollModifyScore() {
        val dice = Dice(sides = listOf(Side(number = 1), Side(number = 2)))
        val viewModel = spyk(CardRollService(dice))

        composeRule.setContent {
            BagCardRoll(viewModel)
        }

        // initially off
        composeRule.onNodeWithTag(BagCardRollTestTag.ROLL_MODIFY_SCORE_CHECKBOX).assertIsOff()

        // toggle on
        composeRule.onNodeWithTag(BagCardRollTestTag.ROLL_MODIFY_SCORE_CHECKBOX).performClick()
        composeRule.onNodeWithTag(BagCardRollTestTag.ROLL_MODIFY_SCORE_CHECKBOX).assertIsOn()

        verify { viewModel.rollModifyScore(true) }
    }
}
