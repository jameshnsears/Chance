package com.github.jameshnsears.chance.ui.zoom.roll

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import com.github.jameshnsears.chance.ui.zoom.DiceTitle
import com.github.jameshnsears.chance.ui.zoom.SideDescription
import com.github.jameshnsears.chance.ui.zoom.SideImageNumber
import com.github.jameshnsears.chance.ui.zoom.SideImageSVG
import com.github.jameshnsears.chance.ui.zoom.ZoomAndroidViewModel
import timber.log.Timber

@Composable
fun ZoomRoll(
    tabRollViewModel: TabRollViewModel,
    zoomAndroidViewModel: ZoomAndroidViewModel
) {
    val diceBag = zoomAndroidViewModel.diceBag.collectAsStateWithLifecycle()

    val resize = zoomAndroidViewModel.resize.collectAsStateWithLifecycle()

    val rollHistory by zoomAndroidViewModel.rollHistory.collectAsStateWithLifecycle()

    val stateFlow =
        tabRollViewModel.tabRollStateFlow.collectAsStateWithLifecycle()

    var settingsTime = stateFlow.value.settingsTime

    val settingsScore = stateFlow.value.settingsScore

    val listState = rememberLazyListState()

    LazyColumn(
        state = listState,
        modifier = Modifier.padding(top = 8.dp, bottom = 110.dp),
    ) {
        Timber.d("resize.Roll=${resize.value}")

        var rollSequenceIndex = 0

        items(rollHistory.entries.toList()) { rollSequence ->
            rollSequenceIndex += 1

            if (settingsTime)
                Row {
                    RollTime(rollSequence)
                }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(end = 8.dp)
            ) {
                if (settingsScore) RollScore(rollSequence)

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier.padding(bottom = 5.dp),
                ) {
                    items(rollSequence.value) { roll ->
                        for (dice: Dice in diceBag.value) {
                            if (dice.epoch == roll.diceEpoch) {
                                RollDetails(tabRollViewModel, zoomAndroidViewModel, roll, dice)
                                break
                            }
                        }
                    }
                }
            }

            if (rollSequenceIndex < rollHistory.entries.size)
                HorizontalDivider(Modifier.padding(top = 8.dp, bottom = 8.dp))
        }
    }
}

@Composable
private fun RollTime(rollHistory: MutableMap.MutableEntry<Long, List<Roll>>) {
    Row {
        Text(
            text = "${UtilityEpochTimeGenerator.formatDay(rollHistory.key)} ${
                UtilityEpochTimeGenerator.formatTime(
                    rollHistory.key
                )
            }",
            modifier = Modifier.padding(start = 8.dp, bottom = 4.dp),
        )
    }
}

@Composable
private fun RollScore(rollHistory: MutableMap.MutableEntry<Long, List<Roll>>) {
    Column(
        Modifier.padding(start = 10.dp, end = 8.dp),
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

        SideDescription(zoomAndroidViewModel, roll.side)

        SideImageSVG(zoomAndroidViewModel, roll.side)
    }
}
