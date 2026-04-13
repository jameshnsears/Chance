package com.github.jameshnsears.chance.ui.dialog.bag.card.side

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
import com.github.jameshnsears.chance.common.ui.AndroidTestHelper
import com.github.jameshnsears.chance.common.ui.theme.ChanceTheme
import com.github.jameshnsears.chance.data.common.repository.RepositoryFactory
import com.github.jameshnsears.chance.data.domain.core.bag.testdouble.BagDataTestDouble
import com.github.jameshnsears.chance.ui.dialog.bag.DialogBag
import com.github.jameshnsears.chance.ui.dialog.bag.DialogBagAndroidViewModel
import com.github.jameshnsears.chance.ui.dialog.bag.DialogBagAndroidViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Test

class CardSideTest : AndroidTestHelper() {
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
            val dialogBagAndroidViewModel: DialogBagAndroidViewModel = viewModel(
                factory = DialogBagAndroidViewModelFactory(
                    application = LocalContext.current.applicationContext as Application,
                    repositoryBag,
                    diceInDialogBag.d2,
                    diceInDialogBag.d2.sides[1],
                )
            )

            ChanceTheme {
                DialogBag(
                    showDialog,
                    dialogBagAndroidViewModel
                )
            }
        }

        composeRule.onNodeWithText("Side").performClick()

        val sideDescription = composeRule.onNodeWithTag(SideTestTag.SIDE_DESCRIPTION)
        sideDescription.assertTextContains(diceInDialogBag.d2.sides[1].description)

        sideDescription.performTextClearance()
        val newTitle = "head"
        sideDescription.performTextInput(newTitle)
        composeRule.waitForIdle()

        sideDescription.assert(hasText(newTitle))
    }
}
