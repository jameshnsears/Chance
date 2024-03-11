package com.github.jameshnsears.chance.ui.dialog.bag.card.side

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.github.jameshnsears.chance.ui.dialog.bag.card.BgCardColourSample
import com.github.jameshnsears.chance.ui.dialog.bag.colour.DialogColourPicker
import com.github.jameshnsears.chance.ui.dialog.dice.R

class BagCardSideTestTag {
    companion object {
        const val SIDE_NUMBER = "SIDE_NUMBER"
        const val SIDE_COLOUR = "SIDE_COLOUR"
        const val SIDE_DESCRIPTION = "SIDE_DESCRIPTION"
        const val SIDE_DESCRIPTION_COLOUR = "SIDE_DESCRIPTION_COLOUR"
        const val SIDE_IMAGE_SVG = "SIDE_IMAGE_SVG"
        const val SIDE_RESET = "SIDE_RESET"
    }
}

@Composable
fun BagCardSide(bagCardSideAndroidViewModel: BagCardSideAndroidViewModel) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp,
        ),
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            SideNumber(bagCardSideAndroidViewModel)

            SideColour(bagCardSideAndroidViewModel)

            SideDescription(bagCardSideAndroidViewModel)

            SideImageSVG(bagCardSideAndroidViewModel)

            SideReset(bagCardSideAndroidViewModel)
        }
    }
}

@Composable
fun SideNumber(bagCardSideAndroidViewModel: BagCardSideAndroidViewModel) {
    val stateFlow =
        bagCardSideAndroidViewModel.stateFlowSide.collectAsStateWithLifecycle()

    Text(
        text = "${stringResource(R.string.dialog_bag_side)} ${stateFlow.value.sideNumber}",
        modifier = Modifier
            .wrapContentSize(Alignment.Center)
            .testTag(BagCardSideTestTag.SIDE_NUMBER),
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
    )
}

@Composable
fun SideColour(
    bagCardSideAndroidViewModel: BagCardSideAndroidViewModel
) {
    val showDialogColourPicker = remember { mutableStateOf(false) }

    val stateFlow =
        bagCardSideAndroidViewModel.stateFlowSide.collectAsStateWithLifecycle()

    val sideNumberColour = stateFlow.value.sideNumberColour

    Row(
        modifier = Modifier
            .padding(top = 8.dp, bottom = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Button(
            onClick = { showDialogColourPicker.value = true },
            modifier = Modifier
                .width(140.dp)
                .testTag(BagCardSideTestTag.SIDE_COLOUR),
        ) {
            Icon(
                painterResource(id = R.drawable.palette_fill0_wght400_grad0_opsz24),
                contentDescription = stringResource(R.string.dialog_bag_side_colour),
                modifier = Modifier.size(24.dp),
            )

            Spacer(Modifier.size(ButtonDefaults.IconSpacing))

            Text(stringResource(R.string.dialog_bag_side_colour))
        }

        Spacer(modifier = Modifier.weight(1f))

        Box(modifier = Modifier.padding(end = 4.dp)) {
            BgCardColourSample(sideNumberColour)
        }
    }

    if (showDialogColourPicker.value) {
        DialogColourPicker(
            showDialogColourPicker,
            stringResource(R.string.dialog_bag_colour_picker_side),
            sideNumberColour,
            bagCardSideAndroidViewModel::sideNumberColour,
        )
    }
}

@Composable
fun SideDescription(
    bagCardSideAndroidViewModel: BagCardSideAndroidViewModel
) {
    val stateFlow =
        bagCardSideAndroidViewModel.stateFlowSide.collectAsStateWithLifecycle()

    val sideDescription = stateFlow.value.sideDescription

    HorizontalDivider(
        modifier = Modifier
            .padding(top = 6.dp, bottom = 10.dp)
    )

    OutlinedTextField(
        value = sideDescription,
        onValueChange = {
            if (it.length <= 60) {
                bagCardSideAndroidViewModel.sideDescription(it)
            }
        },
        label = { Text(stringResource(R.string.dialog_bag_side_description)) },
        singleLine = true,
        modifier = Modifier
            .testTag(BagCardSideTestTag.SIDE_DESCRIPTION)
            .padding(bottom = 8.dp)
            .fillMaxWidth(),
        trailingIcon = {
            if (sideDescription.isNotEmpty()) {
                IconButton(onClick = {
                    bagCardSideAndroidViewModel.sideDescription("")
                }
                ) {
                    Icon(
                        painterResource(id = R.drawable.cancel_fill0_wght400_grad0_opsz24),
                        contentDescription = ""
                    )
                }
            }
        }
    )

    SideDescriptionColour(bagCardSideAndroidViewModel)
}

@Composable
fun SideDescriptionColour(bagCardSideAndroidViewModel: BagCardSideAndroidViewModel) {
    val showDialogColourPicker = remember { mutableStateOf(false) }

    val stateFlow =
        bagCardSideAndroidViewModel.stateFlowSide.collectAsStateWithLifecycle()

    val descriptionColour = stateFlow.value.sideDescriptionColour

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(top = 4.dp),
    ) {
        Button(
            onClick = { showDialogColourPicker.value = true },
            modifier = Modifier
                .width(140.dp)
                .testTag(BagCardSideTestTag.SIDE_DESCRIPTION_COLOUR),
        ) {
            Icon(
                painterResource(id = R.drawable.palette_fill0_wght400_grad0_opsz24),
                contentDescription = stringResource(R.string.dialog_bag_side_colour),
                modifier = Modifier.size(24.dp),
            )

            Spacer(Modifier.size(ButtonDefaults.IconSpacing))

            Text(stringResource(R.string.dialog_bag_side_description_colour))
        }

        Spacer(modifier = Modifier.weight(1f))

        Box(modifier = Modifier.padding(end = 4.dp)) {
            BgCardColourSample(descriptionColour)
        }
    }

    if (showDialogColourPicker.value) {
        DialogColourPicker(
            showDialogColourPicker,
            stringResource(R.string.dialog_bag_colour_picker_description),
            descriptionColour,
            bagCardSideAndroidViewModel::sideDescriptionColour,
        )
    }
}

