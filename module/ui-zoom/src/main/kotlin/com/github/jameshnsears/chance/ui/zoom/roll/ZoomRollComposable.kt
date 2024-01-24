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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.jameshnsears.chance.data.domain.Roll
import com.github.jameshnsears.chance.ui.zoom.DiceDescription
import com.github.jameshnsears.chance.ui.zoom.SideDescription
import com.github.jameshnsears.chance.ui.zoom.SideImageSVG
import com.github.jameshnsears.chance.ui.zoom.SideOutlineRoll
import com.github.jameshnsears.chance.utils.epoch.EpochTime


@Composable
fun ZoomRoll(zoomRollViewModel: ZoomRollViewModel) {
    val listState = rememberLazyListState()

    val bagRepository = remember { zoomRollViewModel.rollRepository.fetch() }

    LazyColumn(
        state = listState,
        modifier = Modifier.padding(bottom = 100.dp)
    ) {

        items(bagRepository.entries.toList()) { rollHistory ->

            var showHistory = true
            if (showHistory) {
                Row {
                    HistoryTimestamp(rollHistory)
                    showHistory = false
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {

                var showScore = true
                if (showScore) {
                    Score(rollHistory)
                    showScore = false
                }

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(start = 5.dp, bottom = 5.dp)
                ) {
                    items(rollHistory.value) { rollSequence ->
                        RollDetails(zoomRollViewModel, rollSequence)
                    }
                }
            }

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
            modifier = Modifier.padding(start = 8.dp, top = 8.dp, bottom = 8.dp)
        )
    }
}

@Composable
private fun Score(rollHistory: MutableMap.MutableEntry<Long, List<Roll>>) {
    Column(
        Modifier.padding(start = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "10",
            fontSize = 72.sp,
        )
    }
}

@Composable
private fun RollDetails(
    zoomRollViewModel: ZoomRollViewModel,
    rollSequence: Roll
) {
    Column(
        Modifier.padding(start = 16.dp, bottom = 5.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        DiceDescription(rollSequence.dice)

        SideOutlineRoll(
            zoomRollViewModel,
            rollSequence.dice,
            rollSequence.side
        )

        SideImageSVG(zoomRollViewModel, rollSequence.side)

        SideDescription(zoomRollViewModel, rollSequence.side)
    }


}