package com.github.jameshnsears.chance.ui.tab.roll

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
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp


@Composable
fun TabRoll() {
    TabRollLayout()
}

@Composable
fun TabRollLayout() {
//    val sliderSidesValue = remember { mutableFloatStateOf(viewModel.sliderSidesPosition.value) }

    Column(modifier = Modifier.padding(10.dp)) {
        ElevatedCard(
            modifier = Modifier
                .padding(top = 8.dp, bottom = 8.dp)
                .fillMaxWidth(),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            ),
        ) {
            Column(modifier = Modifier.padding(10.dp)) {
                DiceFilter()

                RollSequentially()

                UndoRoll()

            }
        }
    }

    TabRollBottomSheet()
}

@Composable
fun UndoRoll() {
    val undoIcon = painterResource(id = R.drawable.undo_fill0_wght400_grad0_opsz24)
    val rollIcon = painterResource(id = R.drawable.custom_casino_fill0_wght400_grad0_opsz24)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, start = 8.dp, end = 8.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = { /* Do something when clicked */ },
            modifier = Modifier
                .width(140.dp)
                .testTag("bagButtonExport"),
        ) {
            Icon(
                undoIcon,
                contentDescription = stringResource(R.string.tab_roll_undo),
                modifier = Modifier.size(24.dp)
            )

            Spacer(Modifier.size(ButtonDefaults.IconSpacing))

            Text(stringResource(R.string.tab_roll_undo))
        }

        Spacer(modifier = Modifier.padding(horizontal = 10.dp))

        Button(
            onClick = { /* Do something when clicked */ },
            modifier = Modifier
                .width(140.dp)
                .testTag("bagButtonImport")
        ) {
            Icon(
                rollIcon,
                contentDescription = stringResource(R.string.tab_roll_roll),
                modifier = Modifier.size(24.dp)
            )

            Spacer(Modifier.size(ButtonDefaults.IconSpacing))

            Text(stringResource(R.string.tab_roll_roll))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiceFilter() {
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
                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                )
            }
        } else {
            null
        }
    )
}

@Composable
fun RollSequentially() {
    var checked by remember { mutableStateOf(true) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = stringResource(R.string.tab_roll_sequentially)
        )

        Switch(
            checked = checked,
            onCheckedChange = {
                checked = it
            }
        )
    }
}

@Composable
fun ShowHistory() {
    var checked by remember { mutableStateOf(true) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = stringResource(R.string.tab_roll_show_history)
        )

        Switch(
            checked = checked,
            onCheckedChange = {
                checked = it
            }
        )
    }
}

@Composable
fun ShowSideNumber() {
    var checked by remember { mutableStateOf(true) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = stringResource(R.string.tab_roll_show_side_number)
        )

        Switch(
            checked = checked,
            onCheckedChange = {
                checked = it
            }
        )
    }
}

@Composable
fun ShowSum() {
    var checked by remember { mutableStateOf(true) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = stringResource(R.string.tab_roll_show_sum)
        )

        Switch(
            checked = checked,
            onCheckedChange = {
                checked = it
            }
        )
    }
}

@Composable
fun UseSound() {
    var checked by remember { mutableStateOf(true) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = stringResource(R.string.tab_roll_use_sound)
        )

        Switch(
            checked = checked,
            onCheckedChange = {
                checked = it
            }
        )
    }
}

@Composable
fun Slider() {
    var sliderPosition by remember { mutableStateOf(0f) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(stringResource(R.string.tab_roll_zoom))

        Spacer(modifier = Modifier.width(10.dp))

        Slider(
            value = sliderPosition,
            onValueChange = { sliderPosition = it },
            valueRange = 0f..100f,
            onValueChangeFinished = {
                // launch some business logic update with the state you hold
                // viewModel.updateSelectedSliderValue(sliderPosition)
            },
            steps = 5,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabRollBottomSheet() {
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState()

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = 32.dp,
        sheetContent = {
            TabRollBottomSheetLayout()
        }
    ) {
    }
}

@Composable
fun TabRollBottomSheetLayout() {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .height(250.dp),
    ) {
        Slider()

        ShowHistory()

        ShowSideNumber()

        ShowSum()

        UseSound()
    }
}