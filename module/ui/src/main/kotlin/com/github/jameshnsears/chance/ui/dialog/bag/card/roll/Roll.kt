package com.github.jameshnsears.chance.ui.dialog.bag.card.roll

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.jameshnsears.chance.common.R
import com.github.jameshnsears.chance.data.domain.core.DiceRollValues

class BagCardRollTestTag {
    companion object {
        const val ROLL_MULTIPLIER_VALUE = "ROLL_MULTIPLIER_VALUE"
        const val ROLL_EXPLODE = "ROLL_EXPLODE"
        const val ROLL_EXPLODE_CHECKBOX = "ROLL_EXPLODE_CHECKBOX"
        const val ROLL_EXPLODE_WHEN = "ROLL_EXPLODE_WHEN"
        const val ROLL_EXPLODE_VALUE = "ROLL_EXPLODE_VALUE"
        const val ROLL_MODIFY_SCORE = "ROLL_MODIFY_SCORE"
        const val ROLL_MODIFY_SCORE_CHECKBOX = "ROLL_MODIFY_SCORE_CHECKBOX"
        const val ROLL_MODIFY_SCORE_VALUE = "ROLL_MODIFY_SCORE_VALUE"
    }
}

@Composable
fun BagCardRoll(
    cardRollService: CardRollService,
) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(bottom = 8.dp)
    ) {
        OutlinedCard(
            modifier = Modifier
                .padding(top = 4.dp)
                .fillMaxWidth(),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp,
            ),
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                RollMultiplier(cardRollService)

                HorizontalDivider(
                    modifier = Modifier
                        .padding(top = 12.dp, bottom = 12.dp)
                )
                RollExplode(cardRollService)

                HorizontalDivider(
                    modifier = Modifier
                        .padding(top = 12.dp, bottom = 12.dp)
                )
                RollScore(cardRollService)
            }
        }
    }
}

@Composable
private fun RollMultiplier(cardRollService: CardRollService) {
    val stateFlowCardRoll =
        cardRollService.stateFlowCardRoll.collectAsStateWithLifecycle(
            lifecycleOwner = LocalLifecycleOwner.current
        )

    Row(
        modifier = Modifier
            .padding(start = 6.dp, top = 8.dp, bottom = 8.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val repeatPainter = painterResource(id = R.drawable.dice_roll_multiplier)

        val iconModifier = remember { Modifier.size(24.dp) }

        Icon(
            repeatPainter,
            contentDescription = stringResource(R.string.multiplier),
            modifier = iconModifier,
        )

        Spacer(Modifier.size(ButtonDefaults.IconSpacing))

        Text(stringResource(R.string.dialog_bag_roll_repeat))

        Spacer(Modifier.width(20.dp))

        GenericExposedDropdownMenuBox(
            cardRollService::rollMultiplierValue,
            BagCardRollTestTag.ROLL_MULTIPLIER_VALUE,
            DiceRollValues.multiplierValues,
            stateFlowCardRoll.value.rollMultiplierValue.toString(),
            90.dp
        )
    }
}

@Composable
private fun RollExplode(cardRollService: CardRollService) {
    val stateFlowCardRoll =
        cardRollService.stateFlowCardRoll.collectAsStateWithLifecycle(
            lifecycleOwner = LocalLifecycleOwner.current
        )

    var rollExplode = stateFlowCardRoll.value.rollExplode

    Row(
        modifier = Modifier
            .padding(top = 8.dp)
            .fillMaxWidth()
            .testTag(BagCardRollTestTag.ROLL_EXPLODE),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 6.dp)
        ) {
            Icon(
                painterResource(id = R.drawable.dice_roll_explode),
                contentDescription = stringResource(R.string.explode),
                modifier = Modifier.size(24.dp),
            )

            Checkbox(
                modifier = Modifier.testTag(BagCardRollTestTag.ROLL_EXPLODE_CHECKBOX),
                checked = rollExplode,
                onCheckedChange = {
                    rollExplode = it
                    cardRollService.rollExplode(it)
                }
            )

            Spacer(Modifier.width(3.dp))

            Text(stringResource(R.string.dialog_bag_roll_explode_side))

            Spacer(Modifier.width(20.dp))
        }

        RollExplodeLayout(cardRollService)
    }

    Row(
        modifier = Modifier
            .padding(start = 6.dp, top = 8.dp, bottom = 8.dp)
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
            .padding(start = 6.dp, top = 8.dp, bottom = 4.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.dialog_bag_roll_explode_depth),
        )
    }
}

@Composable
fun RollExplodeLayout(cardRollService: CardRollService) {
    val stateFlowCardRoll =
        cardRollService.stateFlowCardRoll.collectAsStateWithLifecycle(
            lifecycleOwner = LocalLifecycleOwner.current
        )

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RollExplodeDropdownWhen(cardRollService, stateFlowCardRoll)

            Spacer(Modifier.size(ButtonDefaults.IconSpacing))

            RollExplodeDropdownValue(cardRollService, stateFlowCardRoll)
        }
    }
}

@Composable
private fun RollExplodeDropdownValue(
    cardRollService: CardRollService,
    stateFlow: State<CardRollState>
) {
    GenericExposedDropdownMenuBox(
        cardRollService::rollExplodeValue,
        BagCardRollTestTag.ROLL_EXPLODE_VALUE,
        stateFlow.value.rollExplodeAvailableValues,
        stateFlow.value.rollExplodeValue.toString(),
        90.dp
    )
}

@Composable
private fun RollExplodeDropdownWhen(
    cardRollService: CardRollService,
    stateFlow: State<CardRollState>
) {
    GenericExposedDropdownMenuBox(
        cardRollService::rollExplodeWhen,
        BagCardRollTestTag.ROLL_EXPLODE_WHEN,
        DiceRollValues.explodeWhenValues,
        stateFlow.value.rollExplodeWhen,
        80.dp
    )
}

@Composable
fun RollScore(cardRollService: CardRollService) {
    val stateFlowCardRoll =
        cardRollService.stateFlowCardRoll.collectAsStateWithLifecycle(
            lifecycleOwner = LocalLifecycleOwner.current
        )

    var rollModifyScore = stateFlowCardRoll.value.rollModifyScore

    Row(
        modifier = Modifier
            .padding(start = 6.dp, top = 8.dp, bottom = 8.dp)
            .fillMaxWidth()
            .testTag(BagCardRollTestTag.ROLL_MODIFY_SCORE),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painterResource(id = R.drawable.dice_roll_add_subtract),
            contentDescription = stringResource(R.string.adjustment),
            modifier = Modifier.size(24.dp),
        )

        Checkbox(
            modifier = Modifier.testTag(BagCardRollTestTag.ROLL_MODIFY_SCORE_CHECKBOX),
            checked = rollModifyScore,
            onCheckedChange = {
                rollModifyScore = it
                cardRollService.rollModifyScore(it)
            }
        )

        Spacer(Modifier.width(3.dp))

        Text(stringResource(R.string.dialog_bag_roll_score))

        Spacer(Modifier.width(20.dp))

        GenericExposedDropdownMenuBox(
            cardRollService::rollModifyScoreValue,
            BagCardRollTestTag.ROLL_MODIFY_SCORE_VALUE,
            DiceRollValues.modifyScoreValues,
            stateFlowCardRoll.value.rollModifyScoreValue.toString(),
            90.dp
        )
    }
}
