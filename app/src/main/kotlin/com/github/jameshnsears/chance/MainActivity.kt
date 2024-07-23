package com.github.jameshnsears.chance

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.github.jameshnsears.chance.compose.MainActivityComposable
import com.github.jameshnsears.chance.data.repository.RepositoryFactory
import com.github.jameshnsears.chance.utility.logging.UtilityLoggingLineNumberTree
import timber.log.Timber

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installLogging()

        installSplashScreen()

        val repositoryFactory = RepositoryFactory(application)

        setContent {
            MainActivityComposable(
                application,
                repositoryFactory.repositorySettings,
                repositoryFactory.repositoryBag,
                repositoryFactory.repositoryRoll
            )
        }
    }

    private fun installLogging() {
        if (Timber.treeCount == 0)
            Timber.plant(UtilityLoggingLineNumberTree())
    }
}
