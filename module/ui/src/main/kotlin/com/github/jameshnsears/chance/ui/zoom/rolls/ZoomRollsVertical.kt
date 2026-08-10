package com.github.jameshnsears.chance.ui.zoom.rolls

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.github.jameshnsears.chance.data.domain.core.group.GroupHistory
import com.github.jameshnsears.chance.data.domain.core.roll.Roll
import com.github.jameshnsears.chance.ui.tab.rolls.RollsAndroidViewModel
import com.github.jameshnsears.chance.ui.zoom.ZoomState

@Composable
fun ZoomRollVertical(
    listState: androidx.compose.foundation.lazy.LazyListState,
    entriesList: List<Pair<Int, Map.Entry<Long, List<Roll>>>>,
    seenRollEvents: MutableSet<Long>,
    rollHistorySize: Int,
    groupHistory: GroupHistory,
    rollsAndroidViewModel: RollsAndroidViewModel,
    zoomRollsAndroidViewModel: ZoomRollsAndroidViewModel,
    stateFlowZoom: ZoomState,
    rollEventCount: Int
) {
    ZoomRollList(listState, entriesList) { indexSequence, originalIndex, rollSequence ->
        ZoomRollVerticalItem(
            indexSequence,
            originalIndex,
            rollSequence,
            seenRollEvents,
            rollHistorySize,
            groupHistory,
            rollsAndroidViewModel,
            zoomRollsAndroidViewModel,
            stateFlowZoom,
            entriesList.size,
            rollEventCount
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ZoomRollVerticalItem(
    indexSequence: Int,
    originalIndex: Int,
    rollSequence: Map.Entry<Long, List<Roll>>,
    seenRollEvents: MutableSet<Long>,
    rollHistorySize: Int,
    groupHistory: GroupHistory,
    rollsAndroidViewModel: RollsAndroidViewModel,
    zoomRollsAndroidViewModel: ZoomRollsAndroidViewModel,
    stateFlowZoom: ZoomState,
    entriesListSize: Int,
    rollEventCount: Int
) {
    val isNewRollEvent = remember(rollSequence.key, rollEventCount) {
        (indexSequence == 0) && !seenRollEvents.contains(rollSequence.key)
    }

    LaunchedEffect(rollSequence.key, rollEventCount) {
        if (indexSequence == 0) {
            seenRollEvents.add(rollSequence.key)
        }
    }

    if (stateFlowZoom.rollIndexTime) {
        RollIndexTime(
            rollHistorySize - originalIndex,
            rollSequence,
            stateFlowZoom.resizeViewDp
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
                    stateFlowZoom.resizeViewDp,
                    stateFlowZoom.rollScore,
                    isNewRollEvent
                )
            }

        val scaleFactor = stateFlowZoom.resizeViewDp.value / 65f
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .testTag(ZoomRollsTestTag.LAZY_COLUMN_NOT_EMPTY),
            horizontalArrangement = Arrangement.spacedBy(4.dp * scaleFactor, Alignment.CenterHorizontally),
            verticalArrangement = Arrangement.spacedBy(8.dp * scaleFactor),
        ) {
            rollSequence.value.forEachIndexed { indexRoll, roll ->
                val isLocked =
                    if (indexSequence == 0) stateFlowZoom.lockedRollIndices.contains(roll.unitKey()) else false

                key("${roll.side.uuid}_$indexRoll") {
                    RollDetails(
                        zoomRollsAndroidViewModel,
                        roll,
                        zoomRollsAndroidViewModel.fetchDiceFromUuidCache(roll.uuidDice),
                        groupHistory,
                        stateFlowZoom.resizeViewDp,
                        isLocked = isLocked,
                        onToggleLock = if (indexSequence == 0) {
                            { zoomRollsAndroidViewModel.toggleLock(roll) }
                        } else null,
                        rollEventCount = if (indexSequence == 0) rollEventCount else 0,
                        showGroupTitle = stateFlowZoom.groupTitle,
                        showDiceTitle = stateFlowZoom.diceTitle,
                        showRollBehaviour = stateFlowZoom.rollBehaviour,
                        showSideNumber = stateFlowZoom.sideNumber,
                        showSideSVG = stateFlowZoom.sideSVG,
                        showSideDescription = stateFlowZoom.sideDescription
                    )
                }
            }
        }

        if (indexSequence < entriesListSize - 1)
            HorizontalDivider(Modifier.padding(top = 12.dp, bottom = 12.dp))
    }
}
