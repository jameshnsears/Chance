package com.github.jameshnsears.chance.ui.group

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.github.jameshnsears.chance.common.R
import com.github.jameshnsears.chance.data.domain.core.Dice
import com.github.jameshnsears.chance.data.domain.core.group.Group
import com.github.jameshnsears.chance.ui.dialog.confirm.DialogConfirm
import com.github.jameshnsears.chance.ui.dialog.dice.ButtonFeatureTestTag

@Composable
fun Group(
    group: Group,
    modifier: Modifier = Modifier,
    groupDraft: Group = group,
    diceBag: List<Dice> = emptyList(),
    initiallyExpanded: Boolean = false,
    onNameChange: (String) -> Unit = {},
    onUuidDiceChange: (String, Int) -> Unit = { _, _ -> },
    onNotesChange: (String) -> Unit = {},
    onDelete: () -> Unit = {},
    onSave: () -> Unit = {},
    onMoveUp: (() -> Unit)? = null,
    onMoveDown: (() -> Unit)? = null,
    isSaveEnabled: Boolean = true,
) {
    var expanded by remember { mutableStateOf(initiallyExpanded) }
    var showDeleteConfirmation by remember { mutableStateOf(false) }

    DialogConfirm(
        openDialog = showDeleteConfirmation,
        onDismissRequest = { showDeleteConfirmation = false },
        onConfirmation = {
            showDeleteConfirmation = false
            onDelete()
        },
        title = stringResource(R.string.dialog_group_delete),
        text = stringResource(R.string.dialog_group_delete_confirmation)
    )

    OutlinedCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .testTag(GroupTestTag.GROUP),
        colors = CardDefaults.outlinedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            GroupHeader(
                group = group,
                expanded = expanded,
                onExpandClick = { expanded = !expanded },
                onMoveUp = onMoveUp,
                onMoveDown = onMoveDown
            )

            AnimatedVisibility(visible = expanded) {
                GroupExpandedContent(
                    groupDraft = groupDraft,
                    diceBag = diceBag,
                    onNameChange = onNameChange,
                    onUuidDiceChange = onUuidDiceChange,
                    onNotesChange = onNotesChange,
                    onDelete = { showDeleteConfirmation = true },
                    onSave = {
                        onSave()
                        expanded = false
                    },
                    isSaveEnabled = isSaveEnabled
                )
            }
        }
    }
}

@Composable
private fun GroupHeader(
    group: Group,
    expanded: Boolean,
    onExpandClick: () -> Unit,
    onMoveUp: (() -> Unit)?,
    onMoveDown: (() -> Unit)?
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onExpandClick() }
            .padding(6.dp)
    ) {
        IconButton(
            onClick = onExpandClick,
            modifier = Modifier
                .testTag(GroupTestTag.EXPAND)
        ) {
            Icon(
                imageVector = if (expanded) Icons.Default.KeyboardArrowDown else Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = ""
            )
        }
        Text(
            text = group.name,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .padding(start = 8.dp)
                .weight(1f)
                .testTag(GroupTestTag.NAME),
        )

        if (onMoveDown != null) {
            IconButton(
                onClick = onMoveDown,
                colors = IconButtonDefaults.iconButtonColors(contentColor = MaterialTheme.colorScheme.primary),
                modifier = Modifier
                    .testTag(GroupTestTag.MOVE_DOWN)
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = ""
                )
            }
        }

        if (onMoveUp != null) {
            IconButton(
                onClick = onMoveUp,
                colors = IconButtonDefaults.iconButtonColors(contentColor = MaterialTheme.colorScheme.primary),
                modifier = Modifier
                    .testTag(GroupTestTag.MOVE_UP)
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowUp,
                    contentDescription = ""
                )
            }
        }
    }
}

@Composable
private fun GroupExpandedContent(
    groupDraft: Group,
    diceBag: List<Dice>,
    onNameChange: (String) -> Unit,
    onUuidDiceChange: (String, Int) -> Unit,
    onNotesChange: (String) -> Unit,
    onDelete: () -> Unit,
    onSave: () -> Unit,
    isSaveEnabled: Boolean
) {
    Column {
        DeleteSaveButtons(
            onDelete = onDelete,
            onSave = onSave,
            isSaveEnabled = isSaveEnabled,
        )

        HorizontalDivider(
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            GroupName(
                groupDraft,
                onNameChange
            )
        }

        HorizontalDivider(
            modifier = Modifier.padding(top = 12.dp, bottom = 12.dp)
        )

        GroupUuidDice(
            groupDraft,
            diceBag,
            onUuidDiceChange
        )

        HorizontalDivider(
            modifier = Modifier.padding(top = 12.dp, bottom = 12.dp)
        )

        GroupNotes(
            groupDraft,
            onNotesChange
        )
    }
}

@Composable
fun DeleteSaveButtons(
    onDelete: () -> Unit = {},
    onSave: () -> Unit = {},
    isSaveEnabled: Boolean = true,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp)
    ) {
        Spacer(Modifier.weight(1f))

        TextButton(
            modifier = Modifier
                .defaultMinSize(minWidth = 48.dp, minHeight = 48.dp)
                .testTag(ButtonFeatureTestTag.BUTTON_FEATURE_DELETE),
            onClick = onDelete,
            colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)

        ) {
            Text(
                text = stringResource(R.string.dialog_bag_delete),
                textAlign = TextAlign.End
            )
        }

        Spacer(Modifier.padding(12.dp))

        TextButton(
            modifier = Modifier
                .defaultMinSize(minWidth = 48.dp, minHeight = 48.dp)
                .testTag(ButtonFeatureTestTag.BUTTON_FEATURE_SAVE),
            onClick = onSave,
            enabled = isSaveEnabled,
        ) {
            Text(
                text = stringResource(R.string.dialog_bag_save),
                textAlign = TextAlign.End
            )
        }
    }
}
