package com.github.jameshnsears.chance.ui.dialog.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Rule
import androidx.compose.material.icons.automirrored.outlined.VolumeUp
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.FormatListNumbered
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.PhonelinkRing
import androidx.compose.material.icons.outlined.RecordVoiceOver
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Score
import androidx.compose.material.icons.outlined.Shuffle
import androidx.compose.material.icons.outlined.Title
import androidx.compose.material.icons.outlined.Vibration
import androidx.compose.material.icons.outlined.ViewColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.jameshnsears.chance.common.R
import com.github.jameshnsears.chance.ui.tab.rolls.RollsAndroidViewModel
import com.github.jameshnsears.chance.ui.tab.rolls.SettingsState

@Composable
fun DialogSettings(
    showDialog: MutableState<Boolean>,
    rollsAndroidViewModel: RollsAndroidViewModel,
) {
    Dialog(
        onDismissRequest = {
            showDialog.value = false
        },
    ) {
        DialogSettingsLayout(
            rollsAndroidViewModel
        )
    }
}

@Composable
fun DialogSettingsLayout(
    rollsAndroidViewModel: RollsAndroidViewModel,
) {
    val stateFlowSettings =
        rollsAndroidViewModel.stateFlowSettings.collectAsStateWithLifecycle(
            lifecycleOwner = LocalLifecycleOwner.current
        )

    val state = stateFlowSettings.value
    val scrollState = rememberScrollState()
    val backgroundColor = MaterialTheme.colorScheme.surfaceContainer

    Surface(
        color = backgroundColor,
        shape = MaterialTheme.shapes.extraLarge,
    ) {
        Box(modifier = Modifier.height(550.dp)) {
            Text(
                modifier = Modifier
                    .padding(start = 24.dp, top = 24.dp, bottom = 16.dp),
                text = stringResource(R.string.tab_roll_settings_title),
                style = MaterialTheme.typography.headlineSmall,
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 72.dp, bottom = 8.dp)
                    .verticalScroll(scrollState),
            ) {
                SettingsRollInfoSection(rollsAndroidViewModel, state)

                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
                )

                SettingsRollOrientation(rollsAndroidViewModel, state)

                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
                )

                SettingsSideInfoSection(rollsAndroidViewModel, state)

                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
                )

                SettingsInteractionSection(rollsAndroidViewModel, state)

                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
                )

                UndoAll(
                    rollsAndroidViewModel,
                )
            }

            if (scrollState.canScrollForward) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .height(80.dp)
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    backgroundColor.copy(alpha = 0f),
                                    backgroundColor.copy(alpha = 0.8f),
                                    backgroundColor,
                                ),
                            ),
                        ),
                )
            }
        }
    }
}

@Composable
private fun SettingsRollOrientation(
    rollsAndroidViewModel: RollsAndroidViewModel,
    state: SettingsState
) {
    CommonSwitch(
        stringResource(R.string.tab_roll_settings_orientation),
        Icons.Outlined.ViewColumn,
        state.orientation,
        rollsAndroidViewModel::settingsOrientation,
        DialogSettingsTestTag.SETTINGS_ORIENTATION
    )
}

@Composable
private fun SettingsRollInfoSection(
    rollsAndroidViewModel: RollsAndroidViewModel,
    state: SettingsState
) {
    CommonSwitch(
        stringResource(R.string.tab_roll_settings_roll_time),
        Icons.Outlined.Schedule,
        state.rollIndexTime,
        rollsAndroidViewModel::settingsIndexTime,
        DialogSettingsTestTag.SETTINGS_TIME
    )

    CommonSwitch(
        stringResource(R.string.tab_roll_settings_score),
        Icons.Outlined.Score,
        state.rollScore,
        rollsAndroidViewModel::settingsRollScore,
        DialogSettingsTestTag.SETTINGS_SCORE
    )

    CommonSwitch(
        stringResource(R.string.tab_roll_settings_group_title),
        Icons.Outlined.GridView,
        state.groupTitle,
        rollsAndroidViewModel::settingsGroupTitle,
        DialogSettingsTestTag.SETTINGS_GROUP_TITLE
    )

    CommonSwitch(
        stringResource(R.string.tab_roll_settings_dice_title),
        Icons.Outlined.Title,
        state.diceTitle,
        rollsAndroidViewModel::settingsDiceTitle,
        DialogSettingsTestTag.SETTINGS_DICE_TITLE
    )

    CommonSwitch(
        stringResource(R.string.tab_roll_settings_behaviour),
        Icons.AutoMirrored.Outlined.Rule,
        state.rollBehaviour,
        rollsAndroidViewModel::settingsBehaviour,
        DialogSettingsTestTag.SETTINGS_BEHAVIOUR
    )
}

