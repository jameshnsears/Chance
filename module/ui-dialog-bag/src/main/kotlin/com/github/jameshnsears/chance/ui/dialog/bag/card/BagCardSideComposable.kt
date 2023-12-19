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
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.jameshnsears.chance.ui.dialog.bag.DialogBagViewModel
import com.github.jameshnsears.chance.ui.dialog.dice.R

class BagCardSideTestTag {
    companion object {
        val colour = "colour"
        val image = "image"
        val description = "description"
    }
}

@Composable
fun BagCardSide(viewModel: DialogBagViewModel) {
    ElevatedCard(
        modifier = Modifier
            .padding(top = 4.dp, bottom = 8.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            SideNumber(viewModel.diceIndex)

            SideColour()

            SideImage()

            SideDescription(viewModel)
        }
    }
}

@Composable
fun SideNumber(diceIndex: Int) {
    Text(
        text = "${stringResource(R.string.dialog_bag_side)} $diceIndex",
        modifier = Modifier
            .wrapContentSize(Alignment.Center),
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Bold,
    )
}

@Composable
fun SideColour() {
    val paletteIcon = painterResource(id = R.drawable.palette_fill0_wght400_grad0_opsz24)

    Row(
        modifier = Modifier
            .padding(top = 16.dp)
    ) {
        Button(
            onClick = { /* Do something when clicked */ },
            modifier = Modifier.testTag(BagCardSideTestTag.colour)
        ) {
            Icon(
                paletteIcon,
                contentDescription = stringResource(R.string.dialog_bag_side_colour),
                modifier = Modifier.size(24.dp)
            )

            Spacer(Modifier.size(ButtonDefaults.IconSpacing))

            Text(stringResource(R.string.dialog_bag_side_colour))
        }
    }

    Icon(
        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp),
        imageVector = Icons.Outlined.Info,
        contentDescription = ""
    )

    Text(stringResource(R.string.dialog_bag_side_colour_info))
}


@Composable
fun SideImage() {
    val paletteIcon = painterResource(id = R.drawable.image_fill0_wght400_grad0_opsz24)

    Row(
        modifier = Modifier
            .padding(top = 16.dp)
    ) {
        Button(
            onClick = { /* Do something when clicked */ },
            modifier = Modifier.testTag(BagCardSideTestTag.image)
        ) {
            Icon(
                paletteIcon,
                contentDescription = stringResource(R.string.dialog_bag_side_image),
                modifier = Modifier.size(24.dp)
            )

            Spacer(Modifier.size(ButtonDefaults.IconSpacing))

            Text(stringResource(R.string.dialog_bag_side_image))
        }
    }

    Icon(
        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp),
        imageVector = Icons.Outlined.Info,
        contentDescription = ""
    )

    Text(
        modifier = Modifier.padding(bottom = 8.dp),
        text = stringResource(R.string.dialog_bag_side_image_info)
    )
}


@Composable
fun SideDescription(viewModel: DialogBagViewModel) {
    val sideDescription = viewModel.diceTitle.collectAsStateWithLifecycle()

    OutlinedTextField(
        value = sideDescription.value,
        onValueChange = { viewModel.updateDiceTitle(it) },
        label = { Text(stringResource(R.string.dialog_bag_side_description)) },
        singleLine = true,
        modifier = Modifier
            .testTag(BagCardSideTestTag.description)
            .padding(bottom = 8.dp)
            .fillMaxWidth()
    )
}
