package com.github.jameshnsears.chance.ui.dialog.bag

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import com.github.jameshnsears.chance.data.bag.demo.BagDemoData
import com.github.jameshnsears.chance.data.bag.repository.BagRepositoryMock
import com.github.jameshnsears.chance.ui.dialog.bag.card.BagCardDiceTestTag
import com.github.jameshnsears.chance.ui.theme.ChanceTheme
import com.github.jameshnsears.chance.utils.logging.LoggingLineNumberTreeInstrumentedFeature
import junit.framework.TestCase.fail
import org.junit.Ignore
import org.junit.Test

class DialogBagInstrumentedTest : LoggingLineNumberTreeInstrumentedFeature() {
    @Test
    fun diceTitle() {
        val showDialog = mutableStateOf(true)

        val bagRepository = BagRepositoryMock
        bagRepository.store(
            listOf(
                BagDemoData.diceHeadsTails
            )
        )

        val dice = bagRepository.fetch()[0]
        val side = dice.sides[0]

        composeTestRule.setContent {
            ChanceTheme {
                DialogBag(
                    showDialog,
                    DialogBagAndroidViewModel(
                        ApplicationProvider.getApplicationContext(),
                        bagRepository,
                        dice,
                        side
                    )
                )
            }
        }

        val diceTitle = composeTestRule.onNodeWithTag(BagCardDiceTestTag.diceTitle)
        diceTitle.assertTextContains(
            InstrumentationRegistry.getInstrumentation().targetContext.getString(BagDemoData.dice[0].titleStringsId)
        )

        diceTitle.performTextClearance()
        val newTitle = "heads / tails"
        diceTitle.performTextInput(newTitle)
        composeTestRule.waitForIdle()

        diceTitle.assert(hasText(newTitle))
    }
}
