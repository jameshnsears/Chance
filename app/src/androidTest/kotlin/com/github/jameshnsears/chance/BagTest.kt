package com.github.jameshnsears.chance

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performSemanticsAction
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import com.github.jameshnsears.chance.common.R
import com.github.jameshnsears.chance.ui.tab.bag.BagTestTag
import kotlinx.coroutines.test.runTest
import org.junit.Test

class BagTest : TestSupport() {
    @Test
    fun bagReset() = runTest {
        displayBottomSheet(BagTestTag.BOTTOM_SHEET)

        androidComposeTestRule
            .onNodeWithTag(BagTestTag.RESET)
            .performClick()

        androidComposeTestRule
            .onNodeWithText(getString(R.string.tab_bag_reset_storage_confirm))
            .assertIsDisplayed()
    }

    @Test
    fun bagExport() = runTest {
        displayBottomSheet(BagTestTag.BOTTOM_SHEET)

        Intents.init()

        try {
            intending(hasAction(Intent.ACTION_CREATE_DOCUMENT)).respondWith(
                Instrumentation.ActivityResult(Activity.RESULT_OK, null)
            )

            androidComposeTestRule
                .onNodeWithTag(BagTestTag.EXPORT)
                .performClick()

        } finally {
            Intents.release()
        }
    }

    @Test
    fun bagZoom() = runTest {
        // Expand Bottom Sheet
        displayBottomSheet(BagTestTag.BOTTOM_SHEET)

        // Move Slider to 1
        androidComposeTestRule
            .onNodeWithTag(BagTestTag.RESIZE)
            .performSemanticsAction(SemanticsActions.SetProgress) { it(1f) }

        // Move Slide all the way through it's range: 1..9
        for (i in 1..9) {
            androidComposeTestRule
                .onNodeWithTag(BagTestTag.RESIZE)
                .performSemanticsAction(SemanticsActions.SetProgress) { it(i.toFloat()) }
        }
    }
}
