package com.github.jameshnsears.chance.ui.dialog.bag.card.colour

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.github.jameshnsears.chance.ui.dialog.dice.R
import com.github.skydoves.colorpicker.compose.AlphaSlider
import com.github.skydoves.colorpicker.compose.BrightnessSlider
import com.github.skydoves.colorpicker.compose.ColorPickerController
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.drawColorIndicator
import com.github.skydoves.colorpicker.compose.rememberColorPickerController

@Composable
fun DialogColourPicker(
    showDialog: MutableState<Boolean>,
    dialogTitle: String
) {
    Dialog(
        onDismissRequest = { showDialog.value = false },
    ) {
        DialogColourPickerLayout(showDialog, dialogTitle)
    }
}

@Composable
fun DialogColourPickerLayout(
    showDialog: MutableState<Boolean>,
    dialogTitle: String
) {
    val controller = rememberColorPickerController()

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        val configuration = LocalConfiguration.current

        when (configuration.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> {
                DialogColourPickerLayoutLandscape(showDialog, dialogTitle, controller)
            }

            else -> {
                DialogColourPickerLayoutPortrait(showDialog, dialogTitle, controller)
            }
        }
    }
}

@Composable
fun DialogColourPickerLayoutLandscape(
    showDialog: MutableState<Boolean>,
    dialogTitle: String,
    controller: ColorPickerController
) {
    var hexCode by remember { mutableStateOf("") }
    var textColor by remember { mutableStateOf(Color.Transparent) }

    Column {
        Column(
            modifier = Modifier
                .padding(top = 8.dp)
                .weight(1f)
        )
        {
            Row(Modifier.align(Alignment.Start)) {
                Text(
                    modifier = Modifier.padding(start = 24.dp),
                    text = dialogTitle,
                    fontSize = 24.sp
                )
            }

            HsvColorPicker(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                controller = controller,
                drawOnPosSelected = {
                    drawColorIndicator(
                        controller.selectedPoint.value,
                        controller.selectedColor.value,
                    )
                },
                onColorChanged = { colorEnvelope ->
                    hexCode = colorEnvelope.hexCode
                    textColor = colorEnvelope.color
                },
                initialColor = Color.Black,
            )

        }

        Column(
            verticalArrangement = Arrangement.Bottom
        ) {

            AlphaSlider(
                modifier = Modifier
                    .padding(top = 8.dp, start = 20.dp, end = 20.dp)
                    .height(35.dp)
                    .align(Alignment.CenterHorizontally),
                controller = controller,
            )

            BrightnessSlider(
                modifier = Modifier
                    .padding(top = 16.dp, start = 20.dp, end = 20.dp, bottom = 4.dp)
                    .height(35.dp)
                    .align(Alignment.CenterHorizontally),
                controller = controller,
            )

            ColourPickerCancelOk(showDialog)
        }
    }
}

@Composable
fun DialogColourPickerLayoutPortrait(
    showDialog: MutableState<Boolean>,
    dialogTitle: String,
    controller: ColorPickerController
) {
    var hexCode by remember { mutableStateOf("") }
    var textColor by remember { mutableStateOf(Color.Transparent) }

    Column(
        modifier = Modifier
            .padding(top = 8.dp, bottom = 8.dp)
    )
    {
        Text(
            modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 24.dp, bottom = 12.dp),
            text = dialogTitle,
            fontSize = 24.sp
        )

        Box(
            Modifier
                .height(300.dp)
                .width(300.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            HsvColorPicker(
                modifier = Modifier.padding(1.dp),
                controller = controller,
                drawOnPosSelected = {
                    drawColorIndicator(
                        controller.selectedPoint.value,
                        controller.selectedColor.value,
                    )
                },
                onColorChanged = { colorEnvelope ->
                    hexCode = colorEnvelope.hexCode
                    textColor = colorEnvelope.color
                },
                initialColor = Color.Black,
            )
        }

        AlphaSlider(
            modifier = Modifier
                .padding(top = 16.dp, start = 20.dp, end = 20.dp)
                .height(35.dp)
                .align(Alignment.CenterHorizontally),
            controller = controller,
        )

        BrightnessSlider(
            modifier = Modifier
                .padding(top = 16.dp, start = 20.dp, end = 20.dp, bottom = 4.dp)
                .height(35.dp)
                .align(Alignment.CenterHorizontally),
            controller = controller,
        )

        ColourPickerCancelOk(showDialog)
    }
}

@Composable
fun ColourPickerCancelOk(
    showDialog: MutableState<Boolean>
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 8.dp),
        horizontalArrangement = Arrangement.End,
    ) {
        TextButton(
            modifier = Modifier.padding(end = 18.dp),
            onClick = { showDialog.value = false }) {
            Text(stringResource(R.string.dialog_bag_colour_cancel))
        }

        TextButton(onClick = { showDialog.value = false }) {
            Text(stringResource(R.string.dialog_bag_colour_ok))
        }
    }
}
