package com.github.jameshnsears.chance.ui.zoom.rolls

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.jameshnsears.chance.data.domain.core.group.GroupHistory
import com.github.jameshnsears.chance.data.domain.core.roll.Roll
import com.github.jameshnsears.chance.ui.tab.rolls.RollsAndroidViewModel

@Composable
fun ZoomRollVertical(
    listState: androidx.compose.foundation.lazy.LazyListState,
    entriesList: List<Pair<Int, Map.Entry<Long, List<Roll>>>>,
    seenRollEvents: MutableSet<Long>,
    rollHistorySize: Int,
    groupHistory: GroupHistory,
    rollsAndroidViewModel: RollsAndroidViewModel,
    zoomRollsAndroidViewModel: ZoomRollsAndroidViewModel,
    resizeViewDp: Dp,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag(ZoomRollsTestTag.LAZY_COLUMN),
        state = listState,
        contentPadding = PaddingValues(start = 16.dp, top = 12.dp, end = 16.dp, bottom = 148.dp)
    ) {
        itemsIndexed(
            items = entriesList,
            key = { _, (_, entry) -> entry.key }
        ) { indexSequence, (originalIndex, rollSequence) ->
            ZoomRollVerticalItem(
                indexSequence,
                originalIndex,
                rollSequence,
                seenRollEvents,
                rollHistorySize,
                groupHistory,
                rollsAndroidViewModel,
                zoomRollsAndroidViewModel,
                resizeViewDp,
                entriesList.size
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ZoomRollVerticalItem(
    indexSequence: Int,
    originalIndex: Int,
    rollSequence: Map.Entry<Long, List<Roll>>,
    seenRollEvents: MutableSet<Long>,
    rollHistorySize: Int,
    groupHistory: GroupHistory,
    rollsAndroidViewModel: RollsAndroidViewModel,
    zoomRollsAndroidViewModel: ZoomRollsAndroidViewModel,
    resizeViewDp: Dp,
    entriesListSize: Int
) {
    val stateFlowZoom by zoomRollsAndroidViewModel.stateFlowZoom.collectAsStateWithLifecycle(
        lifecycleOwner = LocalLifecycleOwner.current
    )

    val isNewRollEvent = remember(rollSequence.key) {
        (indexSequence == 0) && !seenRollEvents.contains(rollSequence.key)
    }

    LaunchedEffect(rollSequence.key) {
        if (indexSequence == 0) {
            seenRollEvents.add(rollSequence.key)
        }
    }

    if (stateFlowZoom.rollIndexTime) {
        RollIndexTime(
            rollHistorySize - originalIndex,
            rollSequence
        )
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if (stateFlowZoom.rollScore)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                RollScore(
                    rollsAndroidViewModel,
                    zoomRollsAndroidViewModel,
                    rollSequence,
                    resizeViewDp,
                    stateFlowZoom.rollScore,
                    isNewRollEvent
                )
            }

        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .testTag(ZoomRollsTestTag.LAZY_COLUMN_NOT_EMPTY),
            horizontalArrangement = Arrangement.Center,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            rollSequence.value.forEach { roll ->
                Box(modifier = Modifier.padding(horizontal = 4.dp)) {
                    RollDetails(
                        zoomRollsAndroidViewModel,
                        roll,
                        zoomRollsAndroidViewModel.fetchDiceFromUuidCache(roll.uuidDice),
                        groupHistory,
                        resizeViewDp,
                    )
                }
            }
        }

        if (indexSequence < entriesListSize - 1)
            HorizontalDivider(Modifier.padding(top = 12.dp, bottom = 12.dp))
    }
}