@Composable
private fun SettingsSideInfoSection(
    rollsAndroidViewModel: RollsAndroidViewModel,
    state: SettingsState
) {
    CommonSwitch(
        stringResource(R.string.tab_roll_settings_side_number),
        Icons.Outlined.FormatListNumbered,
        state.sideNumber,
        rollsAndroidViewModel::settingsSideNumber,
        DialogSettingsTestTag.SETTINGS_SIDE_NUMBER
    )

    CommonSwitch(
        stringResource(R.string.tab_roll_settings_side_svg),
        Icons.Outlined.Image,
        state.sideSVG,
        rollsAndroidViewModel::settingsSideSVG,
        DialogSettingsTestTag.SETTINGS_SIDE_SVG
    )

    CommonSwitch(
        stringResource(R.string.tab_roll_settings_side_description),
        Icons.Outlined.Description,
        state.sideDescription,
        rollsAndroidViewModel::settingsSideDescription,
        DialogSettingsTestTag.SETTINGS_SIDE_DESCRIPTION
    )
}

@Composable
private fun SettingsInteractionSection(
    rollsAndroidViewModel: RollsAndroidViewModel,
    state: SettingsState
) {
    CommonSwitch(
        stringResource(R.string.tab_roll_settings_use_shuffle),
        Icons.Outlined.Shuffle,
        state.shuffle,
        rollsAndroidViewModel::settingsShuffle,
        DialogSettingsTestTag.SETTINGS_ROLL_SHUFFLE
    )

    HorizontalDivider(
        modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
    )


    CommonSwitch(
        stringResource(R.string.tab_roll_settings_use_haptics),
        Icons.Outlined.Vibration,
        state.haptics,
        rollsAndroidViewModel::settingsUseHaptics,
        DialogSettingsTestTag.SETTINGS_ROLL_HAPTICS
    )

    CommonSwitch(
        stringResource(R.string.tab_roll_settings_use_sound),
        Icons.AutoMirrored.Outlined.VolumeUp,
        state.rollSound,
        rollsAndroidViewModel::settingsRollSound,
        DialogSettingsTestTag.SETTINGS_ROLL_SOUND
    )

    CommonSwitch(
        stringResource(R.string.tab_roll_settings_score_tts),
        Icons.Outlined.RecordVoiceOver,
        state.rollScoreTTS,
        rollsAndroidViewModel::settingsRollScoreTTS,
        DialogSettingsTestTag.SETTINGS_SCORE_TTS
    )

    CommonSwitch(
        stringResource(R.string.tab_roll_settings_shake_to_roll),
        Icons.Outlined.PhonelinkRing,
        state.shakeToRoll,
        rollsAndroidViewModel::settingsShakeToRoll,
        DialogSettingsTestTag.SETTINGS_SHAKE_TO_ROLL
    )
}

@Composable
private fun UndoAll(
    rollsAndroidViewModel: RollsAndroidViewModel,
) {
    val rollEnabled =
        rollsAndroidViewModel.undoEnabled.collectAsStateWithLifecycle(
            lifecycleOwner = LocalLifecycleOwner.current
        )

    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp, bottom = 18.dp)
    ) {
        Button(
            onClick = {
                rollsAndroidViewModel.undoAll()
            },
            modifier = Modifier
                .width(160.dp)
                .testTag(DialogSettingsTestTag.SETTINGS_UNDO_ALL),
            enabled = rollEnabled.value
        ) {
            Icon(
                painterResource(id = R.drawable.undo),
                contentDescription = stringResource(R.string.tab_roll_settings_undo_all),
                modifier = Modifier.size(24.dp),
            )

            Spacer(Modifier.size(ButtonDefaults.IconSpacing))

            Text(stringResource(R.string.tab_roll_settings_undo_all))
        }
    }
}

@Composable
fun CommonSwitch(
    text: String,
    icon: ImageVector,
    switchState: Boolean,
    dialogRollViewModelMethod: (Boolean) -> Unit,
    testTag: String
) {
    val switched = rememberSaveable { mutableStateOf(switchState) }

    ListItem(
        headlineContent = {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge
            )
        },
        leadingContent = {
            Icon(
                imageVector = icon,
                contentDescription = "",
                modifier = Modifier.size(24.dp)
            )
        },
        trailingContent = {
            Switch(
                checked = switched.value,
                onCheckedChange = {
                    switched.value = it
                    dialogRollViewModelMethod(it)
                },
                modifier = Modifier
                    .testTag(testTag),
                thumbContent = if (switched.value) {
                    {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = "",
                            modifier = Modifier.size(SwitchDefaults.IconSize),
                        )
                    }
                } else {
                    null
                }
            )
        },
        colors = ListItemDefaults.colors(containerColor = androidx.compose.ui.graphics.Color.Transparent),
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                val newValue = !switched.value
                switched.value = newValue
                dialogRollViewModelMethod(newValue)
            }
            .padding(horizontal = 8.dp)
    )
}
