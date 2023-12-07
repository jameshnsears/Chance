package com.github.jameshnsears.chance.ui.zoom

import ZoomColumn
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import com.github.jameshnsears.chance.data.domain.Dice
import com.github.jameshnsears.chance.data.repository.dice.DiceRepositoryMock
import com.github.jameshnsears.chance.data.repository.dice.sample.DiceSampleData
import com.github.jameshnsears.chance.ui.theme.ChanceTheme
import com.github.jameshnsears.chance.utils.logging.LoggingLineNumberTree
import junit.framework.TestCase.fail
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import timber.log.Timber

class ZoomInstrumentedMockTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @BeforeClass
    fun beforeClass() {
        if (Timber.treeCount == 0) {
            Timber.plant(LoggingLineNumberTree())
        }
    }

    @Test
    fun zoom() {
        val diceRepository = DiceRepositoryMock
        diceRepository.store(DiceSampleData.twoDice)

        composeTestRule.setContent {
            ChanceTheme {
                ZoomColumn()
            }
        }

//        val descriptionTextField = composeTestRule.onNodeWithTag("diceDescription")
//        descriptionTextField.assertTextContains(Dice().description)
//        val newDescription = "a b c"
//        descriptionTextField.performTextClearance()
//        descriptionTextField.performTextInput(newDescription)
//        composeTestRule.waitForIdle()
//        descriptionTextField.assert(hasText(newDescription))

        fail("todo")
    }
}
