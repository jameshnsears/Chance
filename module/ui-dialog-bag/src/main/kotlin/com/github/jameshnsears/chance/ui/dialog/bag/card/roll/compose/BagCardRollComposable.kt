package com.github.jameshnsears.chance.ui.dialog.bag.card.roll.compose

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
import androidx.compose.runtime.State
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
import com.github.jameshnsears.chance.data.domain.state.DiceRollValues
import com.github.jameshnsears.chance.ui.dialog.bag.R
import com.github.jameshnsears.chance.ui.dialog.bag.card.roll.CardRollState
import com.github.jameshnsears.chance.ui.dialog.bag.card.roll.CardRollViewModel

class BagCardRollTestTag {
    companion object {
        const val ROLL_MULTIPLIER_VALUE = "ROLL_MULTIPLIER_VALUE"
        const val ROLL_EXPLODE = "ROLL_EXPLODE"
        const val ROLL_EXPLODE_WHEN = "ROLL_EXPLODE_WHEN"
        const val ROLL_EXPLODE_VALUE = "ROLL_EXPLODE_VALUE"
        const val ROLL_MODIFY_SCORE = "ROLL_MODIFY_SCORE"
        const val ROLL_MODIFY_SCORE_VALUE = "ROLL_MODIFY_SCORE_VALUE"
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
                    .padding(top = 8.dp, bottom = 8.dp)
                    .wrapContentSize(Alignment.Center),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
            )

            Roll(cardRollViewModel)
        }
    }
}

@Composable
fun Roll(cardRollViewModel: CardRollViewModel) {
    Column {
        RollMultiplier(cardRollViewModel)

        HorizontalDivider(
            modifier = Modifier
                .padding(top = 12.dp, bottom = 12.dp)
        )
        RollExplode(cardRollViewModel)

        HorizontalDivider(
            modifier = Modifier
                .padding(top = 12.dp, bottom = 12.dp)
        )
        RollScore(cardRollViewModel)
    }
}

@Composable
private fun RollMultiplier(cardRollViewModel: CardRollViewModel) {
    val stateFlowCardRoll =
        cardRollViewModel.stateFlowCardRoll.collectAsStateWithLifecycle(
            lifecycleOwner = androidx.compose.ui.platform.LocalLifecycleOwner.current
        )

    Row(
        modifier = Modifier
            .padding(start = 12.dp, top = 8.dp, bottom = 8.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painterResource(id = com.github.jameshnsears.chance.common.R.drawable.roll_repeat_fill0_wght400_grad0_opsz24),
            contentDescription = "",
            modifier = Modifier.size(24.dp),
        )

        Spacer(Modifier.size(ButtonDefaults.IconSpacing))

        Text(stringResource(R.string.dialog_bag_roll_repeat))

        Spacer(Modifier.size(ButtonDefaults.IconSpacing))

        GenericExposedDropdownMenuBox(
            cardRollViewModel::rollMultiplierValue,
            BagCardRollTestTag.ROLL_MULTIPLIER_VALUE,
            DiceRollValues.multiplierValues,
            stateFlowCardRoll.value.rollMultiplierValue.toString(),
            90.dp
        )
    }
}

@Composable
private fun RollExplode(cardRollViewModel: CardRollViewModel) {
    val stateFlowCardRoll =
        cardRollViewModel.stateFlowCardRoll.collectAsStateWithLifecycle(
            lifecycleOwner = androidx.compose.ui.platform.LocalLifecycleOwner.current
        )

    var rollExplode = stateFlowCardRoll.value.rollExplode

    Row(
        modifier = Modifier
            .padding(top = 8.dp)
            .testTag(BagCardRollTestTag.ROLL_EXPLODE_VALUE)
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
                contentDescription = "",
                modifier = Modifier.size(24.dp),
            )

            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
        }

        RollExplodeLayout(cardRollViewModel)
    }

    Row(
        modifier = Modifier
            .padding(start = 12.dp, top = 8.dp, bottom = 8.dp)
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
            .padding(start = 12.dp, top = 8.dp, bottom = 8.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.dialog_bag_roll_explode_depth),
        )
    }
}

@Composable
fun RollExplodeLayout(cardRollViewModel: CardRollViewModel) {
    val stateFlowCardRoll =
        cardRollViewModel.stateFlowCardRoll.collectAsStateWithLifecycle(
            lifecycleOwner = androidx.compose.ui.platform.LocalLifecycleOwner.current
        )

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RollExplodeDropdownWhen(cardRollViewModel, stateFlowCardRoll)

            Spacer(Modifier.size(ButtonDefaults.IconSpacing))

            RollExplodeDropwonValue(cardRollViewModel, stateFlowCardRoll)
        }
    }
}

@Composable
private fun RollExplodeDropwonValue(
    cardRollViewModel: CardRollViewModel,
    stateFlow: State<CardRollState>
) {
    GenericExposedDropdownMenuBox(
        cardRollViewModel::rollExplodeValue,
        BagCardRollTestTag.ROLL_EXPLODE_VALUE,
        stateFlow.value.rollExplodeAvailableValues,
        stateFlow.value.rollExplodeValue.toString(),
        90.dp
    )
}

@Composable
private fun RollExplodeDropdownWhen(
    cardRollViewModel: CardRollViewModel,
    stateFlow: State<CardRollState>
) {
    GenericExposedDropdownMenuBox(
        cardRollViewModel::rollExplodeWhen,
        BagCardRollTestTag.ROLL_EXPLODE_WHEN,
        DiceRollValues.explodeWhenValues,
        stateFlow.value.rollExplodeWhen,
        80.dp
    )
}

@Composable
fun RollScore(cardRollViewModel: CardRollViewModel) {
    val stateFlowCardRoll =
        cardRollViewModel.stateFlowCardRoll.collectAsStateWithLifecycle(
            lifecycleOwner = androidx.compose.ui.platform.LocalLifecycleOwner.current
        )

    var rollModifyScore = stateFlowCardRoll.value.rollModifyScore

    Row(
        modifier = Modifier
            .padding(top = 8.dp, bottom = 8.dp)
            .fillMaxWidth()
            .testTag(BagCardRollTestTag.ROLL_MODIFY_SCORE),
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
            contentDescription = "",
            modifier = Modifier.size(24.dp),
        )

        Spacer(Modifier.size(ButtonDefaults.IconSpacing))

        Text(stringResource(R.string.dialog_bag_roll_score))

        Spacer(Modifier.size(ButtonDefaults.IconSpacing))

        GenericExposedDropdownMenuBox(
            cardRollViewModel::rollModifyScoreValue,
            BagCardRollTestTag.ROLL_MODIFY_SCORE_VALUE,
            DiceRollValues.modifyScoreValues,
            stateFlowCardRoll.value.rollModifyScoreValue.toString(),
            90.dp
        )
    }
}
