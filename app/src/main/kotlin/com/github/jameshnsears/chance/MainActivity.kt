package com.github.jameshnsears.chance

import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.github.jameshnsears.chance.compose.MainActivityComposable
import com.github.jameshnsears.chance.data.repository.RepositoryFactory
import com.github.jameshnsears.chance.utility.logging.UtilityLoggingLineNumberTree
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import timber.log.Timber

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installLogging()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            installSplashScreen()
        }

        installShortcut()

        val repositoryFactory = RepositoryFactory(application)

        var resizeInitialValue: Int
        runBlocking {
            resizeInitialValue = repositoryFactory.repositorySettings.fetch().first().resize
        }

        setContent {
            MainActivityComposable(
                application,
                repositoryFactory.repositorySettings,
                repositoryFactory.repositoryBag,
                repositoryFactory.repositoryRoll,
                resizeInitialValue
            )
        }
    }

    private fun installLogging() {
        if (Timber.treeCount == 0)
            Timber.plant(UtilityLoggingLineNumberTree())
    }

    private fun installShortcut() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val shortcutManager = getSystemService(ShortcutManager::class.java) as ShortcutManager

            shortcutManager.dynamicShortcuts = listOf(
                createShortcut(
                    getString(R.string.app_shortcut_yes_no),
                    Icon.createWithResource(
                        applicationContext,
                        R.drawable.outline_thumbs_up_down_24
                    )
                )
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.N_MR1)
    private fun createShortcut(
        shortcutId: String,
        icon: Icon
    ): ShortcutInfo {
        return ShortcutInfo.Builder(applicationContext, shortcutId)
            .setShortLabel(shortcutId)
            .setIcon(icon)
            .setIntent(
                Intent(this, ShortcutActivity::class.java).apply {
                    action = Intent.ACTION_VIEW
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
            )
            .build()
    }
}
