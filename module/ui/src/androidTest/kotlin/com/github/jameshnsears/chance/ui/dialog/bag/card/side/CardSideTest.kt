package com.github.jameshnsears.chance.ui.dialog.bag.card.side

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
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
import com.github.jameshnsears.chance.data.domain.core.Side
import com.github.jameshnsears.chance.data.domain.core.bag.testdouble.BagDataTestDouble
import com.github.jameshnsears.chance.ui.dialog.bag.DialogBag
import com.github.jameshnsears.chance.ui.dialog.bag.DialogBagAndroidViewModel
import com.github.jameshnsears.chance.ui.dialog.bag.DialogBagAndroidViewModelFactory
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class CardSideTest : AndroidTestHelper() {
    private val mockViewModel: CardSideService = mockk(relaxed = true)
    private val side = Side(number = 1, description = "Initial Description")
    private val initialState = CardSideState(
        sideNumber = side.number,
        sideNumberColour = "FFFFFFFF",
        sideImageDrawableId = 0,
        sideImageBase64 = "",
        sideImageRequest = null,
        sideImageAvailable = false,
        sideDescription = side.description,
        sideDescriptionColour = "FFFFFFFF",
        sideApplyToAllNumberColour = false,
        sideApplyToAllDescription = false,
        sideApplyToAllSvg = false,
        diceSidesFewerThanSideNumber = false,
        svgImportInProgress = false
    )
    private val stateFlow = MutableStateFlow(initialState)

    @Before
    fun setUp() {
        every { mockViewModel.stateFlowCardSide } returns stateFlow
    }

    @Test
    fun sideNumberDisplayed() {
        composeRule.setContent {
            ChanceTheme {
                BagCardSide(mockViewModel)
            }
        }

        composeRule.onNodeWithTag(SideTestTag.SIDE_NUMBER)
            .assertIsDisplayed()
            .assertTextContains("Side # 1")
    }

    @Test
    fun toggleApplyToAllNumberColour() {
        composeRule.setContent {
            ChanceTheme {
                BagCardSide(mockViewModel)
            }
        }

        composeRule.onNodeWithTag(SideTestTag.SIDE_APPLY_NUMBER_COLOUR)
            .performClick()

        verify { mockViewModel.sideApplyToAllNumberColour(true) }
    }

    @Test
    fun toggleApplyToAllSvg() {
        composeRule.setContent {
            ChanceTheme {
                BagCardSide(mockViewModel)
            }
        }

        composeRule.onNodeWithTag(SideTestTag.SIDE_APPLY_SVG)
            .performClick()
    }

    @Test
    fun componentsDisabledWhenDiceSidesFewerThanSideNumber() {
        stateFlow.value = initialState.copy(diceSidesFewerThanSideNumber = true)

        composeRule.setContent {
            ChanceTheme {
                BagCardSide(mockViewModel)
            }
        }

        composeRule.onNodeWithTag(SideTestTag.SIDE_COLOUR).assertIsNotEnabled()
        composeRule.onNodeWithTag(SideTestTag.SIDE_DESCRIPTION).assertIsNotEnabled()
        composeRule.onNodeWithTag(SideTestTag.SIDE_DESCRIPTION_COLOUR).assertIsNotEnabled()
        composeRule.onNodeWithTag(SideTestTag.SIDE_IMAGE_SVG).assertIsNotEnabled()
        composeRule.onNodeWithTag(SideTestTag.SIDE_IMAGE_SVG_CLEAR).assertIsNotEnabled()
    }

    @Test
    fun componentsEnabledWhenDiceSidesNotFewerThanSideNumber() {
        stateFlow.value = initialState.copy(diceSidesFewerThanSideNumber = false)

        composeRule.setContent {
            ChanceTheme {
                BagCardSide(mockViewModel)
            }
        }

        composeRule.onNodeWithTag(SideTestTag.SIDE_COLOUR).assertIsEnabled()
        composeRule.onNodeWithTag(SideTestTag.SIDE_DESCRIPTION).assertIsEnabled()
        composeRule.onNodeWithTag(SideTestTag.SIDE_DESCRIPTION_COLOUR).assertIsEnabled()
        composeRule.onNodeWithTag(SideTestTag.SIDE_IMAGE_SVG).assertIsEnabled()
        composeRule.onNodeWithTag(SideTestTag.SIDE_IMAGE_SVG_CLEAR).assertIsNotEnabled()
    }

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
