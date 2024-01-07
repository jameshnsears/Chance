package com.github.jameshnsears.chance.ui.zoom.roll

import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.unit.dp
import com.github.jameshnsears.chance.utils.epoch.EpochTime


@Composable
fun ZoomRoll(zoomRollViewModel: ZoomRollViewModel) {
    val listState = rememberLazyListState()

    val bagRepository = remember { zoomRollViewModel.bagRepository.fetch() }

    LazyColumn(
        state = listState
    ) {
        items(bagRepository.entries.toList()) { rollHistory ->
            Column {
                var showEpoch = true

                LazyRow {
                    items(rollHistory.value) { rollSequence ->
                        if (showEpoch) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(text = EpochTime.formatDay(rollHistory.key))
                                Text(text = EpochTime.formatTime(rollHistory.key))
                            }
                            showEpoch = false
                        }

                        val title = rollSequence.dice.title
                        val sideNumber = rollSequence.side.number.toString()

                        Column(
                            Modifier.padding(start = 18.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(title)
                            Text(sideNumber)
                        }
                    }
                }

                Divider()
            }
        }
    }
}
