package com.github.jameshnsears.chance.ui.zoom.roll.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.jameshnsears.chance.data.domain.state.Dice
import com.github.jameshnsears.chance.data.domain.state.Roll
import com.github.jameshnsears.chance.data.domain.utility.epoch.UtilityEpochTimeGenerator
import com.github.jameshnsears.chance.ui.tab.roll.TabRollViewModel
import com.github.jameshnsears.chance.ui.zoom.ZoomAndroidViewModel
import com.github.jameshnsears.chance.ui.zoom.compose.DiceTitle
import com.github.jameshnsears.chance.ui.zoom.compose.SideDescriptionRoll
import com.github.jameshnsears.chance.ui.zoom.compose.SideImageNumber
import com.github.jameshnsears.chance.ui.zoom.compose.SideImageSVG
import timber.log.Timber

@Composable
fun ZoomRoll(
    tabRollViewModel: TabRollViewModel,
    zoomAndroidViewModel: ZoomAndroidViewModel,
) {
    val diceBag = zoomAndroidViewModel.diceBag.collectAsStateWithLifecycle()

    val resizeView = zoomAndroidViewModel.resizeView.collectAsStateWithLifecycle()

    val rollHistory by zoomAndroidViewModel.rollHistory.collectAsStateWithLifecycle()

    val tabRollStateFlow =
        tabRollViewModel.tabRollStateFlow.collectAsStateWithLifecycle()

    val settingsTime = tabRollStateFlow.value.settingsTime

    val settingsScore = tabRollStateFlow.value.settingsScore

    val listState = rememberLazyListState()

    LazyColumn(
        state = listState,
        modifier = Modifier.padding(top = 8.dp, start = 8.dp, bottom = 110.dp, end = 8.dp),
    ) {
        Timber.d("resizeView.ZoomRoll=${resizeView.value}")

        itemsIndexed(rollHistory.entries.toList()) { index, rollSequence ->
            if (settingsTime)
                Row {
                    RollTime(rollSequence)
                }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (settingsScore) RollScore(rollSequence)

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier.padding(bottom = 12.dp),
                ) {
                    items(
                        rollSequence.value,
                        key = {
                            it.side.uuid
                        }
                    ) { roll ->
                        for (dice: Dice in diceBag.value) {
                            if (dice.epoch == roll.diceEpoch) {
                                RollDetails(tabRollViewModel, zoomAndroidViewModel, roll, dice)
                                break
                            }
                        }
                    }
                }
            }

            if (index < rollHistory.entries.size)
                HorizontalDivider(Modifier.padding(top = 8.dp, bottom = 8.dp))
        }
    }
}

@Composable
private fun RollTime(rollHistory: MutableMap.MutableEntry<Long, List<Roll>>) {
    Row {
        Text(
            text = UtilityEpochTimeGenerator.format(rollHistory.key),
            modifier = Modifier.padding(bottom = 4.dp),
        )
    }
}

@Composable
private fun RollScore(rollHistory: MutableMap.MutableEntry<Long, List<Roll>>) {
    Column(
        Modifier.padding(start = 4.dp, end = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "10",
            fontSize = 36.sp,
        )
    }
}

@Composable
private fun RollDetails(
    tabRollViewModel: TabRollViewModel,
    zoomAndroidViewModel: ZoomAndroidViewModel,
    roll: Roll,
    dice: Dice
) {
    val stateFlow =
        tabRollViewModel.tabRollStateFlow.collectAsStateWithLifecycle()

    val settingsDiceTitle = stateFlow.value.settingsDiceTitle

    val settingsSideNumber = stateFlow.value.settingsSideNumber

    Column(
        Modifier.padding(start = 9.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        if (settingsDiceTitle) DiceTitle(dice)

        if (settingsSideNumber) SideImageNumber(zoomAndroidViewModel, dice, roll.side)

        SideDescriptionRoll(zoomAndroidViewModel, dice, roll.side)

        SideImageSVG(zoomAndroidViewModel, roll.side)
    }
}
