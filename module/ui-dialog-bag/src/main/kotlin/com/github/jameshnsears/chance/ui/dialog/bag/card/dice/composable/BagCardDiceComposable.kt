package com.github.jameshnsears.chance.ui.dialog.bag.card.dice.composable

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
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
import com.github.jameshnsears.chance.ui.dialog.bag.R
import com.github.jameshnsears.chance.ui.dialog.bag.card.composable.BagCardColourSample
import com.github.jameshnsears.chance.ui.dialog.bag.card.dice.CardDiceViewModel
import com.github.jameshnsears.chance.ui.dialog.colour.composable.DialogColourPicker
import kotlin.math.roundToInt

class BagCardDiceTestTag {
    companion object {
        const val DICE_TITLE = "DICE_TITLE"
        const val DICE_SIDES = "DICE_SIDES"
        const val DICE_COLOUR = "DICE_COLOUR"
    }
}

@Composable
fun BagCardDice(
    cardDiceViewModel: CardDiceViewModel,
) {
    OutlinedCard(
        modifier = Modifier
            .padding(top = 24.dp, bottom = 24.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp,
        ),
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(
                text = stringResource(R.string.dialog_bag_dice),
                modifier = Modifier
                    .padding(top = 8.dp, bottom = 8.dp)
                    .wrapContentSize(Alignment.Center),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
            )

            DiceTitle(cardDiceViewModel)

            HorizontalDivider(
                modifier = Modifier
                    .padding(top = 12.dp, bottom = 12.dp)
            )

            DiceSides(cardDiceViewModel)

            HorizontalDivider(
                modifier = Modifier
                    .padding(top = 12.dp, bottom = 12.dp)
            )

            DiceColour(cardDiceViewModel)
        }
    }
}

@Composable
fun DiceSides(
    cardDiceViewModel: CardDiceViewModel
) {
    val sliderDisplayValues = BagCardDiceSliderSides(LocalContext.current).values()

    val stateFlowCardDice =
        cardDiceViewModel.stateFlowCardDice.collectAsStateWithLifecycle(
            lifecycleOwner = LocalLifecycleOwner.current
        )

    var sliderPosition = stateFlowCardDice.value.diceSidesPosition

    Row(modifier = Modifier.padding(top = 8.dp)) {
        Text(
            "${stringResource(R.string.dialog_bag_dice_sides)}: ${sliderDisplayValues[sliderPosition.roundToInt()]}",
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .width(80.dp)
        )

        Spacer(modifier = Modifier.width(6.dp))

        Slider(
            value = sliderPosition,
            onValueChange = {
                sliderPosition = it
                cardDiceViewModel.diceSidesSize(sliderDisplayValues[sliderPosition.roundToInt()])
            },
            valueRange = 0f..sliderDisplayValues.lastIndex.toFloat(),
            steps = sliderDisplayValues.lastIndex - 1,
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.secondary,
                activeTrackColor = MaterialTheme.colorScheme.secondary,
                inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            modifier = Modifier
                .padding(start = 8.dp, end = 12.dp)
                .testTag(BagCardDiceTestTag.DICE_SIDES),
        )
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
            text = stringResource(R.string.dialog_bag_dice_sides_info),
        )
    }
}

@Composable
fun DiceTitle(cardDiceViewModel: CardDiceViewModel) {
    val stateFlowCardDice =
        cardDiceViewModel.stateFlowCardDice.collectAsStateWithLifecycle(
            lifecycleOwner = LocalLifecycleOwner.current
        )

    val diceTitle = stateFlowCardDice.value.diceTitle

    OutlinedTextField(
        value = diceTitle,
        onValueChange = {
            if (it.length <= 25)
                cardDiceViewModel.diceTitle(it)
        },
        label = { Text(stringResource(R.string.dialog_bag_dice_title)) },
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 8.dp)
            .testTag(BagCardDiceTestTag.DICE_TITLE),
        trailingIcon = {
            if (diceTitle.isNotEmpty()) {
                IconButton(onClick = {
                    cardDiceViewModel.diceTitle(
                        ""
                    )
                }) {
                    Icon(
                        painterResource(id = R.drawable.cancel_fill0_wght400_grad0_opsz24),
                        contentDescription = "",
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
            text = stringResource(R.string.dialog_bag_dice_title_info),
        )
    }
}

@Composable
fun DiceColour(cardDiceViewModel: CardDiceViewModel) {
    val showDialogColourPicker = rememberSaveable { mutableStateOf(false) }

    val stateFlowCardDice =
        cardDiceViewModel.stateFlowCardDice.collectAsStateWithLifecycle(
            lifecycleOwner = LocalLifecycleOwner.current
        )

    val diceColour = stateFlowCardDice.value.diceColour

    Row(
        modifier = Modifier
            .padding(top = 8.dp, bottom = 8.dp)
            .fillMaxWidth()
            .testTag(BagCardDiceTestTag.DICE_COLOUR),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Button(
            onClick = {
                showDialogColourPicker.value = true
            },
            modifier = Modifier
                .width(180.dp)
                .testTag(BagCardDiceTestTag.DICE_COLOUR),

            ) {
            Icon(
                painterResource(id = R.drawable.palette_fill0_wght400_grad0_opsz24),
                contentDescription = "",
                modifier = Modifier.size(24.dp),
            )

            Spacer(Modifier.size(ButtonDefaults.IconSpacing))

            Text(stringResource(R.string.dialog_bag_dice_colour))
        }

        Spacer(modifier = Modifier.weight(1f))

        Box(modifier = Modifier.padding(end = 8.dp)) {
            BagCardColourSample(diceColour)
        }
    }

    if (showDialogColourPicker.value) {
        DialogColourPicker(
            showDialogColourPicker,
            stringResource(R.string.dialog_bag_colour_picker_dice),
            diceColour,
            cardDiceViewModel::diceColour,
        )
    }
}
