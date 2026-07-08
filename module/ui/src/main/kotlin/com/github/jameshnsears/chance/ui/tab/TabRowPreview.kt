package com.github.jameshnsears.chance.ui.tab

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
import com.github.jameshnsears.chance.ui.tab.setup.dice.DiceAndroidViewModel
import com.github.jameshnsears.chance.ui.tab.setup.groups.GroupsAndroidViewModel
import com.github.jameshnsears.chance.ui.zoom.rolls.ZoomRollsAndroidViewModel
import com.github.jameshnsears.chance.ui.zoom.setup.dice.ZoomDiceAndroidViewModel

@SuppressLint("ViewModelConstructorInComposable")
@Preview
@Composable
fun TabRowPreview() {
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
            TabRow(
                DiceAndroidViewModel(
                    application,
                    repositorySettings,
                    repositoryBag,
                    repositoryRoll,
                    repositoryGroup,
                    3.0f
                ),
                GroupsAndroidViewModel(
                    application,
                    repositoryBag,
                    repositoryGroup,
                    repositoryRoll
                ),
                RollsAndroidViewModel(
                    application,
                    repositorySettings,
                    repositoryBag,
                    repositoryRoll,
                    repositoryGroup
                ),
                ZoomDiceAndroidViewModel(
                    application,
                    repositorySettings,
                    repositoryBag,
                    repositoryRoll
                ),
                ZoomRollsAndroidViewModel(
                    application,
                    repositorySettings,
                    repositoryBag,
                    repositoryRoll,
                    repositoryGroup
                )
            )
        }
    }
}
