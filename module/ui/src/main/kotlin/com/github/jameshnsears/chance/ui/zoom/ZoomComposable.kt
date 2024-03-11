package com.github.jameshnsears.chance.ui.zoom

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.github.jameshnsears.chance.data.domain.state.Dice
import com.github.jameshnsears.chance.data.domain.state.Side
import com.github.jameshnsears.chance.ui.dialog.bag.DialogBag

@Composable
fun SideImageNumberDialog(
    zoomAndroidViewModel: ZoomAndroidViewModel,
    dice: Dice,
    side: Side,
) {
    val showDialog = remember { mutableStateOf(false) }

    val resize = zoomAndroidViewModel.resize.collectAsStateWithLifecycle()

    Box {
        Image(
            painter = painterResource(zoomAndroidViewModel.sideImageNumberShape(dice)),
            contentDescription = null,
            modifier = Modifier
                .padding(vertical = 5.dp)
                .size(resize.value)
                .clickable { showDialog.value = true },
            colorFilter = zoomAndroidViewModel.sideColourFilter(dice.colour),
            contentScale = ContentScale.Crop
        )

        Text(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(top = zoomAndroidViewModel.sideImageNumberPaddingTop(dice)),
            fontSize = zoomAndroidViewModel.sideImageNumberFontSize(dice),
            text = "${side.number}",
            color = zoomAndroidViewModel.sideColor(side.numberColour),
        )

        if (showDialog.value) {
            DialogBag(
                showDialog,
                zoomAndroidViewModel.repositoryBag,
                dice,
                side,
            )
        }
    }
}

@Composable
fun SideImageNumber(
    zoomAndroidViewModel: ZoomAndroidViewModel,
    dice: Dice,
    side: Side,
) {
    val resize = zoomAndroidViewModel.resize.collectAsStateWithLifecycle()

    Box {
        Image(
            painter = painterResource(zoomAndroidViewModel.sideImageNumberShape(dice)),
            contentDescription = null,
            modifier = Modifier
                .padding(vertical = 5.dp)
                .size(resize.value),
            colorFilter = zoomAndroidViewModel.sideColourFilter(dice.colour),
        )

        Text(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(top = zoomAndroidViewModel.sideImageNumberPaddingTop(dice)),
            fontSize = zoomAndroidViewModel.sideImageNumberFontSize(dice),
            text = "${side.number}",
            color = zoomAndroidViewModel.sideColor(side.numberColour),
        )
    }
}

@Composable
fun DiceTitle(dice: Dice) {
    val title = if (dice.title != "")
        dice.title
    else
        stringResource(id = dice.titleStringsId)

    Text(
        text = title
    )
}

@Composable
fun SideImageSVG(
    zoomAndroidViewModel: ZoomAndroidViewModel,
    side: Side
) {
    val resize = zoomAndroidViewModel.resize.collectAsStateWithLifecycle()

    if (side.imageDrawableId != 0) {
        Image(
            painter = painterResource(id = side.imageDrawableId),
            contentDescription = null,
            modifier = Modifier.size(resize.value),
        )
    } else {
        if (side.imageBase64 != "") {
            AsyncImage(
                model = zoomAndroidViewModel.sideImageSVG(side),
                contentDescription = "",
                modifier = Modifier.size(resize.value),
            )
        }
    }
}

@Composable
fun SideDescription(zoomAndroidViewModel: ZoomAndroidViewModel, side: Side) {
    if (side.description != "") {
        Text(
            text = side.description,
            color = zoomAndroidViewModel.sideColor(side.descriptionColour),
        )
    } else if (side.descriptionStringsId != 0) {
        Text(
            text = stringResource(id = side.descriptionStringsId),
            color = zoomAndroidViewModel.sideColor(side.descriptionColour),
        )
    }
}
