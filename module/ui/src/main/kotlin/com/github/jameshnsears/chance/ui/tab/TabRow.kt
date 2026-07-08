package com.github.jameshnsears.chance.ui.tab

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.github.jameshnsears.chance.common.R
import com.github.jameshnsears.chance.common.utility.UtilitySharedPreferencesHelper
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
    diceAndroidViewModel: DiceAndroidViewModel,
    groupsAndroidViewModel: GroupsAndroidViewModel,
    rollsAndroidViewModel: RollsAndroidViewModel,
    zoomDiceAndroidViewModel: ZoomDiceAndroidViewModel,
    zoomRollsAndroidViewModel: ZoomRollsAndroidViewModel
) {
    val context = LocalContext.current
    val utilitySharedPreferencesHelper = remember { UtilitySharedPreferencesHelper(context) }
    val selectedTabIndex = rememberSaveable { mutableIntStateOf(utilitySharedPreferencesHelper.lastTab) }
    val selectedSubTabIndex = rememberSaveable { mutableIntStateOf(utilitySharedPreferencesHelper.lastSubTab) }

    Column {
        PrimaryTabs(selectedTabIndex, utilitySharedPreferencesHelper)

        Column(
            modifier = Modifier.weight(1f)
        ) {
            TabContent(
                selectedTabIndex.intValue,
                selectedSubTabIndex,
                utilitySharedPreferencesHelper,
                diceAndroidViewModel,
                groupsAndroidViewModel,
                rollsAndroidViewModel,
                zoomDiceAndroidViewModel,
                zoomRollsAndroidViewModel
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PrimaryTabs(
    selectedTabIndex: androidx.compose.runtime.MutableIntState,
    utilitySharedPreferencesHelper: UtilitySharedPreferencesHelper
) {
    val tabs = listOf(
        stringResource(R.string.tab_setup),
        stringResource(R.string.tab_roll),
    )

    PrimaryTabRow(
        modifier = Modifier
            .testTag(TabRowTestTag.TAB_ROW)
            .statusBarsPadding(),
        selectedTabIndex = selectedTabIndex.intValue,
        containerColor = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.primary,
    ) {
        tabs.forEachIndexed { index, tabName ->
            Tab(
                selected = selectedTabIndex.intValue == index,
                onClick = {
                    selectedTabIndex.intValue = index
                    utilitySharedPreferencesHelper.lastTab = index
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
}

@Composable
private fun TabContent(
    selectedTabIndex: Int,
    selectedSubTabIndex: androidx.compose.runtime.MutableIntState,
    utilitySharedPreferencesHelper: UtilitySharedPreferencesHelper,
    diceAndroidViewModel: DiceAndroidViewModel,
    groupsAndroidViewModel: GroupsAndroidViewModel,
    rollsAndroidViewModel: RollsAndroidViewModel,
    zoomDiceAndroidViewModel: ZoomDiceAndroidViewModel,
    zoomRollsAndroidViewModel: ZoomRollsAndroidViewModel
) {
    when (selectedTabIndex) {
        0 -> {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                SetupSecondaryTabs(selectedSubTabIndex, utilitySharedPreferencesHelper)

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    when (selectedSubTabIndex.intValue) {
                        1 -> {
                            Groups(
                                groupsAndroidViewModel
                            )
                        }

                        else -> {
                            TabBagDice(
                                diceAndroidViewModel,
                                zoomDiceAndroidViewModel
                            )
                        }
                    }
                }
            }
        }

        else -> {
            TabRoll(
                rollsAndroidViewModel,
                zoomRollsAndroidViewModel
            )
        }
    }
}

@Composable
private fun SetupSecondaryTabs(
    selectedSubTabIndex: androidx.compose.runtime.MutableIntState,
    utilitySharedPreferencesHelper: UtilitySharedPreferencesHelper
) {
    SecondaryTabRow(
        selectedTabIndex = selectedSubTabIndex.intValue,
        containerColor = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.primary,
    ) {
        Tab(
            selected = selectedSubTabIndex.intValue == 0,
            onClick = {
                selectedSubTabIndex.intValue = 0
                utilitySharedPreferencesHelper.lastSubTab = 0
            },
            text = {
                Text(
                    text = stringResource(R.string.tab_setup_dice),
                )
            }
        )
        Tab(
            selected = selectedSubTabIndex.intValue == 1,
            onClick = {
                selectedSubTabIndex.intValue = 1
                utilitySharedPreferencesHelper.lastSubTab = 1
            },
            text = {
                Text(
                    text = stringResource(R.string.tab_setup_groups),
                )
            }
        )
    }
}
