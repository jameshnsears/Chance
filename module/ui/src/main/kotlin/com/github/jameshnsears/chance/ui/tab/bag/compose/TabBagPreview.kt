package com.github.jameshnsears.chance.ui.tab.bag.compose

import android.app.Application
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.github.jameshnsears.chance.data.repository.RepositoryFactory
import com.github.jameshnsears.chance.ui.tab.bag.TabBagAndroidViewModel
import com.github.jameshnsears.chance.ui.theme.ChanceTheme
import com.github.jameshnsears.chance.ui.utility.preview.UtilityPreview
import com.github.jameshnsears.chance.ui.zoom.bag.ZoomBagAndroidViewModel
import com.github.jameshnsears.chance.utility.feature.UtilityFeature
import com.github.jameshnsears.chance.utility.feature.UtilityFeature.Flag
import io.mockk.mockk

@UtilityPreview
@Composable
fun TabBagPreview() {
    UtilityFeature.enabled = setOf(
        Flag.REPO_PROTOCOL_BUFFER_TEST_DOUBLE,
    )

    val repositorySettings = RepositoryFactory().repositorySettings

    val repositoryBag = RepositoryFactory().repositoryBag

    val repositoryRoll = RepositoryFactory().repositoryRoll

    ChanceTheme {
        Surface(
            color = MaterialTheme.colorScheme.background,
        ) {
            TabBagLayout(
                TabBagAndroidViewModel(
                    mockk<Application>(),
                    repositorySettings,
                    repositoryBag,
                    repositoryRoll,
                    3
                ),
                ZoomBagAndroidViewModel(
                    mockk<Application>(),
                    repositorySettings,
                    repositoryBag,
                    repositoryRoll
                )
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@UtilityPreview
@Composable
fun TabBagBottomSheetPreview() {
    UtilityFeature.enabled = setOf(
        Flag.REPO_PROTOCOL_BUFFER_TEST_DOUBLE,
    )

    val repositorySettings = RepositoryFactory().repositorySettings

    val repositoryBag = RepositoryFactory().repositoryBag

    val repositoryRoll = RepositoryFactory().repositoryRoll

    ChanceTheme {
        Surface(
            color = MaterialTheme.colorScheme.background,
        ) {
            TabBagBottomSheetLayout(
                mockk<BottomSheetScaffoldState>(),
                TabBagAndroidViewModel(
                    mockk<Application>(),
                    repositorySettings,
                    repositoryBag,
                    repositoryRoll,
                    3
                ),
                ZoomBagAndroidViewModel(
                    mockk<Application>(),
                    repositorySettings,
                    repositoryBag,
                    repositoryRoll
                )
            )
        }
    }
}
