package com.github.jameshnsears.chance.ui.tab.rolls

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.github.jameshnsears.chance.ui.tab.HapticHelper
import com.github.jameshnsears.chance.ui.tab.rolls.selection.RollSelectionRow
import com.github.jameshnsears.chance.ui.zoom.rolls.ZoomRoll
import com.github.jameshnsears.chance.ui.zoom.rolls.ZoomRollsAndroidViewModel


@Composable
fun TabRoll(
    rollsAndroidViewModel: RollsAndroidViewModel,
    zoomRollsAndroidViewModel: ZoomRollsAndroidViewModel,
) {
    val context = LocalContext.current

    val hapticHelper = remember { HapticHelper(context) }
    val rollsSoundPlayer = remember { RollsSoundPlayer(context) }
    val rollsScoreTtsPlayer = remember { RollsScoreTtsPlayer(context) }

    DisposableEffect(Unit) {
        onDispose {
            rollsSoundPlayer.release()
            rollsScoreTtsPlayer.release()
        }
    }

    LaunchedEffect(Unit) {
        rollsAndroidViewModel.sideEffectFlow.collect { sideEffect ->
            when (sideEffect) {
                is RollSideEffect.RollHaptic -> hapticHelper.playRollHaptic()
                is RollSideEffect.RollSound -> rollsSoundPlayer.play()
                is RollSideEffect.ScoreTTS -> rollsScoreTtsPlayer.playScore(sideEffect.score)
                is RollSideEffect.UndoHaptic -> hapticHelper.playUndoHaptic()
                is RollSideEffect.UndoAllHaptic -> hapticHelper.playUndoAllHaptic()
            }
        }
    }

    TabRollLayout(rollsAndroidViewModel, zoomRollsAndroidViewModel)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabRollLayout(
    rollsAndroidViewModel: RollsAndroidViewModel,
    zoomRollsAndroidViewModel: ZoomRollsAndroidViewModel,
) {
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState()

    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        sheetContainerColor = MaterialTheme.colorScheme.surfaceContainer,
        sheetPeekHeight = 134.dp,
        sheetContent = {
            Box(
                Modifier.testTag(RollsTestTag.BOTTOM_SHEET)
            ) {
                TabRollBottomSheetLayout(
                    rollsAndroidViewModel,
                    zoomRollsAndroidViewModel
                )
            }
        },
    ) {
        ZoomRoll(
            rollsAndroidViewModel,
            zoomRollsAndroidViewModel
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabRollBottomSheetLayout(
    rollsAndroidViewModel: RollsAndroidViewModel,
    zoomRollsAndroidViewModel: ZoomRollsAndroidViewModel
) {
    val isGestureNavigation = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding() < 40.dp
    val columnHeight = if (isGestureNavigation) 250.dp else 265.dp

    Column(
        Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp)
            .height(columnHeight)
            .navigationBarsPadding(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            UndoButton(rollsAndroidViewModel)

            RollButton(rollsAndroidViewModel, zoomRollsAndroidViewModel)
        }

        HorizontalDivider(
            modifier = Modifier
                .padding(top = 12.dp, bottom = 12.dp)
        )

        RollSelectionRow(
            rollsAndroidViewModel,
            zoomRollsAndroidViewModel
        )

        HorizontalDivider(
            modifier = Modifier
                .padding(top = 12.dp, bottom = 12.dp)
        )

        Settings(
            rollsAndroidViewModel
        )
    }
}
