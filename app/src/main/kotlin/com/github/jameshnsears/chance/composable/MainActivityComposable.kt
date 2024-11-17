package com.github.jameshnsears.chance.composable

import android.app.Application
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.jameshnsears.chance.data.repository.bag.RepositoryBagInterface
import com.github.jameshnsears.chance.data.repository.roll.RepositoryRollInterface
import com.github.jameshnsears.chance.data.repository.settings.RepositorySettingsInterface
import com.github.jameshnsears.chance.ui.tab.bag.TabBagAndroidViewModel
import com.github.jameshnsears.chance.ui.tab.bag.TabBagAndroidViewModelFactory
import com.github.jameshnsears.chance.ui.tab.composable.TabRow
import com.github.jameshnsears.chance.ui.tab.roll.TabRollAndroidViewModel
import com.github.jameshnsears.chance.ui.tab.roll.TabRollAndroidViewModelFactory
import com.github.jameshnsears.chance.ui.theme.ChanceTheme
import com.github.jameshnsears.chance.ui.zoom.bag.ZoomBagAndroidViewModel
import com.github.jameshnsears.chance.ui.zoom.bag.ZoomBagAndroidViewModelFactory
import com.github.jameshnsears.chance.ui.zoom.roll.ZoomRollAndroidViewModel
import com.github.jameshnsears.chance.ui.zoom.roll.ZoomRollAndroidViewModelFactory
import com.github.jameshnsears.chance.utility.feature.UtilityFeature

@Composable
fun MainActivityComposable(
    application: Application,
    repositorySettings: RepositorySettingsInterface,
    repositoryBag: RepositoryBagInterface,
    repositoryRoll: RepositoryRollInterface,
    resizeInitialValue: Int
) {
    val tabBagAndroidViewModel: TabBagAndroidViewModel = viewModel(
        factory = TabBagAndroidViewModelFactory(
            application,
            repositorySettings,
            repositoryBag,
            repositoryRoll,
            resizeInitialValue
        )
    )

    val tabRollAndroidViewModel: TabRollAndroidViewModel = viewModel(
        factory = TabRollAndroidViewModelFactory(
            application,
            repositorySettings,
            repositoryBag,
            repositoryRoll
        )
    )

    val zoomBagAndroidViewModel: ZoomBagAndroidViewModel = viewModel(
        factory = ZoomBagAndroidViewModelFactory(
            application,
            repositorySettings,
            repositoryBag,
            repositoryRoll
        )
    )

    val zoomRollAndroidViewModel: ZoomRollAndroidViewModel = viewModel(
        factory = ZoomRollAndroidViewModelFactory(
            application,
            repositorySettings,
            repositoryBag,
            repositoryRoll
        )
    )

    ChanceTheme {
        Scaffold { paddingValues ->
            Surface(
                modifier = Modifier.padding(paddingValues),
                color = MaterialTheme.colorScheme.background
            ) {
                if (UtilityFeature.isEnabled(UtilityFeature.Flag.UI_SHOW_CRASHLYTICS_BUTTON)) {
                    Button(onClick = {
                        throw RuntimeException("Test Crash")
                    }) {
                        Text("CRASHLYTICS")
                    }
                } else {
                    TabRow(
                        tabBagAndroidViewModel,
                        tabRollAndroidViewModel,
                        zoomBagAndroidViewModel,
                        zoomRollAndroidViewModel
                    )
                }
            }
        }
    }
}
