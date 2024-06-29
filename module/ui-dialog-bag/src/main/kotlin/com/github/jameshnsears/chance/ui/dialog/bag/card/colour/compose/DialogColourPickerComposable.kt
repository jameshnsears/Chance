package com.github.jameshnsears.chance.ui.dialog.bag.card.colour.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.github.jameshnsears.chance.ui.utility.colour.UtilityColour
import com.github.skydoves.colorpicker.compose.AlphaSlider
import com.github.skydoves.colorpicker.compose.BrightnessSlider
import com.github.skydoves.colorpicker.compose.ColorPickerController
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.drawColorIndicator
import com.github.skydoves.colorpicker.compose.rememberColorPickerController

@Composable
fun DialogColourPicker(
    showDialog: MutableState<Boolean>,
    dialogTitle: String,
    currentColour: String,
    setColour: (String) -> Unit,
) {
    val scrollState = rememberScrollState()

    Dialog(
        onDismissRequest = { showDialog.value = false },
    ) {
        Column(modifier = Modifier.verticalScroll(scrollState)) {
            DialogColourPickerLayout(
                showDialog,
                dialogTitle,
                currentColour,
                setColour,
            )
        }
    }
}

@Composable
fun DialogColourPickerLayout(
    showDialog: MutableState<Boolean>,
    dialogTitle: String,
    currentColour: String,
    setColour: (String) -> Unit,
) {
    val controller = rememberColorPickerController()

    Surface(
        color = MaterialTheme.colorScheme.secondaryContainer,
        shape = RoundedCornerShape(16.dp),
    ) {
        DialogColourPicker(
            showDialog,
            dialogTitle,
            currentColour,
            setColour,
            controller,
        )
    }
}

@Composable
fun DialogColourPicker(
    showDialog: MutableState<Boolean>,
    dialogTitle: String,
    currentColour: String,
    setColour: (String) -> Unit,
    controller: ColorPickerController,
) {
    var hexCode by remember { mutableStateOf("") }
    var textColor by remember { mutableStateOf(Color.Transparent) }

    Column(
        modifier = Modifier
            .padding(top = 8.dp, bottom = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    )
    {
        Text(
            modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 24.dp),
            text = dialogTitle,
            fontSize = 24.sp,
        )

        Box(
            Modifier
                .height(275.dp)
                .width(275.dp)
                .align(Alignment.CenterHorizontally),
        ) {
            HsvColorPicker(
                modifier = Modifier.padding(24.dp),
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
                initialColor = UtilityColour.makeColor(currentColour),
            )
        }

        AlphaSlider(
            modifier = Modifier
                .padding(top = 12.dp, start = 24.dp, end = 24.dp, bottom = 12.dp)
                .height(35.dp)
                .align(Alignment.CenterHorizontally),
            controller = controller,
            initialColor = UtilityColour.makeColor(currentColour),
        )

        BrightnessSlider(
            modifier = Modifier
                .padding(top = 12.dp, start = 24.dp, end = 24.dp, bottom = 12.dp)
                .height(35.dp)
                .align(Alignment.CenterHorizontally),
            controller = controller,
            initialColor = UtilityColour.makeColor(currentColour),
        )

        Text(
            text = hexCode,
            fontSize = 16.sp,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 12.dp, bottom = 12.dp),
        )

        ColourPickerCancelOk(showDialog, setColour, hexCode)
    }
}

@Composable
fun ColourPickerCancelOk(
    showDialog: MutableState<Boolean>,
    setColour: (String) -> Unit,
    hexCode: String,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 8.dp),
        horizontalArrangement = Arrangement.End,
    ) {
        TextButton(
            modifier = Modifier.padding(end = 18.dp),
            onClick = { showDialog.value = false },
        ) {
            Text(stringResource(com.github.jameshnsears.chance.common.R.string.dialog_cancel))
        }

        TextButton(
            onClick = {
                showDialog.value = false
                setColour(hexCode)
            },
        ) {
            Text(stringResource(com.github.jameshnsears.chance.common.R.string.dialog_ok))
        }
    }
}