@Composable
fun SideImageSVG(
    bagCardSideAndroidViewModel: BagCardSideAndroidViewModel
) {
    val launcherSvg = remember { mutableStateOf<Uri?>(null) }
    val launcherImport =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.OpenDocument()) { uri: Uri? ->
            launcherSvg.value = uri
        }
    launcherSvg.value?.let { uri ->
        bagCardSideAndroidViewModel.sideImageSVGImport(uri)
    }

    val stateFlow =
        bagCardSideAndroidViewModel.stateFlowSide.collectAsStateWithLifecycle()

    val sideImageSVG = stateFlow.value.sideImageSVG

    HorizontalDivider(
        modifier = Modifier
            .padding(top = 10.dp, bottom = 6.dp)
    )

    Row(
        modifier = Modifier
            .padding(top = 4.dp, bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Button(
            onClick = {
                launcherImport.launch(arrayOf("*/svg"))
            },
            modifier = Modifier
                .width(140.dp)
                .testTag(BagCardSideTestTag.SIDE_IMAGE_SVG),
        ) {
            Icon(
                painterResource(id = R.drawable.image_fill0_wght400_grad0_opsz24),
                contentDescription = stringResource(R.string.dialog_bag_side_image),
                modifier = Modifier.size(24.dp),
            )

            Spacer(Modifier.size(ButtonDefaults.IconSpacing))

            Text(stringResource(R.string.dialog_bag_side_image))
        }

        Spacer(modifier = Modifier.weight(1f))

        if (bagCardSideAndroidViewModel.side.imageDrawableId != 0) {
            Image(
                painter = painterResource(id = bagCardSideAndroidViewModel.side.imageDrawableId),
                contentDescription = null,
                modifier = Modifier.size(50.dp),
            )
        } else {
            if (bagCardSideAndroidViewModel.side.imageBase64 != "") {
                AsyncImage(
                    model = sideImageSVG,
                    contentDescription = "",
                    modifier = Modifier.size(50.dp),
                )
            }
        }
    }

    HorizontalDivider(
        modifier = Modifier
            .padding(top = 2.dp)
    )
}

@Composable
fun SideReset(
    bagCardSideAndroidViewModel: BagCardSideAndroidViewModel,
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        Button(
            onClick = {
                bagCardSideAndroidViewModel.sideReset()
            },
            modifier = Modifier
                .padding(top = 8.dp)
                .width(140.dp)
                .testTag(BagCardSideTestTag.SIDE_RESET)
        ) {
            Icon(
                painterResource(id = R.drawable.restart_alt_fill0_wght400_grad0_opsz24),
                contentDescription = stringResource(R.string.dialog_bag_side_reset),
                modifier = Modifier.size(24.dp),
            )

            Spacer(Modifier.size(ButtonDefaults.IconSpacing))

            Text(stringResource(R.string.dialog_bag_side_reset))
        }
    }
}
