package com.github.jameshnsears.chance.ui.dialog.settings.compose

import android.annotation.SuppressLint
import android.app.Application
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.github.jameshnsears.chance.data.repository.RepositoryFactory
import com.github.jameshnsears.chance.ui.tab.roll.TabRollAndroidViewModel
import com.github.jameshnsears.chance.ui.theme.ChanceTheme
import com.github.jameshnsears.chance.ui.utility.preview.UtilityPreview
import com.github.jameshnsears.chance.utility.feature.UtilityFeature
import com.github.jameshnsears.chance.utility.feature.UtilityFeature.Flag
import io.mockk.mockk

@SuppressLint("UnrememberedMutableState")
@UtilityPreview
@Composable
fun DialogSettingsPreview() {
    UtilityFeature.enabled = setOf(
        Flag.REPO_PROTOCOL_BUFFER_TEST_DOUBLE,
    )

    val repositoryFactory = RepositoryFactory()

    ChanceTheme {
        Surface(
            color = MaterialTheme.colorScheme.background,
        ) {
            DialogSettingsLayout(
                TabRollAndroidViewModel(
                    mockk<Application>(),
                    repositoryFactory.repositorySettings,
                    repositoryFactory.repositoryBag,
                    repositoryFactory.repositoryRoll
                )
            )
        }
    }
}
