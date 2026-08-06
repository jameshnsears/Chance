package com.github.jameshnsears.chance.ui.dialog.dice.card.dice

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FormatListNumbered
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Title
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.jameshnsears.chance.common.R
import com.github.jameshnsears.chance.data.domain.core.DiceRollValues
import com.github.jameshnsears.chance.ui.dialog.colour.DialogColourPicker
import com.github.jameshnsears.chance.ui.dialog.dice.card.BagCardColourSample


@Composable
fun BagCardDice(
    cardDiceService: CardDiceService,
) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(bottom = 8.dp),
    ) {
        OutlinedCard(
            modifier = Modifier
                .padding(top = 4.dp)
                .fillMaxWidth(),
            colors = CardDefaults.outlinedCardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
            ),
        ) {
            Column(modifier = Modifier.padding(top = 12.dp)) {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    DiceTitle(cardDiceService)
                }

                HorizontalDivider(
                    modifier = Modifier
                        .padding(top = 12.dp, bottom = 12.dp)
                )

                Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)) {
                    DiceSides(cardDiceService)

                    DiceColour(cardDiceService)
                }
            }
        }
    }
}

@Composable
fun DiceSides(
    cardDiceService: CardDiceService
) {
    val state by cardDiceService.stateFlowCardDice.collectAsStateWithLifecycle()

    DiceSidesTextField(cardDiceService, state)

    DiceSidesInfo(state.diceSiderInfoColour)
}

@Composable
private fun DiceSidesTextField(
    cardDiceService: CardDiceService,
    state: CardDiceState,
) {
    Row(
        modifier = Modifier
            .padding(top = 8.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Outlined.FormatListNumbered,
            contentDescription = "",
        )

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            stringResource(R.string.dialog_bag_dice_sides)
        )

        Spacer(modifier = Modifier.width(12.dp))

        val arbitrarySidesValue = rememberSaveable { mutableStateOf(state.diceSidesSize.toString()) }

        OutlinedTextField(
            value = arbitrarySidesValue.value,
            onValueChange = { newValue ->
                val filtered = newValue.filter { it.isDigit() }.take(4)
                arbitrarySidesValue.value = filtered
                cardDiceService.diceSidesSize(filtered)
            },
            modifier = Modifier
                .width(120.dp)
                .testTag(CardDiceTestTag.DICE_SIDES),
            label = { Text(stringResource(R.string.dialog_bag_dice_sides_arbitrary_info)) },
            isError = arbitrarySidesValue.value.toIntOrNull()
                ?.let { it !in DiceRollValues.SIDES_MIN..DiceRollValues.SIDES_MAX } ?: true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            singleLine = true
        )
    }
}

@Composable
private fun DiceSidesInfo(sliderInfoColour: Float) {
    Row(
        modifier = Modifier
            .padding(bottom = 8.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Outlined.Info,
            contentDescription = stringResource(R.string.info),
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = sliderInfoColour),
        )
    }

    Row(
        modifier = Modifier
            .padding(top = 8.dp, bottom = 12.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.dialog_bag_dice_sides_info),
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = sliderInfoColour),
            style = MaterialTheme.typography.bodySmall,
        )
    }
}

@Composable
fun DiceTitle(cardDiceService: CardDiceService) {
    val stateFlowCardDice =
        cardDiceService.stateFlowCardDice.collectAsStateWithLifecycle(
            lifecycleOwner = LocalLifecycleOwner.current
        )

    val diceTitle = stateFlowCardDice.value.diceTitle

    OutlinedTextField(
        value = diceTitle,
        onValueChange = {
            if (it.length <= 25)
                cardDiceService.diceTitle(it)
        },
        label = { Text(stringResource(R.string.dialog_bag_dice_title)) },
        supportingText = {
            Text(
                text = "${diceTitle.length} / 25",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End,
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Outlined.Title,
                contentDescription = "",
            )
        },
        shape = RoundedCornerShape(8.dp),
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 8.dp)
            .testTag(CardDiceTestTag.DICE_TITLE),
        trailingIcon = {
            if (diceTitle.isNotEmpty()) {
                IconButton(
                    onClick = {
                        cardDiceService.diceTitle(
                            ""
                        )
                    },
                    modifier = Modifier.minimumInteractiveComponentSize()
                ) {
                    Icon(
                        painterResource(id = R.drawable.cancel),
                        contentDescription = stringResource(R.string.dialog_cancel),
                    )
                }
            }
        }
    )

    Row(
        modifier = Modifier
            .padding(bottom = 16.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Outlined.Info,
            contentDescription = stringResource(R.string.info),
        )
    }

    Row(
        modifier = Modifier
            .padding(bottom = 8.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.dialog_bag_dice_title_info_0),
            style = MaterialTheme.typography.bodySmall,
        )
    }
}

@Composable
fun DiceColour(cardDiceService: CardDiceService) {
    val showDialogColourPicker = rememberSaveable { mutableStateOf(false) }

    val stateFlowCardDice =
        cardDiceService.stateFlowCardDice.collectAsStateWithLifecycle(
            lifecycleOwner = LocalLifecycleOwner.current
        )

    val diceColour = stateFlowCardDice.value.diceColour

    Row(
        modifier = Modifier
            .padding(top = 8.dp, bottom = 4.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Button(
            onClick = {
                showDialogColourPicker.value = true
            },
            modifier = Modifier
                .width(180.dp)
                .minimumInteractiveComponentSize()
                .testTag(CardDiceTestTag.DICE_COLOUR),

            ) {
            val palettePainter = painterResource(id = R.drawable.palette)

            val iconModifier = remember { Modifier.size(24.dp) }

            Icon(
                palettePainter,
                contentDescription = stringResource(R.string.colour),
                modifier = iconModifier,
            )

            Spacer(Modifier.size(ButtonDefaults.IconSpacing))

            Text(stringResource(R.string.dialog_bag_dice_colour))
        }

        Spacer(modifier = Modifier.weight(1f))

        Box(modifier = Modifier.padding(end = 4.dp)) {
            BagCardColourSample(diceColour)
        }
    }

    if (showDialogColourPicker.value) {
        DialogColourPicker(
            showDialogColourPicker,
            stringResource(R.string.dialog_bag_colour_picker_dice),
            diceColour,
            cardDiceService::diceColour,
        )
    }
}
