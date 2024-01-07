package com.github.jameshnsears.chance.ui.dialog.bag.card.colour

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.github.jameshnsears.chance.ui.dialog.dice.R

@Composable
@Preview
fun BagCardColorPickerLayoutPortraitComposePreview() {
    val showColourDialog = remember { mutableStateOf(true) }

    DialogColourPickerLayout(
        showColourDialog,
        stringResource(R.string.dialog_bag_colour_side)
    )
}

@Composable
@Preview(heightDp = 300, widthDp = 400)
fun BagCardColorPickerLayoutLandscapeComposePreview() {
    val showColourDialog = remember { mutableStateOf(true) }

    DialogColourPickerLayout(
        showColourDialog,
        stringResource(R.string.dialog_bag_colour_side)
    )
}
