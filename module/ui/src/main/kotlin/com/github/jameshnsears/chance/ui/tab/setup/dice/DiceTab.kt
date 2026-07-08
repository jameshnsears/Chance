package com.github.jameshnsears.chance.ui.tab.setup.dice

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.github.jameshnsears.chance.ui.zoom.setup.dice.ZoomBag
import com.github.jameshnsears.chance.ui.zoom.setup.dice.ZoomDiceAndroidViewModel
import kotlinx.coroutines.launch

@Composable
fun TabBagDice(
    diceAndroidViewModel: DiceAndroidViewModel,
    zoomDiceAndroidViewModel: ZoomDiceAndroidViewModel
) {
    TabBagDiceLayout(
        diceAndroidViewModel,
        zoomDiceAndroidViewModel
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabBagDiceLayout(
    diceAndroidViewModel: DiceAndroidViewModel,
    zoomDiceAndroidViewModel: ZoomDiceAndroidViewModel
) {
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState()

    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        sheetContainerColor = MaterialTheme.colorScheme.surfaceContainer,
        sheetPeekHeight = 72.dp,
        sheetContent = {
            TabBagBottomSheetLayout(
                bottomSheetScaffoldState,
                diceAndroidViewModel,
                zoomDiceAndroidViewModel
            )
        }
    ) {
        TabBag(
            bottomSheetScaffoldState,
            zoomDiceAndroidViewModel
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabBag(
    bottomSheetScaffoldState: BottomSheetScaffoldState,
    zoomDiceAndroidViewModel: ZoomDiceAndroidViewModel,
) {
    val coroutineScope = rememberCoroutineScope()

    Box(
        Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent()
                        if (event.changes.first().pressed) {
                            if (bottomSheetScaffoldState.bottomSheetState.hasExpandedState) {
                                coroutineScope.launch {
                                    bottomSheetScaffoldState.bottomSheetState.partialExpand()
                                }
                            }
                        }

                        // propagate tap...
                    }
                }
            }
    ) {
        ZoomBag(
            zoomDiceAndroidViewModel
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabBagBottomSheetLayout(
    bottomSheetScaffoldState: BottomSheetScaffoldState,
    diceAndroidViewModel: DiceAndroidViewModel,
    zoomDiceAndroidViewModel: ZoomDiceAndroidViewModel
) {
    val isGestureNavigation = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding() < 40.dp
    val columnHeight = if (isGestureNavigation) 330.dp else 360.dp

    Column(
        Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 16.dp)
            .height(columnHeight)
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState())
            .testTag(DiceTestTag.BOTTOM_SHEET),
    ) {
        Resize(
            diceAndroidViewModel,
            zoomDiceAndroidViewModel,
        )

        HorizontalDivider(
            modifier = Modifier
                .padding(top = 12.dp, bottom = 12.dp)
        )

        ImportExport(
            bottomSheetScaffoldState,
            diceAndroidViewModel,
            zoomDiceAndroidViewModel
        )

        HorizontalDivider(
            modifier = Modifier
                .padding(top = 12.dp, bottom = 12.dp)
        )

        ResetStorage(
            bottomSheetScaffoldState,
            diceAndroidViewModel
        )

        HorizontalDivider(
            modifier = Modifier
                .padding(top = 12.dp, bottom = 12.dp)
        )

        Version(bottomSheetScaffoldState)
    }
}
