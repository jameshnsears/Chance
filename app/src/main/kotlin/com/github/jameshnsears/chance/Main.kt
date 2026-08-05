package com.github.jameshnsears.chance

import android.app.Application
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.jameshnsears.chance.common.ui.theme.ChanceTheme
import com.github.jameshnsears.chance.common.utility.feature.UtilityFeature
import com.github.jameshnsears.chance.data.repo.api.bag.RepositoryBagInterface
import com.github.jameshnsears.chance.data.repo.api.group.RepositoryGroupInterface
import com.github.jameshnsears.chance.data.repo.api.roll.RepositoryRollInterface
import com.github.jameshnsears.chance.data.repo.api.settings.RepositorySettingsInterface
import com.github.jameshnsears.chance.ui.navigation.ChanceNavKey
import com.github.jameshnsears.chance.ui.navigation.rememberNavigationState
import com.github.jameshnsears.chance.ui.tab.TabRow
import com.github.jameshnsears.chance.ui.tab.rolls.RollsAndroidViewModel
import com.github.jameshnsears.chance.ui.tab.rolls.RollsAndroidViewModelFactory
import com.github.jameshnsears.chance.ui.tab.setup.dice.DiceAndroidViewModel
import com.github.jameshnsears.chance.ui.tab.setup.dice.DiceAndroidViewModelFactory
import com.github.jameshnsears.chance.ui.tab.setup.groups.GroupsAndroidViewModel
import com.github.jameshnsears.chance.ui.tab.setup.groups.GroupsAndroidViewModelFactory
import com.github.jameshnsears.chance.ui.zoom.rolls.ZoomRollsAndroidViewModel
import com.github.jameshnsears.chance.ui.zoom.rolls.ZoomRollsAndroidViewModelFactory
import com.github.jameshnsears.chance.ui.zoom.setup.dice.ZoomDiceAndroidViewModel
import com.github.jameshnsears.chance.ui.zoom.setup.dice.ZoomDiceAndroidViewModelFactory

@Composable
fun MainComposable(
    application: Application,
    repositorySettings: RepositorySettingsInterface,
    repositoryBag: RepositoryBagInterface,
    repositoryRoll: RepositoryRollInterface,
    repositoryGroup: RepositoryGroupInterface,
    resizeInitialValue: Float
) {
    val tabBagViewModel: DiceAndroidViewModel = viewModel(
        factory = DiceAndroidViewModelFactory(
            application,
            repositorySettings,
            repositoryBag,
            repositoryRoll,
            repositoryGroup,
            resizeInitialValue
        )
    )

    val groupsViewModel: GroupsAndroidViewModel = viewModel(
        factory = GroupsAndroidViewModelFactory(
            application,
            repositoryBag,
            repositoryGroup,
            repositoryRoll
        )
    )

    val tabRollViewModel: RollsAndroidViewModel = viewModel(
        factory = RollsAndroidViewModelFactory(
            application,
            repositorySettings,
            repositoryBag,
            repositoryRoll,
            repositoryGroup
        )
    )

    val zoomBagViewModel: ZoomDiceAndroidViewModel = viewModel(
        factory = ZoomDiceAndroidViewModelFactory(
            application,
            repositorySettings,
            repositoryBag,
            repositoryRoll
        )
    )

    val zoomRollViewModel: ZoomRollsAndroidViewModel = viewModel(
        factory = ZoomRollsAndroidViewModelFactory(
            application,
            repositorySettings,
            repositoryBag,
            repositoryRoll,
            repositoryGroup
        )
    )

    val navigationState = rememberNavigationState(
        startRoute = ChanceNavKey.SetupDice,
        topLevelRoutes = setOf(ChanceNavKey.SetupDice, ChanceNavKey.SetupGroups, ChanceNavKey.Roll)
    )

    ChanceTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        ) { innerPadding ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .consumeWindowInsets(innerPadding),
                color = MaterialTheme.colorScheme.surfaceContainerLow
            ) {
                if (UtilityFeature.isEnabled(UtilityFeature.Flag.UI_SHOW_CRASHLYTICS_BUTTON)) {
                    Button(
                        onClick = {
                            throw RuntimeException("Test Crash")
                        }) {
                        Text("CRASHLYTICS")
                    }
                } else {
                    TabRow(
                        navigationState,
                        tabBagViewModel,
                        groupsViewModel,
                        tabRollViewModel,
                        zoomBagViewModel,
                        zoomRollViewModel
                    )
                }
            }
        }
    }
}
