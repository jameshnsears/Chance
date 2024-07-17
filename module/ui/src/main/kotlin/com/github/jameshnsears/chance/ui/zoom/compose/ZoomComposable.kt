package com.github.jameshnsears.chance.ui.zoom.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.github.jameshnsears.chance.common.R
import com.github.jameshnsears.chance.data.domain.core.Dice
import com.github.jameshnsears.chance.data.domain.core.Side
import com.github.jameshnsears.chance.data.domain.core.roll.Roll
import com.github.jameshnsears.chance.ui.zoom.ZoomAndroidViewModel

@Composable
fun SideImageShape(
    zoomAndroidViewModel: ZoomAndroidViewModel,
    dice: Dice,
    side: Side,
    showDialog: MutableState<Boolean>,
    dialogDice: MutableState<Dice>,
    dialogSide: MutableState<Side>
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
                .clickable {
                    dialogDice.value = dice
                    dialogSide.value = side
                    showDialog.value = true
                }
                .size(resizeView),
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
fun SideRollBehaviour(
    zoomAndroidViewModel: ZoomAndroidViewModel,
    roll: Roll
) {
    val rollSelectionIconColour = if (isSystemInDarkTheme()) Color.White else Color.Black

    val stateFlowZoom =
        zoomAndroidViewModel.stateFlowZoom.collectAsStateWithLifecycle(
            lifecycleOwner = androidx.compose.ui.platform.LocalLifecycleOwner.current
        )

    val resizeView = stateFlowZoom.value.resizeView

    Box {
        Row(verticalAlignment = Alignment.CenterVertically) {
            SideRollBehaviourIcon(
                resizeView,
                rollSelectionIconColour,
                zoomAndroidViewModel,
                R.drawable.roll_repeat_fill0_wght400_grad0_opsz24,
                "${roll.multiplierIndex}"
            )

            if (roll.explodeIndex != 0) {
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))

                SideRollBehaviourIcon(
                    resizeView,
                    rollSelectionIconColour,
                    zoomAndroidViewModel,
                    R.drawable.roll_explode_fill0_wght400_grad0_opsz24,
                    "${roll.explodeIndex}"
                )
            }

            if (roll.scoreAdjustment != 0) {
                SideRollBehaviourIcon(
                    resizeView,
                    rollSelectionIconColour,
                    zoomAndroidViewModel,
                    R.drawable.roll_add_subtract_fill0_wght400_grad0_opsz24,
                    "${roll.scoreAdjustment}"
                )
            }
        }
    }
}

@Composable
private fun SideRollBehaviourIcon(
    resizeView: Dp,
    rollSelectionIconColour: Color,
    zoomAndroidViewModel: ZoomAndroidViewModel,
    drawableId: Int,
    text: String,
) {
    Icon(
        painterResource(id = drawableId),
        contentDescription = "",
        modifier = Modifier.size(resizeView / 4),
        tint = rollSelectionIconColour
    )

    Text(
        fontSize = zoomAndroidViewModel.sideImageShapeNumberFontSize(),
        text = text,
        color = rollSelectionIconColour,
    )
}

@Composable
fun DiceTitle(dice: Dice) {
    Text(
        text = dice.title,
    )
}

@Composable
fun SideImageSVG(
    zoomAndroidViewModel: ZoomAndroidViewModel,
    dice: Dice,
    side: Side,
    showDialog: MutableState<Boolean>,
    dialogDice: MutableState<Dice>,
    dialogSide: MutableState<Side>
) {
    val stateFlowZoom =
        zoomAndroidViewModel.stateFlowZoom.collectAsStateWithLifecycle(
            lifecycleOwner = androidx.compose.ui.platform.LocalLifecycleOwner.current
        )

    val resizeView = stateFlowZoom.value.resizeView

    if (side.imageDrawableId != 0) {
        Image(
            painter = painterResource(id = side.imageDrawableId),
            contentDescription = "",
            modifier = Modifier
                .size(resizeView)
                .clickable {
                    dialogDice.value = dice
                    dialogSide.value = side
                    showDialog.value = true
                }
        )
    } else {
        if (side.imageBase64 != "") {
            val imageRequest: ImageRequest = remember {
                zoomAndroidViewModel.sideImageSVG(side)
            }

            AsyncImage(
                model = imageRequest,
                contentDescription = "",
                modifier = Modifier
                    .size(resizeView)
                    .clickable { showDialog.value = true },
            )
        }
    }
}

@Composable
fun SideDescription(zoomAndroidViewModel: ZoomAndroidViewModel, dice: Dice, side: Side) {
    if (side.description != "") {
        SideDescriptionFromUser(side, zoomAndroidViewModel)
    } else if (zoomAndroidViewModel.diceContainsAtLeastOneSideWithDescription(dice)) {
        Text(text = " ")
    }
}

@Composable
fun SideDescriptionRoll(zoomAndroidViewModel: ZoomAndroidViewModel, side: Side) {
    if (side.description != "") {
        SideDescriptionFromUser(side, zoomAndroidViewModel)
    } else if (side.imageDrawableId != 0 || side.imageBase64 != "") {
        Text(text = " ")
    }
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