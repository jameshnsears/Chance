package com.github.jameshnsears.chance.ui.tab.bag

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
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.github.jameshnsears.chance.ui.zoom.bag.ZoomBag
import com.github.jameshnsears.chance.ui.zoom.bag.ZoomBagAndroidViewModel
import kotlinx.coroutines.launch


@Composable
fun TabBag(
    tabBagAndroidViewModel: TabBagAndroidViewModel,
    zoomBagAndroidViewModel: ZoomBagAndroidViewModel
) {
    TabBagLayout(tabBagAndroidViewModel, zoomBagAndroidViewModel)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabBagLayout(
    tabBagAndroidViewModel: TabBagAndroidViewModel,
    zoomBagAndroidViewModel: ZoomBagAndroidViewModel
) {
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState()

    val coroutineScope = rememberCoroutineScope()

    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        sheetPeekHeight = 65.dp,
        sheetContent = {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
            ) {
                TabBagBottomSheetLayout(
                    bottomSheetScaffoldState,
                    tabBagAndroidViewModel,
                    zoomBagAndroidViewModel,
                )
            }
        }
    ) {
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
                zoomBagAndroidViewModel
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabBagBottomSheetLayout(
    bottomSheetScaffoldState: BottomSheetScaffoldState,
    tabBagAndroidViewModel: TabBagAndroidViewModel,
    zoomBagAndroidViewModel: ZoomBagAndroidViewModel
) {
    val isGestureNavigation = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding() < 40.dp
    val columnHeight = if (isGestureNavigation) 310.dp else 330.dp

    Column(
        Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .padding(start = 8.dp, end = 8.dp, top = 10.dp)
            .height(columnHeight)
            .navigationBarsPadding(),
    ) {
        Resize(
            tabBagAndroidViewModel,
            zoomBagAndroidViewModel,
        )

        HorizontalDivider(
            modifier = Modifier
                .padding(top = 12.dp, bottom = 12.dp)
        )

        Version(bottomSheetScaffoldState)

        HorizontalDivider(
            modifier = Modifier
                .padding(top = 12.dp, bottom = 12.dp)
        )

        ImportExport(
            bottomSheetScaffoldState,
            tabBagAndroidViewModel,
            zoomBagAndroidViewModel
        )

        HorizontalDivider(
            modifier = Modifier
                .padding(top = 12.dp, bottom = 12.dp)
        )

        ResetStorage(
            bottomSheetScaffoldState,
            tabBagAndroidViewModel
        )
    }
}
