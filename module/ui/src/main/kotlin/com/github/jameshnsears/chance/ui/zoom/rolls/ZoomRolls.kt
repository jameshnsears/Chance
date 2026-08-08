package com.github.jameshnsears.chance.ui.zoom.rolls

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.jameshnsears.chance.ui.tab.rolls.RollsAndroidViewModel
import com.github.jameshnsears.chance.ui.tab.rolls.RollsEvent

@Composable
fun ZoomRoll(
    rollsAndroidViewModel: RollsAndroidViewModel,
    zoomRollsAndroidViewModel: ZoomRollsAndroidViewModel,
) {
    val stateFlowZoom by zoomRollsAndroidViewModel.stateFlowZoom.collectAsStateWithLifecycle(
        lifecycleOwner = LocalLifecycleOwner.current
    )

    val rollHistory = stateFlowZoom.rollHistory
    val entriesList = if (stateFlowZoom.history) {
        stateFlowZoom.entriesList
    } else {
        stateFlowZoom.entriesList.take(1)
    }

    val seenRollEvents = remember { mutableSetOf<Long>() }
    var rollEventHappened by remember { mutableStateOf(value = false) }
    var rollEventCount by remember { mutableIntStateOf(0) }

    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = stateFlowZoom.firstVisibleItemIndex,
        initialFirstVisibleItemScrollOffset = stateFlowZoom.firstVisibleItemScrollOffset
    )

    val groupHistory by zoomRollsAndroidViewModel.groupHistory.collectAsStateWithLifecycle(
        lifecycleOwner = LocalLifecycleOwner.current,
    )

    LaunchedEffect(Unit) {
        RollsEvent.sharedFlowTabRollEvent.collect {
            rollEventCount++
            rollEventHappened = true
            // Clear the latest roll from seen so it re-animates if it's a re-roll
            stateFlowZoom.rollHistory.keys.firstOrNull()?.let {
                seenRollEvents.remove(it)
            }
            listState.scrollToItem(0)
        }
    }

    LaunchedEffect(entriesList.size) {
        if (rollEventHappened) {
            listState.scrollToItem(0)
        }
    }

    if (!rollEventHappened) {
        seenRollEvents.addAll(rollHistory.keys)
    }

    LaunchedEffect(listState) {
        snapshotFlow {
            Pair(listState.firstVisibleItemIndex, listState.firstVisibleItemScrollOffset)
        }.collect { (index, offset) ->
            zoomRollsAndroidViewModel.saveScrollPosition(index, offset)
        }
    }


    Surface(
        color = MaterialTheme.colorScheme.surfaceContainerLow,
    ) {
        if (rollHistory.isEmpty()) {
            ZoomRollEmptyState()
        } else if (stateFlowZoom.layout) {
            ZoomRollHistoryHorizontal(
                listState,
                entriesList,
                seenRollEvents,
                rollHistory.size,
                groupHistory,
                rollsAndroidViewModel,
                zoomRollsAndroidViewModel,
                stateFlowZoom.resizeViewDp,
                rollEventCount
            )
        } else {
            ZoomRollVertical(
                listState,
                entriesList,
                seenRollEvents,
                rollHistory.size,
                groupHistory,
                rollsAndroidViewModel,
                zoomRollsAndroidViewModel,
                stateFlowZoom.resizeViewDp,
                rollEventCount
            )
        }
    }
}
