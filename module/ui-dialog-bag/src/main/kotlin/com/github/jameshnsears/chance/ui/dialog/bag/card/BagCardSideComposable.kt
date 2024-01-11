package com.github.jameshnsears.chance.ui.dialog.bag.card

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
import com.github.jameshnsears.chance.ui.dialog.bag.DialogBagAndroidViewModelInterface
import com.github.jameshnsears.chance.ui.dialog.bag.card.colour.DialogColourPicker
import com.github.jameshnsears.chance.ui.dialog.dice.R

class BagCardSideTestTag {
    companion object {
        const val sideNumber = "sideNumber"
        const val sideColour = "sideColour"
        const val sideImage = "sideImage"
        const val sideDescription = "sideDescription"
    }
}

@Composable
fun BagCardSide(dialogBagAndroidViewModelInterface: DialogBagAndroidViewModelInterface) {
    ElevatedCard(
        modifier = Modifier
            .padding(top = 4.dp, bottom = 8.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            SideNumber(dialogBagAndroidViewModelInterface)

            SideColour(dialogBagAndroidViewModelInterface)

            SideImage(dialogBagAndroidViewModelInterface)

            SideDescription(dialogBagAndroidViewModelInterface)
        }
    }
}

@Composable
fun SideNumber(dialogBagAndroidViewModelInterface: DialogBagAndroidViewModelInterface) {
    val sideNumber by dialogBagAndroidViewModelInterface.sideNumber.collectAsStateWithLifecycle()

    Text(
        text = "${stringResource(R.string.dialog_bag_side)} $sideNumber",
        modifier = Modifier
            .wrapContentSize(Alignment.Center)
            .testTag(BagCardSideTestTag.sideNumber),
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp
    )
}

@Composable
fun SideColour(dialogBagAndroidViewModelInterface: DialogBagAndroidViewModelInterface) {
    val sideColour by dialogBagAndroidViewModelInterface.sideColour.collectAsStateWithLifecycle()

    val paletteIcon = painterResource(id = R.drawable.palette_fill0_wght400_grad0_opsz24)

    val showDialogColourPicker = remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .padding(top = 16.dp, bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = { showDialogColourPicker.value = true },
            modifier = Modifier.testTag(BagCardSideTestTag.sideColour)
        ) {
            Icon(
                paletteIcon,
                contentDescription = stringResource(R.string.dialog_bag_side_colour),
                modifier = Modifier.size(24.dp)
            )

            Spacer(Modifier.size(ButtonDefaults.IconSpacing))

            Text(stringResource(R.string.dialog_bag_side_colour))
        }

        Text(
            modifier = Modifier
                .padding(start = 16.dp),
            text = sideColour
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Outlined.Info,
            contentDescription = ""
        )

        Text(
            modifier = Modifier
                .padding(start = 8.dp),
            text = stringResource(R.string.dialog_bag_side_colour_info)
        )
    }

    Divider(
        modifier = Modifier
            .padding(top = 14.dp, bottom = 4.dp)
    )

    if (showDialogColourPicker.value) {
        DialogColourPicker(
            showDialogColourPicker,
            stringResource(R.string.dialog_bag_colour_side)
        )
    }
}

@Composable
fun SideImage(dialogBagAndroidViewModelInterface: DialogBagAndroidViewModelInterface) {
    val sideImageFilename by dialogBagAndroidViewModelInterface.sideImageFilename.collectAsStateWithLifecycle()

    val paletteIcon = painterResource(id = R.drawable.image_fill0_wght400_grad0_opsz24)

    Row(
        modifier = Modifier
            .padding(top = 8.dp, bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = { /* Do something when clicked */ },
            modifier = Modifier.testTag(BagCardSideTestTag.sideImage)
        ) {
            Icon(
                paletteIcon,
                contentDescription = stringResource(R.string.dialog_bag_side_image),
                modifier = Modifier.size(24.dp)
            )

            Spacer(Modifier.size(ButtonDefaults.IconSpacing))

            Text(stringResource(R.string.dialog_bag_side_image))
        }

        Text(
            modifier = Modifier
                .padding(start = 16.dp),
            text = sideImageFilename
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Outlined.Info,
            contentDescription = ""
        )

        Text(
            modifier = Modifier
                .padding(start = 8.dp),
            text = stringResource(R.string.dialog_bag_side_image_info)
        )
    }

    Divider(
        modifier = Modifier
            .padding(top = 12.dp, bottom = 12.dp)
    )
}

@Composable
fun SideDescription(dialogBagAndroidViewModelInterface: DialogBagAndroidViewModelInterface) {
    val sideDescription =
        dialogBagAndroidViewModelInterface.sideDescription.collectAsStateWithLifecycle()

    OutlinedTextField(
        value = sideDescription.value,
        onValueChange = { dialogBagAndroidViewModelInterface.sideDescription(it) },
        label = { Text(stringResource(R.string.dialog_bag_side_description)) },
        singleLine = true,
        modifier = Modifier
            .testTag(BagCardSideTestTag.sideDescription)
            .padding(bottom = 8.dp)
            .fillMaxWidth()
    )
}
