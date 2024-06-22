package com.github.jameshnsears.chance

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.jameshnsears.chance.data.domain.state.Dice
import com.github.jameshnsears.chance.data.repository.bag.RepositoryBagInterface
import com.github.jameshnsears.chance.data.repository.bag.mock.RepositoryBagTestDouble
import com.github.jameshnsears.chance.data.repository.roll.RepositoryRollInterface
import com.github.jameshnsears.chance.data.repository.roll.mock.RepositoryRollTestDouble
import com.github.jameshnsears.chance.data.repository.settings.RepositorySettingsInterface
import com.github.jameshnsears.chance.data.repository.settings.mock.RepositorySettingsTestDouble
import com.github.jameshnsears.chance.data.sample.bag.SampleBagTestData
import com.github.jameshnsears.chance.data.sample.roll.SampleRollTestData
import com.github.jameshnsears.chance.data.sample.settings.SampleSettingsStartup
import com.github.jameshnsears.chance.ui.tab.bag.TabBagAndroidViewModel
import com.github.jameshnsears.chance.ui.tab.compose.TabRow
import com.github.jameshnsears.chance.ui.tab.roll.TabRollAndroidViewModel
import com.github.jameshnsears.chance.ui.theme.ChanceTheme
import com.github.jameshnsears.chance.ui.zoom.ZoomAndroidViewModel
import com.github.jameshnsears.chance.utility.logging.UtilityLoggingLineNumberTree
import kotlinx.coroutines.runBlocking
import timber.log.Timber

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()
        plantLoggingTree()

        val sampleBagTestData = SampleBagTestData()
        val sampleRollTestData = SampleRollTestData(sampleBagTestData).apply {
            rollHistory.clear()
        }

        setContent {
            ChanceTheme {
                // Consider using a Scaffold to provide a standard app structure
                Scaffold(
                    topBar = { /* Add an AppBar here if needed */ }
                ) { paddingValues -> // Use paddingValues for content padding
                    Surface(
                        modifier = Modifier.padding(paddingValues), // Apply padding
                        color = MaterialTheme.colorScheme.background
                    ) {
                        // Use more descriptive names for ViewModels
                        TabRow(
                            tabBagAndroidViewModel(
                                sampleBagTestData,
                                sampleRollTestData
                            ),
                            tabRollViewModel(
                                sampleBagTestData,
                                sampleRollTestData
                            ),
                            zoomAndroidViewModel(
                                sampleBagTestData,
                                sampleRollTestData
                            )
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun zoomAndroidViewModel(
        sampleBagTestData: SampleBagTestData,
        sampleRollTestData: SampleRollTestData
    ): ZoomAndroidViewModel {
        // Use the 'viewModel()' function with a more concise syntax
        return viewModel {
            ZoomAndroidViewModel(
                application, // Assuming 'application' is available in this scope
                repositoryBag(sampleBagTestData),
                repositoryRoll(sampleRollTestData)
            )
        }
    }

    @Composable
    private fun tabRollViewModel(
        sampleBagTestData: SampleBagTestData,
        sampleRollTestData: SampleRollTestData
    ): TabRollAndroidViewModel {
        return viewModel {
            TabRollAndroidViewModel(
                application,
                repositorySettings(),
                repositoryBag(sampleBagTestData),
                repositoryRoll(sampleRollTestData),
            )
        }
    }

    @Composable
    private fun tabBagAndroidViewModel(
        sampleBagTestData: SampleBagTestData,
        sampleRollTestData: SampleRollTestData
    ): TabBagAndroidViewModel {
        return viewModel<TabBagAndroidViewModel>(
            factory = object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    @Suppress("UNCHECKED_CAST")
                    return TabBagAndroidViewModel(
                        application,
                        repositorySettings(),
                        repositoryBag(sampleBagTestData),
                        repositoryRoll(sampleRollTestData),
                    ) as T
                }
            }
        )
    }

    private fun repositorySettings(): RepositorySettingsInterface {
        val repositorySettings = RepositorySettingsTestDouble.getInstance()
        runBlocking {
            repositorySettings.store(SampleSettingsStartup().settings)
        }

        return repositorySettings
    }

    private fun repositoryBag(sampleBagTestData: SampleBagTestData): RepositoryBagInterface {
        val repositoryBag = RepositoryBagTestDouble.getInstance()

        val allDice = mutableListOf<Dice>()

//        allDice.addAll(SampleBagStartup.allDice)
        allDice.addAll(sampleBagTestData.allDice)

        runBlocking {
            repositoryBag.store(
                allDice,
            )
        }

        return repositoryBag
    }

    private fun repositoryRoll(sampleRollTestData: SampleRollTestData): RepositoryRollInterface {
        val repositoryRoll = RepositoryRollTestDouble.getInstance()
        runBlocking {
            repositoryRoll.store(sampleRollTestData.rollHistory)
        }

        return repositoryRoll
    }

    private fun plantLoggingTree() {
        if (Timber.treeCount == 0) {
            Timber.plant(UtilityLoggingLineNumberTree())
        }
    }
}
