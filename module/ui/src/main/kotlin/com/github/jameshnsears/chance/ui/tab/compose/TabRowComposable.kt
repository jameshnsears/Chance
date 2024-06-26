package com.github.jameshnsears.chance.ui.tab.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.github.jameshnsears.chance.ui.R
import com.github.jameshnsears.chance.ui.tab.bag.TabBagAndroidViewModel
import com.github.jameshnsears.chance.ui.tab.bag.compose.TabBag
import com.github.jameshnsears.chance.ui.tab.roll.TabRollAndroidViewModel
import com.github.jameshnsears.chance.ui.tab.roll.compose.TabRoll
import com.github.jameshnsears.chance.ui.zoom.ZoomAndroidViewModel

@Composable
fun TabRow(
    tabBagAndroidViewModel: TabBagAndroidViewModel,
    tabRollAndroidViewModel: TabRollAndroidViewModel,
    zoomAndroidViewModel: ZoomAndroidViewModel
) {
    val tabs = listOf(
        stringResource(R.string.tab_bag),
        stringResource(R.string.tab_roll),
    )

    val selectedTabIndex = rememberSaveable {
        mutableIntStateOf(tabBagAndroidViewModel.stateFlowTabBag.value.tabRowChance)
    }

    Column {
        TabRow(
            selectedTabIndex = selectedTabIndex.intValue,
        ) {
            tabs.forEachIndexed { index, tabName ->
                Tab(
                    text = {
                        Text(
                            text = tabName,
                            fontSize = 18.sp,
                        )
                    },
                    selected = selectedTabIndex.intValue == index,
                    onClick = { selectedTabIndex.intValue = index },
                )
            }
        }

        Column {
            when (selectedTabIndex.intValue) {
                0 -> {
                    tabBagAndroidViewModel.markTabAsCurrentInSettings()
                    TabBag(
                        tabBagAndroidViewModel,
                        zoomAndroidViewModel
                    )
                }

                else -> {
                    tabRollAndroidViewModel.markTabAsCurrentInSettings()
                    TabRoll(
                        tabRollAndroidViewModel,
                        zoomAndroidViewModel
                    )
                }
            }
        }
    }
}
