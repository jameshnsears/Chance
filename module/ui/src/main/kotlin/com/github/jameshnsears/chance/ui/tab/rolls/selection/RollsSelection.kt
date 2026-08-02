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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.jameshnsears.chance.data.domain.core.Dice
import com.github.jameshnsears.chance.data.domain.core.group.Group
import com.github.jameshnsears.chance.ui.tab.rolls.RollsAndroidViewModel

@Composable
fun RollSelectionRow(rollsAndroidViewModel: RollsAndroidViewModel) {
    val stateDiceBag = rollsAndroidViewModel.diceBag.collectAsStateWithLifecycle()

    val stateGroupHistory = rollsAndroidViewModel.groupHistory.collectAsStateWithLifecycle()

    val diceBag = stateDiceBag.value
    val groupHistory = stateGroupHistory.value

    LazyRow(
        state = rollsAndroidViewModel.rollSelectionRowScrollState,
        modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
    ) {
        items(diceBag.size) { index ->
            RollSelectionFilterChip(rollsAndroidViewModel, diceBag[index])
        }
        items(groupHistory.size) { index ->
            RollSelectionGroupFilterChip(rollsAndroidViewModel, groupHistory[index])
        }
    }
}

@Composable
fun RollSelectionFilterChip(rollsAndroidViewModel: RollsAndroidViewModel, dice: Dice) {
    var selected by remember(dice.selected) { mutableStateOf(dice.selected) }

    FilterChip(
        modifier = Modifier
            .padding(end = 8.dp)
            .testTag(RollsSelectionTestTag.ROLL_BUTTON + dice.title),
        onClick = {
            selected = !selected
            rollsAndroidViewModel.markDiceAsSelected(dice, selected)
        },
        label = {
            Text(
                text = dice.title
            )
        },
        selected = selected,
        leadingIcon = if (selected) {
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
    group: Group
) {
    var selected by remember(group.selected) { mutableStateOf(group.selected) }

    FilterChip(
        modifier = Modifier
            .padding(end = 8.dp)
            .testTag(RollsSelectionTestTag.ROLL_BUTTON + group.name),
        onClick = {
            selected = !selected
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
        selected = selected,
        leadingIcon = if (selected) {
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
