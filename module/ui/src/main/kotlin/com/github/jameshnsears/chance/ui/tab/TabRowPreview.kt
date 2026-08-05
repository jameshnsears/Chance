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
import com.github.jameshnsears.chance.ui.navigation.ChanceNavKey
import com.github.jameshnsears.chance.ui.navigation.rememberNavigationState
import com.github.jameshnsears.chance.ui.tab.rolls.RollsAndroidViewModel
import com.github.jameshnsears.chance.ui.tab.rolls.RollsCoreHelper
import com.github.jameshnsears.chance.ui.tab.rolls.RollsSelectionHelper
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

    val repositoryFactory = RepositoryFactory()
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

    val navigationState = rememberNavigationState(
        startRoute = ChanceNavKey.SetupDice,
        topLevelRoutes = setOf(ChanceNavKey.SetupDice, ChanceNavKey.SetupGroups, ChanceNavKey.Roll)
    )

    ChanceTheme {
        Surface(
            color = MaterialTheme.colorScheme.surfaceContainerLow,
        ) {
            TabRow(
                navigationState,
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
                    repositoryGroup,
                    RollsSelectionHelper(repositoryBag, repositoryGroup),
                    RollsCoreHelper(repositoryRoll)
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
