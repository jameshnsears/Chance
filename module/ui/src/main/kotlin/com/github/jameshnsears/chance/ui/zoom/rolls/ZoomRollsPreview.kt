package com.github.jameshnsears.chance.ui.zoom.rolls

import android.annotation.SuppressLint
import android.app.Application
import android.content.SharedPreferences
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.github.jameshnsears.chance.common.ui.theme.ChanceTheme
import com.github.jameshnsears.chance.common.utility.feature.UtilityFeature
import com.github.jameshnsears.chance.common.utility.feature.UtilityFeature.Flag
import com.github.jameshnsears.chance.data.common.repo.RepositoryFactory
import com.github.jameshnsears.chance.ui.tab.rolls.RollsAndroidViewModel

@SuppressLint("ViewModelConstructorInComposable")
@Preview
@Composable
fun ZoomRollPreview() {
    UtilityFeature.enabled = setOf(
        Flag.REPO_PROTOCOL_BUFFER_TEST_DOUBLE,
    )

    val repositorySettings = RepositoryFactory().repositorySettings

    val repositoryBag = RepositoryFactory().repositoryBag

    val repositoryRoll = RepositoryFactory().repositoryRoll

    val repositoryGroup = RepositoryFactory().repositoryGroup

    val context = LocalContext.current
    val application = object : Application() {
        override fun getSharedPreferences(name: String?, mode: Int): SharedPreferences {
            return context.getSharedPreferences(name, mode)
        }
    }

    ChanceTheme {
        Surface(
            color = MaterialTheme.colorScheme.surfaceContainerLow,
        ) {
            ZoomRoll(
                RollsAndroidViewModel(
                    application,
                    repositorySettings,
                    repositoryBag,
                    repositoryRoll,
                    repositoryGroup,
                ),
                ZoomRollsAndroidViewModel(
                    application,
                    repositorySettings,
                    repositoryBag,
                    repositoryRoll,
                    repositoryGroup,
                ),
            )
        }
    }
}
