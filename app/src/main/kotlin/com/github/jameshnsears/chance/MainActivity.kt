package com.github.jameshnsears.chance

import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.github.jameshnsears.chance.common.utility.UtilityLoggingLineNumberTree
import com.github.jameshnsears.chance.data.common.repository.RepositoryFactory
import com.github.jameshnsears.chance.data.domain.core.settings.SettingsDataInterface
import timber.log.Timber

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installLogging()

        enableEdgeToEdge()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            installSplashScreen()
        }

        installShortcut()

        val repositoryFactory = RepositoryFactory(application)

        setContent {
            val settings by produceState<SettingsDataInterface?>(initialValue = null) {
                val flow = repositoryFactory.repositorySettings.fetch()
                flow.collect { value = it }
            }

            MainComposable(
                application,
                repositoryFactory.repositorySettings,
                repositoryFactory.repositoryBag,
                repositoryFactory.repositoryRoll,
                settings?.resize ?: 2
            )
        }
    }

    private fun installLogging() {
        if (BuildConfig.DEBUG && Timber.treeCount == 0) {
            Timber.plant(UtilityLoggingLineNumberTree())
        }
    }

    private fun installShortcut() {
        val shortcutManager = getSystemService(ShortcutManager::class.java) ?: return

        shortcutManager.dynamicShortcuts = listOf(
            createShortcut(
                getString(R.string.app_shortcut_yes_no),
                Icon.createWithResource(
                    applicationContext,
                    R.drawable.thumb_up_down
                )
            )
        )
    }

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
