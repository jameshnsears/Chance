package com.github.jameshnsears.chance.ui.tab.roll.card

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.jameshnsears.chance.common.R
import com.github.jameshnsears.chance.data.domain.state.Dice
import com.github.jameshnsears.chance.ui.tab.roll.TabRollViewModel

class RollCardTestTag {
    companion object {
        const val ROLL_CARD = "ROLL_CARD-"
    }
}

@Composable
fun RollCards(tabRollViewModel: TabRollViewModel) {
    val diceBag by tabRollViewModel.diceBag.collectAsStateWithLifecycle()

    LazyRow {
        items(diceBag.size) { index ->
            RollCards(tabRollViewModel, diceBag[index])
        }
    }
}

@Composable
fun RollCards(tabRollViewModel: TabRollViewModel, dice: Dice) {
    val configuration = LocalConfiguration.current

    var paddingTop = 12.dp

    if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) paddingTop = 8.dp

    ElevatedCard(
        modifier = Modifier
            .padding(top = paddingTop, start = 8.dp, bottom = 8.dp, end = 8.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp,
        ),
    ) {
        Column(
            modifier = Modifier
                .padding(6.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            when (configuration.orientation) {
                Configuration.ORIENTATION_LANDSCAPE -> {
                    DiceRollChipLandscape(tabRollViewModel, dice)
                }

                else -> {
                    DiceRollChipPortrait(tabRollViewModel, dice)
                }
            }
        }
    }
}

@Composable
fun DiceRollChipLandscape(tabRollViewModel: TabRollViewModel, dice: Dice) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically)
    {
        DiceRollFilterChip(tabRollViewModel, dice)

        DiceRollIconsLandscape(dice)
    }
}

@Composable
fun DiceRollFilterChip(tabRollViewModel: TabRollViewModel, dice: Dice) {
    val selected = remember { mutableStateOf(dice.selected) }

    val title = if (dice.title != "")
        dice.title
    else
        stringResource(id = dice.titleStringsId)

    FilterChip(
        modifier = Modifier
            .padding(4.dp)
            .testTag(RollCardTestTag.ROLL_CARD + title),
        selected = selected.value,
        onClick = {
            selected.value = !selected.value
            dice.selected = selected.value

            // TODO
            // tabRollViewModel.
        },
        label = { Text(title) },
        leadingIcon = if (dice.selected) {
            {
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = dice.title,
                    modifier = Modifier.size(FilterChipDefaults.IconSize),
                )
            }
        } else {
            null
        }
    )
}

@Composable
fun DiceRollChipPortrait(tabRollViewModel: TabRollViewModel, dice: Dice) {
    Row {
        DiceRollFilterChip(tabRollViewModel, dice)
    }

    DiceRollIconsPortrait(dice)
}

@Composable
fun DiceRollIconsLandscape(dice: Dice) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (dice.multiplier && dice.explode || dice.modifyScore) {
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            DiceRollIcons(dice)
        }
    }
}

@Composable
fun DiceRollIconsPortrait(dice: Dice) {
    Row(
        modifier = Modifier.width(90.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        DiceRollIcons(dice)
    }
}

@Composable
fun DiceRollIcons(dice: Dice) {
    if (dice.multiplier) {
        Icon(
            painterResource(id = R.drawable.roll_repeat_fill0_wght400_grad0_opsz24),
            contentDescription = stringResource(R.string.tab_roll_feature_repeat),
        )
    }

    if (dice.multiplier && (dice.explode || dice.modifyScore)) {
        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
    }

    if (dice.explode) {
        Icon(
            painterResource(id = R.drawable.roll_explode_fill0_wght400_grad0_opsz24),
            contentDescription = stringResource(R.string.tab_roll_feature_explode),
        )
    }

    if ((dice.multiplier || dice.explode) && dice.modifyScore) {
        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
    }

    if (dice.modifyScore) {
        Icon(
            painterResource(id = R.drawable.roll_add_subtract_fill0_wght400_grad0_opsz24),
            contentDescription = stringResource(R.string.tab_roll_feature_modify_score),
        )
    }

    if (!dice.multiplier && !dice.explode && !dice.modifyScore) {
        Icon(
            painterResource(id = R.drawable.roll_add_subtract_fill0_wght400_grad0_opsz24),
            contentDescription = stringResource(R.string.tab_roll_feature_modify_score),
            modifier = Modifier.alpha(0f)
        )
    }
}