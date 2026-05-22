package com.github.jameshnsears.chance.ui.tab

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
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
        stringResource(R.string.tab_setup) to Icons.Outlined.Edit,
        stringResource(R.string.tab_roll) to R.drawable.roll,
    )

    val selectedTabIndex = rememberSaveable { mutableIntStateOf(0) }

    Column {
        PrimaryTabRow(
            modifier = Modifier
                .testTag(TabRowTestTag.TAB_ROW)
                .statusBarsPadding(),
            selectedTabIndex = selectedTabIndex.intValue,
        ) {
            tabs.forEachIndexed { index, (tabName, tabIcon) ->
                Tab(
                    text = {
                        Text(
                            text = tabName,
                            fontSize = 18.sp,
                        )
                    },
                    icon = {
                        val iconModifier = Modifier.size(24.dp)
                        if (tabIcon is Int) {
                            Icon(
                                painter = painterResource(id = tabIcon),
                                contentDescription = null,
                                modifier = iconModifier
                            )
                        } else if (tabIcon is androidx.compose.ui.graphics.vector.ImageVector) {
                            Icon(
                                imageVector = tabIcon,
                                contentDescription = null,
                                modifier = iconModifier
                            )
                        }
                    },
                    selected = selectedTabIndex.intValue == index,
                    onClick = { selectedTabIndex.intValue = index },
                )
            }
        }

        Column(
            modifier = Modifier.weight(1f)
        ) {
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
