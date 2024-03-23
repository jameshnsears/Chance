package com.github.jameshnsears.chance.ui.dialog.bag.card.roll.compose

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.jameshnsears.chance.ui.dialog.bag.card.roll.CardRollViewModel
import com.github.jameshnsears.chance.ui.dialog.dice.R

class BagCardRollTestTag {
    companion object {
        const val ROLL_MULTIPLIER = "ROLL_MULTIPLIER"
        const val ROLL_MULTIPLIER_VALUE = "ROLL_MULTIPLIER_VALUE"
        const val ROLL_EXPLODE = "ROLL_EXPLODE"
        const val ROLL_EXPLODE_EQUALITY = "ROLL_EXPLODE_EQUALITY"
        const val ROLL_EXPLODE_SIDE = "ROLL_EXPLODE_SIDE"
        const val ROLL_SCORE = "ROLL_SCORE"
        const val ROLL_SCORE_VALUE = "ROLL_SCORE_VALUE"
    }
}

@Composable
fun BagCardRoll(
    cardRollViewModel: CardRollViewModel,
) {
    ElevatedCard(
        modifier = Modifier
            .padding(bottom = 12.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp,
        ),
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = stringResource(R.string.dialog_bag_roll),
                modifier = Modifier
                    .wrapContentSize(Alignment.Center),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
            )

            Roll(cardRollViewModel)
        }
    }
}

@Composable
fun Roll(cardRollViewModel: CardRollViewModel) {
    Column {
        RollMultiplier(cardRollViewModel)

        HorizontalDivider()

        RollExplode(cardRollViewModel)

        HorizontalDivider()

        RollScore(cardRollViewModel)
    }
}

@Composable
private fun RollMultiplier(cardRollViewModel: CardRollViewModel) {
    val stateFlow =
        cardRollViewModel.stateFlowCardRoll.collectAsStateWithLifecycle()

    var rollMultiplier = stateFlow.value.rollMultiplier

    Row(
        modifier = Modifier
            .padding(top = 8.dp, bottom = 12.dp)
            .fillMaxWidth()
            .testTag(BagCardRollTestTag.ROLL_MULTIPLIER),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(
            checked = rollMultiplier,
            onCheckedChange = {
                rollMultiplier = it
                cardRollViewModel.rollMultiplier(it)
            }
        )

        Icon(
            painterResource(id = com.github.jameshnsears.chance.common.R.drawable.roll_repeat_fill0_wght400_grad0_opsz24),
            contentDescription = stringResource(R.string.dialog_bag_roll_repeat),
            modifier = Modifier.size(24.dp),
        )

        Spacer(Modifier.size(ButtonDefaults.IconSpacing))

        Text(stringResource(R.string.dialog_bag_roll_repeat))

        Spacer(Modifier.size(ButtonDefaults.IconSpacing))

        GenericExposedDropdownMenuBox(
            cardRollViewModel::rollMultiplierValue,
            BagCardRollTestTag.ROLL_MULTIPLIER_VALUE,
            listOf("2", "3", "4", "5", "6", "7", "8", "9", "10"),
            90.dp
        )
    }
}

@Composable
private fun RollExplode(cardRollViewModel: CardRollViewModel) {
    val stateFlow =
        cardRollViewModel.stateFlowCardRoll.collectAsStateWithLifecycle()

    var rollExplode = stateFlow.value.rollExplode

    val configuration = LocalConfiguration.current

    Row(
        modifier = Modifier
            .padding(top = 4.dp)
            .testTag(BagCardRollTestTag.ROLL_EXPLODE_SIDE)
            .fillMaxWidth()
            .testTag(BagCardRollTestTag.ROLL_EXPLODE),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = rollExplode,
                onCheckedChange = {
                    rollExplode = it
                    cardRollViewModel.rollExplode(it)
                }
            )

            Icon(
                painterResource(id = com.github.jameshnsears.chance.common.R.drawable.roll_explode_fill0_wght400_grad0_opsz24),
                contentDescription = stringResource(R.string.dialog_bag_roll_explode_when),
                modifier = Modifier.size(24.dp),
            )

            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
        }

        when (configuration.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> {
                RollExplodeLandscape(cardRollViewModel)
            }

            else -> {
                RollExplodePortrait(cardRollViewModel)
            }
        }
    }

    Row(
        modifier = Modifier
            .padding(top = 4.dp, bottom = 12.dp)
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
            text = stringResource(R.string.dialog_bag_roll_explode_depth),
        )
    }
}

