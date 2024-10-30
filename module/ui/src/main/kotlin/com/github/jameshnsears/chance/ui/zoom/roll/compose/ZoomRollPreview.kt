package com.github.jameshnsears.chance.ui.zoom.roll.compose

import android.app.Application
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.github.jameshnsears.chance.data.repository.RepositoryFactory
import com.github.jameshnsears.chance.ui.tab.roll.TabRollAndroidViewModel
import com.github.jameshnsears.chance.ui.theme.ChanceTheme
import com.github.jameshnsears.chance.ui.utility.preview.UtilityPreview
import com.github.jameshnsears.chance.ui.zoom.roll.ZoomRollAndroidViewModel
import com.github.jameshnsears.chance.utility.feature.UtilityFeature
import com.github.jameshnsears.chance.utility.feature.UtilityFeature.Flag
import io.mockk.mockk


@UtilityPreview
@Composable
fun ZoomRollPreview() {
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
            ZoomRoll(
                TabRollAndroidViewModel(
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
                ),
            )
        }
    }
}
