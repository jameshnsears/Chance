package com.github.jameshnsears.chance.ui.tab.rolls

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.jameshnsears.chance.common.R


@Composable
fun UndoButton(rollsAndroidViewModel: RollsAndroidViewModel) {
    val stateFlowUndoEnabled =
        rollsAndroidViewModel.undoEnabled.collectAsStateWithLifecycle(
            lifecycleOwner = LocalLifecycleOwner.current
        )
    val undoEnabled = stateFlowUndoEnabled.value

    OutlinedButton(
        onClick = {
            rollsAndroidViewModel.undo()
        },
        enabled = undoEnabled,
        modifier = Modifier
            .width(120.dp)
            .minimumInteractiveComponentSize()
            .testTag(RollsTestTag.UNDO),
    ) {
        val undoPainter = painterResource(id = R.drawable.undo)
        val iconModifier = remember { Modifier.size(24.dp) }

        Icon(
            undoPainter,
            contentDescription = stringResource(R.string.tab_roll_undo),
            modifier = iconModifier,
        )

        Spacer(Modifier.size(ButtonDefaults.IconSpacing))

        Text(stringResource(R.string.tab_roll_undo))
    }
}
