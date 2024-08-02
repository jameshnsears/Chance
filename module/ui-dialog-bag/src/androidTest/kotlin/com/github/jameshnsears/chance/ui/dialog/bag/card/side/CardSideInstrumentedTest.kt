package com.github.jameshnsears.chance.ui.dialog.bag.card.side

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.test.platform.app.InstrumentationRegistry
import com.github.jameshnsears.chance.data.domain.core.bag.testdouble.BagDataTestDouble
import com.github.jameshnsears.chance.data.repository.RepositoryFactory
import com.github.jameshnsears.chance.ui.dialog.bag.DialogBagInstrumentedHelper
import com.github.jameshnsears.chance.ui.dialog.bag.card.side.compose.BagCardSideTestTag
import com.github.jameshnsears.chance.ui.dialog.bag.compose.DialogBag
import com.github.jameshnsears.chance.ui.theme.ChanceTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Test

class CardSideInstrumentedTest : DialogBagInstrumentedHelper() {
    @Test
    fun changeDescription() = runTest {
        val repositoryBag = RepositoryFactory(
            InstrumentationRegistry.getInstrumentation().targetContext
        ).repositoryBag

        val showDialog = mutableStateOf(true)

        val diceInDialogBag = BagDataTestDouble()

        runBlocking(Dispatchers.Main) {
            repositoryBag.store(
                mutableListOf(
                    BagDataTestDouble().d2,
                ),
            )
        }

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
        val newTitle = "head"
        sideDescription.performTextInput(newTitle)
        composeRule.waitForIdle()

        sideDescription.assert(hasText(newTitle))
    }
}
