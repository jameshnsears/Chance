package com.github.jameshnsears.chance.ui.zoom.bag.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.github.jameshnsears.chance.data.domain.core.Dice
import com.github.jameshnsears.chance.data.domain.core.Side
import com.github.jameshnsears.chance.ui.dialog.bag.compose.DialogBag
import com.github.jameshnsears.chance.ui.zoom.ZoomAndroidViewModel
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
        modifier = Modifier.padding(top = 8.dp, start = 8.dp, bottom = 110.dp, end = 8.dp),
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
                            cardDice,
                            cardSide
                        )

                        SideDescription(zoomAndroidViewModel, dice, side)

                        SideImageSVG(
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

@Composable
fun SideDescription(zoomAndroidViewModel: ZoomAndroidViewModel, dice: Dice, side: Side) {
    if (side.description != "") {
        Text(
            text = side.description,
            color = zoomAndroidViewModel.sideColor(side.descriptionColour)
        )
    } else if (zoomAndroidViewModel.diceContainsAtLeastOneSideWithDescription(dice)) {
        Text(text = " ")
    }
}

@Composable
fun SideImageShape(
    zoomAndroidViewModel: ZoomAndroidViewModel,
    dice: Dice,
    side: Side,
    showDialog: MutableState<Boolean>,
    cardDice: MutableState<Dice>,
    dialogSide: MutableState<Side>,
) {
    val stateFlowZoom =
        zoomAndroidViewModel.stateFlowZoom.collectAsStateWithLifecycle(
            lifecycleOwner = androidx.compose.ui.platform.LocalLifecycleOwner.current
        )

    val resizeView = stateFlowZoom.value.resizeView

    Box {
        Image(
            painter = painterResource(zoomAndroidViewModel.sideImageShapeNumberShape(dice)),
            contentDescription = "",
            modifier = Modifier
                .size(resizeView)
                .padding(top = 8.dp)
                .clickable {
                    cardDice.value = dice
                    dialogSide.value = side
                    showDialog.value = true
                },
            colorFilter = zoomAndroidViewModel.sideColourFilter(dice.colour),
            contentScale = ContentScale.Crop
        )

        Text(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(top = 0.dp),
            fontSize = zoomAndroidViewModel.sideImageShapeNumberFontSize(),
            text = "${side.number}",
            color = zoomAndroidViewModel.sideColor(side.numberColour),
        )
    }
}

@Composable
fun SideImageSVG(
    zoomAndroidViewModel: ZoomAndroidViewModel,
    dice: Dice,
    side: Side,
    showDialog: MutableState<Boolean>,
    cardDice: MutableState<Dice>,
    cardSide: MutableState<Side>
) {
    val stateFlowZoom =
        zoomAndroidViewModel.stateFlowZoom.collectAsStateWithLifecycle(
            lifecycleOwner = androidx.compose.ui.platform.LocalLifecycleOwner.current
        )

    val resizeView = stateFlowZoom.value.resizeView

    val modifier = Modifier
        .size(resizeView)
        .padding(top = 8.dp)
        .clickable {
            cardDice.value = dice
            cardSide.value = side
            showDialog.value = true
        }

    if (side.imageDrawableId != 0) {
        Image(
            painter = painterResource(id = side.imageDrawableId),
            contentDescription = "",
            modifier = modifier
        )
    } else {
        if (side.imageBase64 != "") {
            val imageRequest: ImageRequest = remember {
                zoomAndroidViewModel.sideImageSVG(side)
            }

            AsyncImage(
                model = imageRequest,
                contentDescription = "",
                modifier = modifier
            )
        }
    }
}
