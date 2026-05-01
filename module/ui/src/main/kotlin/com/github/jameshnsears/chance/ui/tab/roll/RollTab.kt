package com.github.jameshnsears.chance.ui.tab.roll

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.github.jameshnsears.chance.ui.tab.roll.selection.RollSelectionRow
import com.github.jameshnsears.chance.ui.zoom.roll.ZoomRoll
import com.github.jameshnsears.chance.ui.zoom.roll.ZoomRollAndroidViewModel


@Composable
fun TabRoll(
    rollAndroidViewModel: RollAndroidViewModel,
    zoomRollAndroidViewModel: ZoomRollAndroidViewModel,
) {
    TabRollLayout(rollAndroidViewModel, zoomRollAndroidViewModel)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabRollLayout(
    rollAndroidViewModel: RollAndroidViewModel,
    zoomRollAndroidViewModel: ZoomRollAndroidViewModel,
) {
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState()

    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        sheetPeekHeight = 125.dp,
        sheetContent = {
            Box(
                Modifier.testTag(RollTestTag.BOTTOM_SHEET)
            ) {
                TabRollBottomSheetLayout(
                    rollAndroidViewModel,
                    bottomSheetScaffoldState
                )
            }
        },
    ) {
        ZoomRoll(
            rollAndroidViewModel,
            zoomRollAndroidViewModel
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabRollBottomSheetLayout(
    rollAndroidViewModel: RollAndroidViewModel,
    bottomSheetScaffoldState: BottomSheetScaffoldState
) {
    val isGestureNavigation = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding() < 40.dp
    val columnHeight = if (isGestureNavigation) 210.dp else 250.dp

    Column(
        Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .padding(start = 8.dp, end = 8.dp)
            .height(columnHeight)
            .navigationBarsPadding(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp, bottom = 8.dp),
            horizontalArrangement = Arrangement.Center,
        ) {
            UndoButton(rollAndroidViewModel)

            RollButton(rollAndroidViewModel)
        }

        HorizontalDivider(
            modifier = Modifier
                .padding(top = 12.dp, bottom = 12.dp)
        )

        RollSelectionRow(rollAndroidViewModel)

        HorizontalDivider(
            modifier = Modifier
                .padding(top = 12.dp, bottom = 12.dp)
        )

        Settings(
            rollAndroidViewModel,
            bottomSheetScaffoldState
        )
    }
}
