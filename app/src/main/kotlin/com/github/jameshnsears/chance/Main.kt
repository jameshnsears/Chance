package com.github.jameshnsears.chance

import android.app.Application
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.jameshnsears.chance.common.ui.theme.ChanceTheme
import com.github.jameshnsears.chance.common.utility.feature.UtilityFeature
import com.github.jameshnsears.chance.data.repo.api.bag.RepositoryBagInterface
import com.github.jameshnsears.chance.data.repo.api.roll.RepositoryRollInterface
import com.github.jameshnsears.chance.data.repo.api.settings.RepositorySettingsInterface
import com.github.jameshnsears.chance.ui.tab.TabRow
import com.github.jameshnsears.chance.ui.tab.bag.BagAndroidViewModelFactory
import com.github.jameshnsears.chance.ui.tab.bag.TabBagAndroidViewModel
import com.github.jameshnsears.chance.ui.tab.roll.RollAndroidViewModel
import com.github.jameshnsears.chance.ui.tab.roll.RollAndroidViewModelFactory
import com.github.jameshnsears.chance.ui.zoom.bag.ZoomBagAndroidViewModel
import com.github.jameshnsears.chance.ui.zoom.bag.ZoomBagAndroidViewModelFactory
import com.github.jameshnsears.chance.ui.zoom.roll.ZoomRollAndroidViewModel
import com.github.jameshnsears.chance.ui.zoom.roll.ZoomRollAndroidViewModelFactory

@Composable
fun MainComposable(
    application: Application,
    repositorySettings: RepositorySettingsInterface,
    repositoryBag: RepositoryBagInterface,
    repositoryRoll: RepositoryRollInterface,
    resizeInitialValue: Int
) {
    val tabBagViewModel: TabBagAndroidViewModel = viewModel(
        factory = BagAndroidViewModelFactory(
            application,
            repositorySettings,
            repositoryBag,
            repositoryRoll,
            resizeInitialValue
        )
    )

    val tabRollViewModel: RollAndroidViewModel = viewModel(
        factory = RollAndroidViewModelFactory(
            application,
            repositorySettings,
            repositoryBag,
            repositoryRoll
        )
    )

    val zoomBagViewModel: ZoomBagAndroidViewModel = viewModel(
        factory = ZoomBagAndroidViewModelFactory(
            application,
            repositorySettings,
            repositoryBag,
            repositoryRoll
        )
    )

    val zoomRollViewModel: ZoomRollAndroidViewModel = viewModel(
        factory = ZoomRollAndroidViewModelFactory(
            application,
            repositorySettings,
            repositoryBag,
            repositoryRoll
        )
    )

    ChanceTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .consumeWindowInsets(innerPadding),
                color = MaterialTheme.colorScheme.background
            ) {
                if (UtilityFeature.isEnabled(UtilityFeature.Flag.UI_SHOW_CRASHLYTICS_BUTTON)) {
                    Button(
                        onClick = {
                            throw RuntimeException("Test Crash")
                        }) {
                        Text("CRASHLYTICS")
                    }
                } else {
                    TabRow(
                        tabBagViewModel,
                        tabRollViewModel,
                        zoomBagViewModel,
                        zoomRollViewModel
                    )
                }
            }
        }
    }
}
