package com.github.jameshnsears.chance.ui.dialog.settings.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
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
    val stateFlow =
        tabRollAndroidViewModel.stateFlowTabRoll.collectAsStateWithLifecycle(
            lifecycleOwner = androidx.compose.ui.platform.LocalLifecycleOwner.current
        )

    val rollTime = stateFlow.value.rollIndexTime
    val rollScore = stateFlow.value.rollScore
    val diceTitle = stateFlow.value.diceTitle
    val sideNumber = stateFlow.value.sideNumber
    val behaviour = stateFlow.value.behaviour
    val sideDescription = stateFlow.value.sideDescription
    val sideSVG = stateFlow.value.sideSVG
    val rollSound = stateFlow.value.rollSound

    Surface(
        color = MaterialTheme.colorScheme.secondaryContainer,
        shape = RoundedCornerShape(16.dp),
    ) {
        Column(
            modifier = Modifier
                .padding(top = 12.dp, start = 12.dp, end = 12.dp, bottom = 8.dp),
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
                stringResource(R.string.tab_roll_settings_side_number),
                sideNumber,
                tabRollAndroidViewModel::settingsSideNumber,
                DialogSettingsTestTag.SETTINGS_SIDE_NUMBER
            )

            CommonSwitch(
                stringResource(R.string.tab_roll_settings_behaviour),
                behaviour,
                tabRollAndroidViewModel::settingsBehaviour,
                DialogSettingsTestTag.SETTINGS_BEHAVIOUR
            )

            CommonSwitch(
                stringResource(R.string.tab_roll_settings_side_description),
                sideDescription,
                tabRollAndroidViewModel::settingsSideDescription,
                DialogSettingsTestTag.SETTINGS_SIDE_DESCRIPTION
            )

            CommonSwitch(
                stringResource(R.string.tab_roll_settings_side_svg),
                sideSVG,
                tabRollAndroidViewModel::settingsSideSVG,
                DialogSettingsTestTag.SETTINGS_SIDE_SVG
            )

            HorizontalDivider(
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .padding(top = 12.dp, bottom = 12.dp)
            )

            CommonSwitch(
                stringResource(R.string.tab_roll_settings_use_sound),
                rollSound,
                tabRollAndroidViewModel::settingsRollSound,
                DialogSettingsTestTag.SETTINGS_ROLL_SOUND
            )
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
            .clickable {
                switched.value = !switched.value
                dialogRollViewModelMethod(switched.value)
            }
            .testTag(testTag)
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
