package com.github.jameshnsears.chance.ui.tab.roll.selection.compose

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.jameshnsears.chance.data.domain.core.Dice
import com.github.jameshnsears.chance.ui.tab.roll.TabRollAndroidViewModel

class RollSelectionTestTag {
    companion object {
        const val ROLL_BUTTON = "ROLL_BUTTON-"
    }
}

@Composable
fun RollSelectionRow(tabRollAndroidViewModel: TabRollAndroidViewModel) {
    val stateDiceBag =
        tabRollAndroidViewModel.diceBag.collectAsStateWithLifecycle(
            lifecycleOwner = LocalLifecycleOwner.current
        )

    val diceBag = stateDiceBag.value

    LazyRow {
        items(diceBag.size) { index ->
            RollSectionFilterChip(tabRollAndroidViewModel, diceBag[index])
        }
    }
}

@Composable
fun RollSectionFilterChip(tabRollAndroidViewModel: TabRollAndroidViewModel, dice: Dice) {
    var selected by remember { mutableStateOf(dice.selected) }

    FilterChip(
        modifier = Modifier
            .padding(start = 12.dp, top = 4.dp, bottom = 4.dp, end = 12.dp)
            .testTag(RollSelectionTestTag.ROLL_BUTTON + dice.title),
        onClick = {
            selected = !selected
            tabRollAndroidViewModel.markDiceAsSelected(dice, !dice.selected)
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
                    contentDescription = "",
                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                )
            }
        } else {
            null
        },
    )
}
