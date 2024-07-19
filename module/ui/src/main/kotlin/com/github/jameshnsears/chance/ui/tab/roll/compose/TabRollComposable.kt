package com.github.jameshnsears.chance.ui.tab.roll.compose

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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.jameshnsears.chance.ui.R
import com.github.jameshnsears.chance.ui.dialog.settings.compose.DialogSettings
import com.github.jameshnsears.chance.ui.tab.roll.TabRollAndroidViewModel
import com.github.jameshnsears.chance.ui.tab.roll.selection.compose.RollSelectionRow
import com.github.jameshnsears.chance.ui.zoom.ZoomAndroidViewModel
import com.github.jameshnsears.chance.ui.zoom.roll.compose.ZoomRoll
import kotlinx.coroutines.launch

class TabRollTestTag {
    companion object {
        const val UNDO = "UNDO"
        const val ROLL = "ROLL"
        const val SETTINGS = "SETTINGS"
    }
}

@Composable
fun TabRoll(
    tabRollAndroidViewModel: TabRollAndroidViewModel,
    zoomAndroidViewModel: ZoomAndroidViewModel,
) {
    TabRollLayout(tabRollAndroidViewModel, zoomAndroidViewModel)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabRollLayout(
    tabRollAndroidViewModel: TabRollAndroidViewModel,
    zoomAndroidViewModel: ZoomAndroidViewModel,
) {
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState()

    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        sheetPeekHeight = 110.dp,
        sheetContent = {
            TabRollBottomSheetLayout(
                tabRollAndroidViewModel,
                bottomSheetScaffoldState
            )
        },
    ) {
        ZoomRoll(
            tabRollAndroidViewModel,
            zoomAndroidViewModel
        )
    }
}

@Composable
fun Undo(tabRollAndroidViewModel: TabRollAndroidViewModel) {
    val stateFlowUndoEnabled =
        tabRollAndroidViewModel.undoEnabled.collectAsStateWithLifecycle(
            lifecycleOwner = androidx.compose.ui.platform.LocalLifecycleOwner.current
        )
    val undoEnabled = stateFlowUndoEnabled.value

    Button(
        onClick = {
            tabRollAndroidViewModel.undo()
        },
        enabled = undoEnabled,
        modifier = Modifier
            .width(120.dp)
            .testTag(TabRollTestTag.UNDO),
    ) {
        Icon(
            painterResource(id = R.drawable.undo_fill0_wght400_grad0_opsz24),
            contentDescription = "",
            modifier = Modifier.size(24.dp),
        )

        Spacer(Modifier.size(ButtonDefaults.IconSpacing))

        Text(stringResource(R.string.tab_roll_undo))
    }
}

@Composable
fun Roll(tabRollAndroidViewModel: TabRollAndroidViewModel) {
    val stateFlowRollEnabled =
        tabRollAndroidViewModel.rollEnabled.collectAsStateWithLifecycle(
            lifecycleOwner = androidx.compose.ui.platform.LocalLifecycleOwner.current
        )
    val rollEnabled = stateFlowRollEnabled.value

    Button(
        onClick = {
            tabRollAndroidViewModel.rollDiceSequence()
        },
        modifier = Modifier
            .padding(start = 18.dp)
            .width(230.dp)
            .testTag(TabRollTestTag.ROLL),
        enabled = rollEnabled
    ) {
        Icon(
            painterResource(id = R.drawable.custom_casino_fill0_wght400_grad0_opsz24),
            contentDescription = "",
            modifier = Modifier.size(24.dp),
        )

        Spacer(Modifier.size(ButtonDefaults.IconSpacing))

        Text(stringResource(R.string.tab_roll_roll))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Settings(
    tabRollAndroidViewModel: TabRollAndroidViewModel,
    bottomSheetScaffoldState: BottomSheetScaffoldState
) {
    val showDialog = rememberSaveable { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, top = 8.dp, bottom = 8.dp)
            .testTag(TabRollTestTag.SETTINGS)
    ) {
        Text(
            text = stringResource(R.string.tab_roll_settings),
        )

        Spacer(modifier = Modifier.weight(1f))

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
                contentDescription = "",
            )
        }
    }

    if (showDialog.value) {
        DialogSettings(
            showDialog,
            tabRollAndroidViewModel
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabRollBottomSheetLayout(
    tabRollAndroidViewModel: TabRollAndroidViewModel,
    bottomSheetScaffoldState: BottomSheetScaffoldState
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .padding(start = 8.dp, end = 8.dp)
            .height(315.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp, bottom = 8.dp),
            horizontalArrangement = Arrangement.Center,
        ) {
            Undo(tabRollAndroidViewModel)

            Spacer(modifier = Modifier.width(20.dp))

            Roll(tabRollAndroidViewModel)
        }

        HorizontalDivider(
            modifier = Modifier
                .padding(top = 12.dp, bottom = 8.dp)
        )

        RollSelectionRow(tabRollAndroidViewModel)

        HorizontalDivider(
            modifier = Modifier
                .padding(top = 12.dp, bottom = 12.dp)
        )

        Settings(
            tabRollAndroidViewModel,
            bottomSheetScaffoldState
        )
    }
}
