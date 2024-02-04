package com.github.jameshnsears.chance.ui.dialog.bag

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.test.platform.app.InstrumentationRegistry
import com.github.jameshnsears.chance.data.domain.DiceBag
import com.github.jameshnsears.chance.data.repository.bag.BagDemoSampleData
import com.github.jameshnsears.chance.data.repository.bag.DiceBagRepositoryTestDouble
import com.github.jameshnsears.chance.ui.dialog.bag.card.dice.BagCardDiceTestTag
import com.github.jameshnsears.chance.ui.theme.ChanceTheme
import com.github.jameshnsears.chance.utils.logging.LoggingLineNumberTreeInstrumentedFeature
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Test

class DialogBagInstrumentedTest : LoggingLineNumberTreeInstrumentedFeature() {
    @Test
    fun diceTitle() {
        val showDialog = mutableStateOf(true)

        val bagRepository = DiceBagRepositoryTestDouble.getInstance()
        runBlocking(Dispatchers.Main) {
            bagRepository.store(
                listOf(
                    BagDemoSampleData.diceHeadsTails,
                ),
            )
        }

        var diceBag: DiceBag
        runBlocking(Dispatchers.Main) {
            diceBag = bagRepository.fetch()
        }

        val dice = diceBag[0]
        val side = dice.sides[0]

        composeTestRule.setContent {
            ChanceTheme {
                DialogBag(
                    showDialog,
                    bagRepository,
                    dice,
                    side,
                )
            }
        }

        val diceTitle = composeTestRule.onNodeWithTag(BagCardDiceTestTag.DICE_TITLE)
        diceTitle.assertTextContains(
            InstrumentationRegistry.getInstrumentation().targetContext.getString(BagDemoSampleData.allDice[0].titleStringsId),
        )

        diceTitle.performTextClearance()
        val newTitle = "heads / tails"
        diceTitle.performTextInput(newTitle)
        composeTestRule.waitForIdle()

        diceTitle.assert(hasText(newTitle))
    }
}
