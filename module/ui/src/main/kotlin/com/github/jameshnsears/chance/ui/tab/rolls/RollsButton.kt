package com.github.jameshnsears.chance.ui.tab.rolls

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.jameshnsears.chance.common.R
import com.github.jameshnsears.chance.ui.zoom.rolls.ZoomRollsAndroidViewModel


@Composable
fun RollButton(
    rollsAndroidViewModel: RollsAndroidViewModel,
    zoomRollsAndroidViewModel: ZoomRollsAndroidViewModel
) {
    val stateFlowRollEnabled =
        rollsAndroidViewModel.rollEnabled.collectAsStateWithLifecycle(
            lifecycleOwner = LocalLifecycleOwner.current
        )

    val lockedRollIndices by zoomRollsAndroidViewModel.lockedRollIndices.collectAsStateWithLifecycle(
        lifecycleOwner = LocalLifecycleOwner.current
    )

    val stateFlowZoom by zoomRollsAndroidViewModel.stateFlowZoom.collectAsStateWithLifecycle(
        lifecycleOwner = LocalLifecycleOwner.current
    )

    val latestRoll = stateFlowZoom.rollHistory.values.firstOrNull()
    val rootRollCount = latestRoll?.count { it.explodeIndex == 0 } ?: 0
    val allLocked = rootRollCount > 0 && lockedRollIndices.size == rootRollCount

    val rollEnabled = stateFlowRollEnabled.value && !allLocked

    // Use Button (Filled) for primary action as per M3 Google style
    androidx.compose.material3.Button(
        onClick = {
            rollsAndroidViewModel.rollDiceSequence(
                lockedRollIndices = lockedRollIndices,
                latestRollHistory = stateFlowZoom.rollHistory
            )
        },
        modifier = Modifier
            .padding(start = 12.dp)
            .width(220.dp)
            .height(56.dp)
            .testTag(if (rollEnabled) RollsTestTag.ROLL_ENABLED else RollsTestTag.ROLL_NOT_ENABLED),
        enabled = rollEnabled,
        shape = MaterialTheme.shapes.extraLarge,
    ) {
        val rollPainter = painterResource(id = R.drawable.roll)

        Icon(
            rollPainter,
            contentDescription = stringResource(R.string.tab_roll_roll),
            modifier = Modifier.size(24.dp),
        )

        Spacer(Modifier.size(ButtonDefaults.IconSpacing))

        Text(
            text = stringResource(R.string.tab_roll_roll),
            style = MaterialTheme.typography.titleMedium
        )
    }
}
