package com.github.jameshnsears.chance.ui.tab.rolls.selection

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.jameshnsears.chance.data.domain.core.Dice
import com.github.jameshnsears.chance.data.domain.core.group.Group
import com.github.jameshnsears.chance.ui.tab.rolls.RollsAndroidViewModel
import com.github.jameshnsears.chance.ui.zoom.rolls.ZoomRollsAndroidViewModel

@Composable
fun RollSelectionRow(
    rollsAndroidViewModel: RollsAndroidViewModel,
    zoomRollsAndroidViewModel: ZoomRollsAndroidViewModel
) {
    val stateDiceBag by rollsAndroidViewModel.diceBag.collectAsStateWithLifecycle()

    val stateGroupHistory by rollsAndroidViewModel.groupHistory.collectAsStateWithLifecycle()

    val stateZoom by zoomRollsAndroidViewModel.stateFlowZoom.collectAsStateWithLifecycle()

    val selectedDiceUuids by rollsAndroidViewModel.selectedDiceUuids.collectAsStateWithLifecycle()
    val selectedGroupUuids by rollsAndroidViewModel.selectedGroupUuids.collectAsStateWithLifecycle()

    val chipsEnabled = stateZoom.lockedRollIndices.isEmpty()

    val diceBag = stateDiceBag
    val groupHistory = stateGroupHistory

    LazyRow(
        state = rollsAndroidViewModel.rollSelectionRowScrollState,
        modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
    ) {
        items(
            count = diceBag.size,
            key = { index -> diceBag[index].uuid }
        ) { index ->
            val dice = diceBag[index]
            RollSelectionFilterChip(
                rollsAndroidViewModel,
                dice,
                selectedDiceUuids.contains(dice.uuid),
                chipsEnabled
            )
        }
        items(
            count = groupHistory.size,
            key = { index -> groupHistory[index].uuid }
        ) { index ->
            val group = groupHistory[index]
            RollSelectionGroupFilterChip(
                rollsAndroidViewModel,
                group,
                selectedGroupUuids.contains(group.uuid),
                chipsEnabled
            )
        }
    }
}

@Composable
fun RollSelectionFilterChip(
    rollsAndroidViewModel: RollsAndroidViewModel,
    dice: Dice,
    isSelected: Boolean,
    enabled: Boolean
) {
    FilterChip(
        modifier = Modifier
            .padding(end = 8.dp)
            .testTag(RollsSelectionTestTag.ROLL_BUTTON + dice.title),
        enabled = enabled,
        onClick = {
            rollsAndroidViewModel.markDiceAsSelected(dice, !isSelected)
        },
        label = {
            Text(
                text = dice.title
            )
        },
        selected = isSelected,
        leadingIcon = if (isSelected) {
            {
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = dice.title,
                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                )
            }
        } else {
            null
        },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = MaterialTheme.colorScheme.primary,
            selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
            selectedLeadingIconColor = MaterialTheme.colorScheme.onPrimary
        ),
    )
}

@Composable
fun RollSelectionGroupFilterChip(
    rollsAndroidViewModel: RollsAndroidViewModel,
    group: Group,
    isSelected: Boolean,
    enabled: Boolean
) {
    FilterChip(
        modifier = Modifier
            .padding(end = 8.dp)
            .testTag(RollsSelectionTestTag.ROLL_BUTTON + group.name),
        enabled = enabled,
        onClick = {
            rollsAndroidViewModel.markGroupAsSelected(group)
        },
        label = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.GridView,
                    contentDescription = null,
                    modifier = Modifier
                        .size(FilterChipDefaults.IconSize)
                        .padding(end = 4.dp)
                )
                Text(
                    text = group.name
                )
            }
        },
        selected = isSelected,
        leadingIcon = if (isSelected) {
            {
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = group.name,
                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                )
            }
        } else {
            null
        },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = MaterialTheme.colorScheme.primary,
            selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
            selectedLeadingIconColor = MaterialTheme.colorScheme.onPrimary
        ),
    )
}
