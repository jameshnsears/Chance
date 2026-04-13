package com.github.jameshnsears.chance.ui.tab.bag

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
import com.github.jameshnsears.chance.ui.zoom.bag.ZoomBagAndroidViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImportExport(
    bottomSheetScaffoldState: BottomSheetScaffoldState,
    tabBagAndroidViewModel: TabBagAndroidViewModel,
    zoomBagAndroidViewModel: ZoomBagAndroidViewModel
) {
    val stateFlowTabBagExport =
        tabBagAndroidViewModel.stateFlowTabBagExport.collectAsStateWithLifecycle(
            lifecycleOwner = LocalLifecycleOwner.current
        )
    val exportStatus = stateFlowTabBagExport.value.exportStatus
    val exportSuccessfulToast = stringResource(R.string.tab_bag_export_success)

    val stateFlowTabBagImport =
        tabBagAndroidViewModel.stateFlowTabBagImport.collectAsStateWithLifecycle(
            lifecycleOwner = LocalLifecycleOwner.current
        )
    val importStatus = stateFlowTabBagImport.value.importStatus
    val importFailureReason = stateFlowTabBagImport.value.importDetail

    val importSuccessfulToast = stringResource(R.string.tab_bag_import_success)
    val importFailureReasonToast = stringResource(
        R.string.tab_bag_import_failure,
        importFailureReason
    )

    val timeFormatter = remember { SimpleDateFormat("HHmmss", Locale.getDefault()) }

    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current

    val launcherImport =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.OpenDocument()) { uri: Uri? ->
            uri?.let {
                tabBagAndroidViewModel.importFileJson(uri)
            }
        }

    val launcherExport =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.CreateDocument("application/json")) { uri: Uri? ->
            uri?.let {
                tabBagAndroidViewModel.exportFileJson(uri)
            }
        }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 8.dp),
        horizontalArrangement = Arrangement.Center,
    ) {
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
                .testTag(BagTestTag.EXPORT),
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

        Spacer(modifier = Modifier.padding(horizontal = 10.dp))

        Button(
            onClick = {
                coroutineScope.launch {
                    bottomSheetScaffoldState.bottomSheetState.partialExpand()
                }

                launcherImport.launch(arrayOf("application/json", "*/json"))
            },
            modifier = Modifier
                .width(150.dp)
                .testTag(BagTestTag.IMPORT),
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

    LaunchedEffect(exportStatus) {
        if (exportStatus == ExportImportStatus.SUCCESS) {
            Toast.makeText(context, exportSuccessfulToast, Toast.LENGTH_SHORT).show()
        }
    }

    /////////////////

    LaunchedEffect(importStatus) {
        if (importStatus == ExportImportStatus.SUCCESS) {
            Toast.makeText(context, importSuccessfulToast, Toast.LENGTH_SHORT).show()
            zoomBagAndroidViewModel.refreshAfterImport()
        }

        if (importStatus == ExportImportStatus.FAILURE) {
            if (importFailureReasonToast.isNotEmpty())
                Toast.makeText(
                    context,
                    importFailureReasonToast,
                    Toast.LENGTH_LONG
                ).show()
        }
    }

    /////////////////

    if (exportStatus != ExportImportStatus.NONE || importStatus != ExportImportStatus.NONE) {
        tabBagAndroidViewModel.resetExportImportStatus()
    }
}
