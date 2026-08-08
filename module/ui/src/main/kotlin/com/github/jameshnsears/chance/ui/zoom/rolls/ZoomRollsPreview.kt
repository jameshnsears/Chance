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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.jameshnsears.chance.common.ui.theme.ChanceTheme
import com.github.jameshnsears.chance.common.utility.feature.UtilityFeature
import com.github.jameshnsears.chance.common.utility.feature.UtilityFeature.Flag
import com.github.jameshnsears.chance.data.common.repo.RepositoryFactory
import com.github.jameshnsears.chance.data.domain.core.settings.SettingsDataInterface
import com.github.jameshnsears.chance.ui.tab.rolls.RollsAndroidViewModel
import com.github.jameshnsears.chance.ui.tab.rolls.RollsAndroidViewModelFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

@SuppressLint("ViewModelConstructorInComposable")
@Composable
private fun ZoomRollPreviewHelper(
    settingsAction: suspend (SettingsDataInterface) -> Unit = {},
) {
    UtilityFeature.enabled = setOf(
        Flag.REPO_PROTOCOL_BUFFER_TEST_DOUBLE,
    )

    val repositoryFactory = remember {
        RepositoryFactory().apply {
            runBlocking {
                resetStorage()
                val settings = repositorySettings.fetch().first()
                settings.groupTitle = true
                settingsAction(settings)
                repositorySettings.store(settings)
            }
        }
    }

    val context = LocalContext.current
    val application = object : Application() {
        override fun getSharedPreferences(name: String?, mode: Int): SharedPreferences {
            return context.getSharedPreferences(name, mode)
        }
    }

    val rollsAndroidViewModel: RollsAndroidViewModel = viewModel(
        factory = RollsAndroidViewModelFactory(
            application,
            repositoryFactory.repositorySettings,
            repositoryFactory.repositoryBag,
            repositoryFactory.repositoryRoll,
            repositoryFactory.repositoryGroup,
        ),
    )

    val zoomRollsAndroidViewModel: ZoomRollsAndroidViewModel = viewModel(
        factory = ZoomRollsAndroidViewModelFactory(
            application,
            repositoryFactory.repositorySettings,
            repositoryFactory.repositoryBag,
            repositoryFactory.repositoryRoll,
            repositoryFactory.repositoryGroup,
        ),
    )

    ChanceTheme {
        Surface(
            color = MaterialTheme.colorScheme.surfaceContainerLow,
        ) {
            ZoomRoll(
                rollsAndroidViewModel,
                zoomRollsAndroidViewModel,
            )
        }
    }
}

@Preview
@Composable
fun ZoomRollHorizontalPreview() {
    ZoomRollPreviewHelper { settings ->
        settings.layout = true // This will now correctly show ZoomRollHistoryHorizontal
    }
}

@Preview
@Composable
fun ZoomRollVerticalPreview() {
    ZoomRollPreviewHelper { settings ->
        settings.layout = false // This will now correctly show ZoomRollVertical
    }
}
