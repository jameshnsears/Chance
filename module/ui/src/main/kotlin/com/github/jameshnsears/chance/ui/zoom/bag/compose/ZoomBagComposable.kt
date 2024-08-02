package com.github.jameshnsears.chance.ui.zoom.bag.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
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
import com.github.jameshnsears.chance.ui.zoom.compose.ZoomSideDescription
import com.github.jameshnsears.chance.ui.zoom.compose.ZoomSideImageSVG
import com.github.jameshnsears.chance.ui.zoom.compose.ZoomSideImageShape
import timber.log.Timber

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

    val cardDice = remember { mutableStateOf(Dice()) }
    val cardSide = remember { mutableStateOf(Side()) }

    val showDialog = remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier.padding(top = 8.dp, start = 0.dp, bottom = 110.dp, end = 8.dp),
    ) {

        itemsIndexed(
            items = diceListState,
            key = { index, item -> "${item.uuid}_${index}" }
        ) { index, dice ->
            Row {
                Text(
                    text = dice.title,
                )
            }

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
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
                        ZoomSideImageShape(
                            zoomAndroidViewModel,
                            dice,
                            side,
                            showDialog,
                            cardDice,
                            cardSide
                        )

                        ZoomSideDescription(zoomAndroidViewModel, dice, side)

                        ZoomSideImageSVG(
                            zoomAndroidViewModel,
                            dice,
                            side,
                            showDialog,
                            cardDice,
                            cardSide
                        )
                    }
                }
            }

            if (index < stateFlowZoom.value.diceBag.size)
                HorizontalDivider(Modifier.padding(bottom = 12.dp))
        }
    }

    if (showDialog.value) {
        Timber.d("ZoomBag: dice.epoch=${cardDice.value.epoch}; side.uuid=${cardSide.value.uuid}")

        DialogBag(
            showDialog,
            zoomAndroidViewModel.repositoryBag,
            cardDice.value,
            cardSide.value,
        )
    }
}
