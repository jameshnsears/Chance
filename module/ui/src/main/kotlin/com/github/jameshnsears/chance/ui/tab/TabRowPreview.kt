package com.github.jameshnsears.chance.ui.tab

import android.annotation.SuppressLint
import android.app.Application
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.github.jameshnsears.chance.common.ui.theme.ChanceTheme
import com.github.jameshnsears.chance.common.utility.UtilityPreview
import com.github.jameshnsears.chance.common.utility.feature.UtilityFeature
import com.github.jameshnsears.chance.common.utility.feature.UtilityFeature.Flag
import com.github.jameshnsears.chance.data.common.repository.RepositoryFactory
import com.github.jameshnsears.chance.ui.tab.bag.TabBagAndroidViewModel
import com.github.jameshnsears.chance.ui.tab.roll.RollAndroidViewModel
import com.github.jameshnsears.chance.ui.zoom.bag.ZoomBagAndroidViewModel
import com.github.jameshnsears.chance.ui.zoom.roll.ZoomRollAndroidViewModel
import io.mockk.mockk

@SuppressLint("ViewModelConstructorInComposable")
@UtilityPreview
@Composable
fun TabRowPreview() {
    UtilityFeature.enabled = setOf(
        Flag.REPO_PROTOCOL_BUFFER_TEST_DOUBLE,
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
                RollAndroidViewModel(
                    mockk<Application>(),
                    repositorySettings,
                    repositoryBag,
                    repositoryRoll
                ),
                ZoomBagAndroidViewModel(
                    mockk<Application>(),
                    repositorySettings,
                    repositoryBag,
                    repositoryRoll
                ),
                ZoomRollAndroidViewModel(
                    mockk<Application>(),
                    repositorySettings,
                    repositoryBag,
                    repositoryRoll
                )
            )
        }
    }
}