@Composable
fun RollExplodeLandscape(cardRollViewModel: CardRollViewModel) {
    val sides = (1..cardRollViewModel.dice.sides.size).toList()

    Column {
        Row(
            modifier = Modifier
                .padding(top = 8.dp, bottom = 4.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(stringResource(R.string.dialog_bag_roll_explode_when))

            Spacer(Modifier.size(ButtonDefaults.IconSpacing))

            GenericExposedDropdownMenuBox(
                cardRollViewModel::rollExplodeWhen,
                BagCardRollTestTag.ROLL_EXPLODE_EQUALITY,
                listOf(
                    stringResource(R.string.dialog_bag_roll_equals),
                    stringResource(R.string.dialog_bag_roll_less_than),
                    stringResource(R.string.dialog_bag_roll_greater_than)
                ),
                80.dp
            )

            Spacer(Modifier.size(ButtonDefaults.IconSpacing))

            Text(stringResource(R.string.dialog_bag_roll_value))

            Spacer(Modifier.size(ButtonDefaults.IconSpacing))

            GenericExposedDropdownMenuBox(
                cardRollViewModel::rollExplodeValue,
                BagCardRollTestTag.ROLL_EXPLODE_SIDE,
                sides.map { it.toString() },
                90.dp
            )
        }
    }
}

@Composable
fun RollExplodePortrait(cardRollViewModel: CardRollViewModel) {
    val sides = (1..cardRollViewModel.dice.sides.size).toList()

    Column {
        Row(
            modifier = Modifier
                .padding(top = 8.dp, bottom = 4.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(stringResource(R.string.dialog_bag_roll_explode_when))

            Spacer(Modifier.size(ButtonDefaults.IconSpacing))

            GenericExposedDropdownMenuBox(
                cardRollViewModel::rollExplodeWhen,
                BagCardRollTestTag.ROLL_EXPLODE_EQUALITY,
                listOf(
                    stringResource(R.string.dialog_bag_roll_equals),
                    stringResource(R.string.dialog_bag_roll_less_than),
                    stringResource(R.string.dialog_bag_roll_greater_than)
                ),
                80.dp
            )
        }

        Row(
            modifier = Modifier
                .padding(top = 4.dp, bottom = 4.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(stringResource(R.string.dialog_bag_roll_value))

            Spacer(Modifier.size(ButtonDefaults.IconSpacing))

            GenericExposedDropdownMenuBox(
                cardRollViewModel::rollExplodeValue,
                BagCardRollTestTag.ROLL_EXPLODE_SIDE,
                sides.map { it.toString() },
                90.dp
            )
        }
    }
}

@Composable
fun RollScore(cardRollViewModel: CardRollViewModel) {
    val stateFlow =
        cardRollViewModel.stateFlowCardRoll.collectAsStateWithLifecycle()

    var rollModifyScore = stateFlow.value.rollModifyScore

    Row(
        modifier = Modifier
            .padding(top = 12.dp, bottom = 4.dp)
            .fillMaxWidth()
            .testTag(BagCardRollTestTag.ROLL_SCORE),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = rollModifyScore,
            onCheckedChange = {
                rollModifyScore = it
                cardRollViewModel.rollModifyScore(it)
            }
        )

        Icon(
            painterResource(id = com.github.jameshnsears.chance.common.R.drawable.roll_add_subtract_fill0_wght400_grad0_opsz24),
            contentDescription = stringResource(R.string.dialog_bag_roll_score),
            modifier = Modifier.size(24.dp),
        )

        Spacer(Modifier.size(ButtonDefaults.IconSpacing))

        Text(stringResource(R.string.dialog_bag_roll_score))

        Spacer(Modifier.size(ButtonDefaults.IconSpacing))

        GenericExposedDropdownMenuBox(
            cardRollViewModel::rollModifyScoreValue,
            BagCardRollTestTag.ROLL_SCORE_VALUE,
            listOf("-5", "-4", "-3", "-2", "-1", "1", "2", "3", "4", "5"),
            90.dp
        )
    }
}
