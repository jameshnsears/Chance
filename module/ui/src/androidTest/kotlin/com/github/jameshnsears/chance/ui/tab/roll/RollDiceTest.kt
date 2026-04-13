package com.github.jameshnsears.chance.ui.tab.roll

import android.app.Application
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipe
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.jameshnsears.chance.common.ui.AndroidTestHelper
import com.github.jameshnsears.chance.common.ui.theme.ChanceTheme
import com.github.jameshnsears.chance.common.utility.feature.UtilityFeature
import com.github.jameshnsears.chance.common.utility.feature.UtilityFeature.Flag
import com.github.jameshnsears.chance.data.common.repository.RepositoryFactory
import com.github.jameshnsears.chance.ui.zoom.roll.ZoomRollAndroidViewModel
import com.github.jameshnsears.chance.ui.zoom.roll.ZoomRollAndroidViewModelFactory
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class RollDiceTest : AndroidTestHelper() {
    /*
        @Test
        fun changeTitle() = runTest {
            val repositoryBag = RepositoryFactory(
                InstrumentationRegistry.getInstrumentation().targetContext
            ).repositoryBag

            val showDialog = mutableStateOf(true)

            val diceInDialogBag = BagDataTestDouble()

            composeRule.setContent {
                val dialogBagAndroidViewModel: DialogBagAndroidViewModel = viewModel(
                    factory = DialogBagAndroidViewModelFactory(
                        application = LocalContext.current.applicationContext as Application,
                        repositoryBag as RepositoryBagInterface,
                        diceInDialogBag.d2,
                        diceInDialogBag.d2.sides[1],
                    )
                )

                ChanceTheme {
                    DialogBag(
                        showDialog,
                        dialogBagAndroidViewModel,
                    )
                }
            }

            composeRule.onNodeWithText(getString(R.string.dialog_bag_dice)).performClick()

            val diceTitle = composeRule.onNodeWithTag(BagCardDiceTestTag.DICE_TITLE)
            composeRule.waitForIdle()
            diceTitle.assertTextContains(diceInDialogBag.d2.title)

            diceTitle.performTextClearance()
            val newTitle = "heads / tails"
            diceTitle.performTextInput(newTitle)
            composeRule.waitForIdle()

            diceTitle.assert(hasText(newTitle))
        }
     */

    @Before
    fun setup() = runTest {
        UtilityFeature.enabled = setOf(
            Flag.REPO_PROTOCOL_BUFFER_TEST_DOUBLE,
        )

        RepositoryFactory().resetStorage()
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Test
    fun rollDice() = runTest {

        val repositorySettings = RepositoryFactory().repositorySettings
        val repositoryBag = RepositoryFactory().repositoryBag
        val repositoryRoll = RepositoryFactory().repositoryRoll

        composeRule.setContent {
            val application = LocalContext.current.applicationContext as Application

            val rollAndroidViewModel: RollAndroidViewModel = viewModel(
                factory = RollAndroidViewModelFactory(
                    application = application,
                    repositorySettings,
                    repositoryBag,
                    repositoryRoll,
                )
            )

            val zoomRollAndroidViewModel: ZoomRollAndroidViewModel = viewModel(
                factory = ZoomRollAndroidViewModelFactory(
                    application = application,
                    repositorySettings,
                    repositoryBag,
                    repositoryRoll,
                )
            )

            ChanceTheme {
                TabRollLayout(
                    rollAndroidViewModel,
                    zoomRollAndroidViewModel
                )
            }
        }

        composeRule.waitForIdle()

        composeRule.mainClock.autoAdvance = false

        composeRule
            .onNodeWithTag(RollTestTag.BOTTOM_SHEET)
            .performTouchInput {
                val startY = centerY + (height / 4)
                val endY = centerY - (height / 4)

                swipe(
                    start = Offset(centerX, startY),
                    end = Offset(centerX, endY),
                    durationMillis = 300
                )
            }

        repeat(10) {
            composeRule.mainClock.advanceTimeBy(100)
            Thread.sleep(100)
        }
//
//        composeRule
//            .waitUntilExactlyOneExists(
//                hasTestTag(RollTestTag.UNDO),
//                timeoutMillis = 5_000
//            )
//
//        composeRule
//            .onNodeWithTag(RollTestTag.UNDO)
//            .assertIsNotEnabled()
//
//        composeRule
//            .onNodeWithTag(RollTestTag.ROLL_NOT_ENABLED)
//            .assertIsNotEnabled()
//
//        composeRule
//            .onNodeWithTag(RollSelectionTestTag.ROLL_BUTTON + "d6")
//            .performClick()
//
//
//
//
////        composeRule.waitUntilAtLeastOneExists(
////            hasTestTag(RollTestTag.ROLL_ENABLED),
////            timeoutMillis = 5_000
////        )
//
//
//        composeRule.waitUntil(
//            timeoutMillis = 10_000
//        ) {
//            try {
//                composeRule
//                    .onNodeWithTag(RollTestTag.ROLL_ENABLED)
//                    .assertIsEnabled()
//                true
//            } catch (_: AssertionError) {
//                false
//            }
//        }
//
//        composeRule
//            .onNodeWithTag(RollTestTag.ROLL_ENABLED)
//            .assertIsEnabled()
    }
    /*

    confirm "Roll" button is not enabled

    click "Roll" button

    confirm one roll is displayed with a multiplier of 3
     */
}
