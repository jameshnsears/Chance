package com.github.jameshnsears.chance.compose

import android.app.Application
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.github.jameshnsears.chance.data.repository.bag.RepositoryBagInterface
import com.github.jameshnsears.chance.data.repository.roll.RepositoryRollInterface
import com.github.jameshnsears.chance.data.repository.settings.RepositorySettingsInterface
import com.github.jameshnsears.chance.ui.tab.bag.TabBagAndroidViewModel
import com.github.jameshnsears.chance.ui.tab.compose.TabRow
import com.github.jameshnsears.chance.ui.tab.roll.TabRollAndroidViewModel
import com.github.jameshnsears.chance.ui.theme.ChanceTheme
import com.github.jameshnsears.chance.ui.zoom.ZoomAndroidViewModel
import com.github.jameshnsears.chance.utility.feature.UtilityFeature

@Composable
fun MainActivityComposable(
    application: Application,
    repositorySettings: RepositorySettingsInterface,
    repositoryBag: RepositoryBagInterface,
    repositoryRoll: RepositoryRollInterface
) {
    ChanceTheme {
        Scaffold { paddingValues ->
            Surface(
                modifier = Modifier.padding(paddingValues),
                color = MaterialTheme.colorScheme.background
            ) {
                if (UtilityFeature.isEnabled(UtilityFeature.Flag.CRASHLYTICS)) {
                    Button(onClick = {
                        throw RuntimeException("Test Crash")
                    }) {
                        Text("CRASHLYTICS")
                    }
                } else {
                    TabRow(
                        TabBagAndroidViewModel(
                            application,
                            repositorySettings,
                            repositoryBag,
                            repositoryRoll,
                        ), TabRollAndroidViewModel(
                            application,
                            repositorySettings,
                            repositoryBag,
                            repositoryRoll,
                        ), ZoomAndroidViewModel(
                            application,
                            repositorySettings,
                            repositoryBag,
                            repositoryRoll,
                        )
                    )
                }
            }
        }
    }
}
