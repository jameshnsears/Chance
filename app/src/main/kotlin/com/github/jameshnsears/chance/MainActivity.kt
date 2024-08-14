package com.github.jameshnsears.chance

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
}
