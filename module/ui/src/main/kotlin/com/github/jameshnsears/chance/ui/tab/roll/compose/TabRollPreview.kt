package com.github.jameshnsears.chance.ui.tab.roll.compose


import android.app.Application
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.github.jameshnsears.chance.data.repository.RepositoryFactory
import com.github.jameshnsears.chance.ui.tab.roll.TabRollAndroidViewModel
import com.github.jameshnsears.chance.ui.theme.ChanceTheme
import com.github.jameshnsears.chance.ui.utility.preview.UtilityPreview
import com.github.jameshnsears.chance.ui.zoom.ZoomAndroidViewModel
import com.github.jameshnsears.chance.utility.feature.UtilityFeature
import io.mockk.mockk

@UtilityPreview
@Composable
fun TabRollPreview() {
    UtilityFeature.enabled = setOf(
        UtilityFeature.Flag.NONE,
    )

    val repositorySettings = RepositoryFactory().repositorySettings

    val repositoryBag = RepositoryFactory().repositoryBag

    val repositoryRoll = RepositoryFactory().repositoryRoll

    ChanceTheme {
        Surface(
            color = MaterialTheme.colorScheme.background,
        ) {
            TabRollLayout(
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

@OptIn(ExperimentalMaterial3Api::class)
@UtilityPreview
@Composable
fun TabRollBottomSheetPreview() {
    val repositorySettings = RepositoryFactory().repositorySettings

    val repositoryBag = RepositoryFactory().repositoryBag

    val repositoryRoll = RepositoryFactory().repositoryRoll

    ChanceTheme {
        Surface(
            color = MaterialTheme.colorScheme.background,
        ) {
            TabRollBottomSheetLayout(
                TabRollAndroidViewModel(
                    mockk<Application>(),
                    repositorySettings,
                    repositoryBag,
                    repositoryRoll,
                ),
                mockk<BottomSheetScaffoldState>()
            )
        }
    }
}
