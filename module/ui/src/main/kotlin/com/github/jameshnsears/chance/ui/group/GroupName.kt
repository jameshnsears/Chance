package com.github.jameshnsears.chance.ui.group

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.github.jameshnsears.chance.common.R
import com.github.jameshnsears.chance.common.utility.feature.UtilityFeature
import com.github.jameshnsears.chance.data.domain.core.group.Group
import com.github.jameshnsears.chance.ui.dialog.dice.card.dice.CardDiceTestTag

@Composable
fun GroupName(
    group: Group,
    onNameChange: (String) -> Unit = {},
) {
    if (UtilityFeature.isEnabled(UtilityFeature.Flag.UI_SHOW_UUID))
        Row {
            Text(
                text = group.uuid,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

    OutlinedTextField(
        value = group.name,
        onValueChange = {
            if (it.length <= 25)
                onNameChange(it)
        },
        label = { Text(stringResource(R.string.dialog_bag_setup_groups_name)) },
        supportingText = {
            Text(
                text = "${group.name.length} / 25",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End,
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Outlined.GridView,
                contentDescription = "",
            )
        },
        shape = RoundedCornerShape(8.dp),
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 8.dp)
            .testTag(CardDiceTestTag.DICE_TITLE),
        trailingIcon = {
            if (group.name.isNotEmpty()) {
                IconButton(
                    onClick = {
                        onNameChange("")
                    },
                    modifier = Modifier.minimumInteractiveComponentSize()
                ) {
                    Icon(
                        painterResource(id = R.drawable.cancel),
                        contentDescription = stringResource(R.string.dialog_cancel),
                    )
                }
            }
        }
    )
}
