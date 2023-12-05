package com.github.jameshnsears.chance.ui.dialog.dice

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlin.math.roundToInt


@Composable
fun DialogDice(
    showDialog: MutableState<Boolean>,
    dialogDiceViewModel: DialogDiceViewModel,
) {
    Dialog(onDismissRequest = { showDialog.value = false }) {
        DialogDiceLayout(
            showDialog,
            dialogDiceViewModel,
            DialogDiceSliderSides(LocalContext.current),
            DialogDiceSliderPenaltyBonus(LocalContext.current),
        )
    }
}

@Composable
fun DialogDiceLayout(
    showDialog: MutableState<Boolean>,
    viewModel: DialogDiceViewModel,
    sliderSidesDisplayValues: DialogDiceSliderValues,
    sliderPenaltyBonusDisplayValues: DialogDiceSliderValues
) {
    val sliderSidesValue = remember { mutableFloatStateOf(viewModel.sliderSidesPosition.value) }

    val sliderPenaltyBonusValue =
        remember { mutableFloatStateOf(viewModel.sliderPenaltyBonusPosition.value) }

    Card(
        shape = RoundedCornerShape(16.dp),
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            DiceTitle(viewModel.diceIndex)

            DiceSlider(
                stringResource(R.string.dialog_dice_sides),
                sliderSidesDisplayValues.values(),
                sliderSidesValue,
                viewModel::updateCurrentSliderSidesPosition,
                "diceSliderSides"
            )

            DiceTextFieldDescription(viewModel)

            DiceButtonColour()

            DiceSlider(
                stringResource(R.string.dialog_dice_penalty_bonus),
                sliderPenaltyBonusDisplayValues.values(),
                sliderPenaltyBonusValue,
                viewModel::updateCurrentSliderPenaltyBonusPosition,
                "diceSliderPenaltyBonus"
            )

            DiceButtonsFooter(showDialog, viewModel)
        }
    }
}

@Composable
fun DiceButtonsFooter(
    showDialog: MutableState<Boolean>,
    viewModel: DialogDiceViewModel
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
    ) {
        TextButton(
            onClick = { showDialog.value = false },
            modifier = Modifier
                .padding(8.dp)
                .testTag("diceButtonClone")
        ) {
            Text(stringResource(R.string.dialog_dice_clone))
        }

        if (viewModel.canBeDeleted()) {
            TextButton(
                onClick = { showDialog.value = false },
                modifier = Modifier
                    .padding(8.dp)
                    .testTag("diceButtonDelete")
            ) {
                Text(stringResource(R.string.dialog_dice_delete))
            }
        }

        TextButton(
            onClick = {
                viewModel.ok()
                showDialog.value = false
            },
            modifier = Modifier
                .padding(8.dp)
                .testTag("diceButtonOk")
        ) {
            Text(stringResource(R.string.dialog_dice_ok))
        }
    }
}

@Composable
fun DiceTitle(diceIndex: Int) {
    Text(
        text = diceIndex.toString(),
        modifier = Modifier
            .wrapContentSize(Alignment.Center),
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp
    )
}

@Composable
fun DiceTextFieldDescription(viewModel: DialogDiceViewModel) {
    val descriptionText = viewModel.description.collectAsStateWithLifecycle()

    OutlinedTextField(
        value = descriptionText.value,
        onValueChange = { viewModel.updateCurrentDescription(it) },
        label = { Text(stringResource(R.string.dialog_dice_description)) },
        singleLine = true,
        modifier = Modifier
            .testTag("diceDescription")
            .fillMaxWidth()
    )
}

@Composable
fun DiceButtonColour() {
    val paletteIcon = painterResource(id = R.drawable.palette_fill0_wght400_grad0_opsz24)

    Row(
        modifier = Modifier
            .padding(top = 16.dp)
    ) {
        Button(
            onClick = { /* Do something when clicked */ },
            modifier = Modifier.testTag("diceButtonColour")
        ) {
            Icon(
                paletteIcon,
                contentDescription = "Color palette",
                modifier = Modifier.size(24.dp)
            )

            Spacer(Modifier.size(ButtonDefaults.IconSpacing))

            Text(stringResource(R.string.dialog_dice_colour))
        }
    }
}

@Composable
fun DiceSlider(
    sliderTitle: String,
    sliderDisplayValues: List<String>,
    sliderPosition: MutableFloatState,
    updateSliderPosition: (Float) -> Unit,
    testTag: String
) {
    Row {
        Text(
            "${sliderTitle}: ${
                sliderDisplayValues[sliderPosition.floatValue.roundToInt()]
            }",
            modifier = Modifier.align(Alignment.CenterVertically)
        )

        Spacer(modifier = Modifier.width(10.dp))

        Slider(
            value = sliderPosition.floatValue,
            onValueChange = {
                sliderPosition.floatValue = it
                updateSliderPosition(it)
            },
            valueRange = 0f..sliderDisplayValues.lastIndex.toFloat(),
            steps = sliderDisplayValues.lastIndex - 1,
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.secondary,
                activeTrackColor = MaterialTheme.colorScheme.secondary,
                inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            modifier = Modifier.testTag(testTag),
        )
    }
}
