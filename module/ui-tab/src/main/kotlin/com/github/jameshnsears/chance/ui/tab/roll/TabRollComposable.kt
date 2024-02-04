package com.github.jameshnsears.chance.ui.tab.roll

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.github.jameshnsears.chance.ui.tab.R
import com.github.jameshnsears.chance.ui.zoom.roll.ZoomRoll
import com.github.jameshnsears.chance.ui.zoom.roll.ZoomRollViewModel

class TabRollTestTag {
    companion object {
        const val UNDO = "UNDO"
        const val ROLL = "ROLL"
        const val HISTORY_CLEAR = "HISTORY_CLEAR"
    }
}

@Composable
fun TabRoll(tabRollViewModel: TabRollViewModel) {
    TabRollLayout(tabRollViewModel)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabRollLayout(tabRollViewModel: TabRollViewModel) {
    val scaffoldState = rememberBottomSheetScaffoldState()

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = 110.dp,
        sheetContent = {
            TabRollBottomSheetLayout(tabRollViewModel)
        },
    ) { _ ->
        ZoomRoll(
            ZoomRollViewModel(
                tabRollViewModel.settingsRepository,
                tabRollViewModel.bagRepository,
                tabRollViewModel.rollRepository,
            ),
        )
    }
}

@Composable
fun UndoRollButtons(tabRollViewModel: TabRollViewModel) {
    val undoIcon = painterResource(id = R.drawable.undo_fill0_wght400_grad0_opsz24)
    val rollIcon = painterResource(id = R.drawable.custom_casino_fill0_wght400_grad0_opsz24)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp, bottom = 8.dp),
        horizontalArrangement = Arrangement.Center,
    ) {
        Button(
            onClick = { /* Do something when clicked */ },
            modifier = Modifier
                .width(115.dp)
                .testTag(TabRollTestTag.UNDO),
        ) {
            Icon(
                undoIcon,
                contentDescription = stringResource(R.string.tab_roll_undo),
                modifier = Modifier.size(24.dp),
            )

            Spacer(Modifier.size(ButtonDefaults.IconSpacing))

            Text(stringResource(R.string.tab_roll_undo))
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { /* Do something when clicked */ },
            modifier = Modifier
                .padding(start = 18.dp)
                .width(115.dp)
                .testTag(TabRollTestTag.ROLL),
        ) {
            Icon(
                rollIcon,
                contentDescription = stringResource(R.string.tab_roll_roll),
                modifier = Modifier.size(24.dp),
            )

            Spacer(Modifier.size(ButtonDefaults.IconSpacing))

            Text(stringResource(R.string.tab_roll_roll))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiceSelection(tabRollViewModel: TabRollViewModel) {
    var selected by remember { mutableStateOf(false) }

    FilterChip(
        selected = selected,
        onClick = { selected = !selected },
        label = { Text("d2") },
        leadingIcon = if (!selected) {
            {
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = "Localized Description",
                    modifier = Modifier.size(FilterChipDefaults.IconSize),
                )
            }
        } else {
            null
        },
    )
}

@Composable
fun RollSequentially() {
    var checked by remember { mutableStateOf(true) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
            .clickable {
                checked = !checked
            },
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = stringResource(R.string.tab_roll_sequentially),
        )

        Switch(
            checked = checked,
            onCheckedChange = {
                checked = it
            },
        )
    }
}

@Composable
fun History(tabRollViewModel: TabRollViewModel) {
    val historyClearIcon =
        painterResource(id = R.drawable.delete_forever_fill0_wght400_grad0_opsz24)

    var checked by remember { mutableStateOf(true) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .clickable {
                checked = !checked
            },
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = stringResource(R.string.tab_roll_show_history),
        )

        Switch(
            checked = checked,
            onCheckedChange = {
                checked = it
            },
        )
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
    ) {
        Button(
            onClick = { /* Do something when clicked */ },
            modifier = Modifier
                .width(115.dp)
                .testTag(TabRollTestTag.HISTORY_CLEAR),
        ) {
            Icon(
                historyClearIcon,
                contentDescription = stringResource(R.string.tab_roll_show_history_clear),
                modifier = Modifier.size(24.dp),
            )

            Spacer(Modifier.size(ButtonDefaults.IconSpacing))

            Text(stringResource(R.string.tab_roll_show_history_clear))
        }
    }
}

@Composable
fun DiceTitle(tabRollViewModel: TabRollViewModel) {
    var checked by remember { mutableStateOf(true) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                checked = !checked
            },
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = stringResource(R.string.tab_roll_show_dice_title),
        )

        Switch(
            checked = checked,
            onCheckedChange = {
                checked = it
            },
        )
    }
}

@Composable
fun SideNumber(tabRollViewModel: TabRollViewModel) {
    var checked by remember { mutableStateOf(true) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                checked = !checked
            },
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = stringResource(R.string.tab_roll_show_side_number),
        )

        Switch(
            checked = checked,
            onCheckedChange = {
                checked = it
            },
        )
    }
}

@Composable
fun Score(tabRollViewModel: TabRollViewModel) {
    var checked by remember { mutableStateOf(true) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                checked = !checked
            },
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = stringResource(R.string.tab_roll_show_score),
        )

        Switch(
            checked = checked,
            onCheckedChange = {
                checked = it
            },
        )
    }
}

@Composable
fun RollSound(tabRollViewModel: TabRollViewModel) {
    var checked by remember { mutableStateOf(true) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                checked = !checked
            },
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = stringResource(R.string.tab_roll_use_sound),
        )

        Switch(
            checked = checked,
            onCheckedChange = {
                checked = it
            },
        )
    }
}

@Composable
fun TabRollBottomSheetLayout(tabRollViewModel: TabRollViewModel) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .height(480.dp),
    ) {
        UndoRollButtons(tabRollViewModel)

        Divider(Modifier.padding(bottom = 8.dp))

        DiceSelection(tabRollViewModel)

        RollSequentially()

        Divider()

        History(tabRollViewModel)

        Divider(Modifier.padding(bottom = 8.dp))

        Score(tabRollViewModel)

        DiceTitle(tabRollViewModel)

        SideNumber(tabRollViewModel)

        RollSound(tabRollViewModel)
    }
}
