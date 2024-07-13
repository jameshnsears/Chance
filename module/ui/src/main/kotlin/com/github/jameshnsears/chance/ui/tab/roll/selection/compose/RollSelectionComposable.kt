package com.github.jameshnsears.chance.ui.tab.roll.selection.compose

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.jameshnsears.chance.common.R
import com.github.jameshnsears.chance.data.domain.core.Dice
import com.github.jameshnsears.chance.ui.tab.roll.TabRollAndroidViewModel
import com.github.jameshnsears.chance.ui.utility.colour.UtilityColour

class RollSelectionTestTag {
    companion object {
        const val ROLL_BUTTON = "ROLL_BUTTON-"
    }
}

@Composable
fun RollSelectionRow(tabRollAndroidViewModel: TabRollAndroidViewModel) {
    val stateDiceBag =
        tabRollAndroidViewModel.diceBag.collectAsStateWithLifecycle(
            lifecycleOwner = androidx.compose.ui.platform.LocalLifecycleOwner.current
        )

    val diceBag = stateDiceBag.value

    LazyRow {
        items(diceBag.size) { index ->
            RollSelectionButton(tabRollAndroidViewModel, diceBag[index])
        }
    }
}

@Composable
fun RollSelectionButton(tabRollAndroidViewModel: TabRollAndroidViewModel, dice: Dice) {
    val rollSelectionIconColour = if (isSystemInDarkTheme()) Color.Black else Color.White

    Column(
        modifier = Modifier.padding(start = 24.dp, top = 8.dp, bottom = 24.dp, end = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.padding(bottom = 18.dp),
            text = dice.title,
            fontSize = 14.sp
        )

        Box(
            modifier = Modifier
                .size(95.dp)
                .rotate(45f)
                .background(MaterialTheme.colorScheme.primary, shape = MaterialTheme.shapes.medium)
                .clickable(onClick = {
                    tabRollAndroidViewModel.markDiceAsSelected(dice, !dice.selected)
                })
                .testTag(RollSelectionTestTag.ROLL_BUTTON + dice.title),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .rotate(-45f)
                    .fillMaxHeight()
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                RollSelectionIconColour(dice)

                RollSelectionIconsCenter(rollSelectionIconColour, dice)

                RollSelectionIconExplode(rollSelectionIconColour, dice)
            }
        }
    }
}

@Composable
private fun RollSelectionIconScore(
    rollSelectionIconColour: Color,
    dice: Dice
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (dice.modifyScore) {
            Icon(
                painterResource(id = R.drawable.roll_add_subtract_fill0_wght400_grad0_opsz24),
                contentDescription = "",
                modifier = Modifier.size(14.dp),
                tint = rollSelectionIconColour
            )

            Text(
                "${dice.modifyScoreValue}",
                fontSize = 12.sp,
                color = rollSelectionIconColour
            )
        }
    }
}

@Composable
private fun RollSelectionIconsCenter(
    rollSelectionIconColour: Color,
    dice: Dice,
) {
    Row(
        modifier = Modifier
            .padding(2.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Box(modifier = Modifier.weight(1f)) {
            RollSelectionIconMultiplier(rollSelectionIconColour, dice)
        }

        Box(modifier = Modifier.weight(1f)) {
            RollSelectionIconsCenterPlus(rollSelectionIconColour, dice)
        }

        Box(modifier = Modifier.weight(1f)) {
            RollSelectionIconScore(rollSelectionIconColour, dice)
        }
    }
}

@Composable
private fun RollSelectionIconColour(
    dice: Dice
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Canvas(modifier = Modifier.size(24.dp)) {
            val outerRadius = size.minDimension / 2.5f
            val innerRadius = size.minDimension / 3f

            // outer white circle
            drawCircle(
                color = Color.White,
                radius = outerRadius
            )

            drawCircle(
                color = UtilityColour.makeColor(dice.colour),
                radius = innerRadius
            )
        }
    }
}

@Composable
private fun RollSelectionIconMultiplier(
    rollSelectionIconColour: Color,
    dice: Dice
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painterResource(id = R.drawable.roll_repeat_fill0_wght400_grad0_opsz24),
            contentDescription = "",
            modifier = Modifier.size(14.dp),
            tint = rollSelectionIconColour
        )

        Text(
            "${dice.multiplierValue}",
            fontSize = 12.sp,
            color = rollSelectionIconColour
        )
    }
}

@Composable
private fun RollSelectionIconsCenterPlus(
    rollSelectionIconColour: Color,
    dice: Dice
) {
    Row {
        if (dice.selected)
            Box(
                modifier = Modifier.size(40.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Done,
                    contentDescription = "",
                    modifier = Modifier.size(24.dp),
                    tint = rollSelectionIconColour
                )
            }
        else
            Box(modifier = Modifier.size(40.dp))
    }
}

@Composable
private fun RollSelectionIconExplode(
    rollSelectionIconColour: Color,
    dice: Dice
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (dice.explode) {
            Icon(
                painterResource(id = R.drawable.roll_explode_fill0_wght400_grad0_opsz24),
                contentDescription = "",
                modifier = Modifier.size(14.dp),
                tint = rollSelectionIconColour
            )

            @Suppress("RemoveSingleExpressionStringTemplate")
            Text(
                "${dice.explodeWhen}",
                fontSize = 12.sp,
                color = rollSelectionIconColour
            )

            Text(
                "${dice.explodeValue}",
                fontSize = 12.sp,
                color = rollSelectionIconColour
            )
        }
    }
}
