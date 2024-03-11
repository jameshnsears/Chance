package com.github.jameshnsears.chance

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.github.jameshnsears.chance.data.domain.state.Dice
import com.github.jameshnsears.chance.data.repository.bag.RepositoryBagInterface
import com.github.jameshnsears.chance.data.repository.bag.RepositoryBagTestDouble
import com.github.jameshnsears.chance.data.repository.roll.RepositoryRollInterface
import com.github.jameshnsears.chance.data.repository.roll.RepositoryRollTestDouble
import com.github.jameshnsears.chance.data.repository.settings.RepositorySettingsInterface
import com.github.jameshnsears.chance.data.repository.settings.RepositorySettingsTestDouble
import com.github.jameshnsears.chance.data.sample.bag.SampleBag
import com.github.jameshnsears.chance.data.sample.roll.SampleRollSampleBag
import com.github.jameshnsears.chance.data.sample.settings.SampleSettings
import com.github.jameshnsears.chance.ui.BuildConfig
import com.github.jameshnsears.chance.ui.tab.TabRow
import com.github.jameshnsears.chance.ui.tab.bag.TabBagAndroidViewModel
import com.github.jameshnsears.chance.ui.tab.roll.TabRollViewModel
import com.github.jameshnsears.chance.ui.theme.ChanceTheme
import com.github.jameshnsears.chance.ui.zoom.ZoomAndroidViewModel
import com.github.jameshnsears.chance.utility.logging.UtilityLoggingLineNumberTree
import kotlinx.coroutines.runBlocking
import timber.log.Timber

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        plantLoggingTree()

        setContent {
            ChanceTheme {
                Surface(
                    color = MaterialTheme.colorScheme.background,
                ) {
                    TabRow(
                        TabBagAndroidViewModel(
                            application,
                            repositorySettings(),
                            repositoryBag(),
                            repositoryRoll(),
                        ),
                        TabRollViewModel(
                            repositorySettings(),
                            repositoryBag(),
                            repositoryRoll(),
                        ),
                        ZoomAndroidViewModel(
                            application,
                            repositoryBag(),
                            repositoryRoll()
                        ),
                    )
                }
            }
        }
    }

    private fun repositorySettings(): RepositorySettingsInterface {
        val repositorySettings = RepositorySettingsTestDouble.getInstance()
        runBlocking {
            repositorySettings.store(SampleSettings.settings)
        }

        return repositorySettings
    }

    private fun repositoryBag(): RepositoryBagInterface {
        val repositoryBag = RepositoryBagTestDouble.getInstance()

        val allDice = mutableListOf<Dice>()
        allDice.addAll(SampleBag.allDice)

        runBlocking {
            repositoryBag.store(
                allDice,
            )
        }

        return repositoryBag
    }

    private fun repositoryRoll(): RepositoryRollInterface {
        val repositoryRoll = RepositoryRollTestDouble.getInstance()
        runBlocking {
            repositoryRoll.store(SampleRollSampleBag.rollHistory)
        }

        return repositoryRoll
    }

    private fun plantLoggingTree() {
        if (Timber.treeCount == 0 && BuildConfig.DEBUG) {
            Timber.plant(UtilityLoggingLineNumberTree())
        }
    }
}
