package com.github.jameshnsears.chance.ui.tab

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.github.jameshnsears.chance.common.R
import com.github.jameshnsears.chance.ui.navigation.ChanceNavKey
import com.github.jameshnsears.chance.ui.navigation.NavigationState
import com.github.jameshnsears.chance.ui.navigation.Navigator
import com.github.jameshnsears.chance.ui.tab.rolls.RollsAndroidViewModel
import com.github.jameshnsears.chance.ui.tab.rolls.TabRoll
import com.github.jameshnsears.chance.ui.tab.setup.dice.DiceAndroidViewModel
import com.github.jameshnsears.chance.ui.tab.setup.dice.TabBagDice
import com.github.jameshnsears.chance.ui.tab.setup.groups.Groups
import com.github.jameshnsears.chance.ui.tab.setup.groups.GroupsAndroidViewModel
import com.github.jameshnsears.chance.ui.zoom.rolls.ZoomRollsAndroidViewModel
import com.github.jameshnsears.chance.ui.zoom.setup.dice.ZoomDiceAndroidViewModel

class TabRowTestTag {
    companion object {
        const val TAB_ROW = "TAB_ROW"
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabRow(
    navigationState: NavigationState,
    diceAndroidViewModel: DiceAndroidViewModel,
    groupsAndroidViewModel: GroupsAndroidViewModel,
    rollsAndroidViewModel: RollsAndroidViewModel,
    zoomDiceAndroidViewModel: ZoomDiceAndroidViewModel,
    zoomRollsAndroidViewModel: ZoomRollsAndroidViewModel
) {
    val navigator = remember { Navigator(navigationState) }

    val entryProvider = entryProvider {
        entry<ChanceNavKey.SetupDice> {
            TabBagDice(
                diceAndroidViewModel,
                zoomDiceAndroidViewModel
            )
        }
        entry<ChanceNavKey.SetupGroups> {
            Groups(
                groupsAndroidViewModel
            )
        }
        entry<ChanceNavKey.Roll> {
            TabRoll(
                rollsAndroidViewModel,
                zoomRollsAndroidViewModel
            )
        }
    }

    @Suppress("UNCHECKED_CAST")
    val decoratedEntryProvider: (ChanceNavKey) -> NavEntry<NavKey> = { key -> entryProvider(key) as NavEntry<NavKey> }

    Column {
        PrimaryTabs(navigationState, navigator)

        Column(
            modifier = Modifier.weight(1f)
        ) {
            NavDisplay(
                entries = navigationState.toDecoratedEntries(decoratedEntryProvider),
                onBack = { navigator.goBack() }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PrimaryTabs(
    navigationState: NavigationState,
    navigator: Navigator
) {
    val tabs = listOf(
        stringResource(R.string.tab_setup) to ChanceNavKey.SetupDice,
        stringResource(R.string.tab_roll) to ChanceNavKey.Roll,
    )

    val selectedTabIndex = if (navigationState.topLevelRoute == ChanceNavKey.Roll) 1 else 0

    Column {
        PrimaryTabRow(
            modifier = Modifier
                .testTag(TabRowTestTag.TAB_ROW)
                .statusBarsPadding(),
            selectedTabIndex = selectedTabIndex,
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.primary,
        ) {
            tabs.forEachIndexed { index, (tabName, route) ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = {
                        navigator.navigate(route)
                    },
                    text = {
                        Text(
                            text = tabName,
                            fontSize = 18.sp,
                        )
                    }
                )
            }
        }

        if (selectedTabIndex == 0) {
            SetupSecondaryTabs(navigationState, navigator)
        }
    }
}

@Composable
private fun SetupSecondaryTabs(
    navigationState: NavigationState,
    navigator: Navigator
) {
    val selectedSubTabIndex = if (navigationState.topLevelRoute == ChanceNavKey.SetupGroups) 1 else 0

    SecondaryTabRow(
        selectedTabIndex = selectedSubTabIndex,
        containerColor = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.primary,
    ) {
        Tab(
            selected = selectedSubTabIndex == 0,
            onClick = {
                navigator.navigate(ChanceNavKey.SetupDice)
            },
            text = {
                Text(
                    text = stringResource(R.string.tab_setup_dice),
                )
            }
        )
        Tab(
            selected = selectedSubTabIndex == 1,
            onClick = {
                navigator.navigate(ChanceNavKey.SetupGroups)
            },
            text = {
                Text(
                    text = stringResource(R.string.tab_setup_groups),
                )
            }
        )
    }
}
