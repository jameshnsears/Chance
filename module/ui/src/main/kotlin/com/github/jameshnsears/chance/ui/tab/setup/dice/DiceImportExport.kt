package com.github.jameshnsears.chance.ui.tab.setup.dice

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.jameshnsears.chance.common.R
import com.github.jameshnsears.chance.data.repo.api.RepositoryImportStatus
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImportExport(
    bottomSheetScaffoldState: BottomSheetScaffoldState,
    diceAndroidViewModel: DiceAndroidViewModel,
) {
    val stateFlowTabBagExport =
        diceAndroidViewModel.stateFlowTabBagExport.collectAsStateWithLifecycle(
            lifecycleOwner = LocalLifecycleOwner.current
        )
    val exportStatus = stateFlowTabBagExport.value.exportStatus

    val stateFlowTabBagImport =
        diceAndroidViewModel.stateFlowTabBagImport.collectAsStateWithLifecycle(
            lifecycleOwner = LocalLifecycleOwner.current
        )
    val importStatus = stateFlowTabBagImport.value.importStatus
    val importFailureReason = stateFlowTabBagImport.value.importDetail

    val launcherImport =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                diceAndroidViewModel.importFileJson(uri)
            }
        }

    val launcherExport =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.CreateDocument("application/json")) { uri: Uri? ->
            uri?.let {
                diceAndroidViewModel.exportFileJson(uri)
            }
        }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 8.dp),
        horizontalArrangement = Arrangement.Center,
    ) {
        ExportButton(bottomSheetScaffoldState, launcherExport)

        Spacer(modifier = Modifier.padding(horizontal = 10.dp))

        ImportButton(bottomSheetScaffoldState, launcherImport)
    }

    ImportExportToasts(
        exportStatus,
        importStatus,
        importFailureReason,
        diceAndroidViewModel
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ExportButton(
    bottomSheetScaffoldState: BottomSheetScaffoldState,
    launcherExport: androidx.activity.result.ActivityResultLauncher<String>
) {
    val coroutineScope = rememberCoroutineScope()
    val timeFormatter = remember { SimpleDateFormat("HHmmss", Locale.getDefault()) }

    Button(
        onClick = {
            coroutineScope.launch {
                bottomSheetScaffoldState.bottomSheetState.partialExpand()
            }

            val formattedDate = timeFormatter.format(Date())
            launcherExport.launch("Chance-$formattedDate.json")
        },
        modifier = Modifier
            .width(150.dp)
            .minimumInteractiveComponentSize()
            .testTag(DiceTestTag.EXPORT),
    ) {
        val storageExportPainter = painterResource(id = R.drawable.storage_export)
        val iconModifier = Modifier.size(24.dp)

        Icon(
            storageExportPainter,
            contentDescription = stringResource(R.string.tab_bag_export),
            modifier = iconModifier,
        )

        Spacer(Modifier.size(ButtonDefaults.IconSpacing))

        Text(stringResource(R.string.tab_bag_export))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ImportButton(
    bottomSheetScaffoldState: BottomSheetScaffoldState,
    launcherImport: androidx.activity.result.ActivityResultLauncher<String>
) {
    val coroutineScope = rememberCoroutineScope()

    Button(
        onClick = {
            coroutineScope.launch {
                bottomSheetScaffoldState.bottomSheetState.partialExpand()
            }

            launcherImport.launch("application/json")
        },
        modifier = Modifier
            .width(150.dp)
            .minimumInteractiveComponentSize()
            .testTag(DiceTestTag.IMPORT),
    ) {
        val storageImportPainter = painterResource(id = R.drawable.storage_import)
        val iconModifier = Modifier.size(24.dp)

        Icon(
            storageImportPainter,
            contentDescription = stringResource(R.string.tab_bag_import),
            modifier = iconModifier,
        )

        Spacer(Modifier.size(ButtonDefaults.IconSpacing))

        Text(stringResource(R.string.tab_bag_import))
    }
}

@Composable
private fun ImportExportToasts(
    exportStatus: ExportImportStatus,
    importStatus: ExportImportStatus,
    importFailureReason: RepositoryImportStatus,
    diceAndroidViewModel: DiceAndroidViewModel
) {
    val context = LocalContext.current
    val exportSuccessfulToast = stringResource(R.string.tab_bag_export_success)
    val importSuccessfulToast = stringResource(R.string.tab_bag_import_success)

    val importFailureDetail = stringResource(importFailureReason.toStringResId())
    val importFailureReasonToast = stringResource(
        R.string.tab_bag_import_failure,
        importFailureDetail
    )

    LaunchedEffect(exportStatus) {
        if (exportStatus == ExportImportStatus.SUCCESS) {
            Toast.makeText(context, exportSuccessfulToast, Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(importStatus) {
        if (importStatus == ExportImportStatus.SUCCESS) {
            Toast.makeText(context, importSuccessfulToast, Toast.LENGTH_SHORT).show()
        }

        if (importStatus == ExportImportStatus.FAILURE) {
            Toast.makeText(
                context,
                importFailureReasonToast,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    if (exportStatus != ExportImportStatus.READY || importStatus != ExportImportStatus.READY) {
        diceAndroidViewModel.resetExportImportStatus()
    }
}

fun RepositoryImportStatus.toStringResId(): Int = when (this) {
    RepositoryImportStatus.JSON_FILE_EMPTY -> R.string.tab_bag_import_failure_json_file_empty
    RepositoryImportStatus.JSON_FILE_UNKNOWN_VERSION -> R.string.tab_bag_import_failure_json_file_unknown_version
    RepositoryImportStatus.JSON_FILE_MISSING_SECTION -> R.string.tab_bag_import_failure_json_file_missing_section
    RepositoryImportStatus.JSON_DICE_MISSING -> R.string.tab_bag_import_failure_json_dice_missing
    RepositoryImportStatus.JSON_DICE_UUID -> R.string.tab_bag_import_failure_json_dice_uuid
    RepositoryImportStatus.JSON_DICE_TITLE -> R.string.tab_bag_import_failure_json_dice_title
    RepositoryImportStatus.JSON_SIDE_SIZE -> R.string.tab_bag_import_failure_json_side_size
    RepositoryImportStatus.JSON_SCHEMA_SETTINGS -> R.string.tab_bag_import_failure_json_schema_settings
    RepositoryImportStatus.JSON_SCHEMA_DICE -> R.string.tab_bag_import_failure_json_schema_dice
    RepositoryImportStatus.JSON_SCHEMA_SIDE -> R.string.tab_bag_import_failure_json_schema_side
    RepositoryImportStatus.JSON_SCHEMA_GROUP -> R.string.tab_bag_import_failure_json_schema_group
    else -> R.string.tab_bag_import_failure
}
