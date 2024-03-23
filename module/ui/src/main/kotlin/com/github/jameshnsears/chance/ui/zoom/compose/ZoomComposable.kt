package com.github.jameshnsears.chance.ui.zoom.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.github.jameshnsears.chance.ui.dialog.bag.compose.DialogBag
import com.github.jameshnsears.chance.ui.zoom.ZoomAndroidViewModel

@Composable
fun SideImageNumberDialog(
    zoomAndroidViewModel: ZoomAndroidViewModel,
    dice: Dice,
    side: Side,
) {
    val showDialog = rememberSaveable { mutableStateOf(false) }

    val resize = zoomAndroidViewModel.resizeView.collectAsStateWithLifecycle()

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
    val resize = zoomAndroidViewModel.resizeView.collectAsStateWithLifecycle()

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
    val resize = zoomAndroidViewModel.resizeView.collectAsStateWithLifecycle()

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
fun SideDescriptionBag(zoomAndroidViewModel: ZoomAndroidViewModel, dice: Dice, side: Side) {
    if (side.description != "") {
        SideDescriptionFromUser(side, zoomAndroidViewModel)
    } else if (side.descriptionStringsId != 0) {
        SideDescriptionFromResources(side, zoomAndroidViewModel)
    } else if (zoomAndroidViewModel.diceContainsAtLeastOneSideWithDescription(dice)) {
        Text(text = " ")
    }
}

@Composable
fun SideDescriptionRoll(zoomAndroidViewModel: ZoomAndroidViewModel, dice: Dice, side: Side) {
    if (side.description != "") {
        SideDescriptionFromUser(side, zoomAndroidViewModel)
    } else if (side.descriptionStringsId != 0) {
        SideDescriptionFromResources(side, zoomAndroidViewModel)
    } else if (side.imageDrawableId != 0 || side.imageBase64 != "") {
        Text(text = " ")
    }
}

@Composable
private fun SideDescriptionFromResources(
    side: Side,
    zoomAndroidViewModel: ZoomAndroidViewModel
) {
    Text(
        text = stringResource(id = side.descriptionStringsId),
        color = zoomAndroidViewModel.sideColor(side.descriptionColour),
    )
}

@Composable
private fun SideDescriptionFromUser(
    side: Side,
    zoomAndroidViewModel: ZoomAndroidViewModel
) {
    Text(
        text = side.description,
        color = zoomAndroidViewModel.sideColor(side.descriptionColour),
    )
}