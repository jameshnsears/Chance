package com.github.jameshnsears.chance.ui.tab.bag

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.github.jameshnsears.chance.ui.R
import com.github.jameshnsears.chance.ui.dialog.confirm.DialogConfirm
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResetStorage(
    bottomSheetScaffoldState: BottomSheetScaffoldState,
    tabBagAndroidViewModel: TabBagAndroidViewModel,
) {
    val showDialogConfirm = remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 8.dp),
        horizontalArrangement = Arrangement.Center,
    ) {
        Button(
            onClick = {
                showDialogConfirm.value = true

                coroutineScope.launch {
                    bottomSheetScaffoldState.bottomSheetState.partialExpand()
                }
            },
            modifier = Modifier
                .width(150.dp)
                .testTag(BagTestTag.IMPORT),
        ) {
            val storageResetPainter = painterResource(id = R.drawable.storage_reset)
            val iconModifier = Modifier.size(24.dp)

            Icon(
                storageResetPainter,
                contentDescription = "",
                modifier = iconModifier,
            )

            Spacer(Modifier.size(ButtonDefaults.IconSpacing))

            Text(stringResource(R.string.tab_bag_reset_storage))
        }
    }

    if (showDialogConfirm.value) {
        DialogConfirm(
            openDialog = showDialogConfirm.value,
            onDismissRequest = {
                showDialogConfirm.value = false
            },
            onConfirmation = {
                tabBagAndroidViewModel.resetStorage()
                showDialogConfirm.value = false
            },
            title = stringResource(R.string.tab_bag_reset_storage),
            text = stringResource(R.string.tab_bag_reset_storage_confirm)
        )
    }
}
