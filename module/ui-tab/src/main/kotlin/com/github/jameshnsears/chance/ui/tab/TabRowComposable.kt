package com.github.jameshnsears.chance.ui.tab

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.github.jameshnsears.chance.data.repository.bag.BagRepositoryInterface
import com.github.jameshnsears.chance.data.repository.roll.RollRepositoryInterface
import com.github.jameshnsears.chance.data.repository.settings.SettingsRepositoryInterface
import com.github.jameshnsears.chance.ui.tab.bag.TabBag
import com.github.jameshnsears.chance.ui.tab.bag.TabBagViewModel
import com.github.jameshnsears.chance.ui.tab.roll.TabRoll
import com.github.jameshnsears.chance.ui.tab.roll.TabRollViewModel

@Composable
fun TabRowChance(
    settingsRepository: SettingsRepositoryInterface,
    bagRepository: BagRepositoryInterface,
    rollRepository: RollRepositoryInterface
) {
    val tabs = listOf(
        stringResource(R.string.tab_bag),
        stringResource(R.string.tab_roll)
    )
    val selectedTabIndex = remember { mutableIntStateOf(0) }

    val iconBag = painterResource(id = R.drawable.custom_bag_fill0_wght400_grad0_opsz24)
    val iconRoll = painterResource(id = R.drawable.custom_casino_fill0_wght400_grad0_opsz24)

    TabRow(selectedTabIndex = selectedTabIndex.intValue) {
        tabs.forEachIndexed { index, tabName ->
            Tab(
                text = { Text(tabName) },
                selected = selectedTabIndex.intValue == index,
                onClick = { selectedTabIndex.intValue = index },
                icon = {
                    if (index == 0)
                        Icon(
                            iconBag,
                            contentDescription = stringResource(R.string.tab_bag),
                            modifier = Modifier.size(24.dp)
                        )
                    else
                        Icon(
                            iconRoll,
                            contentDescription = stringResource(R.string.tab_roll),
                            modifier = Modifier.size(24.dp)
                        )
                },
            )
        }
    }

    Column(modifier = Modifier.padding(top = 65.dp)) {
        when (selectedTabIndex.intValue) {
            0 -> TabBag(
                TabBagViewModel(
                    settingsRepository,
                    bagRepository
                )
            )

            1 -> TabRoll(
                TabRollViewModel(
                    settingsRepository,
                    bagRepository,
                    rollRepository
                )
            )

            else -> throw IllegalStateException("Invalid selected tab index")
        }
    }
}
