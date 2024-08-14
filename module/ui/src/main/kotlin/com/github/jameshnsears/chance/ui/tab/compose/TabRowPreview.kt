package com.github.jameshnsears.chance.ui.tab.compose

import android.app.Application
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.github.jameshnsears.chance.data.repository.RepositoryFactory
import com.github.jameshnsears.chance.ui.tab.bag.TabBagAndroidViewModel
import com.github.jameshnsears.chance.ui.tab.roll.TabRollAndroidViewModel
import com.github.jameshnsears.chance.ui.theme.ChanceTheme
import com.github.jameshnsears.chance.ui.utility.preview.UtilityPreview
import com.github.jameshnsears.chance.ui.zoom.ZoomAndroidViewModel
import com.github.jameshnsears.chance.utility.feature.UtilityFeature
import com.github.jameshnsears.chance.utility.feature.UtilityFeature.Flag
import io.mockk.mockk

@UtilityPreview
@Composable
fun TabRowPreview() {
    UtilityFeature.enabled = setOf(
        Flag.NONE,
    )

    ChanceTheme {
        Surface(
            color = MaterialTheme.colorScheme.background,
        ) {
            val repositorySettings = RepositoryFactory().repositorySettings

            val repositoryBag = RepositoryFactory().repositoryBag

            val repositoryRoll = RepositoryFactory().repositoryRoll

            TabRow(
                TabBagAndroidViewModel(
                    mockk<Application>(),
                    repositorySettings,
                    repositoryBag,
                    repositoryRoll,
                    3
                ),
                TabRollAndroidViewModel(
                    mockk<Application>(),
                    repositorySettings,
                    repositoryBag,
                    repositoryRoll
                ),
                ZoomAndroidViewModel(
                    mockk<Application>(),
                    repositorySettings,
                    repositoryBag,
                    repositoryRoll
                )
            )
        }
    }
}
