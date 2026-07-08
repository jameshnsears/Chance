package com.github.jameshnsears.chance.ui.group

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.github.jameshnsears.chance.common.utility.feature.UtilityFeature
import com.github.jameshnsears.chance.data.domain.core.Dice
import com.github.jameshnsears.chance.data.domain.core.group.Group

@Composable
fun GroupUuidDice(
    group: Group,
    diceBag: List<Dice>,
    onUuidDiceChange: (String, Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 224.dp) // Max 4 items visible (4 * 56dp)
            .verticalScroll(rememberScrollState())
            .testTag(GroupTestTag.DICE_UUIDS)
    ) {
        diceBag.forEach { dice ->
            val quantity = group.uuidDice.count { it == dice.uuid }
            ListItem(
                headlineContent = {
                    if (UtilityFeature.isEnabled(UtilityFeature.Flag.UI_SHOW_UUID)) {
                        Column {
                            Row {
                                Text(
                                    text = dice.title,
                                    style = MaterialTheme.typography.bodyMedium,
                                )
                            }
                            Row {
                                Text(
                                    text = dice.uuid,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    } else {
                        Text(
                            text = dice.title,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                },
                colors = ListItemDefaults.colors(
                    containerColor = Color.Transparent
                ),
                trailingContent = {
                    GroupAddSub(
                        value = quantity,
                        onValueChange = { newQuantity ->
                            onUuidDiceChange(dice.uuid, newQuantity)
                        }
                    )
                }
            )
        }
    }
}
