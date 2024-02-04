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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.github.jameshnsears.chance.data.domain.Dice
import com.github.jameshnsears.chance.data.domain.Side
import com.github.jameshnsears.chance.ui.dialog.bag.DialogBag
import com.github.jameshnsears.chance.ui.zoom.roll.ZoomRollViewModel

@Composable
fun SideOutlineBag(
    zoomBagViewModel: ZoomViewModel,
    dice: Dice,
    side: Side,
) {
    val showDialog = remember { mutableStateOf(false) }

    Box {
        Image(
            painter = painterResource(zoomBagViewModel.diceShape(dice)),
            contentDescription = null,
            modifier = Modifier
                .padding(horizontal = 5.dp, vertical = 5.dp)
                .size(zoomBagViewModel.scale())
                .clickable {
                    showDialog.value = true
                },
            colorFilter = zoomBagViewModel.diceColor(dice.colour),
        )

        Text(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(top = zoomBagViewModel.sideNumberPaddingTop(dice)),
            fontSize = zoomBagViewModel.sideNumberFontSize(dice),
            text = "${side.number}",
            color = zoomBagViewModel.sideColorText(side.colour),
        )

        if (showDialog.value) {
            DialogBag(
                showDialog,
                zoomBagViewModel.bagRepository,
                dice,
                side,
            )
        }
    }
}

@Composable
fun SideOutlineRoll(
    zoomRollViewModel: ZoomRollViewModel,
    dice: Dice,
    side: Side,
) {
    Box {
        Image(
            painter = painterResource(zoomRollViewModel.diceShape(dice)),
            contentDescription = null,
            modifier = Modifier
                .padding(vertical = 5.dp)
                .size(zoomRollViewModel.scale()),
            colorFilter = zoomRollViewModel.diceColor(dice.colour),
        )

        Text(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(top = zoomRollViewModel.sideNumberPaddingTop(dice)),
            fontSize = zoomRollViewModel.sideNumberFontSize(dice),
            text = "${side.number}",
            color = zoomRollViewModel.sideColorText(side.colour),
        )
    }
}

@Composable
fun DiceDescription(dice: Dice) {
    val description = if (dice.title != "")
        dice.title
    else
        stringResource(id = dice.titleStringsId)

    Text(
        text = description,
        modifier = Modifier.padding(top = 8.dp),
    )
}

@Composable
fun SideImageSVG(zoomViewModel: ZoomViewModel, side: Side) {
    if (zoomViewModel.imageDrawableIdAvailable(side)) {
        Image(
            painter = painterResource(id = side.imageDrawableId),
            contentDescription = null,
            modifier = Modifier
                .size(zoomViewModel.scale()),
        )
    }
}

@Composable
fun SideDescription(zoomViewModel: ZoomViewModel, side: Side) {
    if (side.description != "") {
        Text(
            text = side.description,
            color = zoomViewModel.sideColorText(side.descriptionColour),
        )
    } else if (side.descriptionStringsId != 0) {
        Text(
            text = stringResource(id = side.descriptionStringsId),
            color = zoomViewModel.sideColorText(side.descriptionColour),
        )
    }
}
