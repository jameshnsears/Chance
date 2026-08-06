package com.github.jameshnsears.chance.ui.dialog.dice.card.roll

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.jameshnsears.chance.common.R
import com.github.jameshnsears.chance.data.domain.core.DiceRollValues


@Composable
fun BagCardRoll(
    cardRollService: CardRollService,
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
            Column(modifier = Modifier.padding(top = 16.dp)) {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
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
                            text = stringResource(R.string.dialog_bag_roll_info),
                            style = MaterialTheme.typography.bodySmall,
                        )
                    }
                }

                HorizontalDivider(
                    modifier = Modifier
                        .padding(top = 12.dp, bottom = 12.dp)
                )

                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    RollMultiplier(cardRollService)
                }

                HorizontalDivider(
                    modifier = Modifier
                        .padding(top = 12.dp, bottom = 12.dp)
                )

                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    RollExplode(cardRollService)
                }

                HorizontalDivider(
                    modifier = Modifier
                        .padding(top = 12.dp, bottom = 12.dp)
                )

                Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)) {
                    RollScore(cardRollService)
                }
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
            CardRollTestTag.ROLL_MULTIPLIER_VALUE,
            DiceRollValues.MULTIPLIER_VALUES,
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

    val rollExplode = stateFlowCardRoll.value.rollExplode

    FlowRow(
        modifier = Modifier
            .padding(top = 8.dp)
            .fillMaxWidth()
            .testTag(CardRollTestTag.ROLL_EXPLODE),
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .heightIn(min = 48.dp)
                .padding(start = 6.dp)
                .testTag(CardRollTestTag.ROLL_EXPLODE_CHECKBOX)
                .toggleable(
                    value = rollExplode,
                    onValueChange = { cardRollService.rollExplode(it) },
                    role = Role.Checkbox
                )
        ) {
            Icon(
                painterResource(id = R.drawable.dice_roll_explode),
                contentDescription = stringResource(R.string.explode),
                modifier = Modifier.size(24.dp),
            )

            Checkbox(
                modifier = Modifier
                    .minimumInteractiveComponentSize(),
                checked = rollExplode,
                onCheckedChange = null
            )

            Spacer(Modifier.width(3.dp))

            Text(stringResource(R.string.dialog_bag_roll_explode_side))
        }

        Row(
            modifier = Modifier
                .padding(top = 8.dp)
        ) {
            RollExplodeLayout(cardRollService, rollExplode)
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
                text = stringResource(R.string.dialog_bag_roll_explode_meaning),
                style = MaterialTheme.typography.bodySmall,
            )
        }

    }
}

@Composable
fun RollExplodeLayout(cardRollService: CardRollService, rollExplode: Boolean) {
    val stateFlowCardRoll =
        cardRollService.stateFlowCardRoll.collectAsStateWithLifecycle(
            lifecycleOwner = LocalLifecycleOwner.current
        )

    Column {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(ButtonDefaults.IconSpacing),
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(Modifier.width(72.dp))

            RollExplodeDropdownWhen(cardRollService, stateFlowCardRoll, rollExplode)

            RollExplodeDropdownValue(cardRollService, stateFlowCardRoll, rollExplode)
        }
    }
}

@Composable
private fun RollExplodeDropdownValue(
    cardRollService: CardRollService,
    stateFlow: State<CardRollState>,
    enabled: Boolean
) {
    GenericExposedDropdownMenuBox(
        cardRollService::rollExplodeValue,
        CardRollTestTag.ROLL_EXPLODE_VALUE,
        stateFlow.value.rollExplodeAvailableValues,
        stateFlow.value.rollExplodeValue.toString(),
        90.dp,
        enabled
    )
}

@Composable
private fun RollExplodeDropdownWhen(
    cardRollService: CardRollService,
    stateFlow: State<CardRollState>,
    enabled: Boolean
) {
    GenericExposedDropdownMenuBox(
        cardRollService::rollExplodeWhen,
        CardRollTestTag.ROLL_EXPLODE_WHEN,
        DiceRollValues.EXPLODE_WHEN_VALUES,
        stateFlow.value.rollExplodeWhen,
        80.dp,
        enabled
    )
}

@Composable
fun RollScore(cardRollService: CardRollService) {
    val stateFlowCardRoll =
        cardRollService.stateFlowCardRoll.collectAsStateWithLifecycle(
            lifecycleOwner = LocalLifecycleOwner.current
        )

    val rollModifyScore = stateFlowCardRoll.value.rollModifyScore

    Row(
        modifier = Modifier
            .padding(start = 6.dp, top = 8.dp, bottom = 8.dp)
            .fillMaxWidth()
            .testTag(CardRollTestTag.ROLL_MODIFY_SCORE),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .heightIn(min = 48.dp)
                .testTag(CardRollTestTag.ROLL_MODIFY_SCORE_CHECKBOX)
                .toggleable(
                    value = rollModifyScore,
                    onValueChange = { cardRollService.rollModifyScore(it) },
                    role = Role.Checkbox
                )
        ) {
            Icon(
                painterResource(id = R.drawable.dice_roll_add_subtract),
                contentDescription = stringResource(R.string.adjustment),
                modifier = Modifier.size(24.dp),
            )

            Checkbox(
                modifier = Modifier
                    .minimumInteractiveComponentSize(),
                checked = rollModifyScore,
                onCheckedChange = null
            )

            Spacer(Modifier.width(3.dp))

            Text(stringResource(R.string.dialog_bag_roll_score))
        }

        Spacer(Modifier.width(20.dp))

        GenericExposedDropdownMenuBox(
            cardRollService::rollModifyScoreValue,
            CardRollTestTag.ROLL_MODIFY_SCORE_VALUE,
            DiceRollValues.MODIFY_SCORE_VALUES,
            stateFlowCardRoll.value.rollModifyScoreValue.toString(),
            90.dp,
            rollModifyScore
        )
    }
}
