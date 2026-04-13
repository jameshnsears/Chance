package com.github.jameshnsears.chance.ui.dialog.settings

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
import com.github.jameshnsears.chance.ui.tab.roll.RollAndroidViewModel
import io.mockk.mockk

@SuppressLint("ViewModelConstructorInComposable")
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
                RollAndroidViewModel(
                    mockk<Application>(),
                    repositoryFactory.repositorySettings,
                    repositoryFactory.repositoryBag,
                    repositoryFactory.repositoryRoll
                )
            )
        }
    }
}
