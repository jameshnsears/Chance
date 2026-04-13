package com.github.jameshnsears.chance

import android.content.Context
import android.content.Intent
import androidx.compose.ui.test.hasContentDescription
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.jameshnsears.chance.common.ui.AndroidTestHelper
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ShortcutTest : AndroidTestHelper() {
    @Test
    fun launchFromShortcutIntent() = runTest {
        val context = ApplicationProvider.getApplicationContext<Context>()

        val intent = Intent(context, ShortcutActivity::class.java).apply {
            action = Intent.ACTION_VIEW
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        ActivityScenario.launch<ShortcutActivity>(intent).use { scenario ->
            scenario.onActivity { activity ->
                assertNotNull(activity)
                assertFalse(activity.isFinishing)
            }

            composeRule.onNode(
                hasContentDescription(
                    context.getString(R.string.app_shortcut_no)
                ).or(
                    hasContentDescription(
                        context.getString(R.string.app_shortcut_yes)
                    )
                )
            ).assertExists()
        }
    }
}
