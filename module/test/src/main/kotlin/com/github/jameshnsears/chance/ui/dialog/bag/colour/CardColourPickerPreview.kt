package com.github.jameshnsears.chance.ui.dialog.bag.colour

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import com.github.jameshnsears.chance.ui.dialog.dice.R
import com.github.jameshnsears.chance.ui.utility.preview.UtilityPreview

@Composable
@UtilityPreview
fun BagCardColorPickerLayoutPortraitPreview() {
    val showColourDialog = remember { mutableStateOf(true) }

    DialogColourPickerLayout(
        showColourDialog,
        stringResource(R.string.dialog_bag_colour_picker_side),
        "FF000000",
        fun(_: String) {},
    )
}

@Composable
@UtilityPreview
fun BagCardColorPickerLayoutLandscapePreview() {
    val showColourDialog = remember { mutableStateOf(true) }

    DialogColourPickerLayout(
        showColourDialog,
        stringResource(R.string.dialog_bag_colour_picker_side),
        "00FFFFFF",
        fun(_: String) {},
    )
}
