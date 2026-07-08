package com.github.jameshnsears.chance.ui.tab.rolls

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.github.jameshnsears.chance.common.R
import com.github.jameshnsears.chance.ui.dialog.settings.DialogSettings


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Settings(
    rollsAndroidViewModel: RollsAndroidViewModel,
) {
    val showDialog = rememberSaveable { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, bottom = 12.dp)
            .clickable {
                showDialog.value = true
            }
    ) {
        Text(
            text = stringResource(R.string.tab_roll_settings),
        )

        Spacer(modifier = Modifier.weight(1f))

        IconButton(
            onClick = {
                showDialog.value = true
            },
            modifier = Modifier
                .minimumInteractiveComponentSize()
                .testTag(RollsTestTag.SETTINGS)
        ) {
            Icon(
                Icons.Outlined.Settings,
                contentDescription = stringResource(R.string.tab_roll_settings),
            )
        }
    }

    if (showDialog.value) {
        DialogSettings(
            showDialog,
            rollsAndroidViewModel
        )
    }
}
