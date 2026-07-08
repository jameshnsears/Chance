package com.github.jameshnsears.chance.ui.dialog.group

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.jameshnsears.chance.common.R
import com.github.jameshnsears.chance.ui.dialog.dice.ButtonFeatureTestTag
import com.github.jameshnsears.chance.ui.group.GroupName
import com.github.jameshnsears.chance.ui.group.GroupNotes
import com.github.jameshnsears.chance.ui.group.GroupUuidDice
import com.github.jameshnsears.chance.ui.tab.setup.groups.GroupsAndroidViewModel

@Composable
fun DialogGroup(
    groupsAndroidViewModel: GroupsAndroidViewModel,
    showDialog: MutableState<Boolean>,
    onOnboardingDismiss: () -> Unit = {}
) {
    LaunchedEffect(Unit) {
        onOnboardingDismiss()
    }

    Dialog(
        onDismissRequest = {
            showDialog.value = false
        },
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        ),
    ) {
        val view = LocalView.current
        SideEffect {
            (view.parent as? DialogWindowProvider)?.window?.let { window ->
                val controller = WindowCompat.getInsetsController(window, view)
                controller.hide(WindowInsetsCompat.Type.statusBars())
                controller.systemBarsBehavior =
                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        }

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.surfaceContainerLow
        ) {
            DialogGroupContent(
                groupsAndroidViewModel,
                showDialog
            )
        }
    }
}

@Composable
fun DialogGroupContent(
    groupsAndroidViewModel: GroupsAndroidViewModel,
    showDialog: MutableState<Boolean>,
) {
    val diceBag by groupsAndroidViewModel.stateFlowDiceBag.collectAsStateWithLifecycle()
    val newGroup by groupsAndroidViewModel.stateFlowNewGroup.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(2.dp)
    ) {
        DialogGroupHeader(groupsAndroidViewModel, showDialog, newGroup)

        Row(
            modifier = Modifier
                .padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 8.dp),
            ) {
                OutlinedCard(
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .fillMaxWidth(),
                    colors = CardDefaults.outlinedCardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                    ),
                ) {
                    Column(modifier = Modifier.padding(top = 6.dp)) {
                        DialogGroupNameSection(groupsAndroidViewModel, newGroup)

                        HorizontalDivider(
                            modifier = Modifier.padding(top = 12.dp, bottom = 12.dp)
                        )

                        DialogGroupDiceSection(groupsAndroidViewModel, newGroup, diceBag)

                        HorizontalDivider(
                            modifier = Modifier.padding(top = 12.dp, bottom = 6.dp)
                        )

                        DialogGroupNotesSection(groupsAndroidViewModel, newGroup)
                    }
                }
            }
        }
    }
}

@Composable
private fun DialogGroupHeader(
    groupsAndroidViewModel: GroupsAndroidViewModel,
    showDialog: MutableState<Boolean>,
    newGroup: com.github.jameshnsears.chance.data.domain.core.group.Group
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .focusTarget()
            .padding(end = 4.dp),
    ) {
        IconButton(
            modifier = Modifier
                .minimumInteractiveComponentSize(),
            onClick = {
                showDialog.value = false
            }) {
            Icon(
                imageVector = Icons.Outlined.Close,
                contentDescription = stringResource(R.string.close),
            )
        }

        Text(
            text = stringResource(R.string.dialog_bag_setup_groups_title),
            style = MaterialTheme.typography.titleLarge,
        )

        Spacer(Modifier.weight(1f))

        TextButton(
            modifier = Modifier
                .defaultMinSize(minWidth = 48.dp, minHeight = 48.dp)
                .testTag(ButtonFeatureTestTag.BUTTON_FEATURE_SAVE),
            enabled = groupsAndroidViewModel.canSave(newGroup),
            onClick = {
                groupsAndroidViewModel.onSave(newGroup)
                showDialog.value = false
            },
        ) {
            Text(
                text = stringResource(R.string.dialog_bag_save),
                textAlign = TextAlign.End
            )
        }
    }
}

@Composable
private fun DialogGroupNameSection(
    groupsAndroidViewModel: GroupsAndroidViewModel,
    newGroup: com.github.jameshnsears.chance.data.domain.core.group.Group
) {
    Column(modifier = Modifier.padding(horizontal = 14.dp)) {
        GroupName(
            newGroup,
            onNameChange = {
                groupsAndroidViewModel.onNameChange(newGroup, it)
            }
        )
    }

    Row(
        modifier = Modifier
            .padding(start = 12.dp, bottom = 8.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Outlined.Info,
            contentDescription = stringResource(R.string.dialog_bag_setup_groups_name),
        )
    }

    Row(
        modifier = Modifier
            .padding(start = 12.dp, top = 4.dp, bottom = 4.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.dialog_bag_setup_groups_name_info_0),
            style = MaterialTheme.typography.bodySmall,
        )
    }
}

@Composable
private fun DialogGroupDiceSection(
    groupsAndroidViewModel: GroupsAndroidViewModel,
    newGroup: com.github.jameshnsears.chance.data.domain.core.group.Group,
    diceBag: List<com.github.jameshnsears.chance.data.domain.core.Dice>
) {
    GroupUuidDice(
        newGroup,
        diceBag,
        onUuidDiceChange = { uuid, newQuantity ->
            groupsAndroidViewModel.onUuidDiceChange(
                newGroup,
                uuid,
                newQuantity
            )
        }
    )

    Row(
        modifier = Modifier
            .padding(start = 12.dp, top = 16.dp, bottom = 8.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Outlined.Info,
            contentDescription = stringResource(R.string.dialog_bag_setup_groups_select),
        )
    }

    Row(
        modifier = Modifier
            .padding(start = 12.dp, top = 8.dp, bottom = 8.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier.padding(start = 8.dp),
            text = stringResource(R.string.dialog_bag_setup_groups_select),
            style = MaterialTheme.typography.bodySmall,
        )
    }
}

@Composable
private fun DialogGroupNotesSection(
    groupsAndroidViewModel: GroupsAndroidViewModel,
    newGroup: com.github.jameshnsears.chance.data.domain.core.group.Group
) {
    Column(modifier = Modifier.padding(bottom = 12.dp)) {
        GroupNotes(
            newGroup,
            onNotesChange = {
                groupsAndroidViewModel.onNotesChange(newGroup, it)
            }
        )
    }
}
