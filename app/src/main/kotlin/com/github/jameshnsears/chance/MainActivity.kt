package com.github.jameshnsears.chance

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.github.jameshnsears.chance.compose.MainActivityComposable
import com.github.jameshnsears.chance.data.utility.UtilityDataHelper

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        val utilityDataHelper = UtilityDataHelper(application)

        setContent {
            MainActivityComposable(
                application,
                utilityDataHelper.repositorySettings,
                utilityDataHelper.repositoryBag,
                utilityDataHelper.repositoryRoll
            )
        }
    }
}
