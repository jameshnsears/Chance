package com.github.jameshnsears.chance.ui.dialog.roll.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.jameshnsears.chance.ui.R
import com.github.jameshnsears.chance.ui.tab.roll.TabRollViewModel

class DialogRollTestTag {
    companion object {
        const val SETTINGS_TIME = "SETTINGS_TIME"
        const val SETTINGS_SCORE = "SETTINGS_SCORE"
        const val SETTINGS_DICE_TITLE = "SETTINGS_DICE_TITLE"
        const val SETTINGS_SIDE_NUMBER = "SETTINGS_SIDE_NUMBER"
        const val SETTINGS_ROLL_SOUND = "SETTINGS_ROLL_SOUND"
    }
}

@Composable
fun DialogRoll(
    showDialog: MutableState<Boolean>,
    tabRollViewModel: TabRollViewModel
) {
    Dialog(
        onDismissRequest = { showDialog.value = false },
    ) {
        DialogRollLayout(
            tabRollViewModel
        )
    }
}

@Composable
fun DialogRollLayout(
    tabRollViewModel: TabRollViewModel
) {
    val stateFlow =
        tabRollViewModel.tabRollStateFlow.collectAsStateWithLifecycle()

    val settingsTime = stateFlow.value.settingsTime
    val settingsScore = stateFlow.value.settingsScore
    val settingsDiceTitle = stateFlow.value.settingsDiceTitle
    val settingsSideNumber = stateFlow.value.settingsSideNumber

    val settingsRollSound =
        rememberSaveable { mutableStateOf(tabRollViewModel.settingsRollSound.value) }

    Surface(
        color = MaterialTheme.colorScheme.secondaryContainer,
        shape = RoundedCornerShape(16.dp),
    ) {
        Column(
            modifier = Modifier
                .padding(top = 12.dp, start = 12.dp, end = 12.dp, bottom = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                modifier = Modifier
                    .align(Alignment.Start),
                text = stringResource(R.string.tab_roll_settings),
                fontSize = 24.sp,
            )

            CommonSwitch(
                stringResource(R.string.tab_roll_settings_roll_time),
                settingsTime,
                tabRollViewModel::showTime,
                DialogRollTestTag.SETTINGS_TIME
            )

            CommonSwitch(
                stringResource(R.string.tab_roll_settings_score),
                settingsScore,
                tabRollViewModel::showScore,
                DialogRollTestTag.SETTINGS_SCORE
            )

            CommonSwitch(
                stringResource(R.string.tab_roll_settings_dice_title),
                settingsDiceTitle,
                tabRollViewModel::showDiceTitle,
                DialogRollTestTag.SETTINGS_DICE_TITLE
            )

            CommonSwitch(
                stringResource(R.string.tab_roll_settings_side_number),
                settingsSideNumber,
                tabRollViewModel::showSideImageNumber,
                DialogRollTestTag.SETTINGS_SIDE_NUMBER
            )

            HorizontalDivider()

            CommonSwitch(
                stringResource(R.string.tab_roll_settings_use_sound),
                settingsRollSound.value,
                tabRollViewModel::makeRollSound,
                DialogRollTestTag.SETTINGS_ROLL_SOUND
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
