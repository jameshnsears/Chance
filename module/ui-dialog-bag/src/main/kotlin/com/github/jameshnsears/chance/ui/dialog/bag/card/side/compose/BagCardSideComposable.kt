package com.github.jameshnsears.chance.ui.dialog.bag.card.side.compose

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.github.jameshnsears.chance.ui.dialog.bag.R
import com.github.jameshnsears.chance.ui.dialog.bag.card.compose.BagCardColourSample
import com.github.jameshnsears.chance.ui.dialog.bag.card.side.CardSideAndroidViewModel
import com.github.jameshnsears.chance.ui.dialog.bag.card.side.CardSideSvgImportException
import com.github.jameshnsears.chance.ui.dialog.colour.compose.DialogColourPicker

class BagCardSideTestTag {
    companion object {
        const val SIDE_NUMBER = "SIDE_NUMBER"
        const val SIDE_COLOUR = "SIDE_COLOUR"
        const val SIDE_APPLY_NUMBER_COLOUR = "SIDE_APPLY_NUMBER_COLOUR"
        const val SIDE_DESCRIPTION = "SIDE_DESCRIPTION"
        const val SIDE_DESCRIPTION_COLOUR = "SIDE_DESCRIPTION_COLOUR"
        const val SIDE_APPLY_DESCRIPTION = "SIDE_APPLY_DESCRIPTION"
        const val SIDE_IMAGE_SVG = "SIDE_IMAGE_SVG"
        const val SIDE_APPLY_SVG = "SIDE_APPLY_SVG"
    }
}

@Composable
fun BagCardSide(cardSideAndroidViewModel: CardSideAndroidViewModel) {
    OutlinedCard(
        modifier = Modifier
            .padding(top = 4.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp,
        ),
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            SideNumber(cardSideAndroidViewModel)

            SideColour(cardSideAndroidViewModel)

            HorizontalDivider(
                modifier = Modifier
                    .padding(top = 12.dp, bottom = 12.dp)
            )

            SideDescription(cardSideAndroidViewModel)

            HorizontalDivider(
                modifier = Modifier
                    .padding(top = 12.dp, bottom = 12.dp)
            )

            SideImageSVG(cardSideAndroidViewModel)
        }
    }
}

@Composable
fun SideNumber(cardSideAndroidViewModel: CardSideAndroidViewModel) {
    val stateFlowCardSide =
        cardSideAndroidViewModel.stateFlowCardSide.collectAsStateWithLifecycle(
            lifecycleOwner = LocalLifecycleOwner.current
        )

    Text(
        text = "${stringResource(R.string.dialog_bag_side)} ${stateFlowCardSide.value.sideNumber}",
        modifier = Modifier
            .wrapContentSize(Alignment.Center)
            .padding(top = 8.dp, bottom = 8.dp)
            .testTag(BagCardSideTestTag.SIDE_NUMBER),
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
    )
}

