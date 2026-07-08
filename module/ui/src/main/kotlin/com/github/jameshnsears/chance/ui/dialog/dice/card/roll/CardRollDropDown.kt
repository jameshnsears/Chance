package com.github.jameshnsears.chance.ui.dialog.dice.card.roll

import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenericExposedDropdownMenuBox(
    valueChanged: (String) -> Unit,
    testTag: String,
    dropdownContents: List<String>,
    selectedDropdownContent: String,
    width: Dp,
    enabled: Boolean = true
) {
    var isDropdownExpanded by rememberSaveable { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = if (enabled) isDropdownExpanded else false,
        onExpandedChange = { if (enabled) isDropdownExpanded = it },
    ) {
        OutlinedTextField(
            modifier = Modifier
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                .width(width)
                .testTag(testTag),
            readOnly = true,
            singleLine = true,
            value = selectedDropdownContent,
            onValueChange = { },
            enabled = enabled,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isDropdownExpanded) },
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
            shape = RoundedCornerShape(8.dp),
        )
        ExposedDropdownMenu(
            expanded = if (enabled) isDropdownExpanded else false,
            onDismissRequest = { isDropdownExpanded = false },
        ) {
            dropdownContents.forEach { selectionOption ->
                DropdownMenuItem(
                    text = { Text(selectionOption) },
                    modifier = Modifier
                        .testTag("${testTag}-${selectionOption}"),
                    onClick = {
                        isDropdownExpanded = false
                        valueChanged(selectionOption)
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
            }
        }
    }
}
