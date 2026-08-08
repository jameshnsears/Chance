package com.github.jameshnsears.chance.ui.zoom.rolls

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
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
fun ZoomRollHistoryHorizontal(
    listState: androidx.compose.foundation.lazy.LazyListState,
    entriesList: List<Pair<Int, Map.Entry<Long, List<Roll>>>>,
    seenRollEvents: MutableSet<Long>,
    rollHistorySize: Int,
    groupHistory: GroupHistory,
    rollsAndroidViewModel: RollsAndroidViewModel,
    zoomRollsAndroidViewModel: ZoomRollsAndroidViewModel,
    resizeViewDp: Dp,
    rollEventCount: Int
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
            ZoomRollItem(
                indexSequence,
                originalIndex,
                rollSequence,
                seenRollEvents,
                rollHistorySize,
                groupHistory,
                rollsAndroidViewModel,
                zoomRollsAndroidViewModel,
                resizeViewDp,
                entriesList.size,
                rollEventCount
            )
        }
    }
}

@Composable
fun ZoomRollItem(
    indexSequence: Int,
    originalIndex: Int,
    rollSequence: Map.Entry<Long, List<Roll>>,
    seenRollEvents: MutableSet<Long>,
    rollHistorySize: Int,
    groupHistory: GroupHistory,
    rollsAndroidViewModel: RollsAndroidViewModel,
    zoomRollsAndroidViewModel: ZoomRollsAndroidViewModel,
    resizeViewDp: Dp,
    entriesListSize: Int,
    rollEventCount: Int
) {
    val isNewRollEvent = remember(rollSequence.key, rollEventCount) {
        (indexSequence == 0) && !seenRollEvents.contains(rollSequence.key)
    }

    val stateFlowZoom by zoomRollsAndroidViewModel.stateFlowZoom.collectAsStateWithLifecycle(
        lifecycleOwner = LocalLifecycleOwner.current
    )

    val scrollPosition = stateFlowZoom.horizontalScrollPositions[rollSequence.key.toString()] ?: (0 to 0)

    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = scrollPosition.first,
        initialFirstVisibleItemScrollOffset = scrollPosition.second
    )

    LaunchedEffect(listState) {
        snapshotFlow {
            Pair(listState.firstVisibleItemIndex, listState.firstVisibleItemScrollOffset)
        }.collect { (index, offset) ->
            zoomRollsAndroidViewModel.saveHorizontalScrollPosition(
                rollSequence.key.toString(),
                index,
                offset
            )
        }
    }

    LaunchedEffect(rollSequence.key, rollEventCount) {
        if (indexSequence == 0) {
            seenRollEvents.add(rollSequence.key)
        }
    }

        ZoomRollItemCol(
            indexSequence,
            originalIndex,
            rollSequence,
            rollHistorySize,
            groupHistory,
            rollsAndroidViewModel,
            zoomRollsAndroidViewModel,
            resizeViewDp,
            entriesListSize,
            isNewRollEvent,
            stateFlowZoom.rollIndexTime,
            stateFlowZoom.rollScore,
            listState,
            rollEventCount
        )
}

@Composable
fun ZoomRollItemCol(
    indexSequence: Int,
    originalIndex: Int,
    rollSequence: Map.Entry<Long, List<Roll>>,
    rollHistorySize: Int,
    groupHistory: GroupHistory,
    rollsAndroidViewModel: RollsAndroidViewModel,
    zoomRollsAndroidViewModel: ZoomRollsAndroidViewModel,
    resizeViewDp: Dp,
    entriesListSize: Int,
    isNewRollEvent: Boolean,
    showRollIndexTime: Boolean,
    showRollScore: Boolean,
    listState: androidx.compose.foundation.lazy.LazyListState,
    rollEventCount: Int
) {
    Column {
        if (showRollIndexTime)
            RollIndexTime(
                rollHistorySize - originalIndex,
                rollSequence
            )

        ZoomRollItemRow(
            indexSequence,
            rollsAndroidViewModel,
            zoomRollsAndroidViewModel,
            rollSequence,
            resizeViewDp,
            showRollScore,
            isNewRollEvent,
            listState,
            groupHistory,
            rollEventCount
        )

        if (indexSequence < entriesListSize - 1)
            HorizontalDivider(Modifier.padding(top = 12.dp, bottom = 12.dp))
    }
}

@Composable
fun ZoomRollItemRow(
    indexSequence: Int,
    rollsAndroidViewModel: RollsAndroidViewModel,
    zoomRollsAndroidViewModel: ZoomRollsAndroidViewModel,
    rollSequence: Map.Entry<Long, List<Roll>>,
    resizeViewDp: Dp,
    showRollScore: Boolean,
    isNewRollEvent: Boolean,
    listState: androidx.compose.foundation.lazy.LazyListState,
    groupHistory: GroupHistory,
    rollEventCount: Int
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.testTag(ZoomRollsTestTag.LAZY_COLUMN_NOT_EMPTY)
    ) {
        RollScore(
            rollsAndroidViewModel,
            zoomRollsAndroidViewModel,
            rollSequence,
            resizeViewDp,
            showRollScore,
            isNewRollEvent
        )

        ZoomRollItemLR(
            indexSequence,
            listState,
            rollSequence,
            zoomRollsAndroidViewModel,
            groupHistory,
            resizeViewDp,
            rollEventCount
        )
    }
}

@Composable
fun RowScope.ZoomRollItemLR(
    indexSequence: Int,
    listState: androidx.compose.foundation.lazy.LazyListState,
    rollSequence: Map.Entry<Long, List<Roll>>,
    zoomRollsAndroidViewModel: ZoomRollsAndroidViewModel,
    groupHistory: GroupHistory,
    resizeViewDp: Dp,
    rollEventCount: Int
) {
    val lockedRollIndices by zoomRollsAndroidViewModel.lockedRollIndices.collectAsStateWithLifecycle(
        lifecycleOwner = LocalLifecycleOwner.current
    )

    LazyRow(
        state = listState,
        contentPadding = PaddingValues(horizontal = 0.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.Top,
        modifier = Modifier
            .weight(1f),
    ) {
        itemsIndexed(
            rollSequence.value,
            key = { index, item ->
                if (indexSequence == 0) "${rollEventCount}_${item.side}_$index"
                else "${item.side}_$index"
            }
        ) { indexRoll, roll ->
            RollDetails(
                zoomRollsAndroidViewModel,
                roll,
                zoomRollsAndroidViewModel.fetchDiceFromUuidCache(roll.uuidDice),
                groupHistory,
                resizeViewDp,
                isLocked = if (indexSequence == 0) lockedRollIndices.contains(indexRoll) else false,
                onToggleLock = if (indexSequence == 0 && roll.explodeIndex == 0) {
                    { zoomRollsAndroidViewModel.toggleLock(indexRoll) }
                } else null,
                rollEventCount = if (indexSequence == 0) rollEventCount else 0
            )
        }
    }
}