@Composable
fun SideColour(
    cardSideAndroidViewModel: CardSideAndroidViewModel
) {
    val showDialogColourPicker = rememberSaveable { mutableStateOf(false) }

    val stateFlowCardSide =
        cardSideAndroidViewModel.stateFlowCardSide.collectAsStateWithLifecycle(
            lifecycleOwner = LocalLifecycleOwner.current
        )

    val sideNumberColour = stateFlowCardSide.value.sideNumberColour

    val diceSidesFewerThanSdeNumber = stateFlowCardSide.value.diceSidesFewerThanSdeNumber

    Row(
        modifier = Modifier
            .padding(top = 8.dp, bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Button(
            onClick = { showDialogColourPicker.value = true },
            modifier = Modifier
                .width(160.dp)
                .testTag(BagCardSideTestTag.SIDE_COLOUR),
            enabled = !diceSidesFewerThanSdeNumber
        ) {
            Icon(
                painterResource(id = R.drawable.palette_fill0_wght400_grad0_opsz24),
                contentDescription = "",
                modifier = Modifier.size(24.dp),
            )

            Spacer(Modifier.size(ButtonDefaults.IconSpacing))

            Text(stringResource(R.string.dialog_bag_side_colour))
        }

        Spacer(modifier = Modifier.weight(1f))

        Box(modifier = Modifier.padding(end = 8.dp)) {
            BagCardColourSample(sideNumberColour)
        }
    }

    SideApplyToAll(
        stateFlowCardSide.value.sideApplyToAllNumberColour,
        BagCardSideTestTag.SIDE_APPLY_NUMBER_COLOUR,
        cardSideAndroidViewModel::sideApplyToAllNumberColour,
        R.string.dialog_bag_side_colour_apply_to_all
    )

    if (showDialogColourPicker.value) {
        DialogColourPicker(
            showDialogColourPicker,
            stringResource(R.string.dialog_bag_colour_picker_side),
            sideNumberColour,
            cardSideAndroidViewModel::sideNumberColour,
        )
    }
}

@Composable
fun SideDescription(
    cardSideAndroidViewModel: CardSideAndroidViewModel
) {
    val stateFlowCardSide =
        cardSideAndroidViewModel.stateFlowCardSide.collectAsStateWithLifecycle(
            lifecycleOwner = LocalLifecycleOwner.current
        )

    val sideDescription = stateFlowCardSide.value.sideDescription

    val diceSidesFewerThanSdeNumber = stateFlowCardSide.value.diceSidesFewerThanSdeNumber

    OutlinedTextField(
        value = sideDescription,
        onValueChange = {
            if (it.length <= 25) {
                cardSideAndroidViewModel.sideDescription(it)
            }
        },
        label = { Text(stringResource(R.string.dialog_bag_side_description)) },
        singleLine = true,
        modifier = Modifier
            .testTag(BagCardSideTestTag.SIDE_DESCRIPTION)
            .padding(top = 8.dp, bottom = 8.dp)
            .fillMaxWidth(),
        enabled = !diceSidesFewerThanSdeNumber,
        trailingIcon = {
            if (sideDescription.isNotEmpty()) {
                IconButton(onClick = {
                    cardSideAndroidViewModel.sideDescription("")
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

    Row(
        modifier = Modifier
            .padding(top = 8.dp, bottom = 8.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Outlined.Info,
            contentDescription = "",
        )
    }

    Row(
        modifier = Modifier
            .padding(top = 8.dp, bottom = 8.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.dialog_bag_side_description_info),
        )
    }

    SideDescriptionColour(cardSideAndroidViewModel)

    SideApplyToAll(
        stateFlowCardSide.value.sideApplyToAllDescription,
        BagCardSideTestTag.SIDE_APPLY_DESCRIPTION,
        cardSideAndroidViewModel::sideApplyToAllDescription,
        R.string.dialog_bag_side_description_apply_to_all
    )
}

@Composable
fun SideDescriptionColour(cardSideAndroidViewModel: CardSideAndroidViewModel) {
    val showDialogColourPicker = rememberSaveable { mutableStateOf(false) }

    val stateFlowCardSide =
        cardSideAndroidViewModel.stateFlowCardSide.collectAsStateWithLifecycle(
            lifecycleOwner = LocalLifecycleOwner.current
        )

    val descriptionColour = stateFlowCardSide.value.sideDescriptionColour

    val diceSidesFewerThanSdeNumber = stateFlowCardSide.value.diceSidesFewerThanSdeNumber

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(top = 12.dp, bottom = 8.dp),
    ) {
        Button(
            onClick = { showDialogColourPicker.value = true },
            modifier = Modifier
                .width(180.dp)
                .testTag(BagCardSideTestTag.SIDE_DESCRIPTION_COLOUR),
            enabled = !diceSidesFewerThanSdeNumber
        ) {
            Icon(
                painterResource(id = R.drawable.palette_fill0_wght400_grad0_opsz24),
                contentDescription = "",
                modifier = Modifier.size(24.dp),
            )

            Spacer(Modifier.size(ButtonDefaults.IconSpacing))

            Text(stringResource(R.string.dialog_bag_side_description_colour))
        }

        Spacer(modifier = Modifier.weight(1f))

        Box(modifier = Modifier.padding(end = 8.dp)) {
            BagCardColourSample(descriptionColour)
        }
    }

    if (showDialogColourPicker.value) {
        DialogColourPicker(
            showDialogColourPicker,
            stringResource(R.string.dialog_bag_colour_picker_description),
            descriptionColour,
            cardSideAndroidViewModel::sideDescriptionColour,
        )
    }
}

@Composable
fun SideImageSVG(
    cardSideAndroidViewModel: CardSideAndroidViewModel
) {
    val context = LocalContext.current

    val sideImageError = stringResource(R.string.dialog_bag_side_image_error)

    val launcherImport = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        try {
            if (uri != null) {
                cardSideAndroidViewModel.sideImageSvgImport(uri)
            }
        } catch (cardSideSvgImportException: CardSideSvgImportException) {
            Toast.makeText(
                context,
                sideImageError,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    val stateFlowCardSide =
        cardSideAndroidViewModel.stateFlowCardSide.collectAsStateWithLifecycle(
            lifecycleOwner = LocalLifecycleOwner.current
        )

    val sideImageDrawableId = stateFlowCardSide.value.sideImageDrawableId

    val sideImageBase64Request = stateFlowCardSide.value.sideImageRequest

    val diceSidesFewerThanSdeNumber = stateFlowCardSide.value.diceSidesFewerThanSdeNumber

    Row(
        modifier = Modifier
            .padding(top = 8.dp, bottom = 0.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column {
            Button(
                onClick = {
                    launcherImport.launch(arrayOf("image/svg+xml"))
                },
                modifier = Modifier
                    .width(180.dp)
                    .padding(bottom = 6.dp)
                    .testTag(BagCardSideTestTag.SIDE_IMAGE_SVG),
                enabled = !diceSidesFewerThanSdeNumber
            ) {
                Icon(
                    painterResource(id = R.drawable.image_fill0_wght400_grad0_opsz24),
                    contentDescription = "",
                    modifier = Modifier.size(24.dp),
                )

                Spacer(Modifier.size(ButtonDefaults.IconSpacing))

                Text(stringResource(R.string.dialog_bag_side_image))
            }

            Row(
                modifier = Modifier
                    .padding(top = 8.dp, bottom = 8.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = "",
                )
            }

            Row(
                modifier = Modifier
                    .padding(top = 8.dp, bottom = 8.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(R.string.dialog_bag_side_image_info),
                )
            }

            Button(
                onClick = {
                    cardSideAndroidViewModel.sideImageSvgClear()
                },
                modifier = Modifier
                    .width(180.dp)
                    .padding(top = 6.dp)
                    .testTag(BagCardSideTestTag.SIDE_IMAGE_SVG),
                enabled = cardSideAndroidViewModel.sideImageAvailable()
            ) {
                Icon(
                    painterResource(id = R.drawable.reset_image_fill0_wght400_grad0_opsz24),
                    contentDescription = "",
                    modifier = Modifier.size(24.dp),
                )

                Spacer(Modifier.size(ButtonDefaults.IconSpacing))

                Text(stringResource(R.string.dialog_bag_side_image_clear))
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        if (sideImageDrawableId != 0) {
            Image(
                painter = painterResource(id = sideImageDrawableId),
                contentDescription = "",
                modifier = Modifier
                    .size(100.dp)
            )
        } else {
            if (sideImageBase64Request != null) {
                AsyncImage(
                    model = sideImageBase64Request,
                    contentDescription = "",
                    modifier = Modifier
                        .size(100.dp)
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))
    }

    SideApplyToAll(
        stateFlowCardSide.value.sideApplyToAllSvg,
        BagCardSideTestTag.SIDE_APPLY_SVG,
        cardSideAndroidViewModel::sideApplyToAllSvg
    )
}

@Composable
fun SideApplyToAll(
    sideApplyToAll: Boolean,
    testTag: String,
    sideApplyToAllFunction: (Boolean) -> Unit,
    stringResourceId: Int = R.string.dialog_bag_side_image_apply_to_all,
) {
    val switched = rememberSaveable { mutableStateOf(sideApplyToAll) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 0.dp, end = 2.dp)
            .testTag(testTag)
            .clickable {
                val newValue = !switched.value
                switched.value = newValue
                sideApplyToAllFunction(switched.value)
            }
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = stringResource(stringResourceId)
        )

        Switch(
            checked = switched.value,
            onCheckedChange = {
                switched.value = it
                sideApplyToAllFunction(switched.value)
            },
        )
    }
}
