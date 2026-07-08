package com.github.jameshnsears.chance.ui.tab.setup.groups

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.jameshnsears.chance.common.R
import com.github.jameshnsears.chance.ui.dialog.group.DialogGroup
import com.github.jameshnsears.chance.ui.group.Group
import kotlinx.coroutines.launch

@Composable
fun Groups(
    groupsAndroidViewModel: GroupsAndroidViewModel,
) {
    val groupHistory by groupsAndroidViewModel.stateFlowGroupHistory.collectAsStateWithLifecycle()
    val groupDrafts by groupsAndroidViewModel.stateFlowGroupDrafts.collectAsStateWithLifecycle()
    val diceBag by groupsAndroidViewModel.stateFlowDiceBag.collectAsStateWithLifecycle()
    val showDialogGroup = remember { mutableStateOf(value = false) }

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    val navigationBarsPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    val isGestureNavigation = navigationBarsPadding < 40.dp

    Surface(
        color = MaterialTheme.colorScheme.surfaceContainerLow,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .testTag(GroupsTestTag.LAZY_COLUMN),
                state = listState,
                contentPadding = PaddingValues(
                    start = 12.dp,
                    top = 8.dp,
                    end = 12.dp,
                    bottom = 76.dp + navigationBarsPadding
                ),
            ) {
                if (groupHistory.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillParentMaxSize()
                                .testTag(GroupsTestTag.LAZY_COLUMN_EMPTY),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(R.string.tab_setup_groups_empty),
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold,
                            )
                        }
                    }
                } else {
                    item {
                        ChangeInfo()
                    }
                    itemsIndexed(groupHistory, key = { _, group -> group.uuid }) { index, group ->
                        val groupDraft = groupDrafts[group.uuid] ?: group
                        Group(
                            modifier = Modifier.animateItem(),
                            group = group,
                            groupDraft = groupDraft,
                            diceBag = diceBag,
                            onNameChange = {
                                groupsAndroidViewModel.onNameChange(group, it)
                            },
                            onUuidDiceChange = { uuid, quantity ->
                                groupsAndroidViewModel.onUuidDiceChange(group, uuid, quantity)
                            },
                            onNotesChange = {
                                groupsAndroidViewModel.onNotesChange(group, it)
                            },
                            onDelete = {
                                groupsAndroidViewModel.onDelete(group)
                            },
                            onSave = {
                                groupsAndroidViewModel.onSave(group)
                            },
                            onMoveUp = if (index > 0) {
                                {
                                    groupsAndroidViewModel.moveUp(group)
                                    coroutineScope.launch {
                                        listState.animateScrollToItem(index - 1)
                                    }
                                }
                            } else {
                                null
                            },
                            onMoveDown = if (index < groupHistory.size - 1) {
                                {
                                    groupsAndroidViewModel.moveDown(group)
                                    coroutineScope.launch {
                                        listState.animateScrollToItem(index + 1)
                                    }
                                }
                            } else {
                                null
                            },
                            isSaveEnabled = groupsAndroidViewModel.canSave(groupDraft)
                        )
                    }
                }
            }

            FloatingActionButton(
                onClick = {
                    showDialogGroup.value = true
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .navigationBarsPadding()
                    .padding(end = 16.dp, bottom = if (isGestureNavigation) 24.dp else 32.dp),
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 8.dp),
            ) {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = "+",
                )
            }

            if (showDialogGroup.value) {
                DialogGroup(
                    groupsAndroidViewModel,
                    showDialogGroup,
                )
            }
        }
    }
}

@Composable
fun ChangeInfo() {
    Column {
        Row(
            modifier = Modifier
                .padding(start = 12.dp, top = 8.dp, bottom = 8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Outlined.Info,
                contentDescription = stringResource(R.string.dialog_bag_setup_groups_change_info_0),
            )
        }

        Row(
            modifier = Modifier
                .padding(start = 12.dp, top = 8.dp, bottom = 8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.dialog_bag_setup_groups_change_info_0),
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}
