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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.github.jameshnsears.chance.common.R
import com.github.jameshnsears.chance.ui.navigation.ChanceNavKey
import com.github.jameshnsears.chance.ui.navigation.LocalNavigator


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Settings(
) {
    val navigator = LocalNavigator.current

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, bottom = 12.dp)
            .clickable {
                navigator.navigate(ChanceNavKey.DialogSettings)
            }
    ) {
        Text(
            text = stringResource(R.string.tab_roll_settings),
        )

        Spacer(modifier = Modifier.weight(1f))

        IconButton(
            onClick = {
                navigator.navigate(ChanceNavKey.DialogSettings)
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
}
