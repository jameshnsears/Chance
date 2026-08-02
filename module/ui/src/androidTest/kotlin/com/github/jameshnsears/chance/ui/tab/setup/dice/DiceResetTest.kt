package com.github.jameshnsears.chance.ui.tab.setup.dice

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.platform.app.InstrumentationRegistry
import com.github.jameshnsears.chance.common.ui.AndroidTestHelper
import com.github.jameshnsears.chance.common.ui.theme.ChanceTheme
import com.github.jameshnsears.chance.data.common.repo.RepositoryFactory
import com.github.jameshnsears.chance.ui.dialog.confirm.DialogConfirmTestTag
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class DiceResetTest : AndroidTestHelper() {
    private lateinit var repositoryFactory: RepositoryFactory
    private lateinit var viewModel: DiceAndroidViewModel

    @Before
    fun setUp() = runBlocking {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        repositoryFactory = RepositoryFactory(context)
        repositoryFactory.resetStorage()

        viewModel = DiceAndroidViewModel(
            InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as android.app.Application,
            repositoryFactory.repositorySettings,
            repositoryFactory.repositoryBag,
            repositoryFactory.repositoryRoll,
            repositoryFactory.repositoryGroup,
            1.0f,
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Test
    fun confirmReset() = runBlocking {
        val initialDiceCount = repositoryFactory.repositoryBag.fetch().first().size

        // Modify something to verify reset works
        repositoryFactory.repositoryBag.clear()
        assertEquals(0, repositoryFactory.repositoryBag.fetch().first().size)

        composeRule.setContent {
            ChanceTheme {
                val bottomSheetScaffoldState = rememberBottomSheetScaffoldState()
                ResetStorage(
                    bottomSheetScaffoldState = bottomSheetScaffoldState,
                    diceAndroidViewModel = viewModel
                )
            }
        }

        // Click Reset button
        composeRule.onNodeWithTag(DiceTestTag.RESET).performClick()

        // Confirm Dialog is shown
        composeRule.onNodeWithTag(DialogConfirmTestTag.OK).assertIsDisplayed()

        // Click OK
        composeRule.onNodeWithTag(DialogConfirmTestTag.OK).performClick()

        // Verify storage is reset
        val resetDiceCount = repositoryFactory.repositoryBag.fetch().first().size
        assertEquals(initialDiceCount, resetDiceCount)
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Test
    fun cancelReset() = runBlocking {
        repositoryFactory.repositoryBag.clear()
        assertEquals(0, repositoryFactory.repositoryBag.fetch().first().size)

        composeRule.setContent {
            ChanceTheme {
                val bottomSheetScaffoldState = rememberBottomSheetScaffoldState()
                ResetStorage(
                    bottomSheetScaffoldState = bottomSheetScaffoldState,
                    diceAndroidViewModel = viewModel
                )
            }
        }

        // Click Reset button
        composeRule.onNodeWithTag(DiceTestTag.RESET).performClick()

        // Click CANCEL
        composeRule.onNodeWithTag(DialogConfirmTestTag.CANCEL).performClick()

        // Verify storage is NOT reset (remains 0)
        assertEquals(0, repositoryFactory.repositoryBag.fetch().first().size)
    }
}
