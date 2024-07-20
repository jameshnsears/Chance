package com.github.jameshnsears.chance.ui.zoom.bag.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.jameshnsears.chance.data.domain.core.Dice
import com.github.jameshnsears.chance.data.domain.core.Side
import com.github.jameshnsears.chance.ui.dialog.bag.compose.DialogBag
import com.github.jameshnsears.chance.ui.zoom.ZoomAndroidViewModel
import com.github.jameshnsears.chance.ui.zoom.compose.DiceTitle
import com.github.jameshnsears.chance.ui.zoom.compose.SideDescription
import com.github.jameshnsears.chance.ui.zoom.compose.SideImageSVG
import com.github.jameshnsears.chance.ui.zoom.compose.SideImageShape

fun <T> SnapshotStateList<T>.swapList(list: List<T>) {
    clear()
    addAll(list)
}

@Composable
fun ZoomBag(
    zoomAndroidViewModel: ZoomAndroidViewModel
) {
    val stateFlowZoom =
        zoomAndroidViewModel.stateFlowZoom.collectAsStateWithLifecycle(
            lifecycleOwner = androidx.compose.ui.platform.LocalLifecycleOwner.current
        )

    val diceListState = remember { mutableStateListOf<Dice>() }
    diceListState.swapList(stateFlowZoom.value.diceBag)

    val showDialog = remember { mutableStateOf(false) }
    val dialogDice = remember { mutableStateOf(Dice()) }
    val dialogSide = remember { mutableStateOf(Side()) }

    LazyColumn(
        modifier = Modifier.padding(top = 8.dp, start = 8.dp, bottom = 110.dp, end = 8.dp),
    ) {
        itemsIndexed(
            items = diceListState,
            key = { _, item -> item.uuid }
        ) { index, dice ->
            DiceTitle(dice)

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.Top,
                modifier = Modifier.padding(bottom = 12.dp),
            ) {
                items(
                    dice.sides,
                    key = {
                        it.uuid
                    }
                ) { side ->
                    Column(
                        Modifier.padding(start = 9.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        SideImageShape(
                            zoomAndroidViewModel,
                            dice,
                            side,
                            showDialog,
                            dialogDice,
                            dialogSide
                        )

                        SideDescription(zoomAndroidViewModel, dice, side)

                        SideImageSVG(
                            zoomAndroidViewModel,
                            dice,
                            side,
                            showDialog,
                            dialogDice,
                            dialogSide
                        )
                    }
                }
            }

            if (index < stateFlowZoom.value.diceBag.size)
                HorizontalDivider(Modifier.padding(bottom = 12.dp))
        }
    }

    if (showDialog.value) {
        DialogBag(
            showDialog,
            zoomAndroidViewModel.repositoryBag,
            dialogDice.value,
            dialogSide.value,
        )
    }
}
