package com.github.jameshnsears.chance

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.github.jameshnsears.chance.data.bag.demo.BagDemoData
import com.github.jameshnsears.chance.data.bag.repository.BagRepositoryTestDouble
import com.github.jameshnsears.chance.data.roll.repository.RollRepositoryTestDouble
import com.github.jameshnsears.chance.data.roll.sample.RollSampleData
import com.github.jameshnsears.chance.data.settings.repository.SettingsRepositoryTestDouble
import com.github.jameshnsears.chance.ui.tab.TabRowChance
import com.github.jameshnsears.chance.ui.theme.ChanceTheme
import com.github.jameshnsears.chance.utils.logging.LoggingLineNumberTree
import timber.log.Timber


class MainActivity : ComponentActivity() {
    @SuppressLint("UnrememberedMutableState")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        initLogging()

        val settingsRepository = SettingsRepositoryTestDouble

        val bagRepository = BagRepositoryTestDouble
        bagRepository.store(
            listOf(
                BagDemoData.diceHeadsTails
            )
        )

        val rollRepository = RollRepositoryTestDouble
        rollRepository.store(RollSampleData.rollHistory_roll1Sequence1)

        setContent {
            ChanceTheme {
                Surface(
                    color = MaterialTheme.colorScheme.background
                ) {
                    TabRowChance(
                        settingsRepository,
                        bagRepository,
                        rollRepository
                    )
                }
            }
        }
    }

    private fun initLogging() {
        if (Timber.treeCount == 0) {
            Timber.plant(LoggingLineNumberTree())
        }
    }
}
