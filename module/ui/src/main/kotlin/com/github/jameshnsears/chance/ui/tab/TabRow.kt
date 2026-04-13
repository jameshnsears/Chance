package com.github.jameshnsears.chance.ui.tab

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.github.jameshnsears.chance.common.R
import com.github.jameshnsears.chance.ui.tab.bag.TabBag
import com.github.jameshnsears.chance.ui.tab.bag.TabBagAndroidViewModel
import com.github.jameshnsears.chance.ui.tab.roll.RollAndroidViewModel
import com.github.jameshnsears.chance.ui.tab.roll.TabRoll
import com.github.jameshnsears.chance.ui.zoom.bag.ZoomBagAndroidViewModel
import com.github.jameshnsears.chance.ui.zoom.roll.ZoomRollAndroidViewModel

class TabRowTestTag {
    companion object {
        const val TAB_ROW = "TAB_ROW"
    }
}

@Composable
fun TabRow(
    tabBagAndroidViewModel: TabBagAndroidViewModel,
    rollAndroidViewModel: RollAndroidViewModel,
    zoomBagAndroidViewModel: ZoomBagAndroidViewModel,
    zoomRollAndroidViewModel: ZoomRollAndroidViewModel
) {
    val tabs = listOf(
        stringResource(R.string.tab_bag),
        stringResource(R.string.tab_roll),
    )

    val selectedTabIndex = rememberSaveable { mutableIntStateOf(0) }

    Column {
        PrimaryTabRow(
            modifier = Modifier
                .testTag(TabRowTestTag.TAB_ROW)
                .statusBarsPadding(),
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
                    TabBag(
                        tabBagAndroidViewModel,
                        zoomBagAndroidViewModel
                    )
                }

                else -> {
                    TabRoll(
                        rollAndroidViewModel,
                        zoomRollAndroidViewModel
                    )
                }
            }
        }
    }
}
