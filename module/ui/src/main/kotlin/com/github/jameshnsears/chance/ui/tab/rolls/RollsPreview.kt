package com.github.jameshnsears.chance.ui.tab.rolls

import android.annotation.SuppressLint
import android.app.Application
import android.content.SharedPreferences
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.github.jameshnsears.chance.ui.zoom.rolls.ZoomRollsAndroidViewModel
import com.github.jameshnsears.chance.ui.zoom.rolls.ZoomRollsAndroidViewModelFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

@SuppressLint("ViewModelConstructorInComposable")
@Preview
@Composable
fun TabRollPreview() {
    UtilityFeature.enabled = setOf(
        Flag.REPO_PROTOCOL_BUFFER_TEST_DOUBLE,
    )

    val context = LocalContext.current
    val application = object : Application() {
        override fun getSharedPreferences(name: String?, mode: Int): SharedPreferences {
            return context.getSharedPreferences(name, mode)
        }
    }

    val repositoryFactory = remember {
        RepositoryFactory().apply {
            runBlocking {
                resetStorage()
                val diceBag = repositoryBag.fetch().first()
                if (diceBag.size >= 2) {
                    diceBag[0].selected = true
                    diceBag[1].selected = true
                    repositoryBag.store(diceBag)
                }

                val groupHistory = repositoryGroup.fetch().first().toMutableList()
                if (groupHistory.isNotEmpty()) {
                    groupHistory[0] = groupHistory[0].copy(selected = true)
                    repositoryGroup.store(groupHistory)
                }

                val settings = repositorySettings.fetch().first()
                settings.groupTitle = true
                repositorySettings.store(settings)
            }
        }
    }

    val rollsAndroidViewModel: RollsAndroidViewModel = viewModel(
        factory = RollsAndroidViewModelFactory(
            application,
            repositoryFactory.repositorySettings,
            repositoryFactory.repositoryBag,
            repositoryFactory.repositoryRoll,
            repositoryFactory.repositoryGroup
        )
    )

    val zoomRollsAndroidViewModel: ZoomRollsAndroidViewModel = viewModel(
        factory = ZoomRollsAndroidViewModelFactory(
            application,
            repositoryFactory.repositorySettings,
            repositoryFactory.repositoryBag,
            repositoryFactory.repositoryRoll,
            repositoryFactory.repositoryGroup
        )
    )

    ChanceTheme {
        Surface(
            color = MaterialTheme.colorScheme.surfaceContainerLow,
        ) {
            TabRollLayout(
                rollsAndroidViewModel,
                zoomRollsAndroidViewModel
            )
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@OptIn(ExperimentalMaterial3Api::class)
@Preview(widthDp = 700, heightDp = 250)
@Composable
fun TabRollBottomSheetPreview() {
    UtilityFeature.enabled = setOf(
        Flag.REPO_PROTOCOL_BUFFER_TEST_DOUBLE,
    )

    val context = LocalContext.current
    val application = object : Application() {
        override fun getSharedPreferences(name: String?, mode: Int): SharedPreferences {
            return context.getSharedPreferences(name, mode)
        }
    }

    val repositoryFactory = remember {
        RepositoryFactory().apply {
            runBlocking {
                resetStorage()
                val diceBag = repositoryBag.fetch().first()
                if (diceBag.size >= 2) {
                    diceBag[0].selected = true
                    diceBag[1].selected = true
                    repositoryBag.store(diceBag)
                }

                val groupHistory = repositoryGroup.fetch().first().map { it.copy(selected = true) }
                repositoryGroup.store(groupHistory)

                val settings = repositorySettings.fetch().first()
                settings.groupTitle = true
                repositorySettings.store(settings)
            }
        }
    }

    val rollsAndroidViewModel: RollsAndroidViewModel = viewModel(
        factory = RollsAndroidViewModelFactory(
            application,
            repositoryFactory.repositorySettings,
            repositoryFactory.repositoryBag,
            repositoryFactory.repositoryRoll,
            repositoryFactory.repositoryGroup
        )
    )

    ChanceTheme {
        Surface(
            color = MaterialTheme.colorScheme.surfaceContainerLow,
        ) {
            TabRollBottomSheetLayout(rollsAndroidViewModel)
        }
    }
}
