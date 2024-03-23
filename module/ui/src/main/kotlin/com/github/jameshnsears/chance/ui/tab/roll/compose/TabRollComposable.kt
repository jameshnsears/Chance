package com.github.jameshnsears.chance.ui.tab.roll.compose

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
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.github.jameshnsears.chance.ui.R
import com.github.jameshnsears.chance.ui.dialog.roll.compose.DialogRoll
import com.github.jameshnsears.chance.ui.tab.roll.TabRollViewModel
import com.github.jameshnsears.chance.ui.tab.roll.card.compose.RollCards
import com.github.jameshnsears.chance.ui.zoom.ZoomAndroidViewModel
import com.github.jameshnsears.chance.ui.zoom.roll.compose.ZoomRoll
import kotlinx.coroutines.launch

class TabRollTestTag {
    companion object {
        const val UNDO = "UNDO"
        const val ROLL = "ROLL"
        const val ROLL_SEQUENTIAL = "ROLL_SEQUENTIAL"
        const val SETTINGS = "SETTINGS"
    }
}

@Composable
fun TabRoll(
    tabRollViewModel: TabRollViewModel,
    zoomAndroidViewModel: ZoomAndroidViewModel,
) {
    TabRollLayout(tabRollViewModel, zoomAndroidViewModel)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabRollLayout(
    tabRollViewModel: TabRollViewModel,
    zoomAndroidViewModel: ZoomAndroidViewModel,
) {
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState()

    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        sheetPeekHeight = 110.dp,
        sheetContent = {
            TabRollBottomSheetLayout(
                tabRollViewModel,
                bottomSheetScaffoldState
            )
        },
    ) {
        ZoomRoll(
            tabRollViewModel,
            zoomAndroidViewModel
        )
    }
}

@Composable
fun UndoRoll(tabRollViewModel: TabRollViewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp, bottom = 8.dp),
        horizontalArrangement = Arrangement.Center,
    ) {
        Undo(tabRollViewModel)

        Spacer(modifier = Modifier.width(20.dp))

        Roll(tabRollViewModel)
    }
}

@Composable
fun Undo(tabRollViewModel: TabRollViewModel) {
    Button(
        onClick = {
            tabRollViewModel.undo()
        },
        modifier = Modifier
            .width(115.dp)
            .testTag(TabRollTestTag.UNDO),
    ) {
        Icon(
            painterResource(id = R.drawable.undo_fill0_wght400_grad0_opsz24),
            contentDescription = stringResource(R.string.tab_roll_undo),
            modifier = Modifier.size(24.dp),
        )

        Spacer(Modifier.size(ButtonDefaults.IconSpacing))

        Text(stringResource(R.string.tab_roll_undo))
    }
}

@Composable
fun Roll(tabRollViewModel: TabRollViewModel) {
    Button(
        onClick = {
            tabRollViewModel.roll()
        },
        modifier = Modifier
            .padding(start = 18.dp)
            .width(230.dp)
            .testTag(TabRollTestTag.ROLL),
    ) {
        Icon(
            painterResource(id = R.drawable.custom_casino_fill0_wght400_grad0_opsz24),
            contentDescription = stringResource(R.string.tab_roll_roll),
            modifier = Modifier.size(24.dp),
        )

        Spacer(Modifier.size(ButtonDefaults.IconSpacing))

        Text(stringResource(R.string.tab_roll_roll))
    }
}

@Composable
fun RollSequential(tabRollViewModel: TabRollViewModel) {
    val stateSequential =
        rememberSaveable { mutableStateOf(tabRollViewModel.stateSequential.value) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
            .clickable {
                stateSequential.value = !stateSequential.value
                tabRollViewModel.sequential(stateSequential.value)
            }
            .testTag(TabRollTestTag.ROLL_SEQUENTIAL),
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = stringResource(R.string.tab_roll_sequentially),
        )

        Switch(
            checked = stateSequential.value,
            onCheckedChange = {
                stateSequential.value = it
                tabRollViewModel.sequential(it)
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Settings(
    tabRollViewModel: TabRollViewModel,
    bottomSheetScaffoldState: BottomSheetScaffoldState
) {
    val showDialog = rememberSaveable { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .testTag(TabRollTestTag.SETTINGS)
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = stringResource(R.string.tab_roll_settings),
        )

        IconButton(
            onClick = {
                showDialog.value = true

                coroutineScope.launch {
                    bottomSheetScaffoldState.bottomSheetState.partialExpand()
                }
            }
        ) {
            Icon(
                Icons.Outlined.Settings,
                contentDescription = stringResource(R.string.tab_roll_settings)
            )
        }
    }

    if (showDialog.value) {
        DialogRoll(
            showDialog,
            tabRollViewModel
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabRollBottomSheetLayout(
    tabRollViewModel: TabRollViewModel,
    bottomSheetScaffoldState: BottomSheetScaffoldState
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .height(270.dp),
    ) {
        UndoRoll(tabRollViewModel)

        HorizontalDivider()

        RollCards(tabRollViewModel)

        RollSequential(tabRollViewModel)

        HorizontalDivider()

        Settings(
            tabRollViewModel,
            bottomSheetScaffoldState
        )
    }
}
