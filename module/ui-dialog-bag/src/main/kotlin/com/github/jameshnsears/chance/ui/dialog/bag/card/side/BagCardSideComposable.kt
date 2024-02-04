package com.github.jameshnsears.chance.ui.dialog.bag.card.side

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.github.jameshnsears.chance.ui.dialog.bag.DialogBagAndroidViewModel
import com.github.jameshnsears.chance.ui.dialog.bag.card.ColourSampleCanvas
import com.github.jameshnsears.chance.ui.dialog.bag.colour.DialogColourPicker
import com.github.jameshnsears.chance.ui.dialog.dice.R

class BagCardSideTestTag {
    companion object {
        const val SIDE_NUMBER = "SIDE_NUMBER"
        const val SIDE_COLOUR = "SIDE_COLOUR"
        const val SIDE_IMAGE = "SIDE_IMAGE"
        const val SIDE_DESCRIPTION = "SIDE_DESCRIPTION"
        const val SIDE_DESCRIPTION_COLOUR = "SIDE_DESCRIPTION_COLOUR"
    }
}

@Composable
fun BagCardSide(dialogBagAndroidViewModel: DialogBagAndroidViewModel) {
    ElevatedCard(
        modifier = Modifier
            .padding(top = 4.dp, bottom = 8.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp,
        ),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            SideNumber()

            SideColour(dialogBagAndroidViewModel)

            SideImage(dialogBagAndroidViewModel)

            SideDescription(dialogBagAndroidViewModel)
        }
    }
}

@Composable
fun SideNumber() {
    Text(
        text = stringResource(R.string.dialog_bag_side),
        modifier = Modifier
            .wrapContentSize(Alignment.Center)
            .testTag(BagCardSideTestTag.SIDE_NUMBER),
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
    )
}

@Composable
fun SideColour(dialogBagAndroidViewModel: DialogBagAndroidViewModel) {
    val paletteIcon = painterResource(id = R.drawable.palette_fill0_wght400_grad0_opsz24)

    val showDialogColourPicker = remember { mutableStateOf(false) }

    // changes in dialogBagAndroidViewModel.sideColour emit new value to val sideColour
    val sideColour by dialogBagAndroidViewModel.sideColour.collectAsStateWithLifecycle()

    val sideNumber by dialogBagAndroidViewModel.sideNumber.collectAsStateWithLifecycle()

    Row(
        modifier = Modifier
            .padding(top = 16.dp, bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Button(
            onClick = { showDialogColourPicker.value = true },
            modifier = Modifier
                .testTag(BagCardSideTestTag.SIDE_COLOUR),
        ) {
            Icon(
                paletteIcon,
                contentDescription = stringResource(R.string.dialog_bag_side_colour),
                modifier = Modifier.size(24.dp),
            )

            Spacer(Modifier.size(ButtonDefaults.IconSpacing))

            Text(stringResource(R.string.dialog_bag_side_colour))
        }

        Spacer(modifier = Modifier.weight(1f))

        ColourSampleCanvas(sideColour)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Outlined.Info,
            contentDescription = "",
        )

        Text(
            modifier = Modifier
                .padding(start = 8.dp),
            text = "${stringResource(R.string.dialog_bag_side_colour_info)} $sideNumber",
        )
    }

    Divider(
        modifier = Modifier
            .padding(top = 14.dp, bottom = 4.dp),
    )

    if (showDialogColourPicker.value) {
        DialogColourPicker(
            showDialogColourPicker,
            stringResource(R.string.dialog_bag_colour_picker_side),
            sideColour,
            dialogBagAndroidViewModel::sideColour,
        )
    }
}

@Composable
fun SideImage(dialogBagAndroidViewModel: DialogBagAndroidViewModel) {


    val paletteIcon = painterResource(id = R.drawable.image_fill0_wght400_grad0_opsz24)

    Row(
        modifier = Modifier
            .padding(top = 4.dp, bottom = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Button(
            onClick = { /* Do something when clicked */ },
            modifier = Modifier.testTag(BagCardSideTestTag.SIDE_IMAGE),
        ) {
            Icon(
                paletteIcon,
                contentDescription = stringResource(R.string.dialog_bag_side_image),
                modifier = Modifier.size(24.dp),
            )

            Spacer(Modifier.size(ButtonDefaults.IconSpacing))

            Text(stringResource(R.string.dialog_bag_side_image))
        }

        Spacer(modifier = Modifier.weight(1f))

        Image(
            painter = painterResource(id = com.github.jameshnsears.chance.data.R.drawable.demo_bag_centurion),
            contentDescription = null,
            modifier = Modifier
                .padding(horizontal = 5.dp, vertical = 5.dp)
                .size(50.dp),
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Outlined.Info,
            contentDescription = "",
        )

        Text(
            modifier = Modifier
                .padding(start = 8.dp),
            text = stringResource(R.string.dialog_bag_side_image_info),
        )
    }

    Divider(
        modifier = Modifier
            .padding(top = 12.dp, bottom = 12.dp),
    )
}

@Composable
fun SideDescription(dialogBagAndroidViewModel: DialogBagAndroidViewModel) {
    val sideDescription =
        dialogBagAndroidViewModel.sideDescription.collectAsStateWithLifecycle()

    OutlinedTextField(
        value = sideDescription.value,
        onValueChange = { dialogBagAndroidViewModel.sideDescription(it) },
        label = { Text(stringResource(R.string.dialog_bag_side_description)) },
        singleLine = true,
        modifier = Modifier
            .testTag(BagCardSideTestTag.SIDE_DESCRIPTION)
            .padding(bottom = 8.dp)
            .fillMaxWidth(),
    )

    SideDescriptionColour(dialogBagAndroidViewModel)
}

@Composable
fun SideDescriptionColour(dialogBagAndroidViewModel: DialogBagAndroidViewModel) {
    val paletteIcon = painterResource(id = R.drawable.palette_fill0_wght400_grad0_opsz24)

    val showDialogColourPicker = remember { mutableStateOf(false) }

    val descriptionColour by dialogBagAndroidViewModel.sideDescriptionColour.collectAsStateWithLifecycle()

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(top = 4.dp),
    ) {
        Button(
            onClick = { showDialogColourPicker.value = true },
            modifier = Modifier
                .testTag(BagCardSideTestTag.SIDE_DESCRIPTION_COLOUR),
        ) {
            Icon(
                paletteIcon,
                contentDescription = stringResource(R.string.dialog_bag_side_colour),
                modifier = Modifier.size(24.dp),
            )

            Spacer(Modifier.size(ButtonDefaults.IconSpacing))

            Text(stringResource(R.string.dialog_bag_side_colour))
        }

        Spacer(modifier = Modifier.weight(1f))

        ColourSampleCanvas(descriptionColour)
    }

    if (showDialogColourPicker.value) {
        DialogColourPicker(
            showDialogColourPicker,
            stringResource(R.string.dialog_bag_colour_picker_description),
            descriptionColour,
            dialogBagAndroidViewModel::sideDescriptionColour,
        )
    }
}
