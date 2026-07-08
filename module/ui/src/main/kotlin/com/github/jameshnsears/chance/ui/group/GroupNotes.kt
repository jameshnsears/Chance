package com.github.jameshnsears.chance.ui.group

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.github.jameshnsears.chance.data.domain.core.group.Group

@Composable
fun GroupNotes(
    group: Group,
    onNotesChange: (String) -> Unit = {},
) {
    Row(
        modifier = Modifier
            .padding(top = 0.dp, bottom = 16.dp)
            .fillMaxWidth()
    ) {
        OutlinedTextField(
            value = group.notes,
            onValueChange = {
                if (it.length <= 100)
                    onNotesChange(it)
            },
            label = { Text(stringResource(R.string.dialog_bag_setup_groups_notes)) },
            supportingText = {
                Text(
                    text = "${group.notes.length} / 100",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.End,
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 8.dp)
                .testTag(GroupTestTag.NOTES),
            minLines = 2,
            shape = RoundedCornerShape(8.dp),
            trailingIcon = {
                if (group.notes.isNotEmpty()) {
                    IconButton(
                        onClick = {
                            onNotesChange("")
                        },
                        modifier = Modifier
                            .minimumInteractiveComponentSize()
                            .testTag(GroupTestTag.NOTES_CLEAR)
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
}
