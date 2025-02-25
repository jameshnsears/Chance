package com.github.jameshnsears.chance.ui.dialog.settings.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.jameshnsears.chance.ui.R
import com.github.jameshnsears.chance.ui.tab.roll.TabRollAndroidViewModel

class DialogSettingsTestTag {
    companion object {
        const val SETTINGS_TIME = "SETTINGS_TIME"
        const val SETTINGS_SCORE = "SETTINGS_SCORE"

        const val SETTINGS_DICE_TITLE = "SETTINGS_DICE_TITLE"
        const val SETTINGS_SIDE_NUMBER = "SETTINGS_SIDE_NUMBER"
        const val SETTINGS_SIDE_DESCRIPTION = "SETTINGS_SIDE_DESCRIPTION"
        const val SETTINGS_SIDE_SVG = "SETTINGS_SIDE_SVG"
        const val SETTINGS_BEHAVIOUR = "SETTINGS_BEHAVIOUR"

        const val SETTINGS_ROLL_SOUND = "SETTINGS_ROLL_SOUND"

        const val SETTINGS_ROLL_SHUFFLE = "SETTINGS_ROLL_SHUFFLE"

        const val SETTINGS_UNDO_ALL = "SETTINGS_UNDO_ALL"
    }
}

@Composable
fun DialogSettings(
    showDialog: MutableState<Boolean>,
    tabRollAndroidViewModel: TabRollAndroidViewModel
) {
    val scrollState = rememberScrollState()

    Dialog(
        onDismissRequest = {
            showDialog.value = false
            tabRollAndroidViewModel.dismissSettingsDialog()
        },
    ) {
        Column(modifier = Modifier.verticalScroll(scrollState)) {
            DialogSettingsLayout(
                tabRollAndroidViewModel
            )
        }
    }
}

@Composable
fun DialogSettingsLayout(
    tabRollAndroidViewModel: TabRollAndroidViewModel
) {
    val stateFlowSettings =
        tabRollAndroidViewModel.stateFlowSettings.collectAsStateWithLifecycle(
            lifecycleOwner = LocalLifecycleOwner.current
        )

    val rollTime = stateFlowSettings.value.rollIndexTime
    val rollScore = stateFlowSettings.value.rollScore
    val diceTitle = stateFlowSettings.value.diceTitle
    val rollBehaviour = stateFlowSettings.value.rollBehaviour
    val sideNumber = stateFlowSettings.value.sideNumber
    val sideDescription = stateFlowSettings.value.sideDescription
    val sideSVG = stateFlowSettings.value.sideSVG
    val rollSound = stateFlowSettings.value.rollSound
    val shuffle = stateFlowSettings.value.shuffle

    Surface(
        color = MaterialTheme.colorScheme.secondaryContainer,
        shape = RoundedCornerShape(16.dp),
    ) {
        Column(
            modifier = Modifier
                .padding(top = 12.dp, start = 12.dp, end = 12.dp, bottom = 8.dp)
                .height(500.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            CommonSwitch(
                stringResource(R.string.tab_roll_settings_roll_time),
                rollTime,
                tabRollAndroidViewModel::settingsIndexTime,
                DialogSettingsTestTag.SETTINGS_TIME
            )

            CommonSwitch(
                stringResource(R.string.tab_roll_settings_score),
                rollScore,
                tabRollAndroidViewModel::settingsRollScore,
                DialogSettingsTestTag.SETTINGS_SCORE
            )

            HorizontalDivider(
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .padding(top = 12.dp, bottom = 12.dp)
            )

            CommonSwitch(
                stringResource(R.string.tab_roll_settings_dice_title),
                diceTitle,
                tabRollAndroidViewModel::settingsDiceTitle,
                DialogSettingsTestTag.SETTINGS_DICE_TITLE
            )

            CommonSwitch(
                stringResource(R.string.tab_roll_settings_behaviour),
                rollBehaviour,
                tabRollAndroidViewModel::settingsBehaviour,
                DialogSettingsTestTag.SETTINGS_BEHAVIOUR
            )

            CommonSwitch(
                stringResource(R.string.tab_roll_settings_side_number),
                sideNumber,
                tabRollAndroidViewModel::settingsSideNumber,
                DialogSettingsTestTag.SETTINGS_SIDE_NUMBER
            )

            CommonSwitch(
                stringResource(R.string.tab_roll_settings_side_svg),
                sideSVG,
                tabRollAndroidViewModel::settingsSideSVG,
                DialogSettingsTestTag.SETTINGS_SIDE_SVG
            )

            CommonSwitch(
                stringResource(R.string.tab_roll_settings_side_description),
                sideDescription,
                tabRollAndroidViewModel::settingsSideDescription,
                DialogSettingsTestTag.SETTINGS_SIDE_DESCRIPTION
            )

            HorizontalDivider(
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .padding(top = 12.dp, bottom = 12.dp)
            )

            CommonSwitch(
                stringResource(R.string.tab_roll_settings_use_shuffle),
                shuffle,
                tabRollAndroidViewModel::settingsShuffle,
                DialogSettingsTestTag.SETTINGS_ROLL_SHUFFLE
            )

            CommonSwitch(
                stringResource(R.string.tab_roll_settings_use_sound),
                rollSound,
                tabRollAndroidViewModel::settingsRollSound,
                DialogSettingsTestTag.SETTINGS_ROLL_SOUND
            )

            HorizontalDivider(
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .padding(top = 12.dp, bottom = 12.dp)
            )

            UndoAll(
                tabRollAndroidViewModel,
            )
        }
    }
}

@Composable
private fun UndoAll(
    tabRollAndroidViewModel: TabRollAndroidViewModel,
) {
    val rollEnabled =
        tabRollAndroidViewModel.undoEnabled.collectAsStateWithLifecycle(
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
                tabRollAndroidViewModel.undoAll()
            },
            modifier = Modifier
                .width(160.dp)
                .testTag(DialogSettingsTestTag.SETTINGS_UNDO_ALL),
            enabled = rollEnabled.value
        ) {
            Icon(
                painterResource(id = R.drawable.undo_fill0_wght400_grad0_opsz24),
                contentDescription = "",
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
    switchState: Boolean,
    dialogRollViewModelMethod: (Boolean) -> Unit,
    testTag: String
) {
    val switched = rememberSaveable { mutableStateOf(switchState) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp, bottom = 4.dp)
            .testTag(testTag)
            .clickable {
                val newValue = !switched.value
                switched.value = newValue
                dialogRollViewModelMethod(newValue)
            }
    ) {
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
        )
    }
}
