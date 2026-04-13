package com.github.jameshnsears.chance.ui.tab.bag

import android.annotation.SuppressLint
import android.app.Application
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.github.jameshnsears.chance.common.ui.theme.ChanceTheme
import com.github.jameshnsears.chance.common.utility.feature.UtilityFeature
import com.github.jameshnsears.chance.common.utility.feature.UtilityFeature.Flag
import com.github.jameshnsears.chance.data.common.repository.RepositoryFactory
import com.github.jameshnsears.chance.ui.zoom.bag.ZoomBagAndroidViewModel

@SuppressLint("ViewModelConstructorInComposable")
@Preview
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
                    Application(),
                    repositorySettings,
                    repositoryBag,
                    repositoryRoll,
                    3
                ),
                ZoomBagAndroidViewModel(
                    Application(),
                    repositorySettings,
                    repositoryBag,
                    repositoryRoll
                )
            )
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun TabBagBottomSheetPreview() {
    UtilityFeature.enabled = setOf(
        Flag.REPO_PROTOCOL_BUFFER_TEST_DOUBLE,
    )

    val repositorySettings = RepositoryFactory().repositorySettings

    val repositoryBag = RepositoryFactory().repositoryBag

    val repositoryRoll = RepositoryFactory().repositoryRoll

    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState()

    ChanceTheme {
        Surface(
            color = MaterialTheme.colorScheme.background,
        ) {
            TabBagBottomSheetLayout(
                bottomSheetScaffoldState,
                TabBagAndroidViewModel(
                    Application(),
                    repositorySettings,
                    repositoryBag,
                    repositoryRoll,
                    3
                ),
                ZoomBagAndroidViewModel(
                    Application(),
                    repositorySettings,
                    repositoryBag,
                    repositoryRoll
                )
            )
        }
    }
}
