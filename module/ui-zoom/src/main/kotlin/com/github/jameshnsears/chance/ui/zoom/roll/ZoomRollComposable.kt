package com.github.jameshnsears.chance.ui.zoom.roll

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.jameshnsears.chance.data.domain.Roll
import com.github.jameshnsears.chance.ui.zoom.DiceDescription
import com.github.jameshnsears.chance.ui.zoom.SideDescription
import com.github.jameshnsears.chance.ui.zoom.SideImageSVG
import com.github.jameshnsears.chance.ui.zoom.SideOutlineRoll
import com.github.jameshnsears.chance.utils.epoch.EpochTime

@Composable
fun ZoomRoll(zoomRollViewModel: ZoomRollViewModel) {
    val listState = rememberLazyListState()

    val rollHistory by zoomRollViewModel.rollHistory.collectAsStateWithLifecycle()

    LazyColumn(
        state = listState,
        modifier = Modifier.padding(bottom = 110.dp),
    ) {
        var rollSequenceIndex = 0

        items(rollHistory.entries.toList()) { rollSequence ->
            rollSequenceIndex += 1

            Row {
                HistoryTimestamp(rollSequence)
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Score(rollSequence)

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(start = 5.dp, bottom = 5.dp),
                ) {
                    var rollIndex = 0

                    items(rollSequence.value) { roll ->
                        rollIndex += 1
                        RollDetails(zoomRollViewModel, roll)

                        if (rollIndex == rollSequence.value.size) {
                            Column(modifier = Modifier.padding(4.dp)) {}
                        }
                    }
                }
            }

            if (rollSequenceIndex != rollHistory.entries.size)
                Divider(Modifier.padding(top = 8.dp, bottom = 8.dp))
        }
    }
}

@Composable
private fun HistoryTimestamp(rollHistory: MutableMap.MutableEntry<Long, List<Roll>>) {
    Row {
        Text(
            text = " ${EpochTime.formatDay(rollHistory.key)} ${EpochTime.formatTime(rollHistory.key)}",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 8.dp, top = 8.dp, bottom = 4.dp),
        )
    }
}

@Composable
private fun Score(rollHistory: MutableMap.MutableEntry<Long, List<Roll>>) {
    Column(
        Modifier.padding(start = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "10",
            fontSize = 48.sp,
        )
    }
}

@Composable
private fun RollDetails(
    zoomRollViewModel: ZoomRollViewModel,
    roll: Roll,
) {
    zoomRollViewModel.dice(roll.diceEpoch)
    val dice = remember { zoomRollViewModel.dice.value }

    Column(
        Modifier.padding(start = 16.dp, bottom = 5.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        DiceDescription(dice)

        SideOutlineRoll(
            zoomRollViewModel,
            dice,
            roll.side,
        )

        SideImageSVG(zoomRollViewModel, roll.side)

        SideDescription(zoomRollViewModel, roll.side)
    }
}
