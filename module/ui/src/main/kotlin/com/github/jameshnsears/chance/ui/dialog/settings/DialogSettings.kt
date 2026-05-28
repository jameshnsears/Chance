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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.VolumeUp
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.AutoFixHigh
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.FormatListNumbered
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.PhonelinkRing
import androidx.compose.material.icons.outlined.RecordVoiceOver
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Score
import androidx.compose.material.icons.outlined.Shuffle
import androidx.compose.material.icons.outlined.Title
import androidx.compose.material.icons.outlined.Vibration
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
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
import com.github.jameshnsears.chance.ui.tab.roll.RollAndroidViewModel

@Composable
fun DialogSettings(
    showDialog: MutableState<Boolean>,
    rollAndroidViewModel: RollAndroidViewModel,
) {
    Dialog(
        onDismissRequest = {
            showDialog.value = false
            rollAndroidViewModel.dismissSettingsDialog()
        },
    ) {
        DialogSettingsLayout(
            rollAndroidViewModel
        )
    }
}

@Composable
fun DialogSettingsLayout(
    rollAndroidViewModel: RollAndroidViewModel
) {
    val stateFlowSettings =
        rollAndroidViewModel.stateFlowSettings.collectAsStateWithLifecycle(
            lifecycleOwner = LocalLifecycleOwner.current
        )

    val rollTime = stateFlowSettings.value.rollIndexTime
    val rollScore = stateFlowSettings.value.rollScore
    val rollScoreTTS = stateFlowSettings.value.rollScoreTTS
    val diceTitle = stateFlowSettings.value.diceTitle
    val rollBehaviour = stateFlowSettings.value.rollBehaviour
    val sideNumber = stateFlowSettings.value.sideNumber
    val sideDescription = stateFlowSettings.value.sideDescription
    val sideSVG = stateFlowSettings.value.sideSVG
    val haptics = stateFlowSettings.value.haptics
    val shakeToRoll = stateFlowSettings.value.shakeToRoll
    val rollSound = stateFlowSettings.value.rollSound
    val shuffle = stateFlowSettings.value.shuffle

    val scrollState = rememberScrollState()
    val backgroundColor = MaterialTheme.colorScheme.secondaryContainer

    Surface(
        color = backgroundColor,
        shape = RoundedCornerShape(16.dp),
    ) {
        Box(modifier = Modifier.height(455.dp)) {
            Text(
                modifier = Modifier
                    .padding(start = 24.dp, top = 24.dp),
                text = stringResource(R.string.tab_roll_settings),
                style = MaterialTheme.typography.titleLarge,
                )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 84.dp, start = 24.dp, end = 24.dp, bottom = 24.dp)
                    .verticalScroll(scrollState),
            ) {
                CommonSwitch(
                    stringResource(R.string.tab_roll_settings_roll_time),
                    Icons.Outlined.Schedule,
                    rollTime,
                    rollAndroidViewModel::settingsIndexTime,
                    DialogSettingsTestTag.SETTINGS_TIME
                )

                CommonSwitch(
                    stringResource(R.string.tab_roll_settings_score),
                    Icons.Outlined.Score,
                    rollScore,
                    rollAndroidViewModel::settingsRollScore,
                    DialogSettingsTestTag.SETTINGS_SCORE
                )

                CommonSwitch(
                    stringResource(R.string.tab_roll_settings_score_tts),
                    Icons.Outlined.RecordVoiceOver,
                    rollScoreTTS,
                    rollAndroidViewModel::settingsRollScoreTTS,
                    DialogSettingsTestTag.SETTINGS_SCORE_TTS
                )

                HorizontalDivider(
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .padding(top = 12.dp, bottom = 12.dp)
                )

                CommonSwitch(
                    stringResource(R.string.tab_roll_settings_dice_title),
                    Icons.Outlined.Title,
                    diceTitle,
                    rollAndroidViewModel::settingsDiceTitle,
                    DialogSettingsTestTag.SETTINGS_DICE_TITLE
                )

                CommonSwitch(
                    stringResource(R.string.tab_roll_settings_side_number),
                    Icons.Outlined.FormatListNumbered,
                    sideNumber,
                    rollAndroidViewModel::settingsSideNumber,
                    DialogSettingsTestTag.SETTINGS_SIDE_NUMBER
                )

                CommonSwitch(
                    stringResource(R.string.tab_roll_settings_side_svg),
                    Icons.Outlined.Image,
                    sideSVG,
                    rollAndroidViewModel::settingsSideSVG,
                    DialogSettingsTestTag.SETTINGS_SIDE_SVG
                )

                CommonSwitch(
                    stringResource(R.string.tab_roll_settings_side_description),
                    Icons.Outlined.Description,
                    sideDescription,
                    rollAndroidViewModel::settingsSideDescription,
                    DialogSettingsTestTag.SETTINGS_SIDE_DESCRIPTION
                )

                CommonSwitch(
                    stringResource(R.string.tab_roll_settings_behaviour),
                    Icons.Outlined.AutoFixHigh,
                    rollBehaviour,
                    rollAndroidViewModel::settingsBehaviour,
                    DialogSettingsTestTag.SETTINGS_BEHAVIOUR
                )

                HorizontalDivider(
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .padding(top = 12.dp, bottom = 12.dp)
                )

                CommonSwitch(
                    stringResource(R.string.tab_roll_settings_use_shuffle),
                    Icons.Outlined.Shuffle,
                    shuffle,
                    rollAndroidViewModel::settingsShuffle,
                    DialogSettingsTestTag.SETTINGS_ROLL_SHUFFLE
                )

                HorizontalDivider(
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .padding(top = 12.dp, bottom = 12.dp)
                )

                CommonSwitch(
                    stringResource(R.string.tab_roll_settings_use_haptics),
                    Icons.Outlined.Vibration,
                    haptics,
                    rollAndroidViewModel::settingsUseHaptics,
                    DialogSettingsTestTag.SETTINGS_ROLL_HAPTICS
                )

                CommonSwitch(
                    stringResource(R.string.tab_roll_settings_use_sound),
                    Icons.AutoMirrored.Outlined.VolumeUp,
                    rollSound,
                    rollAndroidViewModel::settingsRollSound,
                    DialogSettingsTestTag.SETTINGS_ROLL_SOUND
                )

                CommonSwitch(
                    stringResource(R.string.tab_roll_settings_shake_to_roll),
                    Icons.Outlined.PhonelinkRing,
                    shakeToRoll,
                    rollAndroidViewModel::settingsShakeToRoll,
                    DialogSettingsTestTag.SETTINGS_SHAKE_TO_ROLL
                )

                HorizontalDivider(
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .padding(top = 12.dp, bottom = 12.dp)
                )

                UndoAll(
                    rollAndroidViewModel,
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
private fun UndoAll(
    rollAndroidViewModel: RollAndroidViewModel,
) {
    val rollEnabled =
        rollAndroidViewModel.undoEnabled.collectAsStateWithLifecycle(
            lifecycleOwner = LocalLifecycleOwner.current
        )

    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp, bottom = 4.dp)
    ) {
        Button(
            onClick = {
                rollAndroidViewModel.undoAll()
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
    icon: ImageVector, // New parameter
    switchState: Boolean,
    dialogRollViewModelMethod: (Boolean) -> Unit,
    testTag: String
) {
    val switched = rememberSaveable { mutableStateOf(switchState) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 48.dp)
            .padding(top = 4.dp, bottom = 4.dp)
            .clickable {
                val newValue = !switched.value
                switched.value = newValue
                dialogRollViewModelMethod(newValue)
            }
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            modifier = Modifier.weight(1f),
            text = text,
        )

        Switch(
            checked = switched.value,
            onCheckedChange = {
                switched.value = it
                dialogRollViewModelMethod(it)
            },
            modifier = Modifier
                .minimumInteractiveComponentSize()
                .testTag(testTag),
            thumbContent = if (switched.value) {
                {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = null,
                        modifier = Modifier.size(SwitchDefaults.IconSize),
                    )
                }
            } else {
                null
            }
        )
    }
}
