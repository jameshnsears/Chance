package com.github.jameshnsears.chance.ui.tab.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.github.jameshnsears.chance.ui.R
import com.github.jameshnsears.chance.ui.tab.bag.TabBagAndroidViewModel
import com.github.jameshnsears.chance.ui.tab.bag.compose.TabBag
import com.github.jameshnsears.chance.ui.tab.roll.TabRollViewModel
import com.github.jameshnsears.chance.ui.tab.roll.compose.TabRoll
import com.github.jameshnsears.chance.ui.zoom.ZoomAndroidViewModel

@Composable
fun TabRow(
    tabBagAndroidViewModel: TabBagAndroidViewModel,
    tabRollViewModel: TabRollViewModel,
    zoomAndroidViewModel: ZoomAndroidViewModel
) {
    val tabs = listOf(
        stringResource(R.string.tab_bag),
        stringResource(R.string.tab_roll),
    )

    val selectedTabIndex = rememberSaveable { mutableIntStateOf(0) }

    Column {
        TabRow(
            selectedTabIndex = selectedTabIndex.intValue,
        ) {
            tabs.forEachIndexed { index, tabName ->
                Tab(
                    text = { Text(tabName) },
                    selected = selectedTabIndex.intValue == index,
                    onClick = { selectedTabIndex.intValue = index },
                    icon = {
                        if (index == 0)
                            Icon(
                                painterResource(id = R.drawable.custom_bag_fill0_wght400_grad0_opsz24),
                                contentDescription = stringResource(R.string.tab_bag),
                                modifier = Modifier.size(24.dp),
                            )
                        else
                            Icon(
                                painterResource(id = R.drawable.custom_casino_fill0_wght400_grad0_opsz24),
                                contentDescription = stringResource(R.string.tab_roll),
                                modifier = Modifier.size(24.dp),
                            )
                    }
                )
            }
        }

        Column {
            when (selectedTabIndex.intValue) {
                0 -> TabBag(
                    tabBagAndroidViewModel,
                    zoomAndroidViewModel
                )

                else -> TabRoll(
                    tabRollViewModel,
                    zoomAndroidViewModel
                )
            }
        }
    }
}
