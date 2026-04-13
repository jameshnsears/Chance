package com.github.jameshnsears.chance.ui.tab.roll

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.jameshnsears.chance.ui.R


@Composable
fun RollButton(rollAndroidViewModel: RollAndroidViewModel) {
    val stateFlowRollEnabled =
        rollAndroidViewModel.rollEnabled.collectAsStateWithLifecycle(
            lifecycleOwner = LocalLifecycleOwner.current
        )

    val rollEnabled = stateFlowRollEnabled.value

    Button(
        onClick = {
            rollAndroidViewModel.rollDiceSequence()
        },

        modifier = Modifier.then(
            if (rollEnabled) Modifier
                .padding(start = 18.dp)
                .width(160.dp)
                .testTag(RollTestTag.ROLL_ENABLED)
            else
                Modifier
                    .padding(start = 18.dp)
                    .width(160.dp)
                    .testTag(RollTestTag.ROLL_NOT_ENABLED),
        ),
        enabled = rollEnabled
    ) {
        val rollPainter = painterResource(id = R.drawable.roll)
        val iconModifier = remember { Modifier.size(24.dp) }

        Icon(
            rollPainter,
            contentDescription = "",
            modifier = iconModifier,
        )

        Spacer(Modifier.size(ButtonDefaults.IconSpacing))

        Text(stringResource(R.string.tab_roll_roll))
    }
}
