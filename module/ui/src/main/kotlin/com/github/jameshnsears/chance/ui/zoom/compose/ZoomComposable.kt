package com.github.jameshnsears.chance.ui.zoom.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.github.jameshnsears.chance.data.domain.core.Dice
import com.github.jameshnsears.chance.data.domain.core.Side
import com.github.jameshnsears.chance.ui.zoom.ZoomAndroidViewModel
import com.github.jameshnsears.chance.utility.feature.UtilityFeature

@Composable
fun ZoomSideDescription(zoomAndroidViewModel: ZoomAndroidViewModel, dice: Dice, side: Side) {
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
fun ZoomSideImageShape(
    zoomAndroidViewModel: ZoomAndroidViewModel,
    dice: Dice,
    side: Side,
    showDialog: MutableState<Boolean>,
    cardDice: MutableState<Dice>,
    cardSide: MutableState<Side>,
) {
    val stateFlowZoom =
        zoomAndroidViewModel.stateFlowZoom.collectAsStateWithLifecycle(
            lifecycleOwner = LocalLifecycleOwner.current
        )

    val resizeView = stateFlowZoom.value.resizeView

    if (UtilityFeature.isEnabled(UtilityFeature.Flag.SHOW_EPOCH_UUID))
        Row {
            Text(text = side.uuid)
        }

    Box {
        Image(
            painter = painterResource(zoomAndroidViewModel.sideImageShapeNumberShape(dice)),
            contentDescription = "",
            modifier = Modifier
                .size(resizeView)
                .padding(top = 8.dp)
                .clickable {
                    cardDice.value = dice
                    cardSide.value = side
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
fun ZoomSideImageSVG(
    zoomAndroidViewModel: ZoomAndroidViewModel,
    dice: Dice,
    side: Side,
    showDialog: MutableState<Boolean>,
    cardDice: MutableState<Dice>,
    cardSide: MutableState<Side>
) {
    val stateFlowZoom =
        zoomAndroidViewModel.stateFlowZoom.collectAsStateWithLifecycle(
            lifecycleOwner = LocalLifecycleOwner.current
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
