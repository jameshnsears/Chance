package com.github.jameshnsears.chance.ui.zoom.rolls

import android.annotation.SuppressLint
import android.app.Application
import android.content.SharedPreferences
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.github.jameshnsears.chance.common.ui.theme.ChanceTheme
import com.github.jameshnsears.chance.common.utility.feature.UtilityFeature
import com.github.jameshnsears.chance.common.utility.feature.UtilityFeature.Flag
import com.github.jameshnsears.chance.data.common.repo.RepositoryFactory
import com.github.jameshnsears.chance.ui.tab.rolls.RollsAndroidViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

@SuppressLint("ViewModelConstructorInComposable")
@Preview
@Composable
fun ZoomRollPreview() {
    UtilityFeature.enabled = setOf(
        Flag.REPO_PROTOCOL_BUFFER_TEST_DOUBLE,
    )

    val repositoryFactory = remember {
        RepositoryFactory().apply {
            runBlocking {
                resetStorage()
                val settings = repositorySettings.fetch().first()
                settings.groupTitle = true
                repositorySettings.store(settings)
            }
        }
    }
    val repositorySettings = repositoryFactory.repositorySettings
    val repositoryBag = repositoryFactory.repositoryBag
    val repositoryRoll = repositoryFactory.repositoryRoll
    val repositoryGroup = repositoryFactory.repositoryGroup

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
