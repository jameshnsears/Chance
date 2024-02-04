package com.github.jameshnsears.chance

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.github.jameshnsears.chance.data.repository.bag.BagDemoSampleData
import com.github.jameshnsears.chance.data.repository.bag.BagSampleData
import com.github.jameshnsears.chance.data.repository.bag.DiceBagRepositoryInterface
import com.github.jameshnsears.chance.data.repository.bag.DiceBagRepositoryTestDouble
import com.github.jameshnsears.chance.data.repository.roll.RollRepositoryInterface
import com.github.jameshnsears.chance.data.repository.roll.RollRepositoryTestDouble
import com.github.jameshnsears.chance.data.repository.roll.RollSampleData
import com.github.jameshnsears.chance.data.repository.settings.SettingsRepositoryInterface
import com.github.jameshnsears.chance.data.repository.settings.SettingsRepositoryTestDouble
import com.github.jameshnsears.chance.data.repository.settings.SettingsSampleData
import com.github.jameshnsears.chance.ui.tab.TabRowChance
import com.github.jameshnsears.chance.ui.theme.ChanceTheme
import com.github.jameshnsears.chance.utils.logging.LoggingLineNumberTree
import kotlinx.coroutines.runBlocking
import timber.log.Timber


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        initLogging()

        setContent {
            ChanceTheme {
                Surface(
                    color = MaterialTheme.colorScheme.background,
                ) {
                    TabRowChance(
                        settingsRepositoryTestDouble(),
                        bagRepositoryTestDouble(),
                        rollRepositoryTestDouble(),
                    )
                }
            }
        }
    }

    private fun settingsRepositoryTestDouble(): SettingsRepositoryInterface {
        val settingsRepository = SettingsRepositoryTestDouble.getInstance()
        runBlocking {
            settingsRepository.store(SettingsSampleData.settings)
        }

        return settingsRepository
    }

    private fun bagRepositoryTestDouble(): DiceBagRepositoryInterface {
        val bagRepository = DiceBagRepositoryTestDouble.getInstance()

        runBlocking {
            bagRepository.store(
                BagDemoSampleData.allDice + BagSampleData.allDice,
            )
        }

        return bagRepository
    }

    private fun rollRepositoryTestDouble(): RollRepositoryInterface {
        val rollRepository = RollRepositoryTestDouble.getInstance()
        runBlocking {
            rollRepository.store(RollSampleData.rollHistory)
        }

        return rollRepository
    }

    private fun initLogging() {
        if (Timber.treeCount == 0) {
            Timber.plant(LoggingLineNumberTree())
        }
    }
}
